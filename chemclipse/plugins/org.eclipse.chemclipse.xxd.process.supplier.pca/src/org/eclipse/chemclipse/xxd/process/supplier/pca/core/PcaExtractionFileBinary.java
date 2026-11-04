/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - support for additional fields in trailer
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.statistics.IPeakNumber;
import org.eclipse.chemclipse.model.statistics.IRetentionIndex;
import org.eclipse.chemclipse.model.statistics.IRetentionTime;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.model.statistics.PeakNumber;
import org.eclipse.chemclipse.model.statistics.RetentionIndex;
import org.eclipse.chemclipse.model.statistics.RetentionTime;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.PeakSampleData;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Sample;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Samples;
import org.eclipse.core.runtime.IProgressMonitor;

public class PcaExtractionFileBinary implements IExtractionData {

	private static final Logger logger = Logger.getLogger(PcaExtractionFileBinary.class);

	public static final String DESCRIPTION = "PCA Data Binary";
	public static final String FILE_EXTENSION = ".pdb";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";
	/*
	 * Optional trailer after the variables block, carrying metadata about the values (e.g. area or
	 * concentration + unit). Files written before the trailer existed simply end after the variables
	 * block and are read with the defaults; readers that predate the trailer stop after the variables
	 * block and never look at it. Bump TRAILER_VERSION when appending fields - older readers of a newer
	 * trailer version still read the matrix itself and fall back to the defaults.
	 */
	private static final int TRAILER_MAGIC = 0x50434454; // "PCDT"
	private static final int TRAILER_VERSION = 1;

	private File file;

	public PcaExtractionFileBinary(File file) {

		this.file = file;
	}

	@Override
	public Samples process(IProgressMonitor monitor) {

		return extract();
	}

	public static boolean write(File file, Samples samples) {

		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file))) {
			/*
			 * Samples
			 */
			List<Sample> sampleList = samples.getSamples();
			int sizeSamples = sampleList.size();
			dataOutputStream.writeInt(sizeSamples);
			for(Sample sample : sampleList) {
				dataOutputStream.writeUTF(sample.getSampleName());
				dataOutputStream.writeUTF(sample.getRGB());
				dataOutputStream.writeUTF(sample.getGroupName());
				dataOutputStream.writeUTF(sample.getClassification());
				dataOutputStream.writeUTF(sample.getDescription());
				List<PeakSampleData> peakSampleDataList = sample.getSampleData();
				int sizeSampleData = peakSampleDataList.size();
				dataOutputStream.writeInt(sizeSampleData);
				for(PeakSampleData peakSampleData : peakSampleDataList) {
					dataOutputStream.writeDouble(peakSampleData.getData());
				}
			}
			/*
			 * Variables
			 */
			List<IVariable> variables = samples.getVariables();
			int sizeVariables = variables.size();
			dataOutputStream.writeInt(sizeVariables);
			for(IVariable variable : variables) {
				dataOutputStream.writeUTF(variable.getType());
				dataOutputStream.writeUTF(variable.getValue());
				dataOutputStream.writeBoolean(variable.isSelected());
				dataOutputStream.writeUTF(variable.getClassification());
				dataOutputStream.writeUTF(variable.getDescription());
			}
			/*
			 * Trailer
			 */
			dataOutputStream.writeInt(TRAILER_MAGIC);
			dataOutputStream.writeInt(TRAILER_VERSION);
			dataOutputStream.writeUTF(samples.getValueType());
			dataOutputStream.writeUTF(samples.getValueUnit());
			dataOutputStream.flush();
		} catch(Exception e) {
			logger.warn(e);
			return false;
		}
		return true;
	}

	private Samples extract() {

		Samples samples = null;

		try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file))) {
			/*
			 * Samples
			 */
			List<Sample> sampleList = new ArrayList<>();
			int sizeSamples = dataInputStream.readInt();
			for(int i = 0; i < sizeSamples; i++) {
				String sampleName = dataInputStream.readUTF();
				String rgb = dataInputStream.readUTF();
				String groupName = dataInputStream.readUTF();
				String classification = dataInputStream.readUTF();
				String description = dataInputStream.readUTF();
				Sample sample = new Sample(sampleName, groupName, classification, description);
				sample.setRGB(rgb);
				int sizeSampleData = dataInputStream.readInt();
				for(int j = 0; j < sizeSampleData; j++) {
					double data = dataInputStream.readDouble();
					sample.getSampleData().add(new PeakSampleData(data, null));
				}
				sampleList.add(sample);
			}
			samples = new Samples(sampleList);
			/*
			 * Variables
			 */
			int sizeVariables = dataInputStream.readInt();
			for(int i = 0; i < sizeVariables; i++) {
				try {
					/*
					 * Data
					 */
					String type = dataInputStream.readUTF();
					String value = dataInputStream.readUTF();
					boolean isSelected = dataInputStream.readBoolean();
					String classification = dataInputStream.readUTF();
					String descripttion = dataInputStream.readUTF();

					IVariable variable = null;
					if(type.equals(IRetentionTime.TYPE)) {
						double retentionTimeMinutes = Double.parseDouble(value);
						int retentionTime = (int)Math.round(retentionTimeMinutes * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
						variable = new RetentionTime(retentionTime);
					} else if(type.equals(IRetentionIndex.TYPE)) {
						int retentionIndex = (int)Math.round(Double.parseDouble(value));
						variable = new RetentionIndex(retentionIndex);
					} else if(type.equals(IPeakNumber.TYPE)) {
						int peakNumber = (int)Math.round(Double.parseDouble(value));
						variable = new PeakNumber(peakNumber);
					}

					if(variable != null) {
						variable.setSelected(isSelected);
						variable.setClassification(classification);
						variable.setDescription(descripttion);
						samples.getVariables().add(variable);
					}
				} catch(NumberFormatException e) {
					logger.warn(e);
				}
			}
			/*
			 * Trailer (optional)
			 */
			readTrailer(dataInputStream, samples);
		} catch(Exception e) {
			logger.warn(e);
		}

		return samples;
	}

	private static void readTrailer(DataInputStream dataInputStream, Samples samples) {

		try {
			if(dataInputStream.readInt() != TRAILER_MAGIC) {
				return;
			}
			int version = dataInputStream.readInt();
			if(version < 1 || version > TRAILER_VERSION) {
				logger.info("Unsupported PCA data binary trailer version " + version + ", using default value type.");
				return;
			}
			samples.setValueType(dataInputStream.readUTF());
			samples.setValueUnit(dataInputStream.readUTF());
		} catch(EOFException e) {
			// No (or truncated) trailer, e.g. a file written before the trailer existed - keep the defaults.
		} catch(Exception e) {
			logger.warn(e);
		}
	}
}