/*******************************************************************************
 * Copyright (c) 2020, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.toolbar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageTaskScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferencePage;

public class GroupHandlerScans extends AbstractGroupHandler {

	public static final String NAME = "Scans";
	//
	private static final String IMAGE_HIDE = IApplicationImage.IMAGE_SELECTED_SCANS_ACTIVE;
	private static final String IMAGE_SHOW = IApplicationImage.IMAGE_SELECTED_SCANS_DEFAULT;

	@Override
	public List<IPreferencePage> getPreferencePages() {

		List<IPreferencePage> preferencePages = new ArrayList<>();
		preferencePages.add(new PreferencePageTaskScans());
		return preferencePages;
	}

	@Override
	public List<IPartHandler> getPartHandlerMandatory() {

		List<IPartHandler> partHandler = new ArrayList<>();
		//
		partHandler.add(new PartHandler("Targets", PartSupport.PARTDESCRIPTOR_TARGETS, PreferenceSupplier.P_STACK_POSITION_TARGETS));
		partHandler.add(new PartHandler("Scan Chart", PartSupport.PARTDESCRIPTOR_SCAN_CHART, PreferenceSupplier.P_STACK_POSITION_SCAN_CHART));
		partHandler.add(new PartHandler("Scan Table", PartSupport.PARTDESCRIPTOR_SCAN_TABLE, PreferenceSupplier.P_STACK_POSITION_SCAN_TABLE));
		//
		return partHandler;
	}

	@Override
	public List<IPartHandler> getPartHandlerAdditional() {

		List<IPartHandler> partHandler = new ArrayList<>();
		//
		partHandler.add(new PartHandler("Molecule", PartSupport.PARTDESCRIPTOR_MOLECULE, PreferenceSupplier.P_STACK_POSITION_MOLECULE));
		partHandler.add(new PartHandler("Scan Browse", PartSupport.PARTDESCRIPTOR_SCAN_BROWSE, PreferenceSupplier.P_STACK_POSITION_SCAN_BROWSE));
		partHandler.add(new PartHandler("Synonyms", PartSupport.PARTDESCRIPTOR_SYNONYMS, PreferenceSupplier.P_STACK_POSITION_SYNONYMS));
		partHandler.add(new PartHandler("Column Indices", PartSupport.PARTDESCRIPTOR_COLUMN_INDICES, PreferenceSupplier.P_STACK_POSITION_COLUMN_INDICES));
		partHandler.add(new PartHandler("Flavor Marker", PartSupport.PARTDESCRIPTOR_FLAVOR_MARKER, PreferenceSupplier.P_STACK_POSITION_FLAVOR_MARKER));
		partHandler.add(new PartHandler("Literature", PartSupport.PARTDESCRIPTOR_LITERATURE, PreferenceSupplier.P_STACK_POSITION_LITERATURE));
		partHandler.add(new PartHandler("CAS Numbers", PartSupport.PARTDESCRIPTOR_CAS_NUMBERS, PreferenceSupplier.P_STACK_POSITION_CAS_NUMBERS));
		partHandler.add(new PartHandler("Scan Subtract", PartSupport.PARTDESCRIPTOR_SUBTRACT_SCAN, PreferenceSupplier.P_STACK_POSITION_SUBTRACT_SCAN_PART));
		partHandler.add(new PartHandler("Scan Combined", PartSupport.PARTDESCRIPTOR_COMBINED_SCAN, PreferenceSupplier.P_STACK_POSITION_COMBINED_SCAN_PART));
		partHandler.add(new PartHandler("Scan Comparison", PartSupport.PARTDESCRIPTOR_COMPARISON_SCAN, PreferenceSupplier.P_STACK_POSITION_COMPARISON_SCAN_CHART));
		//
		return partHandler;
	}

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public String getImageHide() {

		return IMAGE_HIDE;
	}

	@Override
	public String getImageShow() {

		return IMAGE_SHOW;
	}

	@Override
	public String getMainMenuSuffix() {

		return ".scans";
	}
}
