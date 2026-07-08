/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.serializer;

import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ScanDeserializerMSD extends JsonDeserializer<IScanMSD> {

	private static final Logger logger = Logger.getLogger(ScanDeserializerMSD.class);

	@Override
	public IScanMSD deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

		ScanMSD scanMSD = new ScanMSD();
		load(scanMSD, jsonParser.getText());
		return scanMSD;
	}

	public static void load(ScanMSD scanMSD, String text) {

		if(text == null || text.isBlank()) {
			return;
		}

		String[] entries = text.split(ScanSerializerMSD.SEPARATOR_TOKEN);
		for(String entry : entries) {
			String[] parts = entry.split("\\|");
			if(parts.length == 2) {
				try {
					double mz = Double.parseDouble(parts[0].trim());
					float abundance = Float.parseFloat(parts[1].trim());
					scanMSD.addIon(new Ion(mz, abundance));
				} catch(NumberFormatException e) {
					logger.warn(e);
				}
			}
		}
	}
}