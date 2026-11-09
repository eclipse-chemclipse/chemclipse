/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.locations;

import java.util.Objects;

public class UserLocation {

	private String name = ""; //$NON-NLS-1$
	private String path = ""; //$NON-NLS-1$

	public UserLocation(String name, String path) {

		this.name = name;
		this.path = path;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getPath() {

		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	@Override
	public int hashCode() {

		return Objects.hash(path);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		UserLocation other = (UserLocation)obj;
		return Objects.equals(path, other.path);
	}

	@Override
	public String toString() {

		return "UserLocation [name=" + name + ", path=" + path + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}