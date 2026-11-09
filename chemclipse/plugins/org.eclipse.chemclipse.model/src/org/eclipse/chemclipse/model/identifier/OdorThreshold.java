/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import java.util.Objects;

public class OdorThreshold implements IOdorThreshold {

	private static final long serialVersionUID = -426496543275664637L;

	private String content = ""; //$NON-NLS-1$
	private String unit = ""; //$NON-NLS-1$

	public OdorThreshold(String content, String unit) {

		this.content = content;
		this.unit = unit;
	}

	@Override
	public String getContent() {

		return content;
	}

	@Override
	public String getUnit() {

		return unit;
	}

	@Override
	public int hashCode() {

		return Objects.hash(content, unit);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		OdorThreshold other = (OdorThreshold)obj;
		return Objects.equals(content, other.content) && Objects.equals(unit, other.unit);
	}

	@Override
	public String toString() {

		return "OdorThreshold [content=" + content + ", unit=" + unit + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}