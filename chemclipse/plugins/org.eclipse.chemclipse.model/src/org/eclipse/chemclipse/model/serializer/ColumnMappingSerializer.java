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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ColumnMappingSerializer extends JsonSerializer<SeparationColumnMapping> {

	@Override
	public void serialize(SeparationColumnMapping separationColumnMapping, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {

		if(separationColumnMapping != null) {
			jsonGenerator.writeString(separationColumnMapping.save());
		} else {
			jsonGenerator.writeString("");
		}
	}
}