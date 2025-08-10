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
package org.eclipse.chemclipse.msd.identifier.supplier.nist.runtime;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

public abstract class AbstractBackgroundTestCase {

	List<String> parameterBackground = new ArrayList<>();

	@Before
	public void setUp() throws Exception {

		parameterBackground.add(INistSupport.INSTRUMENT);
		parameterBackground.add(INistSupport.PAR2);
	}
}
