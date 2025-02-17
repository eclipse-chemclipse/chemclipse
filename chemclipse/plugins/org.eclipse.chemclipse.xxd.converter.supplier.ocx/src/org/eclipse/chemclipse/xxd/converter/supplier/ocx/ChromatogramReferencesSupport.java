/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.io.ChromatogramWriterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.support.HeaderUtil;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ChromatogramWriterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.io.ChromatogramWriterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramReferencesSupport {

	public static void exportReferences(File file, IChromatogram chromatogram, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotWriteableException, IOException {

		ChromatogramWriterMSD chromatogramWriterMSD = new ChromatogramWriterMSD();
		ChromatogramWriterCSD chromatogramWriterCSD = new ChromatogramWriterCSD();
		ChromatogramWriterWSD chromatogramWriterWSD = new ChromatogramWriterWSD();
		//
		HeaderField headerField = PreferenceSupplier.getChromatogramExportReferencesHeaderField();
		List<IChromatogram> chromatogramReferences = chromatogram.getReferencedChromatograms();
		DecimalFormat decimalFormat = getDecimalFormatIndex(chromatogramReferences.size());
		//
		int i = 1;
		for(IChromatogram chromatogramReference : chromatogramReferences) {
			if(!chromatogramReference.getScans().isEmpty()) {
				if(chromatogramReference instanceof IChromatogramCSD referencedChromatogramCSD) {
					/*
					 * CSD
					 */
					File fileReference = getFileReference(file, chromatogramReference, headerField, decimalFormat.format(i), "CSD");
					chromatogramWriterCSD.writeChromatogram(fileReference, referencedChromatogramCSD, monitor);
				} else if(chromatogramReference instanceof IChromatogramMSD referencedChromatogramMSD) {
					/*
					 * MSD
					 */
					File fileReference = getFileReference(file, chromatogramReference, headerField, decimalFormat.format(i), "MSD");
					chromatogramWriterMSD.writeChromatogram(fileReference, referencedChromatogramMSD, monitor);
				} else if(chromatogramReference instanceof IChromatogramWSD referencedChromatogramWSD) {
					/*
					 * WSD
					 */
					File fileReference = getFileReference(file, chromatogramReference, headerField, decimalFormat.format(i), "WSD");
					chromatogramWriterWSD.writeChromatogram(fileReference, referencedChromatogramWSD, monitor);
				}
			}
			i++;
		}
	}

	private static DecimalFormat getDecimalFormatIndex(int sizeReferences) {

		/*
		 * Add at least one digit.
		 */
		StringBuilder builder = new StringBuilder();
		builder.append("0");
		/*
		 * Add more digits if available.
		 */
		int size = Integer.toString(sizeReferences).length();
		for(int i = 1; i < size; i++) {
			builder.append("0");
		}
		//
		return ValueFormat.getDecimalFormatEnglish(builder.toString());
	}

	private static File getFileReference(File file, IChromatogram chromatogram, HeaderField headerField, String index, String type) {

		String extension = VersionConstants.FILE_EXTENSION_CHROMATOGRAM;
		String identifier = HeaderUtil.getHeaderData(chromatogram, headerField, index);
		String directory = file.getParentFile().getAbsolutePath();
		//
		StringBuilder builder = new StringBuilder();
		builder.append(file.getName().substring(0, file.getName().length() - extension.length()));
		builder.append("_");
		builder.append(type);
		builder.append("_");
		builder.append(identifier);
		builder.append(extension);
		String name = builder.toString();
		//
		return new File(directory + File.separator + name);
	}
}