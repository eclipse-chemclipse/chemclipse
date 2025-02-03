/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.parts;

import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractPart<T extends Composite> extends AbstractUpdater<T> {

	/**
	 * Create the part with the given main topic.
	 * 
	 * @param parent
	 * @param topic
	 */
	protected AbstractPart(Composite parent, String topic, DataUpdateSupport dataUpdateSupport) {

		super(topic, dataUpdateSupport);
		setControl(createControl(parent));
	}

	/**
	 * Create the control.
	 * 
	 * @param parent
	 * @return T
	 */
	protected abstract T createControl(Composite parent);
}
