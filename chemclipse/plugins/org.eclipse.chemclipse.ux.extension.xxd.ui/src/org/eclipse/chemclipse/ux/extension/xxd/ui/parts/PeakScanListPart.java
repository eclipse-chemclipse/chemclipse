/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - support connection between list and editor
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedPeakScanListUI;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class PeakScanListPart extends AbstractPart<ExtendedPeakScanListUI> {

	private static final Logger logger = Logger.getLogger(PeakScanListPart.class);
	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;
	//
	private boolean linkWithEditor = true;

	@Inject
	public PeakScanListPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	public static final class LinkWithEditorHandler {

		@Execute
		void execute(MPart part, MDirectToolItem toolItem) {

			Object object = part.getObject();
			if(object instanceof PeakScanListPart listPart) {
				listPart.linkWithEditor = toolItem.isSelected();
				listPart.updatePeakSelection(null);
			}
		}
	}

	@Override
	public void setFocus() {

		ExtendedPeakScanListUI control = getControl();
		if(control != null) {
			getControl().setFocus();
		}
	}

	@Inject
	@Optional
	public void updatePeakSelection(@UIEventTopic(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION) IPeak peak) {

		if(linkWithEditor) {
			getControl().updateSelection();
		}
	}

	@Override
	protected ExtendedPeakScanListUI createControl(Composite parent) {

		return new ExtendedPeakScanListUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(isCloseEvent(topic)) {
				getControl().updateChromatogramSelection(null);
				unloadData();
				return false;
			} else if(isChromatogramEvent(topic)) {
				if(object instanceof IChromatogramSelection) {
					IChromatogramSelection<?, ?> chromatogramSelection = (IChromatogramSelection<?, ?>)object;
					getControl().updateChromatogramSelection(chromatogramSelection);
					return true;
				}
			} else if(isUpdateEditorEvent(topic)) {
				logger.info(object);
				getControl().refreshTableViewer();
				return true;
			} else if(isIdentificationTopic(topic)) {
				getControl().updateChromatogramSelection();
				linkWithEditor = false; // TODO Workaround: This otherwise breaks the table selection after undo.
			}
		}
		//
		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isChromatogramEvent(topic) || isUpdateEditorEvent(topic) || isCloseEvent(topic) || isIdentificationTopic(topic);
	}

	private boolean isChromatogramEvent(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isUpdateEditorEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}

	private boolean isIdentificationTopic(String topic) {

		return IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION.equals(topic);
	}
}
