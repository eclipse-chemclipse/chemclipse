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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.exceptions.AnalysisSupportException;
import org.junit.Test;

public class AnalysisSupport_5_Test {

	@Test
	public void testConstruct_1() {

		assertThrows(AnalysisSupportException.class, () -> {
			new AnalysisSupport(null, 10);
		});
	}
}