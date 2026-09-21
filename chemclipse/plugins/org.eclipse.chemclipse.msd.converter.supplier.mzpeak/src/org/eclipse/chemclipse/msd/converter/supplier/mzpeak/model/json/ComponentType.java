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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Describes an instrument component like the ion source, mass analyzer, or detector
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"component_type", "order", "parameters"})
public class ComponentType {

	/**
	 * The kind of component this is
	 */
	@JsonProperty(value = "component_type", required = true)
	@JsonPropertyDescription("The kind of component this is")
	private ComponentTypeKind componentType;

	/**
	 * The order in which the analytes travels through the component
	 */
	@JsonProperty(value = "order", required = true)
	@JsonPropertyDescription("The order in which the analytes travels through the component")
	private Integer order;

	/**
	 * Additional parameters describing this component, like the particular hardware type or components
	 */
	@JsonProperty(value = "parameters", required = true)
	@JsonPropertyDescription("Additional parameters describing this component, like the particular hardware type or components")
	private List<Param> parameters = new ArrayList<Param>();

	public ComponentTypeKind getComponentType() {

		return componentType;
	}

	public void setComponentType(ComponentTypeKind componentType) {

		this.componentType = componentType;
	}

	public Integer getOrder() {

		return order;
	}

	public void setOrder(Integer order) {

		this.order = order;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}

	/**
	 * The kind of component this is
	 */
	public enum ComponentTypeKind {

		IONSOURCE("ionsource"), ANALYZER("analyzer"), DETECTOR("detector");

		private final String value;
		private final static Map<String, ComponentTypeKind> CONSTANTS = new HashMap<String, ComponentTypeKind>();

		static {
			for(ComponentTypeKind c : values()) {
				CONSTANTS.put(c.value, c);
			}
		}

		ComponentTypeKind(String value) {

			this.value = value;
		}

		@Override
		public String toString() {

			return this.value;
		}

		@JsonValue
		public String value() {

			return this.value;
		}

		@JsonCreator
		public static ComponentTypeKind fromValue(String value) {

			ComponentTypeKind constant = CONSTANTS.get(value);
			if(constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}
	}
}
