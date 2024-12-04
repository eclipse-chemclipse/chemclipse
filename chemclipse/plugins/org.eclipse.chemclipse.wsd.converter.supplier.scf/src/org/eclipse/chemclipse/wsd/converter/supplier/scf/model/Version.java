/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.model;

public class Version implements Comparable<Version> {

	private int major;
	private int revision;

	public Version(String version) {

		if(version == null || version.isEmpty()) {
			throw new IllegalArgumentException("Version string cannot be null or empty");
		}
		String[] parts = version.split("\\.");
		if(parts.length != 2) {
			throw new IllegalArgumentException("Version must follow 'version.revision' format");
		}
		this.major = Integer.parseInt(parts[0]);
		this.revision = Integer.parseInt(parts[1]);
	}

	public int getMajor() {

		return major;
	}

	public int getRevision() {

		return revision;
	}

	@Override
	public int compareTo(Version other) {

		if(this.major != other.major) {
			return Integer.compare(this.major, other.major);
		}
		return Integer.compare(this.revision, other.revision);
	}

	@Override
	public String toString() {

		return major + "." + revision;
	}
}
