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
package org.eclipse.chemclipse.xxd.process.ui.support;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier.SupplierType;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.chemclipse.xxd.process.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.ui.toolbar.Processor;

/**
 * Processors the user has excluded from the chart context menus and the main menu.
 */
public class ProcessorMenuBlacklist {

	private static String cachedSettings = null;
	private static Set<String> cachedProcessorIds = Collections.emptySet();

	private ProcessorMenuBlacklist() {

	}

	/**
	 * Returns true if the processor shall not be offered in the menus.
	 */
	public static boolean contains(IProcessSupplier<?> processSupplier) {

		if(processSupplier == null) {
			return false;
		}

		return getProcessorIds().contains(processSupplier.getId());
	}

	/**
	 * All processors that can be put on the blacklist. Active processors are blacklisted.
	 */
	public static List<Processor> getProcessors() {

		IProcessSupplierContext processSupplierContext = new ProcessTypeSupport();
		Set<IProcessSupplier<?>> processSuppliers = processSupplierContext.getSupplier(processSupplier -> processSupplier.getType() != SupplierType.STRUCTURAL);
		return ProcessorSupport.getActiveProcessors(processSuppliers, PreferenceSupplier.getMenuBlacklistProcessors());
	}

	public static void setProcessors(List<Processor> processors) {

		PreferenceSupplier.setMenuBlacklistProcessors(ProcessorSupport.getActiveProcessors(processors));
	}

	private static synchronized Set<String> getProcessorIds() {

		String settings = PreferenceSupplier.getMenuBlacklistProcessors();
		if(!settings.equals(cachedSettings)) {
			cachedProcessorIds = Set.copyOf(ProcessorSupport.getActiveProcessorIds(settings));
			cachedSettings = settings;
		}

		return cachedProcessorIds;
	}
}
