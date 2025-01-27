/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.internal.provider;

import org.eclipse.chemclipse.model.locations.UserLocation;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

public class UserLocationsLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String NAME = "Name";
	public static final String PATH = "Path";
	//
	public static final String[] TITLES = { //
			NAME, //
			PATH //
	};
	public static final int[] BOUNDS = { //
			300, //
			250 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof UserLocation userLocation) {
			switch(columnIndex) {
				case 0:
					text = userLocation.getName();
					break;
				case 1:
					text = userLocation.getPath();
					break;
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_FOLDER, IApplicationImageProvider.SIZE_16x16);
	}
}