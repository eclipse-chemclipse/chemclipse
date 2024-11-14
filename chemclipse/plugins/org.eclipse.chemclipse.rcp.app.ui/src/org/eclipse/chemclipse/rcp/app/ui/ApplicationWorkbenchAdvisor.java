/*******************************************************************************
 * Copyright (c) 2008, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - exclude unrelated import/export wizards
 *******************************************************************************/
package org.eclipse.chemclipse.rcp.app.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.chemclipse.support.ui.workbench.WorkbenchAdvisorSupport;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

@SuppressWarnings("restriction")
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.perspective.main";
	/*
	 * Hide all Java IDE related wizards by default, except:
	 * org.eclipse.ui.wizards.import.ExternalProject
	 * org.eclipse.ui.wizards.import.Preferences
	 * org.eclipse.ui.wizards.export.Preferences
	 */
	private static final String D_SHOW_ECLIPSE_WIZARDS = "application.show.eclipse.wizards";
	private static final String IMPORT_EXPORT_WIZARDS = "org\\.eclipse\\.ui\\.wizards\\.(import|export)\\.(?![Preferences|ExternalProject]).*";
	private static final String EQUINOX_WIZARDS = "org\\.eclipse\\.equinox\\.p2\\.replication.*";
	private static final String TEAM_E4_WIZARDS = "org\\.eclipse\\.(team|e4)\\.ui.*";
	private static final String ECLIPSE_DEBUG = "org\\.eclipse\\.debug.*";

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {

		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {

		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
		WorkbenchAdvisorSupport.declareProjectExplorerImages(configurer);
		/*
		 * Test if a property is set to keep the wizards.
		 */
		boolean removeEclipseWizards = true;
		Properties properties = System.getProperties();
		Object showEclipseWizards = properties.get(D_SHOW_ECLIPSE_WIZARDS);
		if(showEclipseWizards != null) {
			if(Boolean.valueOf(showEclipseWizards.toString())) {
				removeEclipseWizards = false;
			}
		}
		/*
		 * Remove
		 */
		if(removeEclipseWizards) {
			WorkbenchPlugin workbenchPlugin = WorkbenchPlugin.getDefault();
			removeWizards(workbenchPlugin.getImportWizardRegistry());
			removeWizards(workbenchPlugin.getExportWizardRegistry());
		}
	}

	@Override
	public String getInitialWindowPerspectiveId() {

		return PERSPECTIVE_ID;
	}

	@Override
	public IAdaptable getDefaultPageInput() {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot();
	}

	private void removeWizards(IWizardRegistry wizardRegistry) {

		if(wizardRegistry instanceof AbstractExtensionWizardRegistry abstractWizardRegistry) {
			IWizardCategory[] categories = wizardRegistry.getRootCategory().getCategories();
			for(IWizardDescriptor wizard : getAllWizards(categories)) {
				if(wizard instanceof WorkbenchWizardElement wizardElement) {
					if(removeWizard(wizardElement)) {
						abstractWizardRegistry.removeExtension(wizardElement.getConfigurationElement().getDeclaringExtension(), new Object[]{wizardElement});
					}
				}
			}
		}
	}

	private boolean removeWizard(IWizardDescriptor wizard) {

		String id = wizard.getId();
		/*
		 * Inspect the ids here.
		 */
		return id.matches(IMPORT_EXPORT_WIZARDS) || //
				id.matches(EQUINOX_WIZARDS) || //
				id.matches(TEAM_E4_WIZARDS) || //
				id.matches(ECLIPSE_DEBUG); //
	}

	private IWizardDescriptor[] getAllWizards(IWizardCategory[] categories) {

		List<IWizardDescriptor> results = new ArrayList<>();
		for(IWizardCategory wizardCategory : categories) {
			results.addAll(Arrays.asList(wizardCategory.getWizards()));
			results.addAll(Arrays.asList(getAllWizards(wizardCategory.getCategories())));
		}
		//
		return results.toArray(new IWizardDescriptor[0]);
	}
}