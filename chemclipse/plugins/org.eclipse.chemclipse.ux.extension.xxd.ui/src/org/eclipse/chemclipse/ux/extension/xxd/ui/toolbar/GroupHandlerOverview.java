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
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageTaskOverview;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferencePage;

public class GroupHandlerOverview extends AbstractGroupHandler {

	public static final String NAME = "Overview";
	//
	private static final String IMAGE_HIDE = IApplicationImage.IMAGE_CHROMATOGRAM_OVERVIEW_ACTIVE;
	private static final String IMAGE_SHOW = IApplicationImage.IMAGE_CHROMATOGRAM_OVERVIEW_DEFAULT;

	@Override
	public List<IPreferencePage> getPreferencePages() {

		List<IPreferencePage> preferencePages = new ArrayList<>();
		preferencePages.add(new PreferencePageTaskOverview());
		return preferencePages;
	}

	@Override
	public List<IPartHandler> getPartHandlerMandatory() {

		List<IPartHandler> partHandler = new ArrayList<>();
		//
		partHandler.add(new PartHandler("Header", PartSupport.PARTDESCRIPTOR_HEADER_DATA, PreferenceSupplier.P_STACK_POSITION_HEADER_DATA));
		partHandler.add(new PartHandler("Overview (Chromatogram)", PartSupport.PARTDESCRIPTOR_CHROMATOGRAM_OVERVIEW, PreferenceSupplier.P_STACK_POSITION_CHROMATOGRAM_OVERVIEW));
		partHandler.add(new PartHandler("Statistics (Chromatogram)", PartSupport.PARTDESCRIPTOR_CHROMATOGRAM_STATISTICS, PreferenceSupplier.P_STACK_POSITION_CHROMATOGRAM_STATISTICS));
		partHandler.add(new PartHandler("Signal/Noise (Chromatogram)", PartSupport.PARTDESCRIPTOR_CHROMATOGRAM_SIGNAL_NOISE, PreferenceSupplier.P_STACK_POSITION_CHROMATOGRAM_SIGNAL_NOISE));
		//
		return partHandler;
	}

	@Override
	public List<IPartHandler> getPartHandlerAdditional() {

		List<IPartHandler> partHandler = new ArrayList<>();
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

		return ".overview";
	}
}
