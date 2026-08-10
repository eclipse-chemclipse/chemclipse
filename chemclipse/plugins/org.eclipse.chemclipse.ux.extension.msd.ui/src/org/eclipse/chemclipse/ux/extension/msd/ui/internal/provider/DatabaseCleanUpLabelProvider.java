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
package org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.wizards.DatabaseEntryClean;
import org.eclipse.swt.graphics.Image;

public class DatabaseCleanUpLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String NAME = "Name";
	public static final String CAS_NUMBER = "CAS#";

	public static final String[] TITLES = { //
			NAME, //
			CAS_NUMBER //
	};

	public static final int[] BOUNDS = { //
			400, //
			150 //
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
		if(element instanceof DatabaseEntryClean entry) {
			switch(columnIndex) {
				case 0:
					text = entry.getName();
					break;
				case 1:
					text = entry.getCasNumber();
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DATABASE, IApplicationImageProvider.SIZE_16x16);
	}
}