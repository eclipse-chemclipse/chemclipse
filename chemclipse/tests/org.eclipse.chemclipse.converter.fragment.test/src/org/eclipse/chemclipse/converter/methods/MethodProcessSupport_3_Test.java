/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.converter.methods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.processing.DataCategory;
import org.junit.Test;

public class MethodProcessSupport_3_Test extends MethodProcessSupportTestCase {

	@Test
	public void test1() {

		DataCategory[] methodCategories = new DataCategory[]{DataCategory.CSD, DataCategory.MSD, DataCategory.WSD, DataCategory.VSD};
		List<DataCategory[]> processCategories = new ArrayList<>();
		processCategories.add(new DataCategory[]{DataCategory.CSD, DataCategory.MSD});
		DataCategory[] dataCategories = MethodProcessSupport.getDataTypes(getProcessMethod(methodCategories, processCategories));

		assertNotNull(dataCategories);
		assertEquals(2, dataCategories.length);
		assertEquals("CSD", dataCategories[0].name());
		assertEquals("MSD", dataCategories[1].name());
	}

	@Test
	public void test2() {

		DataCategory[] methodCategories = new DataCategory[]{DataCategory.CSD, DataCategory.MSD, DataCategory.WSD, DataCategory.VSD};
		List<DataCategory[]> processCategories = new ArrayList<>();
		processCategories.add(new DataCategory[]{DataCategory.MSD, DataCategory.WSD});
		DataCategory[] dataCategories = MethodProcessSupport.getDataTypes(getProcessMethod(methodCategories, processCategories));

		assertNotNull(dataCategories);
		assertEquals(2, dataCategories.length);
		assertEquals("MSD", dataCategories[0].name());
		assertEquals("WSD", dataCategories[1].name());
	}

	@Test
	public void test3() {

		DataCategory[] methodCategories = new DataCategory[]{DataCategory.CSD, DataCategory.MSD, DataCategory.WSD, DataCategory.VSD};
		List<DataCategory[]> processCategories = new ArrayList<>();
		processCategories.add(new DataCategory[]{DataCategory.WSD, DataCategory.VSD});
		DataCategory[] dataCategories = MethodProcessSupport.getDataTypes(getProcessMethod(methodCategories, processCategories));

		assertNotNull(dataCategories);
		assertEquals(2, dataCategories.length);
		assertEquals("VSD", dataCategories[0].name());
		assertEquals("WSD", dataCategories[1].name());
	}

	@Test
	public void test4() {

		DataCategory[] methodCategories = new DataCategory[]{DataCategory.CSD, DataCategory.MSD, DataCategory.WSD, DataCategory.VSD};
		List<DataCategory[]> processCategories = new ArrayList<>();
		processCategories.add(new DataCategory[]{DataCategory.CSD, DataCategory.VSD});
		DataCategory[] dataCategories = MethodProcessSupport.getDataTypes(getProcessMethod(methodCategories, processCategories));

		assertNotNull(dataCategories);
		assertEquals(2, dataCategories.length);
		assertEquals("CSD", dataCategories[0].name());
		assertEquals("VSD", dataCategories[1].name());
	}
}