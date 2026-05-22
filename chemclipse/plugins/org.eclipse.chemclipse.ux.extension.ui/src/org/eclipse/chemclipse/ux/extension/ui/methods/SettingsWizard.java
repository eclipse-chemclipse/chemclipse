/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - move method to open wizard, refactor for new settings
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.methods;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.function.Supplier;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences.DialogBehavior;
import org.eclipse.chemclipse.ux.extension.ui.l10n.ExtensionMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class SettingsWizard extends Wizard {

	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 600;

	private SettingsWizard(String title) {

		setWindowTitle(title);
		setNeedsProgressMonitor(false);
	}

	@Override
	public boolean performFinish() {

		return true;
	}

	/**
	 * Opens a wizard to edit the given preferences if the user confirms the given {@link IProcessorPreferences} are updated via the public set methods.
	 * Returns false if the user pressed CANCEL.
	 *
	 * @return <code>true</code> if user has confirmed, <code>false</code> otherwise
	 */
	public static <T> boolean openEditPreferencesWizard(Shell shell, IProcessorPreferences<T> preferences, boolean showProfileToolbar) {

		IProcessSupplier<T> processorSupplier = preferences.getSupplier();
		SettingsWizard settingsWizard = new SettingsWizard(ExtensionMessages.editProcessorOptions);

		SettingsPreferencesPage<T> settingsPreferencePage = new SettingsPreferencesPage<>(preferences, showProfileToolbar);
		settingsPreferencePage.setTitle(MessageFormat.format(ExtensionMessages.selectOptionsForProcessorName, processorSupplier.getName()));
		settingsPreferencePage.setMessage(processorSupplier.getDescription());
		settingsWizard.addPage(settingsPreferencePage);

		WizardDialog wizardDialog = new WizardDialog(shell, settingsWizard);
		wizardDialog.setMinimumPageSize(SettingsWizard.DEFAULT_WIDTH, SettingsWizard.DEFAULT_HEIGHT);
		if(wizardDialog.open() == Window.OK) {
			preferences.setAskForSettings(!settingsPreferencePage.getIsDontAskAgainEdited());
			preferences.setUseSystemDefaults(false);
			preferences.setUserSettings(settingsPreferencePage.getSettingsEdited());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Opens a wizard to edit the preferences for the given collection of processor preferences
	 */
	public static void openManagePreferencesWizard(Shell shell, Supplier<Collection<IProcessorPreferences<?>>> preferenceSupplier) {

		SettingsWizard wizard = new SettingsWizard(ExtensionMessages.manageProcessorOptions);
		SettingsPreferencesEditPage page = new SettingsPreferencesEditPage(preferenceSupplier);
		page.setTitle(ExtensionMessages.managePreferences);
		page.setDescription(ExtensionMessages.processorOptionsBelowSelectToManageRemoveState);
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
	 * @return the preferences to use or <code>null</code> if user canceled the wizard
	 * @throws IOException
	 *             if reading the settings failed
	 */
	public static <T> IProcessorPreferences<T> getSettings(Shell shell, IProcessorPreferences<T> preferences, boolean showProfileToolbar) throws IOException {

		IProcessSupplier<T> processSupplier = preferences.getSupplier();
		Class<T> settingsClass = processSupplier.getSettingsClass();
		if(settingsClass == null) {
			return preferences;
		} else {
			if(preferences.getDialogBehaviour() == DialogBehavior.SHOW) {
				if(!processSupplier.getSettingsParser().getInputValues().isEmpty()) {
					if(!openEditPreferencesWizard(shell, preferences, showProfileToolbar)) {
						return null;
					}
				}
			}
			return preferences;
		}
	}
}