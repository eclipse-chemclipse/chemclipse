/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.core;

public abstract class AbstractMeasurementResult<T> implements IMeasurementResult<T> {

	private static final long serialVersionUID = 6203262781404791751L;
	private String name = ""; //$NON-NLS-1$
	private String identifier = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$
	private final T result;

	public AbstractMeasurementResult(String name, String identifier, String description, T result) {

		this.name = name;
		this.identifier = identifier;
		this.description = description;
		this.result = result;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public String getIdentifier() {

		return identifier;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public T getResult() {

		return result;
	}

	@Override
	public String toString() {

		return "AbstractMeasurementResult [name=" + name + ", identifier=" + identifier + ", description=" + description + ", result=" + result + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		AbstractMeasurementResult<?> other = (AbstractMeasurementResult<?>)obj;
		if(identifier == null) {
			if(other.identifier != null) {
				return false;
			}
		} else if(!identifier.equals(other.identifier)) {
			return false;
		}
		return true;
	}
}
