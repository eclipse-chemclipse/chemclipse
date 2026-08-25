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
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Describes a single instrument configuration that was used. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#dataProcessingList
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "methods"})
public class DataProcessingMethod {

	/**
	 * A unique identifier for the data processing method.
	 */
	@JsonProperty("id")
	@JsonPropertyDescription("A unique identifier for the data processing method.")
	private String id;

	@JsonProperty("methods")
	private List<ProcessingMethod> methods = new ArrayList<ProcessingMethod>();

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public List<ProcessingMethod> getMethods() {

		return methods;
	}

	public void setMethods(List<ProcessingMethod> methods) {

		this.methods = methods;
	}
}
