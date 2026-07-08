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
package org.eclipse.chemclipse.msd.model.service;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.serializer.ScanDeserializerMSD;
import org.eclipse.chemclipse.msd.model.serializer.ScanSerializerMSD;
import org.eclipse.chemclipse.support.settings.serialization.ISerializationService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;

@Component(service = {ISerializationService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ScanSerializationServiceMSD implements ISerializationService {

	@Override
	public Class<?> getSupportedClass() {

		return IScanMSD.class;
	}

	@Override
	public JsonSerializer<IScanMSD> getSerializer() {

		return new ScanSerializerMSD();
	}

	@Override
	public JsonDeserializer<IScanMSD> getDeserializer() {

		return new ScanDeserializerMSD();
	}
}