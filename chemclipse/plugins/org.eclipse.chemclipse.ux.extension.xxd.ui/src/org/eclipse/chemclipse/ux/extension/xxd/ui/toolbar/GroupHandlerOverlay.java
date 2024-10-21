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
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageTaskOverlay;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferencePage;

public class GroupHandlerOverlay extends AbstractGroupHandler {

	public static final String NAME = "Overlay";
	//
	private static final String IMAGE_HIDE = IApplicationImage.IMAGE_CHROMATOGRAM_OVERLAY_ACTIVE;
	private static final String IMAGE_SHOW = IApplicationImage.IMAGE_CHROMATOGRAM_OVERLAY_DEFAULT;

	@Override
	public List<IPreferencePage> getPreferencePages() {

		List<IPreferencePage> preferencePages = new ArrayList<>();
		preferencePages.add(new PreferencePageTaskOverlay());
		return preferencePages;
	}

	@Override
	public List<IPartHandler> getPartHandlerMandatory() {

		List<IPartHandler> partHandler = new ArrayList<>();
		//
		partHandler.add(new PartHandler("Overlay (Chromatogram)", PartSupport.PARTDESCRIPTOR_OVERLAY_CHROMATOGRAM, PreferenceSupplier.P_STACK_POSITION_OVERLAY_CHROMATOGRAM_DEFAULT));
		partHandler.add(new PartHandler("Overlay (NMR)", PartSupport.PARTDESCRIPTOR_OVERLAY_NMR, PreferenceSupplier.P_STACK_POSITION_OVERLAY_NMR));
		partHandler.add(new PartHandler("Overlay (VSD)", PartSupport.PARTDESCRIPTOR_OVERLAY_VSD, PreferenceSupplier.P_STACK_POSITION_OVERLAY_VSD));
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

		return ".overlay";
	}
}
