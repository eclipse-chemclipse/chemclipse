/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.support.settings.UserManagement;
import org.eclipse.chemclipse.ux.extension.ui.Activator;

public class PreferenceSupplierDataExplorer extends AbstractPreferenceSupplier {

	public static final String P_SELECTED_DRIVE_PATH = "selectedDrivePath";
	public static final String DEF_SELECTED_DRIVE_PATH = "";
	public static final String P_SELECTED_HOME_PATH = "selectedHomePath";
	public static final String DEF_SELECTED_HOME_PATH = "";
	public static final String P_SELECTED_WORKSPACE_PATH = "selectedWorkspacePath";
	public static final String DEF_SELECTED_WORKSPACE_PATH = "";
	public static final String P_SELECTED_USER_LOCATION_PATH = "selectedUserLocationPath";
	public static final String DEF_SELECTED_USER_LOCATION_PATH = "";

	public static final String P_USER_LOCATION_PATH = "userLocation";
	public static final String DEF_USER_LOCATION_PATH = UserManagement.getUserHome();

	public static final String P_OPEN_FIRST_DATA_MATCH_ONLY = "openFirstDataMatchOnly";
	public static final boolean DEF_OPEN_FIRST_DATA_MATCH_ONLY = true;
	public static final String P_OPEN_EDITOR_MULTIPLE_TIMES = "openEditorMultipleTimes";
	public static final boolean DEF_OPEN_EDITOR_MULTIPLE_TIMES = true;
	public static final String P_USER_LOCATIONS = "userLocations";
	public static final String DEF_USER_LOCATIONS = "";

	public static final String P_USER_LOCATIONS_TEMPLATE_FOLDER = "userLocationsTemplateFolder";
	public static final String DEF_USER_LOCATIONS_TEMPLATE_FOLDER = "";

	/*
	 * Performance related workarounds. Set to false for less pretty but faster.
	 */
	public static final String P_FILTER_FILES = "filterFiles";
	public static final boolean DEF_FILTER_FILES = true;

	public static final String P_SHOW_ICONS = "showIcons";
	public static final boolean DEF_SHOW_ICONS = true;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplierDataExplorer.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_SELECTED_DRIVE_PATH, DEF_SELECTED_DRIVE_PATH);
		putDefault(P_SELECTED_HOME_PATH, DEF_SELECTED_HOME_PATH);
		putDefault(P_SELECTED_WORKSPACE_PATH, DEF_SELECTED_WORKSPACE_PATH);
		putDefault(P_SELECTED_USER_LOCATION_PATH, DEF_SELECTED_USER_LOCATION_PATH);
		putDefault(P_USER_LOCATION_PATH, DEF_USER_LOCATION_PATH);
		putDefault(P_OPEN_FIRST_DATA_MATCH_ONLY, DEF_OPEN_FIRST_DATA_MATCH_ONLY);
		putDefault(P_OPEN_EDITOR_MULTIPLE_TIMES, DEF_OPEN_EDITOR_MULTIPLE_TIMES);
		putDefault(P_USER_LOCATIONS, DEF_USER_LOCATIONS);

		putDefault(P_USER_LOCATIONS_TEMPLATE_FOLDER, DEF_USER_LOCATIONS_TEMPLATE_FOLDER);

		putDefault(P_FILTER_FILES, DEF_FILTER_FILES);
		putDefault(P_SHOW_ICONS, DEF_SHOW_ICONS);
	}

	public static String getSelectedDrivePath() {

		return INSTANCE().get(P_SELECTED_DRIVE_PATH);
	}

	public static void setSelectedDrivePath(String directoryPath) {

		INSTANCE().set(P_SELECTED_DRIVE_PATH, directoryPath);
	}

	public static String getSelectedHomePath() {

		return INSTANCE().get(P_SELECTED_HOME_PATH);
	}

	public static void setSelectedHomePath(String directoryPath) {

		INSTANCE().set(P_SELECTED_HOME_PATH, directoryPath);
	}

	public static String getSelectedWorkspacePath() {

		return INSTANCE().get(P_SELECTED_WORKSPACE_PATH);
	}

	public static void setSelectedWorkspaceath(String directoryPath) {

		INSTANCE().set(P_SELECTED_WORKSPACE_PATH, directoryPath);
	}

	public static String getSelectedUserLocationPath() {

		return INSTANCE().get(P_SELECTED_USER_LOCATION_PATH);
	}

	public static void setSelectedUserLocationPath(String directoryPath) {

		INSTANCE().set(P_SELECTED_USER_LOCATION_PATH, directoryPath);
	}

	public static String getUserLocationPath() {

		return INSTANCE().get(P_USER_LOCATION_PATH);
	}

	public static void setUserLocationPath(String directoryPath) {

		INSTANCE().set(P_USER_LOCATION_PATH, directoryPath);
	}

	public static boolean isOpenFirstDataMatchOnly() {

		return INSTANCE().getBoolean(P_OPEN_FIRST_DATA_MATCH_ONLY);
	}

	public static boolean isOpenEditorMultipleTimes() {

		return INSTANCE().getBoolean(P_OPEN_EDITOR_MULTIPLE_TIMES);
	}

	public static String getUserLocations() {

		return INSTANCE().get(P_USER_LOCATIONS);
	}

	public static void setUserLocations(String userLocations) {

		INSTANCE().set(P_USER_LOCATIONS, userLocations);
	}

	public static String getUserLocationsTemplateFolder() {

		return INSTANCE().get(P_USER_LOCATIONS_TEMPLATE_FOLDER);
	}

	public static void setUserLocationsTemplateFolder(String filterPath) {

		INSTANCE().set(P_USER_LOCATIONS_TEMPLATE_FOLDER, filterPath);
	}

	public static boolean filterFiles() {

		return INSTANCE().getBoolean(P_FILTER_FILES);
	}

	public static boolean showIcons() {

		return INSTANCE().getBoolean(P_SHOW_ICONS);
	}
}