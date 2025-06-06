/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - header was missing
 *******************************************************************************/
package org.eclipse.chemclipse.converter.methods;

import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessExecutionConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessExecutor;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;

public final class MetaProcessorProcessSupplier extends AbstractProcessSupplier<MetaProcessorSettings> implements IProcessExecutor {

	private final IProcessMethod processMethod;

	public MetaProcessorProcessSupplier(String id, IProcessMethod processMethod, MethodProcessTypeSupplier parent) {

		super(id, processMethod.getName(), processMethod.getDescription(), processMethod.isFinal() ? null : MetaProcessorSettings.class, parent, MethodProcessSupport.getDataTypes(processMethod));
		this.processMethod = processMethod;
	}

	@Override
	public String getCategory() {

		return processMethod.getCategory();
	}

	public IProcessMethod getProcessMethod() {

		return processMethod;
	}

	@Override
	public <X> void execute(IProcessorPreferences<X> preferences, ProcessExecutionContext context) throws Exception {

		X settings = preferences.getSettings();
		if(settings instanceof MetaProcessorSettings processorSettings) {
			IProcessExecutionConsumer<?> callerDelegate = context.getContextObject(IProcessExecutionConsumer.class);
			if(callerDelegate != null) {
				ProcessEntryContainer.applyProcessEntries(processMethod, context, (processEntry, processSupplier) -> processorSettings.getProcessorPreferences(processEntry, processEntry.getPreferences(processSupplier)), callerDelegate);
			}
		}
	}
}
