/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.service;

import org.eclipse.chemclipse.model.columns.SeparationColumnMapping;
import org.eclipse.chemclipse.model.serializer.ColumnMappingDeserializer;
import org.eclipse.chemclipse.model.serializer.ColumnMappingSerializer;
import org.eclipse.chemclipse.support.settings.serialization.ISerializationService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;

@Component(service = {ISerializationService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ColumnMappingSerializationService implements ISerializationService {

	@Override
	public Class<?> getSupportedClass() {

		return SeparationColumnMapping.class;
	}

	@Override
	public JsonSerializer<SeparationColumnMapping> getSerializer() {

		return new ColumnMappingSerializer();
	}

	@Override
	public JsonDeserializer<SeparationColumnMapping> getDeserializer() {

		return new ColumnMappingDeserializer();
	}
}