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
 * Christoph Läubrich - adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.chemclipse.model.core.IPeak;

public class PeakClassifierSupport {

	private static final String SEPARATOR = "|"; //$NON-NLS-1$

	/**
	 * Returns the classifier as String or "" if none is set yet.
	 * 
	 * @param object
	 * @return
	 */
	public static String getClassifier(Object object) {

		if(object instanceof IPeak peak) {
			List<String> classifier = new ArrayList<>(peak.getClassifiers());
			Collections.sort(classifier);
			return StringUtils.join(classifier, " " + SEPARATOR + " "); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return ""; //$NON-NLS-1$
	}

	public static void setClassifier(Object object, String content) {

		if(object instanceof IPeak peak) {
			if(content != null) {
				peak.clearClassifiers();
				for(String part : content.split("\\" + SEPARATOR)) { // Escape is required here. //$NON-NLS-1$
					String classification = part.trim();
					if(!classification.isEmpty()) {
						peak.addClassifier(classification);
					}
				}
			}
		}
	}
}
