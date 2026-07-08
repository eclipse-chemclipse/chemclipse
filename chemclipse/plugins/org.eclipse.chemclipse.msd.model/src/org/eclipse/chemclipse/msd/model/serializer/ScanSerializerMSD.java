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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.text.ValueFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ScanSerializerMSD extends JsonSerializer<IScanMSD> {

	public static final String SEPARATOR_TOKEN = ";";
	public static final String SEPARATOR_ENTRY = "|";
	public static final String WHITE_SPACE = " ";

	@Override
	public void serialize(IScanMSD scanMSD, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

		if(scanMSD != null) {
			jsonGenerator.writeString(save(scanMSD));
		} else {
			jsonGenerator.writeString("");
		}
	}

	public static String save(IScanMSD scanMSD) {

		if(scanMSD == null) {
			return "";
		}

		DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0000");
		List<IIon> ions = new ArrayList<>(scanMSD.getIons());
		Collections.sort(ions, (i1, i2) -> Double.compare(i1.getIon(), i2.getIon()));
		StringBuilder builder = new StringBuilder();
		Iterator<IIon> iterator = ions.iterator();
		while(iterator.hasNext()) {
			IIon ion = iterator.next();
			builder.append(decimalFormat.format(ion.getIon()));
			builder.append(WHITE_SPACE);
			builder.append(SEPARATOR_ENTRY);
			builder.append(WHITE_SPACE);
			builder.append(decimalFormat.format(ion.getAbundance()));
			if(iterator.hasNext()) {
				builder.append(SEPARATOR_TOKEN);
			}
		}
		return builder.toString().trim();
	}
}