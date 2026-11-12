/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.jcampdx.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.eclipse.chemclipse.csd.converter.io.AbstractChromatogramCSDReader;
import org.eclipse.chemclipse.csd.converter.supplier.jcampdx.model.IVendorChromatogram;
import org.eclipse.chemclipse.csd.converter.supplier.jcampdx.model.IVendorScan;
import org.eclipse.chemclipse.csd.converter.supplier.jcampdx.model.VendorChromatogram;
import org.eclipse.chemclipse.csd.converter.supplier.jcampdx.model.VendorScan;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.support.ChromatogramSupport;
import org.eclipse.chemclipse.xxd.converter.supplier.jcampdx.io.DataTypeXY;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import jakarta.activation.UnsupportedDataTypeException;

public class ChromatogramReader extends AbstractChromatogramCSDReader {

	public static final String CONVERTER_ID_CSD = "org.eclipse.chemclipse.csd.converter.supplier.jcampdx";

	private static final Logger logger = Logger.getLogger(ChromatogramReader.class);

	private static final String COMMENT_MARKER = "$$";
	private static final String HEADER_MARKER = "##";

	private static final String TITLE = "##TITLE=";

	private static final String RETENTION_TIME_MARKER = "##RETENTION_TIME=";
	private static final String TIME_MARKER = "##TIME=";
	private static final String TIC_MARKER = "##TIC=";
	private static final String NAME_MARKER = "##NAME=";
	private static final String HIT_MARKER = "##HIT=";

	private static final String XYDATA = "##XYDATA=";

	private static final String XUNITS = "##XUNITS=";
	private static final String YUNITS = "##YUNITS=";

	private static final String FIRSTX = "##FIRSTX=";
	private static final String LASTX = "##LASTX=";
	private static final String DELTAX = "##DELTAX=";
	private static final String XFACTOR = "##XFACTOR=";
	private static final String YFACTOR = "##YFACTOR=";
	private static final String NPOINTS = "##NPOINTS=";
	private static final String FIRSTY = "##FIRSTY=";

	@Override
	public IChromatogramCSD read(File file, IProgressMonitor monitor) throws IOException {

		DataTypeXY dataTypeXY = isDataTypeXY(file);
		if(dataTypeXY == DataTypeXY.X_COMMA_Y || dataTypeXY == DataTypeXY.XY_RANGE) {
			return readChromatogramDataXY(file, monitor);
		} else if(dataTypeXY == DataTypeXY.X_INCREMENTAL_Y_RANGE) {
			return readChromatogramDataRepeatedY(file, monitor);
		}
		return null;
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		return read(file, monitor);
	}

