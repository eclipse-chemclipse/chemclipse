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
package org.eclipse.chemclipse.converter.methods;

import org.eclipse.chemclipse.converter.core.IFileContentMatcher;
import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;

public interface IMethodSupplierSetter extends IMethodSupplier {

	void setId(final String id);

	void setDescription(final String description);

	void setFilterName(final String filterName);

	void setFileExtension(final String fileExtension);

	void setFileName(final String fileName);

	void setDirectoryExtension(final String directoryExtension);

	void setMagicNumberMatcher(final IMagicNumberMatcher magicNumberMatcher);

	public void setFileContentMatcher(IFileContentMatcher fileContentMatcher);
}
