/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 * Christoph Läubrich - support new lazy table model, support double-click
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.wizards;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.converter.ISupplierFileIdentifier;
import org.eclipse.chemclipse.ux.extension.ui.model.DataExplorerTreeSettings;
import org.eclipse.chemclipse.ux.extension.ui.swt.DataExplorerTreeRoot;
import org.eclipse.chemclipse.ux.extension.ui.swt.DataExplorerTreeUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.MultiDataExplorerTreeUI;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class InputEntriesWizardPage extends WizardPage {

	private final class WizardMultiDataExplorerTreeUI extends MultiDataExplorerTreeUI {

		private InputEntriesWizardPage page;

		private WizardMultiDataExplorerTreeUI(Composite parent, IPreferenceStore preferenceStore, InputEntriesWizardPage page) {

			super(parent, SWT.NONE, new DataExplorerTreeSettings(preferenceStore));
			this.page = page;
		}

		@Override
		protected void handleSelection(File[] files, DataExplorerTreeUI dataExplorerTreeUI) {

			dataExplorerTreeRoot = dataExplorerTreeUI.getRoot();
			selectedItems.clear();
			//
			for(File file : files) {
				Map<ISupplierFileIdentifier, Collection<ISupplier>> identifier = getIdentifierSupplier().apply(file);
				if(!identifier.isEmpty()) {
					selectedItems.put(file, identifier);
				}
			}
			//
			validate();
		}

		@Override
		protected String getUserLocationPreferenceKey() {

			String key = inputWizardSettings.getUserLocationPreferenceKey();
			if(key != null) {
				return key;
			}
			return super.getUserLocationPreferenceKey();
		}

		@Override
		protected void handleDoubleClick(File file) {

			IWizard wizard = page.getWizard();
			if(wizard.canFinish()) {
				wizard.performFinish();
			}
		}
	}

	private final InputWizardSettings inputWizardSettings;
	private DataExplorerTreeRoot dataExplorerTreeRoot = DataExplorerTreeRoot.NONE;
	private final Map<File, Map<ISupplierFileIdentifier, Collection<ISupplier>>> selectedItems = new HashMap<>();
	private MultiDataExplorerTreeUI multiDataExplorerTreeUI;

	public InputEntriesWizardPage(InputWizardSettings inputWizardSettings) {

		super(InputEntriesWizardPage.class.getName());
		this.inputWizardSettings = inputWizardSettings;
		//
		setTitle(inputWizardSettings.getTitle());
		setDescription(inputWizardSettings.getDescription());
		//
		validate();
	}

	private void validate() {

		if(selectedItems.isEmpty()) {
			setPageComplete(false);
			setErrorMessage("Please select at least one valid data item");
		} else {
			setPageComplete(true);
			setErrorMessage(null);
		}
	}

	public Map<File, Map<ISupplierFileIdentifier, Collection<ISupplier>>> getSelectedItems() {

		return selectedItems;
	}

	@Override
	public void createControl(Composite parent) {

		multiDataExplorerTreeUI = new WizardMultiDataExplorerTreeUI(parent, inputWizardSettings.getPreferenceStore(), this);
		multiDataExplorerTreeUI.setSupplierFileIdentifier(inputWizardSettings.getSupplierFileEditorSupportList());
		multiDataExplorerTreeUI.expandLastDirectoryPath();
		//
		setControl(multiDataExplorerTreeUI.getControl());
	}

	public DataExplorerTreeRoot getTreeSelection() {

		return dataExplorerTreeRoot;
	}

	public void savePath() {

		multiDataExplorerTreeUI.saveLastDirectoryPath();
	}
}