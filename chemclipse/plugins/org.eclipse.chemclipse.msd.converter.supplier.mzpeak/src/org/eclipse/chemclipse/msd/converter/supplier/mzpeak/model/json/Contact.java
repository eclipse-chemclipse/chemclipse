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
 * A person or entity that is responsible for some portion of the data or processing that resulted in this archive. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#contact
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"contact_name", "contact_affiliation", "parameters"})
public class Contact {

	/**
	 * The name of the contact person. This is equivalent to `MS:1000586|contact name` (http://purl.obolibrary.org/obo/MS_1000586)
	 */
	@JsonProperty("contact_name")
	@JsonPropertyDescription("The name of the contact person. This is equivalent to `MS:1000586|contact name` (http://purl.obolibrary.org/obo/MS_1000586)")
	private String contactName;

	/**
	 * The home institute of the contact person. This is equivalent to `MS:1000590|contact affiliation` (http://purl.obolibrary.org/obo/MS_1000590)
	 */
	@JsonProperty("contact_affiliation")
	@JsonPropertyDescription("The home institute of the contact person. This is equivalent to `MS:1000590|contact affiliation` (http://purl.obolibrary.org/obo/MS_1000590)")
	private String contactAffiliation;

	/**
	 * Parameters describing the contact, such as name, organization, email, website, or address.
	 */
	@JsonProperty(value = "parameters", required = true)
	@JsonPropertyDescription("Parameters describing the contact, such as name, organization, email, website, or address.")
	private List<Param> parameters = new ArrayList<Param>();

	public String getContactName() {

		return contactName;
	}

	public void setContactName(String contactName) {

		this.contactName = contactName;
	}

	public String getContactAffiliation() {

		return contactAffiliation;
	}

	public void setContactAffiliation(String contactAffiliation) {

		this.contactAffiliation = contactAffiliation;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
