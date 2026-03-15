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
 * Philip Wenig - support process method resume option
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.editors;

import java.util.function.BiConsumer;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier.SupplierType;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.xxd.process.ui.menu.IMenuIcon;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.AbstractChartMenuEntry;

public class ProcessorSupplierMenuEntry<T> extends AbstractChartMenuEntry {

	private final IProcessSupplier<T> processorSupplier;
	private final BiConsumer<IProcessSupplier<T>, IProcessSupplierContext> executionConsumer;
	private final IProcessSupplierContext context;

	private static final Logger logger = Logger.getLogger(ProcessorSupplierMenuEntry.class);

	public ProcessorSupplierMenuEntry(IProcessSupplier<T> processorSupplier, IProcessSupplierContext context, BiConsumer<IProcessSupplier<T>, IProcessSupplierContext> executionConsumer) {

		this.executionConsumer = executionConsumer;
		this.processorSupplier = processorSupplier;
		this.context = context;
	}

	@Override
	public String getCategory() {

		return processorSupplier.getCategory();
	}

	@Override
	public String getName() {

		String name = processorSupplier.getName().replace("&", "&&");
		if(processorSupplier.getType() == SupplierType.INTERACTIVE) {
			return name + " ...";
		}
		return name;
	}

	@Override
	public Image getIcon() {

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] config = registry.getConfigurationElementsFor(IMenuIcon.EXTENSION_POINT_ID);
		try {
			for(IConfigurationElement element : config) {
				// NOTE: some process type suppliers add hard-coded prefixes
				final String id = element.getAttribute("id");
				if(!(processorSupplier.getId().contains(id))) {
					continue;
				}
				final Object object = element.createExecutableExtension("class");
				if(object instanceof IMenuIcon menuIcon) {
					return menuIcon.getImage();
				}
			}
		} catch(CoreException e) {
			logger.warn(e);
		}
		return null;
	}

	@Override
	public String getToolTipText() {

		return processorSupplier.getDescription();
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		executionConsumer.accept(processorSupplier, context);
	}
}
