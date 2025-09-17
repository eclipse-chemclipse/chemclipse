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
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add method to get/set masspectrum comparator
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.settings;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.identifier.AbstractIdentifierDeltaPenaltyCalculationSettings;
import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparator;
import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparisonSupplier;
import org.eclipse.chemclipse.msd.identifier.comparison.MassSpectrumComparator;
import org.eclipse.chemclipse.msd.identifier.comparison.MassSpectrumComparatorDynamicSettingProperty;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.support.settings.ComboSettingsProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class AbstractIdentifierSettingsMSD extends AbstractIdentifierDeltaPenaltyCalculationSettings implements IMassSpectrumComparatorSettings, IExcludedIonsSettings {

	@JsonProperty(value = "Mass Spectrum Comparator", defaultValue = DEFAULT_COMPARATOR_ID)
	@JsonPropertyDescription(value = "Select the algorithm used for mass spectrum comparison calculation.")
	@ComboSettingsProperty(MassSpectrumComparatorDynamicSettingProperty.class)
	private String massSpectrumComparatorId = DEFAULT_COMPARATOR_ID;
	@JsonIgnore
	private IMarkedIons excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	@JsonIgnore
	private IMassSpectrumComparator comparator = null; // The comparator will be created dynamically.

	@Override
	public String getMassSpectrumComparatorId() {

		return massSpectrumComparatorId;
	}

	@Override
	public void setMassSpectrumComparatorId(String massSpectrumComparatorId) {

		comparator = null;
		this.massSpectrumComparatorId = massSpectrumComparatorId;
	}

	@Override
	public IMarkedIons getExcludedIons() {

		return excludedIons;
	}

	@JsonIgnore
	public IMassSpectrumComparator getMassSpectrumComparator() {

		if(comparator == null) {
			comparator = MassSpectrumComparator.getMassSpectrumComparator(getMassSpectrumComparatorId());
		}
		return comparator;
	}

	public void setMassSpectrumComparator(IMassSpectrumComparator comparator) {

		IMassSpectrumComparisonSupplier supplier = comparator.getMassSpectrumComparisonSupplier();
		if(supplier != null) {
			this.massSpectrumComparatorId = supplier.getId();
		}
		this.comparator = comparator;
	}
}
