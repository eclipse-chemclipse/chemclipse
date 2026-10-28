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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.ui.dialogs;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download.BlastDatabase;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download.BlastDatabaseDownloader;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download.InstallationStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class BlastDatabaseDialog extends TitleAreaDialog {

	private final List<BlastDatabase> databases;
	private final File folder;
	private final Map<BlastDatabase, InstallationStatus> statuses = new HashMap<>();

	private CheckboxTableViewer tableViewer;
	private List<BlastDatabase> selection = List.of();

	public BlastDatabaseDialog(Shell shell, List<BlastDatabase> databases, File folder) {

		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.databases = databases;
		this.folder = folder;
		for(BlastDatabase database : databases) {
			statuses.put(database, BlastDatabaseDownloader.getStatus(database, folder));
		}
	}

	public List<BlastDatabase> getSelection() {

		return selection;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Nucleotide BLAST");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Download BLAST Databases");
		setMessage("Select the NCBI nucleotide databases to download or update into " + folder.getAbsolutePath());

		Composite composite = (Composite)super.createDialogArea(parent);
		tableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 400;
		table.setLayoutData(gridData);

		createColumn("Database", 200, BlastDatabase::name);
		createColumn("Description", 350, BlastDatabase::description);
		createColumn("Volumes", 70, database -> Integer.toString(database.volumes()));
		createColumn("Download Size", 110, database -> BlastDatabaseDownloader.formatSize(database.downloadSize()));
		createColumn("Last Updated", 100, database -> formatDate(database.lastUpdated()));
		createColumn("Status", 120, database -> statuses.get(database).label());

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(databases);
		tableViewer.setCheckedElements(databases.stream().filter(database -> statuses.get(database) == InstallationStatus.UPDATE_AVAILABLE).toArray());
		tableViewer.addCheckStateListener(_ -> updateButtons());

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, "Download", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		updateButtons();
	}

	@Override
	protected void okPressed() {

		selection = Arrays.stream(tableViewer.getCheckedElements()).map(BlastDatabase.class::cast).toList();
		super.okPressed();
	}

	private void createColumn(String title, int width, Function<BlastDatabase, String> text) {

		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setText(title);
		column.getColumn().setWidth(width);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				return element instanceof BlastDatabase database ? text.apply(database) : "";
			}
		});
	}

	private void updateButtons() {

		getButton(IDialogConstants.OK_ID).setEnabled(tableViewer.getCheckedElements().length > 0);
	}

	private static String formatDate(String lastUpdated) {

		if(lastUpdated == null) {
			return "";
		}
		int index = lastUpdated.indexOf('T');
		return index > 0 ? lastUpdated.substring(0, index) : lastUpdated;
	}
}
