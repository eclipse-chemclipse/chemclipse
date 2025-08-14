/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.identifier.supplier.wikidata.fragment.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.xxd.identifier.supplier.wikidata.query.QueryEntity;
import org.junit.Test;

public class Caffeine_Test {

	@Test
	public void testCAS() {

		String item = QueryEntity.fromCAS("58-08-2");
		assertEquals("http://www.wikidata.org/entity/Q60235", item);
	}

	@Test
	public void testName() {

		String url = QueryEntity.fromName("caffeine");
		assertEquals("http://www.wikidata.org/entity/Q60235", url);
	}
}
