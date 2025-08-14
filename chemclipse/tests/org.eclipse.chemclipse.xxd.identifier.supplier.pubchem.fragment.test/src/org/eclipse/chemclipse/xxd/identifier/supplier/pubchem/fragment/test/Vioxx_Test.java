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
package org.eclipse.chemclipse.xxd.identifier.supplier.pubchem.fragment.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.xxd.identifier.supplier.pubchem.rest.PowerUserGateway;
import org.junit.Before;
import org.junit.Test;

public class Vioxx_Test {

	private ILibraryInformation libraryInformation;

	@Before
	public void setUp() {

		libraryInformation = new LibraryInformation();
		libraryInformation.setName("Vioxx");
	}

	@Test
	public void testSMILES() {

		String smiles = PowerUserGateway.getSMILES(libraryInformation);
		assertEquals("CS(=O)(=O)C1=CC=C(C=C1)C2=C(C(=O)OC2)C3=CC=CC=C3", smiles);
	}

	@Test
	public void testCID() {

		assertEquals(Integer.valueOf(5090), PowerUserGateway.getCIDS(libraryInformation).get(0));
	}
}
