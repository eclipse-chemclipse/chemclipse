/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - select the rating symbol by score
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.provider;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.rcp.ui.icons.core.RatingImageSupport;
import org.eclipse.swt.graphics.Image;

public class IdentificationTargetSupport {

	private IdentificationTargetSupport() {

	}

	public static Image getRatingSymbol(IIdentificationTarget identificationTarget) {

		if(identificationTarget == null) {
			return null;
		}

		IComparisonResult comparisonResult = identificationTarget.getComparisonResult();
		String fileName = RatingImageSupport.getImageName(comparisonResult.getRatingSupplier().getScore());
		if(!fileName.isEmpty()) {
			return ApplicationImageFactory.getInstance().getImage(fileName, IApplicationImageProvider.SIZE_16x16);
		}

		return null;
	}
}
