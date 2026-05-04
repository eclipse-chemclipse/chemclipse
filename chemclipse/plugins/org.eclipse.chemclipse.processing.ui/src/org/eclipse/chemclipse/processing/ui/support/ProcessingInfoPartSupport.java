/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - use {@link IMessageProvider} interface, add support for E4 DI
 *******************************************************************************/
package org.eclipse.chemclipse.processing.ui.support;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IMessageProvider;
import org.eclipse.chemclipse.processing.ui.Activator;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import jakarta.inject.Inject;

@Creatable
public class ProcessingInfoPartSupport {

	private static final Logger logger = Logger.getLogger(ProcessingInfoPartSupport.class);

	private static final String TITLE = "Processing Error";
	private static final String MESSAGE = "Please check the 'Feedback' part.";

	private UISynchronize uiSynchronize = null;
	@Inject
	private ProcessingInfoUpdateNotifier processingInfoUpdateNotifier;

	@Inject
	private IEclipseContext context;

	/**
	 * Use getInstance() instead or create this support via:
	 * <code>ContextInjectionFactory.make(ProcessingInfoViewSupport.class, eclipseContext);</code>
	 */
	public ProcessingInfoPartSupport() {

	}

	public static ProcessingInfoPartSupport getInstance() {

		return Activator.getDefault().getProcessingInfoPartSupport();
	}

	public void update(final IMessageProvider messageProvider, final boolean focusProcessingInfoPart) {

		update(messageProvider, true, focusProcessingInfoPart);
	}

	/**
	 * Update the message provider and show the processing info part on demand.
	 *
	 * @param messageProvider
	 * @param focusProcessingInfoPart
	 */
	public void update(final IMessageProvider messageProvider, final boolean displayDialogOnError, final boolean focusProcessingInfoPart) {

		if(messageProvider == null) {
			return;
		}
		/*
		 * Update the message.
		 */
		try {
			processingInfoUpdateNotifier.update(messageProvider);
		} catch(RuntimeException e) {
			logger.error("Calling the info update notifier failed.", e);
		}
		/*
		 * Display a message if an error occurred.
		 */
		if(messageProvider.hasErrorMessages()) {
			if(displayDialogOnError) {
				getUISynchronize().asyncExec(() -> {

					Shell shell = DisplayUtils.getShell();
					if(shell != null) {
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
						messageBox.setText(TITLE);
						messageBox.setMessage(MESSAGE);
						messageBox.open();
					}
					/*
					 * Focus the view if requested, this will open the feedback view if required.
					 */
					if(focusProcessingInfoPart) {
						//lazy resolve EPartService instead of injecting it so there is time for EPartService to be initialized
						EPartService windowPartService = context.get(EPartService.class);
						if(windowPartService != null) {
							windowPartService.showPart(IPerspectiveAndViewIds.VIEW_FEEDBACK, PartState.VISIBLE);
						}
					}
				});
			}
		}
	}

	/**
	 * Updates the messages and focus on the processing error part automatically.
	 *
	 * @param messageProvider
	 */
	public void update(final IMessageProvider messageProvider) {

		if(messageProvider == null) {
			return;
		}

		update(messageProvider, messageProvider.hasErrorMessages());
	}

	private UISynchronize getUISynchronize() {

		if(uiSynchronize == null) {
			uiSynchronize = new UISynchronize() {

				@Override
				public void syncExec(Runnable runnable) {

					Display.getDefault().syncExec(runnable);
				}

				@Override
				public void asyncExec(Runnable runnable) {

					Display.getDefault().asyncExec(runnable);
				}

				@Override
				protected boolean isUIThread(Thread thread) {

					return false;
				}

				@Override
				protected void showBusyWhile(Runnable runnable) {

				}

				@Override
				protected boolean dispatchEvents() {

					return false;
				}
			};
		}

		return uiSynchronize;
	}

}
