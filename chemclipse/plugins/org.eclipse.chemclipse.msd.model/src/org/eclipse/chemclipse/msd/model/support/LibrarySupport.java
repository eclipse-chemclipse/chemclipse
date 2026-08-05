/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.identifier.ColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.support.model.SeparationColumnType;

public class LibrarySupport {

	public static final String DESCRIPTION = "Excel Library Data";
	public static final String FILE_EXTENSION = ".xlsx";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";

	public static Map<String, Map<String, String>> readExcelData(File file) throws Exception {

		Map<String, Map<String, String>> dataMap = new HashMap<>();
		try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
			Sheet sheet = workbook.getSheetAt(0);
			if(sheet == null) {
				return dataMap;
			}
			/*
			 * Header
			 */
			Row headerRow = sheet.getRow(0);
			if(headerRow == null) {
				return dataMap;
			}
			/*
			 * Column Definitions
			 */
			Map<Integer, String> columnHeaders = new HashMap<>();
			for(Cell cell : headerRow) {
				String header = getCellValueAsString(cell).trim();
				if(!header.isEmpty()) {
					columnHeaders.put(cell.getColumnIndex(), header);
				}
			}
			/*
			 * Data
			 */
			int lastRow = sheet.getLastRowNum();
			for(int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if(row == null) {
					continue;
				}

				Cell nameCell = row.getCell(0);
				if(nameCell == null) {
					continue;
				}

				String name = getCellValueAsString(nameCell).trim();
				if(name.isEmpty()) {
					continue;
				}

				Map<String, String> rowData = new HashMap<>();
				int lastCell = row.getLastCellNum();
				for(int j = 1; j < lastCell; j++) {
					String header = columnHeaders.get(j);
					if(header == null) {
						continue;
					}
					Cell cell = row.getCell(j);
					if(cell != null) {
						String value = getCellValueAsString(cell).trim();
						if(!value.isEmpty()) {
							rowData.put(header, value);
						}
					}
				}
				/*
				 * Validation
				 */
				if(!rowData.isEmpty()) {
					dataMap.put(name, rowData);
				}
			}
		}

