/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.targets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TargetListUtil {

	public static final String EXAMPLE = "Styrene | 100-42-5 | comment | contributor | referenceId"; //$NON-NLS-1$
	public static final String SEPARATOR_TOKEN = ";"; //$NON-NLS-1$
	public static final String SEPARATOR_ENTRY = "|"; //$NON-NLS-1$

	public String createList(String[] items) {

		List<String> list = getValues(items);
		String values = ""; //$NON-NLS-1$
		for(String value : list) {
			values = values.concat(value + SEPARATOR_TOKEN);
		}
		return values;
	}

	public String[] parseString(String stringList) {

		String[] decodedArray;
		if(stringList.contains(SEPARATOR_TOKEN)) {
			decodedArray = stringList.split(SEPARATOR_TOKEN);
		} else {
			decodedArray = new String[]{stringList};
		}
		return decodedArray;
	}

	public List<String> getList(String preferenceEntry) {

		List<String> values = new ArrayList<>();
		if(!"".equals(preferenceEntry)) { //$NON-NLS-1$
			String[] items = parseString(preferenceEntry);
			if(items.length > 0) {
				for(String item : items) {
					values.add(item);
				}
			}
		}
		Collections.sort(values);
		return values;
	}

	private List<String> getValues(String[] items) {

		List<String> values = new ArrayList<>();
		if(items != null) {
			int size = items.length;
			for(int i = 0; i < size; i++) {
				values.add(items[i]);
			}
		}
		return values;
	}
}
