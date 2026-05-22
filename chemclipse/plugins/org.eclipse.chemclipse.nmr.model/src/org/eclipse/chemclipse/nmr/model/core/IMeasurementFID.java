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
 * Philip Wenig - get rid of system settings
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.model.core;

import java.util.List;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;

public interface IMeasurementFID extends IComplexSignalMeasurement<ISignalFID> {

	AcquisitionParameter getAcquisitionParameter();

	/**
	 * Contains the signals of this {@link IMeasurementFID}, ordered with the lowest data time first
	 *
	 * @return the signals that makes up this {@link IMeasurementFID}
	 */
	@Override
	List<? extends ISignalFID> getSignals();

	DataDimension getDataDimension();

	@Override
	default String getFilterStatistics() {

		return "";
	}
}
