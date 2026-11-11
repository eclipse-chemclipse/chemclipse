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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.identifier.supplier.openchemlib.ui.services;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.swt.ui.services.IMoleculeImageService;
import org.eclipse.chemclipse.xxd.identifier.supplier.openchemlib.ui.support.MoleculeParser;
import org.eclipse.chemclipse.xxd.identifier.supplier.openchemlib.ui.support.SWTDepictor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.gui.generic.GenericRectangle;

@Component(service = {IMoleculeImageService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class MoleculeImageService implements IMoleculeImageService {

	private static final Logger logger = Logger.getLogger(MoleculeImageService.class);
	/*
	 * Keep small molecules from being inflated to fill the whole canvas.
	 */
	private static final int MAX_BOND_LENGTH = 50;
	private static final int MARGIN = 10;
	/*
	 * Plain structural formula without stereo annotations (abs, R/S) and their colors.
	 */
	private static final int DISPLAY_MODE = AbstractDepictor.cDModeSuppressChiralText //
			| AbstractDepictor.cDModeSuppressESR //
			| AbstractDepictor.cDModeSuppressCIPParity //
			| AbstractDepictor.cDModeNoColorOnESRAndCIP;

	@Activate
	public void start() {

		logger.info("Service started: " + getName());
	}

	@Override
	public String getName() {

		return "OpenChemLib";
	}

	@Override
	public String getVersion() {

		Version version = FrameworkUtil.getBundle(getClass()).getVersion();
		return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}

	@Override
	public String getDescription() {

		return "This service draws the structural formula from the molfile or SMILES using OpenChemLib.";
	}

	@Override
	public boolean isOnline() {

		return false;
	}

	@Override
	public Image create(Display display, ILibraryInformation libraryInformation, int width, int height) {

		if(width <= 2 * MARGIN || height <= 2 * MARGIN) {
			return null;
		}

		StereoMolecule molecule = MoleculeParser.parse(libraryInformation);
		if(molecule == null) {
			return null;
		}

		Image image = new Image(display, width, height);
		GC gc = new GC(image);
		SWTDepictor depictor = new SWTDepictor(molecule, DISPLAY_MODE);
		try {
			gc.setAntialias(SWT.ON);
			gc.setTextAntialias(SWT.ON);
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillRectangle(0, 0, width, height);
			depictor.setForegroundColor(0x000000, 0xFFFFFF);
			GenericRectangle viewRect = new GenericRectangle(MARGIN, MARGIN, width - 2 * MARGIN, height - 2 * MARGIN);
			depictor.validateView(gc, viewRect, AbstractDepictor.cModeInflateToMaxAVBL | MAX_BOND_LENGTH);
			depictor.paint(gc);
			return image;
		} catch(RuntimeException e) {
			logger.warn(e);
			image.dispose();
			return null;
		} finally {
			depictor.dispose();
			gc.dispose();
		}
	}

	@Override
	public Class<? extends IWorkbenchPreferencePage> getPreferencePage() {

		return null;
	}
}
