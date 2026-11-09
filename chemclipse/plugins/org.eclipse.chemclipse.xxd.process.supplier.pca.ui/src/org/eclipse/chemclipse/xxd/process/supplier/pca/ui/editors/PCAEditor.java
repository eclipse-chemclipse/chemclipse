/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - get rid of JavaFX
 * Lorenz Gerber - fix data update
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.editors;

import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt.AnalysisEditorUI;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class PCAEditor {

	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.xxd.process.supplier.pca.ui/org.eclipse.chemclipse.xxd.process.supplier.pca.ui.parts.PCAEditor"; //$NON-NLS-1$
	public static final String ICON_URI = ApplicationImageFactory.getInstance().getURI(IApplicationImage.IMAGE_CHROMATOGRAM, IApplicationImageProvider.SIZE_16x16);
	public static final String ID = "org.eclipse.chemclipse.xxd.process.supplier.pca.ui.parts.PCAEditor"; //$NON-NLS-1$
	public static final String LABEL = StatisticsMessages.pcaEditor;
	public static final String TOOL_TIPS = StatisticsMessages.pcaEditor;

	@Inject
	private Composite parent;
	@Inject
	private IEclipseContext context;

	private AnalysisEditorUI analysisEditorUI;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void createControl() {

		analysisEditorUI = new AnalysisEditorUI(parent, SWT.NONE);

		if(context != null) {
			IEvaluation<IVariable, ISample, IResult> evaluation = context.get(IEvaluation.class);
			if(evaluation != null) {
				analysisEditorUI.setInput(evaluation);
			} else {
				ISamplesPCA<IVariable, ISample> samples = context.get(ISamplesPCA.class);
				analysisEditorUI.setInput(samples);
			}
		}
	}

	@PreDestroy
	public void preDestroy() {

		if(context != null) {
			context.remove(ISamplesPCA.class);
			context.remove(IEvaluation.class);
		}

		List<String> clearTopics = Arrays.asList(IChemClipseEvents.TOPIC_PCA_UPDATE_SELECTION);
		UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_EDITOR_PCA_CLOSE, clearTopics);
	}

	@Focus
	public void setFocus() {

		analysisEditorUI.setFocus();
	}
}