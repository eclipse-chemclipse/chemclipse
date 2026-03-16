/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring data type
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.processing.supplier.base.core;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.nmr.model.core.IMeasurementFID;
import org.eclipse.chemclipse.processing.DataCategory;

public abstract class AbstractFIDSignalFilter<ConfigType> extends AbstractComplexSignalFilter<ConfigType, IMeasurementFID> {

	private static final long serialVersionUID = -6422870258150962140L;

	protected AbstractFIDSignalFilter(Class<ConfigType> configClass) {

		super(configClass);
	}

	@Override
	public DataCategory[] getDataCategories() {

		return new DataCategory[]{DataCategory.NMR};
	}

	@Override
	public boolean acceptsIMeasurement(IMeasurement item) {

		return item instanceof IMeasurementFID fidMeasurement && accepts(fidMeasurement);
	}

	protected boolean accepts(IMeasurementFID item) {

		return true;
	}
}
