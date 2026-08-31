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

import java.util.Map;

import org.eclipse.chemclipse.model.core.ExtractTransferDefinition;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class ExtractTransferDefinitionListUI extends ExtendedTableViewer {

	private final Map<String, String> libraryFields;

	public ExtractTransferDefinitionListUI(Composite parent, int style, Map<String, String> libraryFields) {

		super(parent, style);
		this.libraryFields = libraryFields;
		createColumns();
	}

	private void createColumns() {

		TableViewerColumn colUse = new TableViewerColumn(this, SWT.CENTER);
		colUse.getColumn().setText("Use");
		colUse.getColumn().setWidth(40);
		colUse.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {

				if(element instanceof ExtractTransferDefinition definition) {
					String imageKey = definition.isUse() ? IApplicationImage.IMAGE_SELECTED : IApplicationImage.IMAGE_DESELECTED;
					return ApplicationImageFactory.getInstance().getImage(imageKey, IApplicationImageProvider.SIZE_16x16);
				}
				return null;
			}

			@Override
			public String getText(Object element) {

				return "";
			}
		});

		TableViewerColumn col1 = new TableViewerColumn(this, SWT.NONE);
		col1.getColumn().setText("Source Field");
		col1.getColumn().setWidth(150);
		col1.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ExtractTransferDefinition def) {
					return libraryFields.getOrDefault(def.getSourceField(), def.getSourceField());
				}
				return "";
			}
		});

		TableViewerColumn col2 = new TableViewerColumn(this, SWT.NONE);
		col2.getColumn().setText("Regular Expression");
		col2.getColumn().setWidth(200);
		col2.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ExtractTransferDefinition def) {
					return def.getRegularExpression();
				}
				return "";
			}
		});

		TableViewerColumn col3 = new TableViewerColumn(this, SWT.NONE);
		col3.getColumn().setText("Group Index");
		col3.getColumn().setWidth(80);
		col3.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ExtractTransferDefinition def) {
					return String.valueOf(def.getGroupIndex());
				}
				return "";
			}
		});

		TableViewerColumn col4 = new TableViewerColumn(this, SWT.NONE);
		col4.getColumn().setText("Data Type");
		col4.getColumn().setWidth(100);
		col4.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ExtractTransferDefinition def) {
					return def.getDataType() != null ? def.getDataType().label() : "";
				}
				return "";
			}
		});

		TableViewerColumn col5 = new TableViewerColumn(this, SWT.NONE);
		col5.getColumn().setText("Sink Field");
		col5.getColumn().setWidth(150);
		col5.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ExtractTransferDefinition definition) {
					return libraryFields.getOrDefault(definition.getSinkField(), definition.getSinkField());
				}
				return "";
			}
		});

		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
	}
}