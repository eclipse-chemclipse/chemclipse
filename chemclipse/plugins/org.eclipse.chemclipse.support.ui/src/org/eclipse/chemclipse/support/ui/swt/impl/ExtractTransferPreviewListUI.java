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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.swt.impl;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class ExtractTransferPreviewListUI extends ExtendedTableViewer {

	public ExtractTransferPreviewListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	private void createColumns() {

		TableViewerColumn col1 = new TableViewerColumn(this, SWT.NONE);
		col1.getColumn().setText("Source Value");
		col1.getColumn().setWidth(300);
		col1.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof String[] entry && entry.length >= 2) {
					return entry[1];
				}
				return "";
			}
		});

		TableViewerColumn colUse = new TableViewerColumn(this, SWT.CENTER);
		colUse.getColumn().setText("Use");
		colUse.getColumn().setWidth(40);
		colUse.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {

				if(element instanceof String[] entry && entry.length >= 1) {
					String imageKey = Boolean.parseBoolean(entry[0]) ? IApplicationImage.IMAGE_SELECTED : IApplicationImage.IMAGE_DESELECTED;
					return ApplicationImageFactory.getInstance().getImage(imageKey, IApplicationImageProvider.SIZE_16x16);
				}
				return null;
			}

			@Override
			public String getText(Object element) {

				return "";
			}
		});

		TableViewerColumn col2 = new TableViewerColumn(this, SWT.NONE);
		col2.getColumn().setText("Target Value");
		col2.getColumn().setWidth(300);
		col2.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof String[] entry && entry.length >= 3) {
					return entry[2];
				}
				return "";
			}
		});

		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
	}
}