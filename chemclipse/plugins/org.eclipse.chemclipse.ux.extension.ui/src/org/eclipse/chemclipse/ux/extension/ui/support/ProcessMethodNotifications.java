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
 * Philip Wenig - formatting
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.support;

import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.event.EventHandler;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Creatable
@Singleton
public class ProcessMethodNotifications extends AbstractNotifications<IProcessMethod> {

	private EventHandler eventHandlerCreate = event -> created((IProcessMethod)event.getProperty(IChemClipseEvents.EVENT_BROKER_DATA));

	private EventHandler eventHandlerSelect = event -> select((IProcessMethod)event.getProperty(IChemClipseEvents.EVENT_BROKER_DATA));

	private EventHandler eventHandlerUpdate = event -> updated((IProcessMethod)event.getProperty(IChemClipseEvents.EVENT_BROKER_DATA), (IProcessMethod)event.getProperty(IChemClipseEvents.PROPERTY_METHOD_OLD_OBJECT));

	@PostConstruct
	protected void setupListener(IEventBroker eventBroker) {

		eventBroker.subscribe(IChemClipseEvents.TOPIC_METHOD_CREATED, null, eventHandlerCreate, false);
		eventBroker.subscribe(IChemClipseEvents.TOPIC_METHOD_SELECTED, null, eventHandlerSelect, false);
		eventBroker.subscribe(IChemClipseEvents.TOPIC_METHOD_UPDATE, null, eventHandlerUpdate, false);
	}

	protected void teardownListener(IEventBroker eventBroker) {

		eventBroker.unsubscribe(eventHandlerCreate);
		eventBroker.unsubscribe(eventHandlerSelect);
		eventBroker.unsubscribe(eventHandlerUpdate);
	}
}
