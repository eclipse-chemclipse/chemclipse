/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.model.services;

import org.eclipse.chemclipse.support.settings.serialization.ISerializationService;
import org.eclipse.chemclipse.tsd.model.core.TraceRanges;
import org.eclipse.chemclipse.tsd.model.serializer.TraceRangesDeserializer;
import org.eclipse.chemclipse.tsd.model.serializer.TraceRangesSerializer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;

@Component(service = {ISerializationService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TraceRangesSerializationService implements ISerializationService {

	@Override
	public Class<?> getSupportedClass() {

		return TraceRanges.class;
	}

	@Override
	public JsonSerializer<TraceRanges> getSerializer() {

		return new TraceRangesSerializer();
	}

	@Override
	public JsonDeserializer<TraceRanges> getDeserializer() {

		return new TraceRangesDeserializer();
	}
}