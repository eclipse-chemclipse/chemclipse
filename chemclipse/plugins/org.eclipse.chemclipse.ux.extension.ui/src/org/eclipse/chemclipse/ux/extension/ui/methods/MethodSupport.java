/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.methods;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.methods.IProcessEntry;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ListProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.progress.core.InfoType;
import org.eclipse.chemclipse.progress.core.StatusLineLogger;
import org.eclipse.chemclipse.swt.ui.components.IMethodListener;
import org.eclipse.chemclipse.ux.extension.ui.l10n.ExtensionMessages;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

public class MethodSupport {

	/**
	 * 
	 * @param object
	 * @return the {@link ListProcessEntryContainer} for this object if available and not read-only, <code>null</code> otherwise
	 */
	public static final ListProcessEntryContainer getContainer(Object object) {

		if(object instanceof IProcessEntry processEntry) {
			ProcessEntryContainer parent = processEntry.getParent();
			if(parent instanceof ListProcessEntryContainer container) {
				if(!container.isReadOnly()) {
					return container;
				}
			}
		}
		return null;
	}

	public static void runMethod(IMethodListener methodListener, IProcessMethod processMethod, Shell shell) {

		if(methodListener != null && processMethod != null) {
			try {
				/*
				 * Resume at a given position?
				 */
				MethodParameters methodParameters = ResumeMethodSupport.selectMethodParameters(shell, processMethod);
				processMethod.setActiveProfile(methodParameters.getProfile());
				processMethod.setResumeIndex(methodParameters.getResumeIndex());
				//
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
				dialog.run(true, false, monitor -> methodListener.execute(processMethod, monitor));
			} catch(InvocationTargetException e) {
				IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
				processingInfo.addErrorMessage(processMethod.getName(), "Execution failed", e.getCause());
				StatusLineLogger.setInfo(InfoType.ERROR_MESSAGE, ExtensionMessages.processMethodFailedSeeFeedback);
				ProcessingInfoPartSupport.getInstance().update(processingInfo);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch(MethodCancelException e) {
				IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
				processingInfo.addWarnMessage(processMethod.getName(), ExtensionMessages.processMethodExecutionCanceled);
				ProcessingInfoPartSupport.getInstance().update(processingInfo);
			}
		}
	}
}
