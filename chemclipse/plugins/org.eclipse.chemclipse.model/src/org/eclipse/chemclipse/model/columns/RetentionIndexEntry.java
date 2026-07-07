/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.columns;

public class RetentionIndexEntry implements IRetentionIndexEntry {

	private int retentionTime;
	private float retentionIndex;
	private String name;

	public RetentionIndexEntry(int retentionTime, float retentionIndex, String name) {

		this.retentionTime = retentionTime;
		this.retentionIndex = retentionIndex;
		this.name = name;
	}

	@Override
	public void copyFrom(IRetentionIndexEntry retentionIndexEntry) {

		this.retentionTime = retentionIndexEntry.getRetentionTime();
		this.retentionIndex = retentionIndexEntry.getRetentionIndex();
		this.name = retentionIndexEntry.getName();
	}

	@Override
	public int getRetentionTime() {

		return retentionTime;
	}

	@Override
	public float getRetentionIndex() {

		return retentionIndex;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public void setName(String name) {

		if(name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
	}

	@Override
	public boolean equals(Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		IRetentionIndexEntry other = (IRetentionIndexEntry)otherObject;
		return retentionTime == other.getRetentionTime() && retentionIndex == other.getRetentionIndex();
	}

	@Override
	public int hashCode() {

		return 7 * Integer.hashCode(retentionTime) + 11 * Float.hashCode(retentionIndex);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("retentionTime=" + retentionTime);
		builder.append(",");
		builder.append("retentionIndex=" + retentionIndex);
		builder.append(",");
		builder.append("name=" + name);
		builder.append("]");
		return builder.toString();
	}
}