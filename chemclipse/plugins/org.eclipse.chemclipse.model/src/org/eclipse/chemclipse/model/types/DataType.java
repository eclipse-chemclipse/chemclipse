/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - add MALDI support
 *******************************************************************************/
package org.eclipse.chemclipse.model.types;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.support.text.ILabel;

public enum DataType implements ILabel {

	NONE("--"), // Used e.g. as an initial value for the Scan Table
	AUTO_DETECT("Auto-Detect"), //
	MSD_NOMINAL("Quadrupole, Ion Trap"), //
	MSD_TANDEM("MS/MS"), //
	MSD_HIGHRES("Orbitrap, TOF-MS"), //
	MSD("Mass Selective Data (MSD)"), //
	CSD("Current Selective Cata (CSD)"), //
	WSD("Wavelength Selective Data (WSD)"), //
	VSD("Vibrational Spectroscopy Data (VSD)"), //
	SCAN_VSD("Vibrational Spectroscopy Scan (FT-IR, Raman)"), //
	SCAN_WSD("Wavelength Selective Scan (UV/Vis)"), //
	TSD("Time Selective Data (GC-IMS, GCxGC, ..)"), //
	NMR("Nuclear Magnetic Resonance"), //
	CAL("Retention Index Calibration"), //
	PCR("Polymerase Chain Reaction"), //
	SEQ("Sequences"), //
	MTH("Methods"), //
	QDB("Quantitation Databases"), //
	MALDI("MALDI-TOF MS"), //
	MSD_DATABASE("Mass Spectral Databases"); //

	private String label = "";

	private DataType(String label) {

		this.label = label;
	}

	public String label() {

		return label;
	}

	public static DataType fromDataCategory(DataCategory category) {

		switch(category) {
			case MSD:
				return DataType.MSD;
			case WSD:
				return DataType.WSD;
			case CSD:
				return DataType.CSD;
			case VSD:
				return DataType.VSD;
			case NMR:
				return DataType.NMR;
			case MALDI:
				return DataType.MALDI;
			case MSD_DATABASE:
				return DataType.MSD_DATABASE;
			default:
				return DataType.AUTO_DETECT;
		}
	}

	public DataCategory toDataCategory() {

		switch(this) {
			case MSD:
				return DataCategory.MSD;
			case WSD:
				return DataCategory.WSD;
			case CSD:
				return DataCategory.CSD;
			case VSD:
				return DataCategory.VSD;
			case NMR:
				return DataCategory.NMR;
			case MALDI:
				return DataCategory.MALDI;
			case MSD_DATABASE:
				return DataCategory.MSD_DATABASE;
			default:
				return DataCategory.AUTO_DETECT;
		}
	}

	/**
	 * This DataCategory was formerly used to map VSD (vibrational spectroscopy chromatograms)
	 * 
	 * @return
	 */
	public static String ISD_LEGACY() {

		return "ISD";
	}

	/**
	 * Returns the data type inclusive legacy check.
	 * 
	 * @param name
	 * @param def
	 * @return
	 */
	public static DataType valueOf(String name, DataType def) {

		if(ISD_LEGACY().equals(name)) {
			return DataType.VSD;
		} else {
			try {
				return DataType.valueOf(name);
			} catch(Exception e) {
				return def;
			}
		}
	}

	public static DataCategory[] convert(DataType[] dataTypes) {

		DataCategory[] categories = new DataCategory[dataTypes.length];
		for(int i = 0; i < categories.length; i++) {
			categories[i] = dataTypes[i].toDataCategory();
		}
		//
		return categories;
	}

	public static DataType[] convert(DataCategory[] categories) {

		DataType[] dataTypes = new DataType[categories.length];
		for(int i = 0; i < categories.length; i++) {
			dataTypes[i] = fromDataCategory(categories[i]);
		}
		//
		return dataTypes;
	}
}