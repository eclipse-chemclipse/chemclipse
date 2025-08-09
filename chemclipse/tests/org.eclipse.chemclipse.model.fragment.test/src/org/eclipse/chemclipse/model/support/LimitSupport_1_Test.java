/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.junit.Test;

public class LimitSupport_1_Test {

	@Test
	public void test1() {

		assertFalse(LimitSupport.doIdentify(null, 90.0f));
	}

	@Test
	public void test2() {

		Set<IIdentificationTarget> identificationTargets = new HashSet<>();
		assertTrue(LimitSupport.doIdentify(identificationTargets, 90.0f));
	}

	@Test
	public void test3() {

		Set<IIdentificationTarget> identificationTargets = new HashSet<>();
		identificationTargets.add(createIdentificationTarget(90.0f));
		assertFalse(LimitSupport.doIdentify(identificationTargets, 90.0f));
	}

	@Test
	public void test4() {

		Set<IIdentificationTarget> identificationTargets = new HashSet<>();
		identificationTargets.add(createIdentificationTarget(89.9f));
		assertTrue(LimitSupport.doIdentify(identificationTargets, 90.0f));
	}

	private IIdentificationTarget createIdentificationTarget(float matchFactor) {

		ILibraryInformation libraryInformation = new LibraryInformation();
		IComparisonResult comparisonResult = new ComparisonResult(matchFactor, 0, 0, 0);
		return new IdentificationTarget(libraryInformation, comparisonResult);
	}
}
