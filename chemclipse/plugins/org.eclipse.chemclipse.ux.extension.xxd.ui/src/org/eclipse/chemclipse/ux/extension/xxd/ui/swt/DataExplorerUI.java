/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - performance optimization and cleanup, refactor handling of Suppliers
 * Matthias Mailänder - right-click refresh option
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.converter.ISupplierFileIdentifier;
import org.eclipse.chemclipse.ux.extension.ui.model.DataExplorerTreeSettings;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierFileEditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.MultiDataExplorerTreeUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors.ProjectExplorerSupportFactory;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.GenericSupplierEditorSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.SupplierEditorSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageFileExplorer;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DataExplorerUI extends MultiDataExplorerTreeUI {

	private ISupplierFileIdentifier supplierFileIdentifier;

	public DataExplorerUI(Composite parent, ISupplierFileIdentifier supplierFileIdentifier) {

		super(parent, SWT.NONE, new DataExplorerTreeSettings(Activator.getDefault().getPreferenceStore()));
		//
		this.supplierFileIdentifier = supplierFileIdentifier;
		setSupplierFileEditorSupport();
	}

	@Override
	protected List<Class<? extends IPreferencePage>> addPreferencePages() {

		List<Class<? extends IPreferencePage>> preferencePages = new ArrayList<>();
		//
		preferencePages.add(PreferencePageFileExplorer.class);
		preferencePages.add(PreferencePage.class);
		//
		return preferencePages;
	}

	@Override
	protected void setSupplierFileEditorSupport() {

		IEclipseContext context = Activator.getDefault().getEclipseContext();
		IPreferenceStore preferenceStore = getDataExplorerTreeSettings().getPreferenceStore();
		List<ISupplierFileEditorSupport> editorSupportList = new ArrayList<>();
		/*
		 * MSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_MSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.MSD, () -> context));
		}
		/*
		 * MSD Library
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_LIBRARY_MSD)) {
			editorSupportList.add(org.eclipse.chemclipse.ux.extension.msd.ui.support.DatabaseSupport.getInstanceEditorSupport());
		}
		/*
		 * MSD Scan
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_SCANS_MSD)) {
			editorSupportList.add(org.eclipse.chemclipse.ux.extension.msd.ui.support.MassSpectrumSupport.getInstanceEditorSupport());
		}
		/*
		 * CSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_CSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.CSD, () -> context));
		}
		/*
		 * WSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_WSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.WSD, () -> context));
		}
		/*
		 * VSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_VSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.VSD, () -> context));
		}
		/*
		 * TSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_TSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.TSD, () -> context));
		}
		/*
		 * VSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_SCAN_VSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.SCAN_VSD, () -> context));
		}
		/*
		 * WSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_SCAN_WSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.SCAN_WSD, () -> context));
		}
		/*
		 * FSD
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_SCAN_FSD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.SCAN_FSD, () -> context));
		}
		/*
		 * NMR
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_NMR)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.NMR, () -> context));
		}
		/*
		 * CAL
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_CAL)) {
			editorSupportList.add(new ProjectExplorerSupportFactory(DataType.CAL).getInstanceEditorSupport());
		}
		/*
		 * PCR
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_PCR)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.PCR, () -> context));
		}
		/*
		 * SEQ
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_SEQUENCE)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.SEQ, () -> context));
		}
		/*
		 * MTH
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_METHOD)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.MTH, () -> context));
		}
		/*
		 * QDB
		 */
		if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_DATA_QUANT_DB)) {
			editorSupportList.add(new SupplierEditorSupport(DataType.QDB, () -> context));
		}
		//
		editorSupportList.add(new GenericSupplierEditorSupport(supplierFileIdentifier, () -> context));
		setSupplierFileIdentifier(editorSupportList);
		expandLastDirectoryPath();
	}
}