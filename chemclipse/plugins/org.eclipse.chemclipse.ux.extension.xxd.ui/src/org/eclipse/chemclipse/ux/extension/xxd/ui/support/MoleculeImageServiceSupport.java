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
package org.eclipse.chemclipse.ux.extension.xxd.ui.support;

import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.swt.ui.services.IMoleculeImageService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class MoleculeImageServiceSupport {

	public static Object[] getMoleculeImageServices() {

		return Activator.getDefault().getMoleculeImageServices();
	}

	public static IMoleculeImageService getDefaultMoleculeImageService() {

		return Activator.getDefault().getMoleculeImageService();
	}

	public static IMoleculeImageService getMoleculeImageServiceSelection() {

		return getMoleculeImageServiceSelection(getMoleculeImageServices());
	}

	public static IMoleculeImageService getMoleculeImageServiceSelection(Object[] moleculeImageServices) {

		IMoleculeImageService moleculeImageServiceSelection = null;
		if(moleculeImageServices.length > 0) {
			/*
			 * Get the stored service preference or the
			 * first available offline service.
			 */
			moleculeImageServiceSelection = getMoleculeImageServicesPreference(moleculeImageServices);
			if(moleculeImageServiceSelection == null) {
				moleculeImageServiceSelection = (IMoleculeImageService)moleculeImageServices[0];
			}
		}

		return moleculeImageServiceSelection;
	}

	public static ComboViewer createComboViewerServices(Composite composite) {

		ComboViewer comboViewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IMoleculeImageService moleculeImageService) {
					combo.setToolTipText(moleculeImageService.getDescription());
					return moleculeImageService.getName();
				}

				return null;
			}
		});
		/*
		 * Don't add the addSelectionListener(...) here to enable a
		 * more fine grained control how to update the molecule display.
		 */
		combo.setToolTipText("Select a service to create the molecule image.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 200;
		combo.setLayoutData(gridData);
		/*
		 * Input
		 */
		IMoleculeImageService moleculeImageService = null;
		Object[] moleculeImageServices = MoleculeImageServiceSupport.getMoleculeImageServices();

		if(moleculeImageServices != null) {
			comboViewer.setInput(moleculeImageServices);
			moleculeImageService = MoleculeImageServiceSupport.getDefaultMoleculeImageService();
			if(moleculeImageService != null) {
				comboViewer.setSelection(new StructuredSelection(moleculeImageService));
			}
		} else {
			comboViewer.setInput(null);
		}

		return comboViewer;
	}

	/*
	 * May return null.
	 */
	private static IMoleculeImageService getMoleculeImageServicesPreference(Object[] moleculeImageServices) {

		String imageServiceSelection = PreferenceSupplier.getMoleculeImageServiceId();
		for(int i = 0; i < moleculeImageServices.length; i++) {
			IMoleculeImageService moleculeImageService = (IMoleculeImageService)moleculeImageServices[i];
			if(moleculeImageService.getClass().getName().equals(imageServiceSelection)) {
				return moleculeImageService;
			}
		}

		return null;
	}
}
