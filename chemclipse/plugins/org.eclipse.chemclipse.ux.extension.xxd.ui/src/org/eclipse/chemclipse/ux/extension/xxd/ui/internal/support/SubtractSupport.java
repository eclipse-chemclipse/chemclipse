/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.support;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.calculator.SubtractCalculator;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.FrameworkUtil;

public class SubtractSupport {

	public static IScanMSD getSessionSubtractMassSpectrum() {

		IEclipseContext context = EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(SubtractCalculator.class).getBundleContext());
		if(context.get("SessionSubtractMassSpectrum") instanceof IScanMSD scanMSD) {
			return scanMSD;
		}
		return null;
	}
}
