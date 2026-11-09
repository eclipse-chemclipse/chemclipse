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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.quickstart;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.ux.extension.ui.definitions.ITileDefinition;
import org.eclipse.chemclipse.xxd.process.supplier.pca.io.WorkspaceIO;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.handlers.CreatePcaEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.component.annotations.Component;

import jakarta.inject.Named;

@Component(service = ITileDefinition.class)
public class WorkspaceTileDefinition implements ITileDefinition {

	private static final Logger logger = Logger.getLogger(WorkspaceTileDefinition.class);

	@Override
	public String getTitle() {

		return StatisticsMessages.workspace;
	}

	@Override
	public String getDescription() {

		return StatisticsMessages.loadSavedPcaWorkspace;
	}

	@Override
	public String getPreferredPerspective() {

		return CreatePcaEvaluation.PCA_PERSPECTIVE;
	}

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IEclipseContext context) {

		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setText(StatisticsMessages.loadPcaWorkspace);
		fileDialog.setFilterExtensions(WorkspaceIO.FILTER_EXTENSION);
		fileDialog.setFilterNames(WorkspaceIO.FILTER_NAME);
		String path = fileDialog.open();
		if(path != null) {
			try {
				IEvaluation<IVariable, ISample, IResult> workspace = WorkspaceIO.read(new File(path));
				if(workspace != null && workspace.getSamples() != null) {
					CreatePcaEvaluation.createPart(workspace, context, new File(path).getName());
				} else {
					MessageDialog.openWarning(shell, StatisticsMessages.pcaWorkspace, StatisticsMessages.workspaceFileInvalid);
				}
			} catch(IOException e) {
				logger.warn(e);
				Throwable cause = e.getCause() != null ? e.getCause() : e;
				MessageDialog.openError(shell, StatisticsMessages.pcaWorkspace, NLS.bind(StatisticsMessages.failedToLoadWorkspace, cause.getMessage()));
			}
		}
	}
}
