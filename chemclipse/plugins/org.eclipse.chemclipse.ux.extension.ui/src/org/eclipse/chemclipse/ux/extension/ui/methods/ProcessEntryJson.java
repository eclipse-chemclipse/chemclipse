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
package org.eclipse.chemclipse.ux.extension.ui.methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.methods.IProcessEntry;
import org.eclipse.chemclipse.processing.methods.ProcessEntry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Converts process entries from and to a human readable JSON representation,
 * e.g. to exchange them via the clipboard.
 */
public class ProcessEntryJson {

	private static final String PROCESSOR_ID = "processorId";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String DATA_CATEGORIES = "dataCategories";
	private static final String SETTINGS = "settings";
	private static final String SKIP_VALIDATION = "skipValidation";
	private static final String ENTRIES = "entries";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private ProcessEntryJson() {

	}

	/**
	 * Returns a single JSON object for one entry and a JSON array for several entries.
	 */
	public static String toJson(List<IProcessEntry> processEntries) throws IOException {

		JsonNode jsonNode;
		if(processEntries.size() == 1) {
			jsonNode = toNode(processEntries.get(0));
		} else {
			ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
			for(IProcessEntry processEntry : processEntries) {
				arrayNode.add(toNode(processEntry));
			}
			jsonNode = arrayNode;
		}

		return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
	}

	/**
	 * Accepts a single JSON object or a JSON array of objects.
	 * The returned entries have no parent and need to be added to a container.
	 *
	 * @throws IOException
	 *             if the text is no valid process entry JSON
	 */
	public static List<IProcessEntry> fromJson(String json) throws IOException {

		JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
		if(jsonNode == null || jsonNode.isMissingNode()) {
			throw new IOException("The text is empty.");
		}

		List<IProcessEntry> processEntries = new ArrayList<>();
		if(jsonNode.isArray()) {
			for(JsonNode element : jsonNode) {
				processEntries.add(fromNode(element));
			}
		} else {
			processEntries.add(fromNode(jsonNode));
		}

		return processEntries;
	}

	private static ObjectNode toNode(IProcessEntry processEntry) {

		ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
		objectNode.put(PROCESSOR_ID, processEntry.getProcessorId());
		objectNode.put(NAME, processEntry.getName());
		objectNode.put(DESCRIPTION, processEntry.getDescription());

		ArrayNode dataCategories = objectNode.putArray(DATA_CATEGORIES);
		for(DataCategory dataCategory : processEntry.getDataCategories()) {
			dataCategories.add(dataCategory.name());
		}

		ObjectNode settings = objectNode.putObject(SETTINGS);
		for(Map.Entry<String, String> entry : processEntry.getSettingsMap().entrySet()) {
			settings.set(entry.getKey(), settingsToNode(entry.getValue()));
		}

		if(processEntry.isSkipValidation()) {
			objectNode.put(SKIP_VALIDATION, true);
		}

		if(processEntry.getNumberOfEntries() > 0) {
			ArrayNode entries = objectNode.putArray(ENTRIES);
			for(IProcessEntry childEntry : processEntry) {
				entries.add(toNode(childEntry));
			}
		}

		return objectNode;
	}

	private static IProcessEntry fromNode(JsonNode jsonNode) throws IOException {

		if(!jsonNode.isObject()) {
			throw new IOException("A process entry must be a JSON object.");
		}

		String processorId = jsonNode.path(PROCESSOR_ID).asText("");
		if(processorId.isBlank()) {
			throw new IOException("The process entry has no " + PROCESSOR_ID + ".");
		}

		ProcessEntry processEntry = new ProcessEntry(null);
		processEntry.setProcessorId(processorId);
		processEntry.setName(jsonNode.path(NAME).asText(""));
		processEntry.setDescription(jsonNode.path(DESCRIPTION).asText(""));
		processEntry.setSkipValidation(jsonNode.path(SKIP_VALIDATION).asBoolean(false));

		for(JsonNode dataCategory : jsonNode.path(DATA_CATEGORIES)) {
			try {
				processEntry.addDataCategory(DataCategory.valueOf(dataCategory.asText()));
			} catch(IllegalArgumentException e) {
				throw new IOException("Unknown data category: " + dataCategory.asText(), e);
			}
		}

		for(Map.Entry<String, JsonNode> entry : jsonNode.path(SETTINGS).properties()) {
			processEntry.setActiveProfile(entry.getKey());
			processEntry.setSettings(settingsFromNode(entry.getValue()));
		}
		processEntry.setActiveProfile(IProcessEntry.DEFAULT_PROFILE);

		for(JsonNode childEntry : jsonNode.path(ENTRIES)) {
			processEntry.addProcessEntry(fromNode(childEntry));
		}

		return processEntry;
	}

	/**
	 * The settings are stored as JSON strings. Embed them as JSON
	 * to keep the clipboard content readable.
	 */
	private static JsonNode settingsToNode(String settings) {

		if(settings != null && !settings.isBlank()) {
			try {
				JsonNode jsonNode = OBJECT_MAPPER.readTree(settings);
				if(jsonNode != null && jsonNode.isContainerNode()) {
					return jsonNode;
				}
			} catch(IOException e) {
				// Keep the original text.
			}
		}

		return OBJECT_MAPPER.getNodeFactory().textNode(settings == null ? "" : settings);
	}

	private static String settingsFromNode(JsonNode jsonNode) {

		if(jsonNode.isContainerNode()) {
			return jsonNode.toString();
		}

		return jsonNode.asText("");
	}
}
