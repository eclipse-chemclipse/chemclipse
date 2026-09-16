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
import org.eclipse.jface.bindings.keys.KeyStroke;
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

		KeyStroke keyStroke = getKeyStroke();
		if(keyStroke != null) {
			return Character.toLowerCase(keyStroke.getNaturalKey());
		}

		return 0;
	}

	@Override
	public int getStateMask() {

		KeyStroke keyStroke = getKeyStroke();
		if(keyStroke != null) {
			return keyStroke.getModifierKeys();
		}

		return 0;
	}

	private KeyStroke getKeyStroke() {

		TriggerSequence triggerSequence = bindingService.getBestActiveBindingFor(command);
		if(triggerSequence instanceof KeySequence keySequence) {
			KeyStroke[] keyStrokes = keySequence.getKeyStrokes();
			if(keyStrokes.length > 0) {
				return keyStrokes[0];
			}
		}
		return null;
	}
}
