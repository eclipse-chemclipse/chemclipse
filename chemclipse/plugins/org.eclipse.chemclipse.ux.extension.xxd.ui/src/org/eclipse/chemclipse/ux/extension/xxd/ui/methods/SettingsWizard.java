/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - move method to open wizard, refactor for new settings
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.function.Supplier;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.NodeProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessSupplierContext;
import org.eclipse.chemclipse.processing.supplier.ProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessorPreferences.DialogBehavior;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class SettingsWizard extends Wizard {

	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 400;

	private SettingsWizard(String title) {
		setWindowTitle(title);
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {

		return true;
	}

	/**
	 * Opens a wizard to edit the given preferences if the user confirms the given {@link ProcessorPreferences} are updated via the public set methods
	 * 
	 * @param shell
	 * @param preferences
	 * @return <code>true</code> if user has confirmed, <code>false</code> otherwise
	 * @throws IOException
	 */
	public static <T> boolean openEditPreferencesWizard(Shell shell, ProcessorPreferences<T> preferences) throws IOException {

		IProcessSupplier<T> processorSupplier = preferences.getSupplier();
		SettingsWizard wizard = new SettingsWizard("Edit Processor Options");
		SettingsPreferencesPage<T> page = new SettingsPreferencesPage<>(preferences);
		page.setTitle("Select options to use for " + processorSupplier.getName());
		page.setMessage(processorSupplier.getDescription());
		wizard.addPage(page);
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.setMinimumPageSize(SettingsWizard.DEFAULT_WIDTH, SettingsWizard.DEFAULT_HEIGHT);
		if(wizardDialog.open() == WizardDialog.OK) {
			preferences.setAskForSettings(!page.getIsDontAskAgainEdited());
			boolean useSystem = page.getIsUseSystemDefaultsEdited();
			if(useSystem) {
				preferences.setUseSystemDefaults(true);
			} else {
				preferences.setUseSystemDefaults(false);
				preferences.setUserSettings(page.getSettingsEdited());
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Opens a wizard to edit the preferences stored inside the given {@link ProcessTypeSupport}
	 * 
	 * @param shell
	 * @param processTypeSupport
	 */
	public static void openManagePreferencesWizard(Shell shell, Supplier<Collection<ProcessorPreferences<?>>> preferenceSupplier) {

		SettingsWizard wizard = new SettingsWizard("Manage Processor Options");
		SettingsPreferencesEditPage page = new SettingsPreferencesEditPage(preferenceSupplier);
		page.setTitle("Manage Preferences");
		page.setDescription("Below you find all currently stored processor Options, select one to manage or remove the stored state");
		wizard.addPage(page);
		WizardDialog wizardDialog = new WizardDialog(shell, wizard) {

			@Override
			protected void createButtonsForButtonBar(Composite parent) {

				createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
			}

			@Override
			protected void buttonPressed(int buttonId) {

				cancelPressed();
			}

			@Override
			public void updateButtons() {

			}
		};
		wizardDialog.setMinimumPageSize(SettingsWizard.DEFAULT_WIDTH, SettingsWizard.DEFAULT_HEIGHT);
		wizardDialog.open();
	}

	/**
	 * Obtain the settings from the preferences, maybe asking the user for input
	 * 
	 * @param shell
	 * @param processorSupplier
	 * @return
	 * @throws IOException
	 *             if reading the settings failed
	 * @throws CancellationException
	 *             if user canceled the wizard
	 */
	public static <T> T getSettings(Shell shell, ProcessorPreferences<T> preferences) throws IOException, CancellationException {

		IProcessSupplier<T> processSupplier = preferences.getSupplier();
		Class<T> settingsClass = processSupplier.getSettingsClass();
		if(settingsClass == null) {
			return null;
		} else {
			if(preferences.getDialogBehaviour() == DialogBehavior.SHOW) {
				if(!processSupplier.getSettingsParser().getInputValues().isEmpty()) {
					if(!openEditPreferencesWizard(shell, preferences)) {
						throw new CancellationException("user has canceled the wizard");
					}
				}
			}
			return preferences.getSettings();
		}
	}

	/**
	 * 
	 * @param processorId
	 * @return the preferences for this processor id
	 */
	public static <T> ProcessorPreferences<T> getWorkspacePreferences(IProcessSupplier<T> supplier) {

		return new NodeProcessorPreferences<T>(supplier, getStorage().node(supplier.getId()));
	}

	private static IEclipsePreferences preferences;

	/**
	 * 
	 * @return all active preferences for this {@link ProcessTypeSupport}
	 */
	public static Collection<ProcessorPreferences<?>> getAllPreferences(ProcessSupplierContext context) {

		List<ProcessorPreferences<?>> result = new ArrayList<>();
		try {
			IEclipsePreferences storage = getStorage();
			String[] childrenNames = storage.childrenNames();
			for(String name : childrenNames) {
				Preferences node = storage.node(name);
				if(node.keys().length == 0) {
					// empty default node
					continue;
				}
				IProcessSupplier<?> processorSupplier = context.getSupplier(name);
				if(processorSupplier != null) {
					result.add(new NodeProcessorPreferences<>(processorSupplier, node));
				}
			}
		} catch(BackingStoreException e) {
			// can't load it then
		}
		return result;
	}

	private static IEclipsePreferences getStorage() {

		if(preferences == null) {
			preferences = InstanceScope.INSTANCE.getNode(IProcessSupplier.class.getName());
		}
		return preferences;
	}
}
