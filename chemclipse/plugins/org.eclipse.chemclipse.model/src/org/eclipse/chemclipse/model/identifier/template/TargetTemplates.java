/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.targets.TargetListUtil;

public class TargetTemplates extends HashMap<String, TargetTemplate> {

	private static final long serialVersionUID = -439374805911311705L;
	private static final Logger logger = Logger.getLogger(TargetTemplates.class);

	public static final String DESCRIPTION = "Target Templates"; //$NON-NLS-1$
	public static final String FILE_EXTENSION = ".txt"; //$NON-NLS-1$
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION; //$NON-NLS-1$
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")"; //$NON-NLS-1$ //$NON-NLS-2$

	private TargetListUtil targetListUtil = new TargetListUtil();

	public TargetTemplates() {

	}

	/**
	 * Initializes this templates from the given settings.
	 */
	public TargetTemplates(String targetTemplates) {

		load(targetTemplates);
	}

	public void add(TargetTemplate targetTemplate) {

		if(targetTemplate != null) {
			put(targetTemplate.getName(), targetTemplate);
		}
	}

	public void addAll(Collection<TargetTemplate> targetTemplates) {

		for(TargetTemplate targetTemplate : targetTemplates) {
			add(targetTemplate);
		}
	}

	public void load(String targetTemplates) {

		loadSettings(targetTemplates);
	}

	public void loadDefault(String targetTemplates) {

		loadSettings(targetTemplates);
	}

	public String save() {

		StringBuilder builder = new StringBuilder();
		Iterator<TargetTemplate> iterator = values().iterator();
		while(iterator.hasNext()) {
			TargetTemplate targetTemplate = iterator.next();
			extractTargetTemplate(targetTemplate, builder);
			if(iterator.hasNext()) {
				builder.append(TargetListUtil.SEPARATOR_TOKEN);
			}
		}
		return builder.toString().trim();
	}

	public void importItems(File file) {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = bufferedReader.readLine()) != null) {
				TargetTemplate template = extractTargetTemplate(line);
				if(template != null) {
					add(template);
				}
			}
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	public boolean exportItems(File file) {

		try {
			PrintWriter printWriter = new PrintWriter(file);
			Iterator<TargetTemplate> iterator = values().iterator();
			while(iterator.hasNext()) {
				StringBuilder builder = new StringBuilder();
				TargetTemplate template = iterator.next();
				extractTargetTemplate(template, builder);
				printWriter.println(builder.toString());
			}
			printWriter.flush();
			printWriter.close();
			return true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
			return false;
		}
	}

	private void loadSettings(String targetTemplates) {

		if(!"".equals(targetTemplates)) { //$NON-NLS-1$
			String[] items = targetListUtil.parseString(targetTemplates);
			if(items.length > 0) {
				for(String item : items) {
					TargetTemplate targetTemplate = extractTargetTemplate(item);
					if(targetTemplate != null) {
						add(targetTemplate);
					}
				}
			}
		}
	}

	public String extractTargetTemplate(TargetTemplate targetTemplate) {

		StringBuilder builder = new StringBuilder();
		extractTargetTemplate(targetTemplate, builder);
		return builder.toString();
	}

	public TargetTemplate extractTargetTemplate(String item) {

		TargetTemplate targetTemplate = null;

		if(!"".equals(item)) { //$NON-NLS-1$
			String[] values = item.split("\\" + TargetListUtil.SEPARATOR_ENTRY); //$NON-NLS-1$
			targetTemplate = new TargetTemplate();
			targetTemplate.setName((values.length > 0) ? values[0].trim() : ""); //$NON-NLS-1$
			targetTemplate.setCasNumber((values.length > 1) ? values[1].trim() : ""); //$NON-NLS-1$
			targetTemplate.setComments((values.length > 2) ? values[2].trim() : ""); //$NON-NLS-1$
			targetTemplate.setContributor((values.length > 3) ? values[3].trim() : ""); //$NON-NLS-1$
			targetTemplate.setReferenceId((values.length > 4) ? values[4].trim() : ""); //$NON-NLS-1$
		}

		return targetTemplate;
	}

	private void extractTargetTemplate(TargetTemplate targetTemplate, StringBuilder builder) {

		builder.append(targetTemplate.getName());
		builder.append(" "); //$NON-NLS-1$
		builder.append(TargetListUtil.SEPARATOR_ENTRY);
		builder.append(" "); //$NON-NLS-1$
		builder.append(targetTemplate.getCasNumber());
		builder.append(" "); //$NON-NLS-1$
		builder.append(TargetListUtil.SEPARATOR_ENTRY);
		builder.append(" "); //$NON-NLS-1$
		builder.append(targetTemplate.getComments());
		builder.append(" "); //$NON-NLS-1$
		builder.append(TargetListUtil.SEPARATOR_ENTRY);
		builder.append(" "); //$NON-NLS-1$
		builder.append(targetTemplate.getContributor());
		builder.append(" "); //$NON-NLS-1$
		builder.append(TargetListUtil.SEPARATOR_ENTRY);
		builder.append(" "); //$NON-NLS-1$
		builder.append(targetTemplate.getReferenceId());
	}
}
