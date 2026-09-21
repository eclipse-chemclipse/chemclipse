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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"version", "id", "accession", "file_description", "instrument_configuration_list", "data_processing_method_list", "software_list", "sample_list", "cv_list", "run"})
public class Metadata {

	/**
	 * The mzPeak file format version, formatted as a semantic version
	 */
	@JsonProperty(value = "version", required = true)
	@JsonPropertyDescription("The mzPeak file format version, formatted as a semantic version")
	private String version;

	/**
	 * An optional id for the mzPeak archive used for referencing from external files. It is recommended to use LSIDs when possible.
	 */
	@JsonProperty("id")
	@JsonPropertyDescription("An optional id for the mzPeak archive used for referencing from external files. It is recommended to use LSIDs when possible.")
	private String id;

	/**
	 * An optional accession number for the mzPeak archive used for storage, e.g. in PRIDE.
	 */
	@JsonProperty("accession")
	@JsonPropertyDescription("An optional accession number for the mzPeak archive used for storage, e.g. in PRIDE.")
	private String accession;

	/**
	 * mzPeak metadata file description
	 * <p>
	 * Describe the JSON format of the file description section
	 */
	@JsonProperty("file_description")
	@JsonPropertyDescription("Describe the JSON format of the file description section")
	private FileDescription fileDescription;

	/**
	 * mzPeak metadata instrument configurations
	 * <p>
	 * Describe the JSON format of instrument configurations used to acquire a mass spectrometry experiment
	 */
	@JsonProperty("instrument_configuration_list")
	@JsonPropertyDescription("Describe the JSON format of instrument configurations used to acquire a mass spectrometry experiment")
	private List<InstrumentConfiguration> instrumentConfigurationList = new ArrayList<InstrumentConfiguration>();

	/**
	 * mzPeak metadata data processing method list
	 * <p>
	 * Describe the JSON format of data processing method list
	 */
	@JsonProperty("data_processing_method_list")
	@JsonPropertyDescription("Describe the JSON format of data processing method list")
	private List<DataProcessingMethod> dataProcessingMethodList = new ArrayList<DataProcessingMethod>();

	/**
	 * mzPeak metadata software list
	 * <p>
	 * Describe the JSON format of software list
	 */
	@JsonProperty("software_list")
	@JsonPropertyDescription("Describe the JSON format of software list")
	private List<Software> softwareList = new ArrayList<Software>();

	/**
	 * mzPeak metadata sample list
	 * <p>
	 * Describe the JSON format of the sample list. Multiple samples can be present in a single run in scenarios like multiplexing or pooling.
	 */
	@JsonProperty("sample_list")
	@JsonPropertyDescription("Describe the JSON format of the sample list. Multiple samples can be present in a single run in scenarios like multiplexing or pooling.")
	private List<Sample> sampleList = new ArrayList<Sample>();

	/**
	 * mzPeak controlled vocabulary list
	 * <p>
	 * Describe the JSON format of the controlled vocabulary list, analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#cvList
	 */
	@JsonProperty("cv_list")
	@JsonPropertyDescription("Describe the JSON format of the controlled vocabulary list, analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#cvList")
	private List<Cv> cvList = new ArrayList<Cv>();

	/**
	 * mzPeak metadata MS run
	 * <p>
	 * Describe the JSON format of the run-level metadata section, analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#run
	 */
	@JsonProperty("run")
	@JsonPropertyDescription("Describe the JSON format of the run-level metadata section, analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#run")
	private MsRun run;

	public String getVersion() {

		return version;
	}

	public void setVersion(String version) {

		this.version = version;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getAccession() {

		return accession;
	}

	public void setAccession(String accession) {

		this.accession = accession;
	}

	public FileDescription getFileDescription() {

		return fileDescription;
	}

	public void setFileDescription(FileDescription fileDescription) {

		this.fileDescription = fileDescription;
	}

	public List<InstrumentConfiguration> getInstrumentConfigurationList() {

		return instrumentConfigurationList;
	}

	public void setInstrumentConfigurationList(List<InstrumentConfiguration> instrumentConfigurationList) {

		this.instrumentConfigurationList = instrumentConfigurationList;
	}

	public List<DataProcessingMethod> getDataProcessingMethodList() {

		return dataProcessingMethodList;
	}

	public void setDataProcessingMethodList(List<DataProcessingMethod> dataProcessingMethodList) {

		this.dataProcessingMethodList = dataProcessingMethodList;
	}

	public List<Software> getSoftwareList() {

		return softwareList;
	}

	public void setSoftwareList(List<Software> softwareList) {

		this.softwareList = softwareList;
	}

	public List<Sample> getSampleList() {

		return sampleList;
	}

	public void setSampleList(List<Sample> sampleList) {

		this.sampleList = sampleList;
	}

	public List<Cv> getCvList() {

		return cvList;
	}

	public void setCvList(List<Cv> cvList) {

		this.cvList = cvList;
	}

	public MsRun getRun() {

		return run;
	}

	public void setRun(MsRun run) {

		this.run = run;
	}
}
