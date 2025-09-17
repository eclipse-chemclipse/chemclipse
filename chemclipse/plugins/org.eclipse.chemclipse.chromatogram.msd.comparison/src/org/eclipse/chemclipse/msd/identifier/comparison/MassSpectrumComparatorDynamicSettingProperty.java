/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Christoph Läubrich - complete refactoring
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.comparison;

import java.util.Collection;

import org.eclipse.chemclipse.msd.identifier.comparison.exceptions.NoMassSpectrumComparatorAvailableException;
import org.eclipse.chemclipse.support.settings.ComboSettingsProperty.ComboSupplier;

public class MassSpectrumComparatorDynamicSettingProperty implements ComboSupplier<IMassSpectrumComparisonSupplier> {

	@Override
	public Collection<IMassSpectrumComparisonSupplier> items() {

		return MassSpectrumComparator.getMassSpectrumComparatorSupport().getSuppliers();
	}

	@Override
	public IMassSpectrumComparisonSupplier fromString(String string) {

		if(string == null) {
			return null;
		}
		try {
			return MassSpectrumComparator.getMassSpectrumComparatorSupport().getMassSpectrumComparisonSupplier(string);
		} catch(NoMassSpectrumComparatorAvailableException e) {
			return null;
		}
	}

	@Override
	public String asString(IMassSpectrumComparisonSupplier item) {

		if(item == null) {
			return "";
		}
		return item.getId();
	}
}
