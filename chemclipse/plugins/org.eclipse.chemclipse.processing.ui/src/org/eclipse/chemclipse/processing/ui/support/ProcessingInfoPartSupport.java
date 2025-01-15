/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - use {@link IMessageProvider} interface, add support for E4 DI
 *******************************************************************************/
package org.eclipse.chemclipse.processing.ui.support;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IMessageProvider;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.ui.Activator;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

@Creatable
public class ProcessingInfoPartSupport {

	private static final Logger logger = Logger.getLogger(ProcessingInfoPartSupport.class);
	//
	private static final String TITLE = "Processing Error";
	private static final String MESSAGE = "Please check the 'Feedback' part.";
	//
	private UISynchronize uiSynchronize = null;
	private ProcessingInfoUpdateNotifier processingInfoUpdateNotifier = null;

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
		 * Log the errors.
		 */
		if(messageProvider.hasErrorMessages()) {
			for(IProcessingMessage message : messageProvider.getMessages()) {
				if(message.getMessageType() == MessageType.ERROR) {
					logger.error(message.getDescription() + ": " + message.getMessage(), message.getException());
				}
			}
		}
		/*
		 * Update the message.
		 */
		try {
			getProcessingInfoUpdateNotifier().update(messageProvider);
		} catch(RuntimeException e) {
			logger.error("Calling the info update notifier failed.", e);
		}
		/*
		 * Display a message if an error occurred.
		 */
		if(messageProvider.hasErrorMessages()) {
			if(displayDialogOnError) {
				getUISynchronize().asyncExec(new Runnable() {

					@Override
					public void run() {

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
							try {
								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(IPerspectiveAndViewIds.VIEW_FEEDBACK);
							} catch(PartInitException e) {
								logger.warn(e);
							}
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
		//
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
		//
		return uiSynchronize;
	}

	private ProcessingInfoUpdateNotifier getProcessingInfoUpdateNotifier() {

		if(processingInfoUpdateNotifier == null) {
			processingInfoUpdateNotifier = new ProcessingInfoUpdateNotifier();
		}
		//
		return processingInfoUpdateNotifier;
	}
}
