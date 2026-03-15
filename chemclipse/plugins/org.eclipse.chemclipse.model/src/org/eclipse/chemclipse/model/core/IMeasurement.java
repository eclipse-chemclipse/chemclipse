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
 * Christoph Läubrich - add default file / dirty methods
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;

public interface IMeasurement extends IMeasurementInfo, IMeasurementResultSupport, IAdaptable {

	/**
	 * Measurements might adapt to different other specialized objects
	 */
	@Override
	default <T> T getAdapter(Class<T> adapter) {

		return null;
	}

	/**
	 *
	 * @return the file this measurement was loaded from or <code>null</code> if no file could be determined
	 */
	default File getFile() {

		return null;
	}

	default boolean isDirty() {

		return false;
	}

	default int getModCount() {

		return 0;
	}
}
