/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring Observable
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.chemclipse.processing.filter.FilterContext;
import org.eclipse.chemclipse.ux.extension.xxd.ui.editors.EditorExtension;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class DynamicSettingsUI {

	private Composite parent;
	private Object layoutData;
	private Composite composite;

	public DynamicSettingsUI(Composite parent, Object layoutData) {

		this.parent = parent;
		this.layoutData = layoutData;
		parent.addDisposeListener(e -> disposeCurrent());
	}

	public <T, C> void setActiveContext(FilterContext<T, C> context, PropertyChangeListener observer) {

		disposeCurrent();
		if(context != null) {
			C config = context.getFilterConfig();
			if(config != null) {
				EditorExtension editorExtension = Adapters.adapt(config, EditorExtension.class);
				if(editorExtension != null) {
					composite = new Composite(parent, SWT.NONE);
					composite.setLayout(new FillLayout());
					composite.setLayoutData(layoutData);
					PropertyChangeSupport extension = editorExtension.createExtension(composite);
					if(observer != null) {
						composite.addDisposeListener(e -> extension.removePropertyChangeListener(observer));
						extension.addPropertyChangeListener(observer);
					}
				}
			}
		}
	}

	private void disposeCurrent() {

		if(composite != null) {
			composite.dispose();
			composite = null;
		}
	}
}