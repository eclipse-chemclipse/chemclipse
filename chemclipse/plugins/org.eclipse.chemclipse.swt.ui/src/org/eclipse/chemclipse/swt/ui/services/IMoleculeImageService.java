/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.swt.ui.services;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The implementation shall return a molecule image, given by the information stored in the library information.
 */
public interface IMoleculeImageService {

	String getName();

	String getDescription();

	String getVersion();

	Image create(Display display, ILibraryInformation libraryInformation, int width, int height);

	Class<? extends IWorkbenchPreferencePage> getPreferencePage();
}
