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
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * mzPeak metadata MS run
 * <p>
 * Describe the JSON format of the run-level metadata section, analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#run
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"parameters", "id", "default_data_processing_id", "default_instrument_id", "default_source_file_id", "start_time"})
public class MsRun {

	/**
	 * Parameters describing the run not otherwise covered by the attributes.
	 */
	@JsonProperty("parameters")
	@JsonPropertyDescription("Parameters describing the run not otherwise covered by the attributes.")
	private List<Param> parameters = new ArrayList<Param>();

	/**
	 * A unique identifier for the run
	 */
	@JsonProperty(value = "id", required = true)
	@JsonPropertyDescription("A unique identifier for the run")
	private String id;

	/**
	 * The default data processing identifier, as drawn from https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/data_processing.json
	 */
	@JsonProperty(value = "default_data_processing_id", required = true)
	@JsonPropertyDescription("The default data processing identifier, as drawn from https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/data_processing.json")
	private String defaultDataProcessingId;

	/**
	 * The default instrument configuration, as drawn from https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/instrument_configuration.json
	 */
	@JsonProperty(value = "default_instrument_id", required = true)
	@JsonPropertyDescription("The default instrument configuration, as drawn from https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/instrument_configuration.json")
	private Integer defaultInstrumentId;

	/**
	 * The default source file the content references, as drawn from https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/file_description.json
	 */
	@JsonProperty(value = "default_source_file_id", required = true)
	@JsonPropertyDescription("The default source file the content references, as drawn from https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/file_description.json")
	private String defaultSourceFileId;

	/**
	 * The time that data acquistion started, encoded in an RFC 3339 format (https://datatracker.ietf.org/doc/html/rfc3339)
	 */
	@JsonProperty("start_time")
	@JsonPropertyDescription("The time that data acquistion started, encoded in an RFC 3339 format (https://datatracker.ietf.org/doc/html/rfc3339)")
	private Date startTime = null;

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getDefaultDataProcessingId() {

		return defaultDataProcessingId;
	}

	public void setDefaultDataProcessingId(String defaultDataProcessingId) {

		this.defaultDataProcessingId = defaultDataProcessingId;
	}

	public Integer getDefaultInstrumentId() {

		return defaultInstrumentId;
	}

	public void setDefaultInstrumentId(Integer defaultInstrumentId) {

		this.defaultInstrumentId = defaultInstrumentId;
	}

	public String getDefaultSourceFileId() {

		return defaultSourceFileId;
	}

	public void setDefaultSourceFileId(String defaultSourceFileId) {

		this.defaultSourceFileId = defaultSourceFileId;
	}

	public Date getStartTime() {

		return startTime;
	}

	public void setStartTime(Date startTime) {

		this.startTime = startTime;
	}
}