		return dataMap;
	}

	private static String getCellValueAsString(Cell cell) {

		if(cell == null) {
			return "";
		}
		/*
		 * Get the cell type.
		 */
		CellType cellType = cell.getCellType();
		if(cellType == CellType.FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}
		/*
		 * Extract its content.
		 */
		switch(cellType) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				double numericValue = cell.getNumericCellValue();
				if(numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
					return String.valueOf((long)numericValue);
				}
				return String.valueOf(numericValue);
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			default:
				return "";
		}
	}

	public static int update(IMassSpectra massSpectra, Map<String, Map<String, String>> dataMap) {

		int updated = 0;
		if(massSpectra == null || dataMap == null || dataMap.isEmpty()) {
			return updated;
		}
		/*
		 * Enrich the library entries.
		 */
		for(IScanMSD scan : massSpectra.getList()) {
			if(scan instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
				ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
				Map<String, String> rowData = dataMap.get(libraryInformation.getName());
				if(rowData != null && !rowData.isEmpty()) {
					updateExcelData(libraryMassSpectrum, libraryInformation, rowData);
					updated++;
				}
			}
		}

		return updated;
	}

	public static IRegularLibraryMassSpectrum merge(List<IScanMSD> libraryEntriesSource) {

		RegularLibraryMassSpectrum libraryMassSpectrumMerged = null;
		if(libraryEntriesSource != null && !libraryEntriesSource.isEmpty()) {
			ICombinedMassSpectrum combinedMassSpectrum = getCombinedMassSpectrum(libraryEntriesSource);
			libraryMassSpectrumMerged = new RegularLibraryMassSpectrum();
			libraryMassSpectrumMerged.addIons(combinedMassSpectrum.getIons(), false);
			libraryMassSpectrumMerged.setRetentionTime(libraryEntriesSource.get(0).getRetentionTime());
			libraryMassSpectrumMerged.setRetentionIndex(libraryEntriesSource.get(0).getRetentionIndex());
			mergeLibraryInformation(libraryMassSpectrumMerged.getLibraryInformation(), libraryEntriesSource);
		}

		return libraryMassSpectrumMerged;
	}

	private static void updateExcelData(IRegularLibraryMassSpectrum libraryMassSpectrum, ILibraryInformation libraryInformation, Map<String, String> rowData) {

		for(Map.Entry<String, String> entry : rowData.entrySet()) {
			String value = entry.getValue();
			if(value == null || value.isEmpty()) {
				continue;
			}
			/*
			 * Set Value
			 */
			switch(entry.getKey()) {
				case "Retention Time":
					libraryMassSpectrum.setRetentionTime((int)(parseDouble(value, 0) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					break;
				case "Retention Index":
					libraryMassSpectrum.setRetentionIndex((float)parseDouble(value, 0));
					break;
				case "CAS":
					libraryInformation.setCasNumber(value);
					break;
				case "MW":
					libraryInformation.setMolWeight(parseDouble(value, 0));
					break;
				case "Formula":
					libraryInformation.setFormula(value);
					break;
				case "SMILES":
					libraryInformation.setSmiles(value);
					break;
				case "InChI":
					libraryInformation.setInChI(value);
					break;
				case "Reference Identifier":
					libraryInformation.setReferenceIdentifier(value);
					break;
				case "Comments":
					libraryInformation.setComments(value);
					break;
				default:
					break;
			}
		}
	}

	private static double parseDouble(String value, double def) {

		try {
			return Double.parseDouble(value);
		} catch(NumberFormatException e) {
			return def;
		}
	}

	private static ICombinedMassSpectrum getCombinedMassSpectrum(List<IScanMSD> scans) {

		ICombinedMassSpectrumCalculator calculator = new CombinedNominalMassSpectrumCalculator();
		for(IScanMSD scan : scans) {
			for(IIon ion : scan.getIons()) {
				calculator.addIon(ion.getIon(), ion.getAbundance());
			}
		}

		return calculator.createMassSpectrum(CalculationType.SUM);
	}

	private static void mergeLibraryInformation(ILibraryInformation libraryInformationTarget, List<IScanMSD> libraryEntriesSource) {

		boolean initialize = true;
		Iterator<IScanMSD> iterator = libraryEntriesSource.iterator();
		while(iterator.hasNext()) {
			if(iterator.next() instanceof IRegularLibraryMassSpectrum libraryMassSpectrumSource) {
				if(initialize) {
					initializeLibraryData(libraryMassSpectrumSource, libraryInformationTarget);
					initialize = false;
				} else {
					mergeLibraryData(libraryMassSpectrumSource, libraryInformationTarget);
				}
			}
		}
	}

	private static void initializeLibraryData(IRegularLibraryMassSpectrum libraryMassSpectrumSource, ILibraryInformation libraryInformationTarget) {

		ILibraryInformation libraryInformationSource = libraryMassSpectrumSource.getLibraryInformation();
		libraryInformationTarget.setName(libraryInformationSource.getName());
		libraryInformationTarget.setFormula(libraryInformationSource.getFormula());
		libraryInformationTarget.setSmiles(libraryInformationSource.getSmiles());
		libraryInformationTarget.setInChI(libraryInformationSource.getInChI());
		libraryInformationTarget.setInChIKey(libraryInformationSource.getInChIKey());
		libraryInformationTarget.setMolWeight(libraryInformationSource.getMolWeight());
		libraryInformationTarget.setExactMass(libraryInformationSource.getExactMass());
		libraryInformationTarget.setComments(libraryInformationSource.getComments());
		libraryInformationTarget.setMiscellaneous(libraryInformationSource.getMiscellaneous());
		libraryInformationTarget.setReferenceIdentifier(libraryInformationSource.getReferenceIdentifier());
		libraryInformationTarget.setRetentionTime(libraryInformationSource.getRetentionTime());
		libraryInformationTarget.setDatabase(libraryInformationSource.getDatabase());
		libraryInformationTarget.setContributor(libraryInformationSource.getContributor());
		libraryInformationTarget.setCompoundClass(libraryInformationSource.getCompoundClass());
		libraryInformationTarget.setMoleculeStructure(libraryInformationSource.getMoleculeStructure());
		/*
		 * Retention Index
		 */
		float retentionIndex = libraryInformationSource.getRetentionIndex();
		if(libraryInformationTarget.getRetentionIndex() == 0) {
			if(retentionIndex > 0) {
				libraryInformationTarget.setRetentionIndex(retentionIndex);
			}
		} else {
			if(retentionIndex > 0) {
				ISeparationColumn defaultColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.DEFAULT);
				libraryInformationTarget.add(new ColumnIndexMarker(defaultColumn, retentionIndex));
			}
		}
		/*
		 * CAS#
		 */
		for(String cas : libraryInformationSource.getCasNumbers()) {
			libraryInformationTarget.addCasNumber(cas);
		}
		/*
		 * Synonyms
		 */
		String targetName = libraryInformationTarget.getName();
		libraryInformationSource.getSynonyms().stream().filter(s -> !s.isEmpty() && !s.equals(targetName)).forEach(libraryInformationTarget.getSynonyms()::add);
		/*
		 * Column Indices
		 */
		for(IColumnIndexMarker marker : libraryInformationSource.getColumnIndexMarkers()) {
			libraryInformationTarget.add(marker);
		}
		/*
		 * Flavor Marker
		 */
		for(IFlavorMarker marker : libraryInformationSource.getFlavorMarkers()) {
			libraryInformationTarget.add(marker);
		}
	}

	private static void mergeLibraryData(IRegularLibraryMassSpectrum libraryMassSpectrumSource, ILibraryInformation libraryInformationTarget) {

		ILibraryInformation libraryInformationSource = libraryMassSpectrumSource.getLibraryInformation();
		String targetName = libraryInformationTarget.getName();
		String sourceName = libraryInformationSource.getName();
		/*
		 * Synonyms
		 */
		if(!sourceName.isEmpty() && !sourceName.equals(targetName)) {
			libraryInformationTarget.getSynonyms().add(sourceName);
		}
		libraryInformationSource.getSynonyms().stream().filter(s -> !s.isEmpty() && !s.equals(targetName)).forEach(libraryInformationTarget.getSynonyms()::add);
		/*
		 * CAS#
		 */
		for(String cas : libraryInformationSource.getCasNumbers()) {
			if(!libraryInformationTarget.getCasNumbers().contains(cas)) {
				libraryInformationTarget.addCasNumber(cas);
			}
		}
		if(libraryInformationTarget.getFormula().isEmpty()) {
			libraryInformationTarget.setFormula(libraryInformationSource.getFormula());
		}
		if(libraryInformationTarget.getSmiles().isEmpty()) {
			libraryInformationTarget.setSmiles(libraryInformationSource.getSmiles());
		}
		if(libraryInformationTarget.getInChI().isEmpty()) {
			libraryInformationTarget.setInChI(libraryInformationSource.getInChI());
		}
		if(libraryInformationTarget.getInChIKey().isEmpty()) {
			libraryInformationTarget.setInChIKey(libraryInformationSource.getInChIKey());
		}
		if(libraryInformationTarget.getMolWeight() == 0) {
			libraryInformationTarget.setMolWeight(libraryInformationSource.getMolWeight());
		}
		if(libraryInformationTarget.getExactMass() == 0) {
			libraryInformationTarget.setExactMass(libraryInformationSource.getExactMass());
		}
		if(libraryInformationTarget.getDatabase().isEmpty()) {
			libraryInformationTarget.setDatabase(libraryInformationSource.getDatabase());
		}
		if(libraryInformationTarget.getContributor().isEmpty()) {
			libraryInformationTarget.setContributor(libraryInformationSource.getContributor());
		}
		if(libraryInformationTarget.getCompoundClass().isEmpty()) {
			libraryInformationTarget.setCompoundClass(libraryInformationSource.getCompoundClass());
		}
		if(libraryInformationTarget.getMoleculeStructure().isEmpty()) {
			libraryInformationTarget.setMoleculeStructure(libraryInformationSource.getMoleculeStructure());
		}
		if(libraryInformationTarget.getRetentionTime() == 0) {
			libraryInformationTarget.setRetentionTime(libraryInformationSource.getRetentionTime());
		}
		/*
		 * Retention Index
		 */
		if(libraryInformationTarget.getRetentionIndex() == 0) {
			libraryInformationTarget.setRetentionIndex(libraryInformationSource.getRetentionIndex());
		} else {
			float retentionIndex = libraryInformationSource.getRetentionIndex();
			if(retentionIndex > 0) {
				ISeparationColumn defaultColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.DEFAULT);
				libraryInformationTarget.add(new ColumnIndexMarker(defaultColumn, retentionIndex));
			}
		}
		mergeTextField(libraryInformationTarget.getComments(), libraryInformationSource.getComments(), libraryInformationTarget::setComments);
		mergeTextField(libraryInformationTarget.getMiscellaneous(), libraryInformationSource.getMiscellaneous(), libraryInformationTarget::setMiscellaneous);
		mergeTextField(libraryInformationTarget.getReferenceIdentifier(), libraryInformationSource.getReferenceIdentifier(), libraryInformationTarget::setReferenceIdentifier);
		/*
		 * Column Indices
		 */
		for(IColumnIndexMarker marker : libraryInformationSource.getColumnIndexMarkers()) {
			libraryInformationTarget.add(marker);
		}
		/*
		 * Flavor Marker
		 */
		for(IFlavorMarker marker : libraryInformationSource.getFlavorMarkers()) {
			libraryInformationTarget.add(marker);
		}

	}

	private static void mergeTextField(String existing, String addition, Consumer<String> setter) {

		if(!addition.isEmpty()) {
			if(existing.isEmpty()) {
				setter.accept(addition);
			} else if(!Arrays.asList(existing.split(", ")).contains(addition)) {
				setter.accept(existing + ", " + addition);
			}
		}
	}
}