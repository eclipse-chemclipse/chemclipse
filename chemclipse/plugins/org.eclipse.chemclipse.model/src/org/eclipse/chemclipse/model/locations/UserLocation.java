/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.locations;

import java.util.Objects;

public class UserLocation {

	private String name = "";
	private String path = "";

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

		return "UserLocation [name=" + name + ", path=" + path + "]";
	}
}