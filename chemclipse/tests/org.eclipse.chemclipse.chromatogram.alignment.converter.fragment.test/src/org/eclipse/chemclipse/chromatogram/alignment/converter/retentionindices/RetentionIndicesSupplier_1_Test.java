/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.alignment.converter.retentionindices;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RetentionIndicesSupplier_1_Test {

	private IRetentionIndicesSupplier supplier = new RetentionIndicesSupplier();

	@Test
	public void testSupplier_1() {

		assertEquals("Description", "", supplier.getDescription());
	}
}
