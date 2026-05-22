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
 * Philip Wenig - support instruments
 *******************************************************************************/
package org.eclipse.chemclipse.processing.methods;

import java.util.Iterator;
import java.util.Set;

/**
 * An IProcessEntryContainer holds some {@link IProcessEntry}s
 *
 */
public interface IProcessEntryContainer extends Iterable<IProcessEntry> {

	/**
	 * Empty string "" is used for backward compatibility.
	 * Don't change this.
	 */
	String DEFAULT_PROFILE = "Default Profile";
	int DEFAULT_RESUME_INDEX = 0; // Process all items.

	/**
	 *
	 * @return an informative name describing the container
	 */
	String getName();

	/**
	 * Returns the active profile.
	 */
	String getActiveProfile();

	/**
	 * Sets the active profile.
	 */
	void setActiveProfile(String activeProfile);

	void addProfile(String profile);

	void deleteProfile(String profile);

	/**
	 * Returns the unmodifiable set of profiles.
	 */
	Set<String> getProfiles();

	/**
	 * return an informative description of this container
	 */
	default String getDescription() {

		return "";
	}

	/**
	 * This flag defines if the process method supports the resume operation.
	 * Both resume and profile selection will be checked.
	 *
	 * This option needs to be activated on purpose.
	 */
	boolean isSupportResume();

	void setSupportResume(boolean supportResume);

	int getResumeIndex();

	void setResumeIndex(int resumeIndex);

	int getNumberOfEntries();

	/**
	 * Compares that this all contained {@link IProcessEntry}s are equal to the other one given, the default implementation works as follows:
	 * <ol>
	 * <li>if other is null, and this container does not contains any entry, <code>true</code> is returned</li>
	 * <li>if any entry is not contentEquals to the other one <code>false</code> is returned</li>
	 * <li>if any of the iterator return more elements than the other <code>false</code> is returned
	 * </ol>
	 *
	 * this method is different to {@link java.lang.Object#equals(Object)} that it does compares for user visible properties to be equal in contrast to objects identity and it allows to compare different instance type, this also means that it is not required that
	 * Object1.contentEquals(Object2} == Object2.contentEquals(Object1}
	 */
	default boolean entriesEquals(IProcessEntryContainer other) {

		Iterator<IProcessEntry> thisEntries = iterator();
		if(other == null) {
			return !thisEntries.hasNext();
		}
		Iterator<IProcessEntry> otherEntries = other.iterator();
		while(thisEntries.hasNext() && otherEntries.hasNext()) {
			IProcessEntry thisEntry = thisEntries.next();
			IProcessEntry otherEntry = otherEntries.next();
			if(!thisEntry.contentEquals(otherEntry)) {
				return false;
			}
		}
		if(otherEntries.hasNext() || thisEntries.hasNext()) {
			// not all where consumed
			return false;
		}
		return true;
	}
}