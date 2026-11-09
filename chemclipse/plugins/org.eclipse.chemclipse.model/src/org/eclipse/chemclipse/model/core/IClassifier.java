/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring to ChemClipse style
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.util.Collection;

public interface IClassifier {

	Collection<String> getClassifiers();

	void clearClassifiers();

	void addClassifier(String classifier);

	void removeClassifier(String classifier);

	static String asString(IClassifier classifier) {

		return asString(classifier.getClassifiers());
	}

	static String asString(Iterable<? extends CharSequence> classifiers) {

		if(classifiers != null) {
			return String.join(", ", classifiers); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}
}