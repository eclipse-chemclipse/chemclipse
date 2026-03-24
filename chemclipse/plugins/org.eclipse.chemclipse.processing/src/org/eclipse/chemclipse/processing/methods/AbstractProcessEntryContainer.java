/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.processing.methods;

import java.util.function.BiFunction;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.methods.SubProcessExecutionConsumer.SubProcess;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessExecutionConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;

public abstract class AbstractProcessEntryContainer implements IProcessEntryContainer {

	private static final Logger logger = Logger.getLogger(AbstractProcessEntryContainer.class);

	/*
	 * The flag supportResume shall be persisted. By default, it is false, due to the following reasons:
	 * - Allow backward compatibility
	 * - Some meta methods must be executed at a whole
	 * So, the user explicitly has to define that a method may support the resume action.
	 * The selected resume index shall be not persisted as the user shall be asked each time
	 * to select the entry point if needed.
	 */
	private boolean supportResume = false;
	private int resumeIndex = IProcessEntryContainer.DEFAULT_RESUME_INDEX; // transient

	@Override
	public boolean isSupportResume() {

		return supportResume;
	}

	@Override
	public void setSupportResume(boolean supportResume) {

		this.supportResume = supportResume;
	}

	@Override
	public int getResumeIndex() {

		return resumeIndex;
	}

	@Override
	public void setResumeIndex(int resumeIndex) {

		this.resumeIndex = resumeIndex;
	}

	public static <X, T> T applyProcessEntries(IProcessEntryContainer container, ProcessExecutionContext context, IProcessExecutionConsumer<T> consumer) {

		return applyProcessEntries(container, context, (processEntry, processSupplier) -> processEntry.getPreferences(processSupplier), consumer);
	}

	public static <X, T> T applyProcessEntries(IProcessEntryContainer container, ProcessExecutionContext context, BiFunction<IProcessEntry, IProcessSupplier<X>, IProcessorPreferences<X>> preferenceSupplier, IProcessExecutionConsumer<T> consumer) {

		int resumeIndex = container.isSupportResume() ? container.getResumeIndex() : DEFAULT_RESUME_INDEX;

		int index = -1;
		for(IProcessEntry processEntry : container) {
			/*
			 * Resume method at a given position?
			 */
			index++;
			if(index < resumeIndex) {
				continue;
			}
			/*
			 * Validation
			 */
			IProcessSupplier<X> processor = context.getSupplier(processEntry.getProcessorId());
			if(processor == null) {
				context.addWarnMessage(processEntry.getName(), "The processor was not found, the execution wil be skipped.");
				continue;
			}
			/*
			 * Process
			 */
			try {
				IProcessorPreferences<X> processorPreferences = preferenceSupplier.apply(processEntry, processor);
				context.setContextObject(IProcessEntry.class, processEntry);
				context.setContextObject(IProcessSupplier.class, processor);
				context.setContextObject(IProcessExecutionConsumer.class, consumer);
				context.setContextObject(IProcessorPreferences.class, processorPreferences);
				ProcessExecutionContext entryContext = context.split(processor.getContext());

				try {
					if(processEntry.getNumberOfEntries() > 0) {
						/*
						 * Combined method
						 */
						AbstractProcessSupplier.applyProcessor(processorPreferences, new SubProcessExecutionConsumer<>(consumer, new SubProcess<T>() {

							@Override
							public <SubX> void execute(IProcessorPreferences<SubX> preferences, IProcessExecutionConsumer<T> parent, ProcessExecutionContext subcontext) {

								applyProcessEntries(processEntry, subcontext, preferenceSupplier, parent);
							}
						}), entryContext);
					} else {
						/*
						 * Simple method
						 */
						AbstractProcessSupplier.applyProcessor(processorPreferences, consumer, entryContext);
					}
				} finally {
					context.setContextObject(IProcessSupplier.class, null);
					context.setContextObject(IProcessEntry.class, null);
					context.setContextObject(IProcessExecutionConsumer.class, null);
					context.setContextObject(IProcessorPreferences.class, null);
				}
			} catch(RuntimeException e) {
				context.addErrorMessage(processEntry.getName(), "Internal error when running the process method.");
				logger.error(e);
			}
		}
		/*
		 * Result
		 */
		return consumer.getResult();
	}
}
