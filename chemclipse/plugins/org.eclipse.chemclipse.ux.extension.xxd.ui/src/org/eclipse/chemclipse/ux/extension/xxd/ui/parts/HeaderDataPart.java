/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.model.core.IMeasurementInfo;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.IOverviewListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.OverviewSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedHeaderDataUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class HeaderDataPart extends AbstractPart<ExtendedHeaderDataUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;
	private final OverviewSupport overviewSupport = new OverviewSupport();

	@Inject
	public HeaderDataPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
		overviewSupport.setOverviewListener(new IOverviewListener() {

			@Override
			public void update(Object object) {

				if(object instanceof IMeasurementInfo measurementInfo) {
					getControl().setInput(measurementInfo);
				}
			}
		});
	}

	@Override
	protected ExtendedHeaderDataUI createControl(Composite parent) {

		return new ExtendedHeaderDataUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		return overviewSupport.process(objects, topic);
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return overviewSupport.isUpdateTopic(topic);
	}
}
