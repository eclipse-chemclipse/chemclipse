/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.msd.ui.support;

import org.eclipse.chemclipse.msd.converter.database.DatabaseConverter;
import org.eclipse.chemclipse.processing.converter.AbstractSupplierFileIdentifier;

public class DatabaseIdentifier extends AbstractSupplierFileIdentifier {

	public DatabaseIdentifier() {
		super(DatabaseConverter.getDatabaseConverterSupport().getSupplier());
	}

	@Override
	public String getType() {

		return TYPE_DATABASE_MSD;
	}
}
