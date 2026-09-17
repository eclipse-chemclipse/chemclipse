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
package org.eclipse.chemclipse.ux.extension.dsd.ui.parts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.dsd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.dsd.ui.swt.ExtendedNucleotideSequenceUI;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class NucleotideSequencePart extends AbstractPart<ExtendedNucleotideSequenceUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;

	private Set<String> topics = new HashSet<>();

	@Inject
	public NucleotideSequencePart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
		initializeTopics();
	}

	@Override
	protected ExtendedNucleotideSequenceUI createControl(Composite parent) {

		return new ExtendedNucleotideSequenceUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		/*
		 * 0 => because only one property was used to register the event.
		 */
		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			if(object instanceof IChromatogramSelection chromatogramSelection) {
				getControl().updateInput(chromatogramSelection);
				return true;
			} else {
				if(topic.equals(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE)) {
					getControl().updateInput(null);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return topics.contains(topic);
	}

	private void initializeTopics() {

		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE);
	}
}
