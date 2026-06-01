/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.toolbar.GroupHandler;
import org.eclipse.chemclipse.ux.extension.xxd.ui.toolbar.GroupHandlerOverlay;
import org.eclipse.jface.preference.ComboFieldEditor;

public class PreferencePageTaskOverlay extends AbstractPreferencePageTask {

	public PreferencePageTaskOverlay() {

		super(GroupHandler.getGroupHandler(GroupHandlerOverlay.NAME));
	}

	@Override
	public void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_STACK_POSITION_OVERLAY_CHROMATOGRAM_EXTRA, "Overlay Chromatogram Extra:", PreferenceSupplier.PART_STACKS, getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		super.createFieldEditors();
	}
}
