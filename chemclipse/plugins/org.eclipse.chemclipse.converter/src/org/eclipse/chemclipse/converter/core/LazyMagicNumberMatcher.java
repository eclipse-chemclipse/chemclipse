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
package org.eclipse.chemclipse.converter.core;

import java.io.File;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Defers creating the contributed matcher until a file is checked,
 * so listing the converters does not load classes or start bundles.
 */
class LazyMagicNumberMatcher implements IMagicNumberMatcher {

	private final IConfigurationElement element;
	private IMagicNumberMatcher delegate;
	private boolean created;

	LazyMagicNumberMatcher(IConfigurationElement element) {

		this.element = element;
	}

	@Override
	public boolean checkFileFormat(File file) {

		IMagicNumberMatcher magicNumberMatcher = getDelegate();
		if(magicNumberMatcher != null) {
			return magicNumberMatcher.checkFileFormat(file);
		}

		return false;
	}

	private synchronized IMagicNumberMatcher getDelegate() {

		if(!created) {
			try {
				delegate = (IMagicNumberMatcher)element.createExecutableExtension(Converter.IMPORT_MAGIC_NUMBER_MATCHER);
			} catch(Exception e) {
				delegate = null;
			}
			created = true;
		}

		return delegate;
	}
}