	private IChromatogramCSD readChromatogramDataXY(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			/*
			 * Parse each line
			 */
			String line;
			String name = "";
			SubMonitor subMonitor = SubMonitor.convert(monitor, (int)Math.min(Integer.MAX_VALUE, file.length()));
			while((line = bufferedReader.readLine()) != null && !monitor.isCanceled()) {
				subMonitor.worked(line.length() + 1);
				/*
				 * Each scan starts with the marker:
				 * [##NAME=Acetoin (Butanon-2, 3-hydroxy-)]
				 * ##TIC=27243
				 * ##SCAN=1
				 * ##TIME=102.3358
				 * ##NPOINTS=0
				 * ##XYDATA=(X,Y)
				 * ...
				 * [##HIT=90]
				 */
				if(line.startsWith(NAME_MARKER)) {
					name = line.trim().replace(NAME_MARKER, "");
				} else if(line.startsWith(TIC_MARKER)) {
					float abundance = 0;
					try {
						String value = line.replace(TIC_MARKER, "").trim();
						abundance = Float.parseFloat(value); // TIC
					} catch(NumberFormatException e) {
						logger.warn(e);
					}
					/*
					 * Read until
					 * RETENTION_TIME_MARKER
					 * or
					 * TIME_MARKER
					 * is reached.
					 */
					boolean searchForRetentionTime = true;
					while(searchForRetentionTime) {
						if((line = bufferedReader.readLine()) != null) {
							subMonitor.worked(line.length() + 1);
							if(isRetentionTimeMarker(line)) {
								searchForRetentionTime = false;
							}
						}
					}

					if(line != null) {
						int retentionTime = getRetentionTime(line);
						if(retentionTime >= 0 && abundance > 0) {
							IVendorScan scan = new VendorScan(abundance);
							scan.setRetentionTime(retentionTime);
							chromatogram.addScan(scan);
							/*
							 * Add the identification
							 */
							if(!name.equals("")) {
								/*
								 * Find the hit value and set it.
								 */
								boolean findHitMarker = true;
								float matchFactor = 100.0f;
								while((line = bufferedReader.readLine()) != null && findHitMarker) {
									subMonitor.worked(line.length() + 1);
									if(line.startsWith(HIT_MARKER)) {
										try {
											String hitValue = line.replace(HIT_MARKER, "").trim();
											matchFactor = Float.parseFloat(hitValue);
											findHitMarker = false;
										} catch(NumberFormatException e) {
											logger.warn(e);
										}
									} else if(line.startsWith(NAME_MARKER) || line.startsWith(TIC_MARKER)) {
										findHitMarker = false;
									}
								}
								/*
								 * Add the target.
								 */
								try {
									ILibraryInformation libraryInformation = new LibraryInformation();
									libraryInformation.setName(name);
									IComparisonResult comparisonResult = new ComparisonResult(matchFactor, matchFactor, 0.0f, 0.0f);
									IIdentificationTarget scanTargetCSD = new IdentificationTarget(libraryInformation, comparisonResult);
									scan.getTargets().add(scanTargetCSD);
								} catch(ReferenceMustNotBeNullException e) {
									logger.warn(e);
								}
							}
						}
					}
					/*
					 * Reset Name
					 */
					name = "";
				}
			}

			chromatogram.setFile(file);
			ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);
			chromatogram.setConverterId(CONVERTER_ID_CSD);
		}

		return chromatogram;
	}

	private IChromatogramCSD readChromatogramDataRepeatedY(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;

			float firstX = 0;
			float firstY = 0;
			float lastX = 0;
			float deltaX = 0;
			float xFactor = 0;
			float yFactor = 0;
			int nPoints = 0;
			int index = 0;

			SubMonitor subMonitor = SubMonitor.convert(monitor, (int)Math.min(Integer.MAX_VALUE, file.length()));
			while((line = bufferedReader.readLine()) != null && !monitor.isCanceled()) {
				subMonitor.worked(line.length() + 1);
				if(line.contains(COMMENT_MARKER)) {
					line = line.substring(0, line.indexOf(COMMENT_MARKER));
				}
				if(line.contains(TITLE)) {
					chromatogram.setSampleName(line.replace(TITLE, "").trim());
				}
				if(line.startsWith(NPOINTS)) {
					nPoints = Integer.parseInt(line.replace(NPOINTS, "").trim());
				}
				if(line.startsWith(XYDATA)) {
					if(!line.contains("(X++(Y..Y))")) {
						throw new UnsupportedDataTypeException("Unknown line compression type: " + line);
					}
				}
				if(line.startsWith(FIRSTX)) {
					firstX = Float.parseFloat(line.replace(FIRSTX, "").trim());
				}
				if(line.startsWith(FIRSTY)) {
					firstY = Float.parseFloat(line.replace(FIRSTY, "").trim());
				}
				if(line.startsWith(LASTX)) {
					lastX = Float.parseFloat(line.replace(LASTX, "").trim());
				}
				if(line.startsWith(DELTAX)) {
					deltaX = Float.parseFloat(line.replace(DELTAX, "").trim());
				}
				if(line.startsWith(XFACTOR)) {
					xFactor = Float.valueOf(line.replace(XFACTOR, "").trim());
				}
				if(line.startsWith(YFACTOR)) {
					yFactor = Float.valueOf(line.replace(YFACTOR, "").trim());
				}
				if(line.startsWith(XUNITS)) {
					String xUnit = line.replace(XUNITS, "").trim();
					if(!(xUnit.equals("TIME") || xUnit.equals("ARBITRARY"))) {
						throw new UnsupportedDataTypeException("Unsupported X unit: " + xUnit);
					}
				}
				if(line.startsWith(YUNITS)) {
					String yUnit = line.replace(YUNITS, "").trim();
					if(!(yUnit.equals("ARBITRARY"))) {
						throw new UnsupportedDataTypeException("Unsupported X unit: " + yUnit);
					}
				}
				if(!line.startsWith(HEADER_MARKER)) {
					deltaX = (lastX - firstX) / (nPoints - 1);
					try (Scanner scanner = new Scanner(line).useLocale(Locale.US)) {
						if(!scanner.hasNextFloat()) {
							continue;
						}
						scanner.nextDouble(); // X
						while(scanner.hasNextFloat()) {
							float rawY = scanner.nextFloat();
							float x = firstX + index * deltaX * xFactor;
							float y = rawY * yFactor;
							VendorScan scan = new VendorScan(y);
							scan.setRetentionTime(Math.round(x * (float)IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
							chromatogram.addScan(scan);
							index++;
						}
					}
				}
			}
			if(nPoints > 0) {
				int scans = chromatogram.getNumberOfScans();
				if(scans != nPoints) {
					logger.warn("Expected " + nPoints + " but got " + scans + " scans instead.");
				}
			}
			double firstRT = chromatogram.getScans().getFirst().getX() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR;
			if(firstX != firstRT) {
				logger.warn("Expected first X to be " + firstX + " but calculated " + chromatogram.getScans().getFirst().getX() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
			}
			if(firstY != chromatogram.getScans().getFirst().getY()) {
				logger.warn("Expected first X to be " + firstX + " but was " + chromatogram.getScans().getFirst().getY());
			}
			double lastRT = chromatogram.getScans().getLast().getX() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR;
			if(lastX != lastRT) {
				logger.warn("Expected last X to be " + lastX + " but calculated " + lastRT);
			}
		}
		return chromatogram;
	}

	private boolean isRetentionTimeMarker(String line) {

		return line.startsWith(RETENTION_TIME_MARKER) || line.startsWith(TIME_MARKER);
	}

	private int getRetentionTime(String line) {

		/*
		 * The retention time is stored in seconds scale.
		 * Milliseconds = seconds * 1000.0d
		 */
		int retentionTime = -1;
		try {
			if(line.startsWith(RETENTION_TIME_MARKER)) {
				String value = line.replace(RETENTION_TIME_MARKER, "").trim();
				retentionTime = (int)(Double.parseDouble(value) * 1000.0d);
			} else if(line.startsWith(TIME_MARKER)) {
				String value = line.replace(TIME_MARKER, "").trim();
				retentionTime = (int)(Double.parseDouble(value) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
			}
		} catch(NumberFormatException e) {
			logger.warn(e);
		}
		return retentionTime;
	}

	private DataTypeXY isDataTypeXY(File file) throws IOException {

		try (FileReader fileReader = new FileReader(file)) {
			try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				String line;
				while((line = bufferedReader.readLine()) != null) {
					if(!line.startsWith(HEADER_MARKER)) {
						return DataTypeXY.UNKNOWN;
					}
					if(line.startsWith(XYDATA)) {
						return DataTypeXY.fromHeaderLine(line);
					}
				}
			}
		}
		return DataTypeXY.UNKNOWN;
	}
}