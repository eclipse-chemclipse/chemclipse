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
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;
import org.eclipse.swtchart.extensions.events.AbstractHandledEventProcessor;
import org.eclipse.ui.keys.IBindingService;

public abstract class AbstractTriggerSequenceHandler extends AbstractHandledEventProcessor {

	private IBindingService bindingService;
	private String command;

	protected AbstractTriggerSequenceHandler(IBindingService bindingService, String command) {

		this.bindingService = bindingService;
		this.command = command;
	}

	@Override
	public int getEvent() {

		return IKeyboardSupport.EVENT_KEY_UP;
	}

	@Override
	public int getButton() {

		TriggerSequence triggerSequence = bindingService.getBestActiveBindingFor(command);
		if(triggerSequence instanceof KeySequence keysequence) {
			if(keysequence.getKeyStrokes().length > 0) {
				return keysequence.getKeyStrokes()[0].getNaturalKey();
			}
		}
		return 0;
	}

	@Override
	public int getStateMask() {

		TriggerSequence triggerSequence = bindingService.getBestActiveBindingFor(command);
		if(triggerSequence instanceof KeySequence keysequence) {
			if(keysequence.getKeyStrokes().length > 0) {
				return keysequence.getKeyStrokes()[0].getModifierKeys();
			}
		}
		return 0;
	}
}
