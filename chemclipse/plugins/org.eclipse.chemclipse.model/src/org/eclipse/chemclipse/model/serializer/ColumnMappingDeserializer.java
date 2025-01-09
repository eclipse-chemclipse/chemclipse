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
package org.eclipse.chemclipse.model.serializer;

import java.io.IOException;

import org.eclipse.chemclipse.model.columns.SeparationColumnMapping;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ColumnMappingDeserializer extends JsonDeserializer<SeparationColumnMapping> {

	@Override
	public SeparationColumnMapping deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

		SeparationColumnMapping separationColumnMapping = new SeparationColumnMapping();
		separationColumnMapping.load(jsonParser.getText());
		//
		return separationColumnMapping;
	}
}