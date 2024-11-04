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
package org.eclipse.chemclipse.tsd.model.serializer;

import java.io.IOException;

import org.eclipse.chemclipse.tsd.model.core.TraceRanges;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TraceRangesDeserializer extends JsonDeserializer<TraceRanges> {

	@Override
	public TraceRanges deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

		TraceRanges traceRanges = new TraceRanges();
		traceRanges.load(jsonParser.getText());
		//
		return traceRanges;
	}
}