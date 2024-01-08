/*******************************************************************************
 * Copyright (c) 2022, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - preference initializer
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.classification.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.xxd.classification.Activator;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String P_LIST_PATH_IMPORT = "listPathImport";
	public static final String DEF_LIST_PATH_IMPORT = "";
	public static final String P_LIST_PATH_EXPORT = "listPathExport";
	public static final String DEF_LIST_PATH_EXPORT = "";
	//
	private static IPreferenceSupplier preferenceSupplier;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public IScopeContext getScopeContext() {

		return InstanceScope.INSTANCE;
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getContext().getBundle().getSymbolicName();
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	@Override
	public Map<String, String> getDefaultValues() {

		Map<String, String> defaultValues = new HashMap<>();
		defaultValues.put(P_LIST_PATH_IMPORT, DEF_LIST_PATH_IMPORT);
		defaultValues.put(P_LIST_PATH_EXPORT, DEF_LIST_PATH_EXPORT);
		return defaultValues;
	}

	public static String getListPathImport() {

		return INSTANCE().get(P_LIST_PATH_IMPORT, DEF_LIST_PATH_IMPORT);
	}

	public static void setListPathImport(String filterPath) {

		INSTANCE().put(P_LIST_PATH_IMPORT, filterPath);
	}

	public static String getListPathExport() {

		return INSTANCE().get(P_LIST_PATH_EXPORT, DEF_LIST_PATH_EXPORT);
	}

	public static void setListPathExport(String filterPath) {

		INSTANCE().put(P_LIST_PATH_EXPORT, filterPath);
	}
}