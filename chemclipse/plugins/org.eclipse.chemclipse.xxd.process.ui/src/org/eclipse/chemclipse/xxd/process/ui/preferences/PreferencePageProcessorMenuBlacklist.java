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
package org.eclipse.chemclipse.xxd.process.ui.preferences;

import org.eclipse.chemclipse.xxd.process.ui.l10n.ProcessMessages;
import org.eclipse.chemclipse.xxd.process.ui.support.ProcessorMenuBlacklist;
import org.eclipse.chemclipse.xxd.process.ui.toolbar.ProcessorBlacklistSelectionUI;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageProcessorMenuBlacklist extends PreferencePage implements IWorkbenchPreferencePage {

	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 600;

	private ProcessorBlacklistSelectionUI processorSelectionUI;

	public PreferencePageProcessorMenuBlacklist() {

		setTitle(ProcessMessages.processorMenuBlacklist);
		setDescription(ProcessMessages.processorMenuBlacklistDescription);
		noDefaultAndApplyButton();
		setSize(new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected Control createContents(Composite parent) {

		processorSelectionUI = new ProcessorBlacklistSelectionUI(parent, SWT.NONE);
		processorSelectionUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		processorSelectionUI.setInput(ProcessorMenuBlacklist.getProcessors());
		return processorSelectionUI;
	}

	@Override
	public boolean performOk() {

		if(processorSelectionUI != null) {
			ProcessorMenuBlacklist.setProcessors(processorSelectionUI.getProcessors());
		}

		return true;
	}
}
