/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.editors;

import java.io.File;

import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.rcp.app.ui.handlers.OpenSnippetHandler;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

public interface SnippetEditorDescriptor extends EditorDescriptor {

	/**
	 * 
	 * @return the ID of the editors
	 */
	String getEditorSnippetId();

	@Execute
	default boolean openEditor(File file, ISupplier supplier, IEclipseContext eclipseContext) {

		OpenSnippetHandler.openSnippet(getEditorSnippetId(), eclipseContext, (context, part) -> {

			part.setLabel(file.getName());
			context.set(File.class, file);
			context.set(ISupplier.class, supplier);
			return null;
		});
		return true;
	}
}
