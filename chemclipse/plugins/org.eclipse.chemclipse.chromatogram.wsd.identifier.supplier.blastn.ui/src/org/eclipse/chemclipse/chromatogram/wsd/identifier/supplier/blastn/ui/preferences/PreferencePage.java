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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.ui.preferences;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download.BlastDatabase;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download.BlastDatabaseDownloader;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download.Mirror;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.ui.Activator;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.ui.dialogs.BlastDatabaseDialog;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.text.ILabel;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.ButtonFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final Logger logger = Logger.getLogger(PreferencePage.class);

	private static final String TITLE = "Nucleotide BLAST";

	private DirectoryFieldEditor databaseFieldEditor;
	private ComboFieldEditor mirrorFieldEditor;

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle(TITLE);
		setDescription("Basic Local Alignment Search Tool");
	}

	@Override
	protected void createFieldEditors() {

		addField(new DirectoryFieldEditor(PreferenceSupplier.P_BLAST_EXECUTABLE_PATH, "BLAST installation path", getFieldEditorParent()));
		addField(new LabelFieldEditor("Uses PATH environment variable if left empty.", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		databaseFieldEditor = new DirectoryFieldEditor(PreferenceSupplier.P_BLAST_DATABASE_PATH, "BLAST database path", getFieldEditorParent());
		addField(databaseFieldEditor);
		addField(new LabelFieldEditor("Select the location where update_blastdb downloaded the databases to or download them here.", getFieldEditorParent()));
		mirrorFieldEditor = new ComboFieldEditor(PreferenceSupplier.P_BLAST_DATABASE_MIRROR, "Download mirror", ILabel.getOptions(Mirror.values()), getFieldEditorParent());
		addField(mirrorFieldEditor);
		ButtonFieldEditor downloadButton = new ButtonFieldEditor("Download or Update Databases...", getFieldEditorParent());
		addField(downloadButton);
		downloadButton.getButton().addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> downloadDatabases()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	private void downloadDatabases() {

		File folder = getDatabaseFolder();
		if(folder == null) {
			return;
		}

		/*
		 * The combo box value is only accessible through the preference store.
		 */
		mirrorFieldEditor.store();
		Mirror mirror = PreferenceSupplier.getDatabaseMirror();

		try {
			List<BlastDatabase> databases = new ArrayList<>();
			new ProgressMonitorDialog(getShell()).run(true, false, monitor -> {
				monitor.beginTask("Fetch the list of BLAST databases from " + mirror.label(), IProgressMonitor.UNKNOWN);
				try {
					databases.addAll(BlastDatabaseDownloader.getNucleotideDatabases(mirror));
				} catch(IOException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			});

			BlastDatabaseDialog dialog = new BlastDatabaseDialog(getShell(), databases, folder);
			if(dialog.open() != Window.OK || dialog.getSelection().isEmpty()) {
				return;
			}

			List<BlastDatabase> selection = dialog.getSelection();
			new ProgressMonitorDialog(getShell()).run(true, true, monitor -> {
				SubMonitor subMonitor = SubMonitor.convert(monitor, "Download BLAST databases", selection.size());
				for(BlastDatabase database : selection) {
					try {
						BlastDatabaseDownloader.download(database, folder, mirror, subMonitor.split(1));
					} catch(IOException e) {
						throw new InvocationTargetException(e);
					}
				}
			});
			databaseFieldEditor.store();
			MessageDialog.openInformation(getShell(), TITLE, "The BLAST databases have been downloaded to " + folder.getAbsolutePath() + ".");
		} catch(InvocationTargetException e) {
			logger.error(e.getCause());
			MessageDialog.openError(getShell(), TITLE, "Failed to download the BLAST databases: " + e.getCause().getMessage());
		} catch(InterruptedException e) {
			/*
			 * Canceled by the user. Completed volumes are kept and skipped on the next attempt.
			 */
			logger.warn(e);
		}
	}

	/**
	 * Returns the configured database folder or asks for one if none is set yet.
	 */
	private File getDatabaseFolder() {

		String path = databaseFieldEditor.getStringValue();
		if(path.isBlank()) {
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			directoryDialog.setText(TITLE);
			directoryDialog.setMessage("Select the folder to download the BLAST databases to.");
			path = directoryDialog.open();
			if(path == null) {
				return null;
			}
			databaseFieldEditor.setStringValue(path);
		}
		return new File(path);
	}
}
