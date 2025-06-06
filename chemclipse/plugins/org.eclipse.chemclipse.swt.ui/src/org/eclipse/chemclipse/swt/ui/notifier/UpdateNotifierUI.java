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
package org.eclipse.chemclipse.swt.ui.notifier;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.swt.widgets.Display;

public class UpdateNotifierUI {

	public static void update(Display display, String topic, Object object) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(topic, object));
		}
	}

	public static void update(Display display, IChromatogramSelection chromatogramSelection) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(chromatogramSelection));
		}
	}

	public static void update(Display display, IPeak peak) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(peak));
		}
	}

	public static void update(Display display, IScan scan) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(scan));
		}
	}

	public static void update(Display display, IIdentificationTarget identificationTarget) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(identificationTarget));
		}
	}

	public static void update(Display display, IScan scan1, IIdentificationTarget identificationTarget) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(scan1, identificationTarget));
		}
	}

	public static void update(Display display, IScan scan1, IScan scan2) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(scan1, scan2));
		}
	}

	public static void update(Display display, IEditHistory editHistory) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(editHistory));
		}
	}

	public static void update(Display display, IQuantitationCompound quantitationCompound) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(quantitationCompound));
		}
	}

	public static void update(Display display, ITargetSupplier targetSupplier) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.update(targetSupplier));
		}
	}

	public static void updateParts(Display display) {

		if(display != null) {
			display.asyncExec(() -> UpdateNotifier.updateParts());
		}
	}
}
