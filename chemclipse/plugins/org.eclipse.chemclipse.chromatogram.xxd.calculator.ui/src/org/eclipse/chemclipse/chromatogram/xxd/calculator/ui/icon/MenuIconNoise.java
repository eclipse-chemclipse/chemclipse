/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.ui.icon;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.xxd.process.ui.menu.IMenuIcon;
import org.eclipse.swt.graphics.Image;

public class MenuIconNoise implements IMenuIcon {

	@Override
	public Image getImage() {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PREFERENCES, IApplicationImageProvider.SIZE_16x16);
	}
}