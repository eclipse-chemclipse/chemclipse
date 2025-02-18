/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Lorenz Gerber - add additional message field
 * Philip Wenig - support process method resume option
 *******************************************************************************/
package org.eclipse.chemclipse.processing.supplier;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.core.runtime.IProgressMonitor;

public class ProcessExecutionContext implements IProcessSupplierContext, IMessageConsumer {

	private final IProgressMonitor monitor;
	private final IProcessSupplierContext context;
	private final IMessageConsumer consumer;
	private ProcessExecutionContext parent;
	//
	private final Map<Class<?>, Object> contextMap = new IdentityHashMap<>();

	public ProcessExecutionContext(IProgressMonitor monitor, IMessageConsumer rootConsumer, IProcessSupplierContext rootContext) {

		this(monitor, rootConsumer, rootContext, null);
	}

	private ProcessExecutionContext(IProgressMonitor monitor, IMessageConsumer rootConsumer, IProcessSupplierContext rootContext, ProcessExecutionContext parent) {

		this.consumer = rootConsumer;
		this.context = rootContext;
		this.parent = parent;
		this.monitor = monitor;
	}

	public IProgressMonitor getProgressMonitor() {

		return monitor;
	}

	public ProcessExecutionContext getParent() {

		return parent;
	}

	public <T> ProcessExecutionContext getParent(Class<T> clazz, Predicate<T> predicate) {

		if(parent != null) {
			T contextObject = parent.getContextObject(clazz);
			if(predicate.test(contextObject)) {
				return parent;
			} else {
				return parent.getParent(clazz, predicate);
			}
		}
		return parent;
	}

	@Override
	public void addMessage(String description, String message, String details, String solution, Throwable t, MessageType type) {

		consumer.addMessage(description, message, details, solution, t, type);
	}

	@Override
	public <T> IProcessSupplier<T> getSupplier(String id) {

		IProcessSupplier<T> supplier = context.getSupplier(id);
		if(supplier == null && parent != null) {
			return parent.getSupplier(id);
		}
		return supplier;
	}

	@Override
	public void visitSupplier(Consumer<? super IProcessSupplier<?>> consumer) {

		context.visitSupplier(consumer);
		if(parent != null) {
			parent.visitSupplier(consumer);
		}
	}

	public ProcessExecutionContext split() {

		return split(context);
	}

	public ProcessExecutionContext split(IProcessSupplierContext childContext) {

		return new ProcessExecutionContext(monitor, consumer, childContext, this);
	}

	@SuppressWarnings("unchecked")
	public <T> T setContextObject(Class<T> type, T object) {

		if(object == null) {
			return (T)contextMap.remove(type);
		} else {
			return (T)contextMap.put(type, object);
		}
	}

	public <T> T getContextObject(Class<T> type) {

		Object object = contextMap.get(type);
		if(type.isInstance(object)) {
			return type.cast(object);
		}
		if(parent != null) {
			return parent.getContextObject(type);
		}
		return null;
	}
}
