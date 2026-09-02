/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.IIntegrationSettings;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.support.traces.ITrace;

public interface IPeakIntegrationSettings extends IIntegrationSettings {

	/**
	 * Adds a report decider. A {@link IReportDecider} can set values at which a
	 * peak will not be reported.
	 * 
	 * @param reportDecider
	 */
	void addReportDecider(IReportDecider reportDecider);

	/**
	 * Removes a report decider.
	 * 
	 * @param reportDecider
	 */
	void removeReportDecider(IReportDecider reportDecider);

	/**
	 * Selected traces. It could be either m/z or wavelengths.
	 * If the traces are null, empty or ITrace.TOTAL_INTENSITY is stored,
	 * then use all available signals.
	 * 
	 * @return {@link IMarkedTraces}
	 */
	IMarkedTraces<ITrace> getMarkedTraces();

	/**
	 * Returns an IAreaSupport instance.<br/>
	 * You can edit in the area support, which parts of the integrated peak
	 * areas should be summed.
	 * 
	 * @return IAreaSupport
	 */
	IAreaSupport getAreaSupport();

	/**
	 * Returns the {@link IIntegrationSupport} instance.<br/>
	 * You can edit here, which parts should not be integrated.
	 * 
	 * @return IIntegrationSupport
	 */
	IIntegrationSupport getIntegrationSupport();

	/**
	 * Returns an object which gives information about the setting status of a
	 * particular peak.<br/>
	 * E.g. should the peak be integrated, should its area be summed on.
	 * 
	 * @param peak
	 * @return ISettingStatus
	 */
	ISettingStatus getSettingStatus(IPeak peak);
}
