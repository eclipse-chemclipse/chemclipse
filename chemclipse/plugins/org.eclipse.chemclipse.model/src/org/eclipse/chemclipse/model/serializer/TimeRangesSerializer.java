/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.serializer;

import java.io.IOException;

import org.eclipse.chemclipse.model.ranges.TimeRanges;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TimeRangesSerializer extends JsonSerializer<TimeRanges> {

	@Override
	public void serialize(TimeRanges timeRanges, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

		if(timeRanges != null) {
			jsonGenerator.writeString(timeRanges.save());
		} else {
			jsonGenerator.writeString(""); //$NON-NLS-1$
		}
	}
}