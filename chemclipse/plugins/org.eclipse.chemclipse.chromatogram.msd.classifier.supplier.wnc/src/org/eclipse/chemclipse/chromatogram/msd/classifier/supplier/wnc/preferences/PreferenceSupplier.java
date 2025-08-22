/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.l10n.Messages;
import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model.TargetTrace;
import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model.TargetTraces;
import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.settings.ClassifierSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String DEFAULT_TRACES = "Water:18;Nitrogen:28;Oxygen:32;Carbon Dioxide:44;Solvent Tailing:84;Column Bleed:207";

	public static final String P_TARGET_TRACES = "targetTraces"; //$NON-NLS-1$
	public static final String DEF_TARGET_TRACES = Messages.targetTraces;
	public static final String ENTRY_DELIMITER = ";"; //$NON-NLS-1$
	public static final String VALUE_DELIMITER = ":"; //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(PreferenceSupplier.class);
	private static IPreferenceSupplier preferenceSupplier = null;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_TARGET_TRACES, DEF_TARGET_TRACES);
	}

	public static ClassifierSettings getClassifierSettings() {

		ClassifierSettings classifierSettings = new ClassifierSettings();
		TargetTraces targetTraces = getTargetTraces();
		classifierSettings.getTargetTraces().add(targetTraces);
		return classifierSettings;
	}

	public static TargetTraces getTargetTraces() {

		return getTargetTraces(INSTANCE().get(P_TARGET_TRACES, DEF_TARGET_TRACES));
	}

	/**
	 * Returns a list of WNC ions to preserve stored in the settings.
	 * 
	 * @return List<IWNCIon>
	 */
	public static TargetTraces getTargetTraces(String content) {

		/*
		 * E.g. "water:18;nitrogen:28;carbon dioxide:44"
		 */
		TargetTraces ions = new TargetTraces();
		if(!content.isEmpty()) {
			String[] items = parseString(content);
			if(items.length > 0) {
				String name;
				Integer ion;
				String[] values;
				for(String item : items) {
					try {
						values = item.split(VALUE_DELIMITER);
						if(values.length > 1) {
							name = values[0];
							ion = Integer.parseInt(values[1]);
							ions.add(new TargetTrace(ion, name));
						}
					} catch(NumberFormatException e) {
						logger.warn(e);
					}
				}
			}
		}

		return ions;
	}

	/**
	 * Returns a string array.<br/>
	 * E.g. "water:18;nitrogen:28;carbon dioxide:44"
	 * 
	 * @param stringList
	 * @return String[]
	 */
	public static String[] parseString(String stringList) {

		String[] decodedArray;
		if(stringList.contains(ENTRY_DELIMITER)) {
			StringTokenizer stringTokenizer = new StringTokenizer(stringList, ENTRY_DELIMITER);
			int arraySize = stringTokenizer.countTokens();
			decodedArray = new String[arraySize];
			for(int i = 0; i < arraySize; i++) {
				decodedArray[i] = stringTokenizer.nextToken(ENTRY_DELIMITER);
			}
		} else {
			decodedArray = new String[1];
			decodedArray[0] = stringList;
		}
		return decodedArray;
	}

	/**
	 * Stores the ions.
	 * 
	 * @param targetTraces
	 */
	public static void storeTargetTraces(TargetTraces targetTraces) {

		String values = ""; //$NON-NLS-1$
		if(targetTraces != null) {
			StringBuilder builder = new StringBuilder();
			List<Integer> keys = new ArrayList<>(targetTraces.getKeys());
			int size = keys.size();
			if(size >= 1) {
				for(int index = 0; index < size; index++) {
					int ion = keys.get(index);
					TargetTrace targetTrace = targetTraces.getTargetTrace(ion);
					if(targetTrace != null) {
						builder.append(targetTrace.getName());
						builder.append(VALUE_DELIMITER);
						builder.append(targetTrace.getIon());
						builder.append(ENTRY_DELIMITER);
					}
				}
			}
			values = builder.toString();
		}

		INSTANCE().put(P_TARGET_TRACES, values);
	}
}