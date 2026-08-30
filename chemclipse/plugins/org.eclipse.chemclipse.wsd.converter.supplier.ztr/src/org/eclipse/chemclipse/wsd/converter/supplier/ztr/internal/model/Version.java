/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.model;

public class Version implements Comparable<Version> {

	private byte major;
	private byte minor;

	public Version(byte major, byte minor) {

		this.major = major;
		this.minor = minor;
	}

	public int getMajor() {

		return major;
	}

	public int getMinor() {

		return minor;
	}

	@Override
	public int compareTo(Version other) {

		if(this.major != other.major) {
			return Integer.compare(this.major, other.major);
		}
		return Integer.compare(this.minor, other.minor);
	}

	@Override
	public String toString() {

		return major + "." + minor;
	}
}
