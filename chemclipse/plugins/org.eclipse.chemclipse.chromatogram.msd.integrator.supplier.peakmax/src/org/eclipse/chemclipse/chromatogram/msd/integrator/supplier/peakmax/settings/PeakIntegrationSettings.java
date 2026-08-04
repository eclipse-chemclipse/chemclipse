/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - Formatting
 * Matthias Mailänder - list ions to integrate
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.peakmax.settings;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.AbstractPeakIntegrationSettings;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.support.settings.StringSettingsProperty;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.eclipse.chemclipse.support.util.TraceSettingUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class PeakIntegrationSettings extends AbstractPeakIntegrationSettings {

	@JsonIgnore
	private static final String TIC = "0";

	@JsonProperty(value = "Ions to integrate", defaultValue = TIC)
	@JsonPropertyDescription(value = "List the ions to integrate, separated by a white space. 0 = TIC")
	@StringSettingsProperty(regExp = "(\\d+[;|\\s]?)+", description = "must be space separated digits.", isMultiLine = false, allowEmpty = false)
	private String ionsToIntegrate = TIC;
	@JsonProperty(value = "Area Constraint", defaultValue = "true")
	@JsonPropertyDescription(value = "If selected, calculated areas < 1 are set to 0.")
	private boolean useAreaConstraint = true;
	/*
	 * The selected ions are handled separately.
	 * They must not be persisted. If selected ions is
	 * empty, TIC will be integrated.
	 */
	@JsonIgnore
	private IMarkedTraces<ITrace> markedTraces = null;

	@Override
	public IMarkedTraces<ITrace> getMarkedTraces() {

		if(markedTraces == null) {
			markedTraces = super.getMarkedTraces();
			if(!ionsToIntegrate.equals(TIC)) {
				TraceSettingUtil ionSettingUtil = new TraceSettingUtil();
				int[] ions = ionSettingUtil.extractTraces(ionSettingUtil.deserialize(ionsToIntegrate));
				for(int ion : ions) {
					markedTraces.add(new TraceNominalMSD(ion));
				}
			}
		}

		return markedTraces;
	}

	public void setSelectedIon(String ionsToIntegrate) {

		this.ionsToIntegrate = ionsToIntegrate;
	}

	public boolean isUseAreaConstraint() {

		return useAreaConstraint;
	}

	public void setUseAreaConstraint(boolean useAreaConstraint) {

		this.useAreaConstraint = useAreaConstraint;
	}
}