/*******************************************************************************
 * Copyright (c) 2016, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.io.IColumnFormat;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.support.text.ValueFormat;

public class CalibrationFileWriter {

	public static final String TOPIC_PROCESSING_FILE_CREATED = "processing/file/created/amdis/cal"; // $NON-NLS-1$
	//
	private static final Logger logger = Logger.getLogger(CalibrationFileWriter.class);
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.000");

	public void write(File file, ISeparationColumnIndices separationColumnIndices) {

		try (PrintWriter printWriter = new PrintWriter(file)) {
			/*
			 * Column data
			 * #COLUMN_NAME=DB5
			 * ...
			 */
			ISeparationColumn separationColumn = separationColumnIndices.getSeparationColumn();
			if(separationColumn != null) {
				printWriter.println(IColumnFormat.COLUMN_NAME + IColumnFormat.HEADER_VALUE_DELIMITER + separationColumn.getName());
				printWriter.println(IColumnFormat.COLUMN_LENGTH + IColumnFormat.HEADER_VALUE_DELIMITER + separationColumn.getLength());
				printWriter.println(IColumnFormat.COLUMN_DIAMETER + IColumnFormat.HEADER_VALUE_DELIMITER + separationColumn.getDiameter());
				printWriter.println(IColumnFormat.COLUMN_PHASE + IColumnFormat.HEADER_VALUE_DELIMITER + separationColumn.getPhase());
			}
			/*
			 * RI data
			 */
			List<IRetentionIndexEntry> retentionIndexEntries = new ArrayList<>(separationColumnIndices.values());
			Collections.sort(retentionIndexEntries, (r1, r2) -> Integer.compare(r1.getRetentionTime(), r2.getRetentionTime()));
			for(IRetentionIndexEntry retentionIndexEntry : retentionIndexEntries) {
				/*
				 * e.g.
				 * 11.336 1700.0 100 937 Heptadecane
				 */
				printWriter.print(decimalFormat.format(retentionIndexEntry.getRetentionTime() / AbstractChromatogram.MINUTE_CORRELATION_FACTOR));
				printWriter.print(IColumnFormat.RI_VALUE_DELIMITER);
				printWriter.print(retentionIndexEntry.getRetentionIndex());
				printWriter.print(IColumnFormat.RI_VALUE_DELIMITER);
				printWriter.print(100); // Fixed
				printWriter.print(IColumnFormat.RI_VALUE_DELIMITER);
				printWriter.print(999); // Fixed
				printWriter.print(IColumnFormat.RI_VALUE_DELIMITER);
				printWriter.println(retentionIndexEntry.getName());
			}
			printWriter.flush();
		} catch(FileNotFoundException e) {
			logger.warn(e);
		}
	}
}