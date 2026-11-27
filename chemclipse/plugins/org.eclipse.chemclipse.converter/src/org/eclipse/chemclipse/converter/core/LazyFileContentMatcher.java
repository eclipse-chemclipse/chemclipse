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
class LazyFileContentMatcher implements IFileContentMatcher {

	private final IConfigurationElement element;
	private IFileContentMatcher delegate;

	LazyFileContentMatcher(IConfigurationElement element) {

		this.element = element;
	}

	@Override
	public boolean checkFileFormat(File file) {

		return getDelegate().checkFileFormat(file);
	}

	private synchronized IFileContentMatcher getDelegate() {

		if(delegate == null) {
			try {
				delegate = (IFileContentMatcher)element.createExecutableExtension(Converter.IMPORT_FILE_CONTENT_MATCHER);
			} catch(Exception e) {
				delegate = new NoFileContentMatcher(); // default to a dummy implementation that allows everything
			}
		}

		return delegate;
	}
}
