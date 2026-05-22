/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core;

import java.util.List;
import java.util.Set;

public interface IIonTransitionSettings {

	/**
	 * Returns the ion transition. If it doesn't exists, a the element and the group will be created.
	 * Keep in mind that the transition group index is 0 based.
	 */
	IIonTransition getIonTransition(double filter1FirstIon, double filter1LastIon, double filter3FirstIon, double filter3LastIon, double collisionEnergy, double filter1Resolution, double filter3Resolution, int transitionGroup);

	IIonTransition getIonTransition(String compoundName, double filter1FirstIon, double filter1LastIon, double filter3FirstIon, double filter3LastIon, double collisionEnergy, double filter1Resolution, double filter3Resolution, int transitionGroup);

	/**
	 * Returns the ion transition. If it doesn't exists, a the element and the group will be created.
	 * Keep in mind that the transition group index is 0 based.
	 */
	IIonTransition getIonTransition(double filter1Ion, double filter3Ion, double collisionEnergy, double filter1Resolution, double filter3Resolution, int transitionGroup);

	/**
	 * Returns a new ion transition if an ion transition with the given compound name doesn't exist yet.
	 */
	IIonTransition getIonTransition(IIonTransition ionTransition, String compoundName);

	/**
	 * The index is 0 based.
	 * The method may return null.
	 */
	IIonTransitionGroup get(int index);

	/**
	 * Do not remove elements from the list.
	 * It will not affect the original list.
	 */
	List<IIonTransitionGroup> getIonTransitionGroups();

	/**
	 * Size of transition groups.
	 */
	int size();

	/**
	 * Returns a set of all ion transitions.
	 */
	Set<IIonTransition> getIonTransitions();
}
