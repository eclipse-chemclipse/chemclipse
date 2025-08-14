/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.targets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.xxd.filter.peaks.settings.DeleteTargetsFilterSettings;
import org.eclipse.chemclipse.xxd.filter.support.TargetsDeleteOption;
import org.junit.Before;
import org.junit.Test;

public class TargetsFilter_1_Test {

	private DeleteTargetsFilterSettings settings = new DeleteTargetsFilterSettings();
	private ITargetSupplier targetSupplier = getTargetSupplier();

	@Before
	public void setUp() {

		settings.setTargetDeleteOption(TargetsDeleteOption.PROPERTY_LEVEL);
	}

	@Test
	public void test1() {

		settings.setProperty("");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test2() {

		settings.setProperty("-1");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test3() {

		settings.setProperty("0");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test4() {

		settings.setProperty(null);
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test5() {

		settings.setProperty("a");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test6a() {

		settings.setProperty("1");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(2, targetSupplier.getTargets().size());
		assertFalse(contains(targetSupplier, "Styrene"));
	}

	@Test
	public void test6b() {

		settings.setProperty("> 1");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(1, targetSupplier.getTargets().size());
		assertTrue(contains(targetSupplier, "Styrene"));
	}

	@Test
	public void test6c() {

		settings.setProperty(">= 1");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(0, targetSupplier.getTargets().size());
	}

	@Test
	public void test7a() {

		settings.setProperty("2");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(2, targetSupplier.getTargets().size());
		assertFalse(contains(targetSupplier, "Benzene"));
	}

	@Test
	public void test7b() {

		settings.setProperty("> 2");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(2, targetSupplier.getTargets().size());
		assertTrue(contains(targetSupplier, "Styrene"));
		assertTrue(contains(targetSupplier, "Benzene"));
	}

	@Test
	public void test7c() {

		settings.setProperty(">= 2");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(1, targetSupplier.getTargets().size());
		assertTrue(contains(targetSupplier, "Styrene"));
	}

	@Test
	public void test8a() {

		settings.setProperty("3");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(2, targetSupplier.getTargets().size());
		assertFalse(contains(targetSupplier, "Propane"));
	}

	@Test
	public void test8b() {

		settings.setProperty("> 3");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test8c() {

		settings.setProperty(">= 3");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(2, targetSupplier.getTargets().size());
		assertTrue(contains(targetSupplier, "Styrene"));
		assertTrue(contains(targetSupplier, "Benzene"));
	}

	@Test
	public void test9a() {

		settings.setProperty("4");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test9b() {

		settings.setProperty("> 4");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	@Test
	public void test9c() {

		settings.setProperty(">= 4");
		TargetsFilter.filter(targetSupplier, settings);
		assertEquals(3, targetSupplier.getTargets().size());
	}

	private IIdentificationTarget createIdentificationTarget(String name, float matchFactor) {

		ILibraryInformation libraryInformation = new LibraryInformation();
		libraryInformation.setName(name);
		IIdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, new ComparisonResult(matchFactor));

		return identificationTarget;
	}

	private boolean contains(ITargetSupplier targetSupplier, String name) {

		for(IIdentificationTarget target : targetSupplier.getTargets()) {
			if(target.getLibraryInformation().getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	private ITargetSupplier getTargetSupplier() {

		ITargetSupplier targetSupplier = new ITargetSupplier() {

			Set<IIdentificationTarget> targets = new HashSet<>();
			{
				/*
				 * 3 Level (1 = best match)
				 */
				targets.add(createIdentificationTarget("Styrene", 95.3f));
				targets.add(createIdentificationTarget("Benzene", 89.2f));
				targets.add(createIdentificationTarget("Propane", 88.7f));
			}

			@Override
			public Set<IIdentificationTarget> getTargets() {

				return targets;
			}
		};

		return targetSupplier;
	}
}