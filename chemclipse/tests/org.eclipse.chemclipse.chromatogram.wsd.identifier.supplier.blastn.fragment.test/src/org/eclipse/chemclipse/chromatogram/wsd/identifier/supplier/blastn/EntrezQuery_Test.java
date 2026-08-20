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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.WebNucleotideBLAST;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.WebIdentifierSettings;
import org.junit.jupiter.api.Test;

public class EntrezQuery_Test {

	WebIdentifierSettings webIdentifierSettings = new WebIdentifierSettings();

	@Test
	public void testExcludeNone() {

		webIdentifierSettings.setOnlyTypeMaterial(false);
		webIdentifierSettings.setExcludeModels(false);
		webIdentifierSettings.setExcludeUncultured(false);
		String entrez = WebNucleotideBLAST.createEntrezQuery(webIdentifierSettings);
		assertEquals("", entrez);
	}

	@Test
	public void testExcludeAll() {

		webIdentifierSettings.setOnlyTypeMaterial(true);
		webIdentifierSettings.setExcludeModels(true);
		webIdentifierSettings.setExcludeUncultured(true);
		String entrez = WebNucleotideBLAST.createEntrezQuery(webIdentifierSettings);
		assertEquals("sequence_from_type[filter] NOT(XM_000001:XM_9999999[pacc] OR XM_000000001:XM_999999999[pacc] OR XR_000000001:XR_999999999[pacc] OR (environmental samples[organism] OR metagenomes[orgn] OR txid32644[orgn]) OR env [DIV])", entrez);
	}

	@Test
	public void testExcludeModelsUncultured() {

		webIdentifierSettings.setExcludeModels(true);
		webIdentifierSettings.setExcludeUncultured(true);
		String entrez = WebNucleotideBLAST.createEntrezQuery(webIdentifierSettings);
		assertEquals("all [filter] NOT(XM_000001:XM_9999999[pacc] OR XM_000000001:XM_999999999[pacc] OR XR_000000001:XR_999999999[pacc] OR (environmental samples[organism] OR metagenomes[orgn] OR txid32644[orgn]) OR env [DIV])", entrez);
	}
}
