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
package org.eclipse.chemclipse.pcr.converter.supplier.rdml.fragement.test.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.eclipse.chemclipse.pcr.converter.supplier.rdml.PathResolver;
import org.eclipse.chemclipse.pcr.converter.supplier.rdml.core.PCRImportConverter;
import org.eclipse.chemclipse.pcr.converter.supplier.rdml.fragment.test.TestPathHelper;
import org.eclipse.chemclipse.pcr.model.core.IChannel;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.pcr.model.core.IWell;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class Test_Example_11_ITest {

	private IPlate plate;

	@Before
	public void setUp() throws Exception {

		File importFile = new File(PathResolver.getAbsolutePath(TestPathHelper.EXAMPLE_1_1));
		IProcessingInfo<IPlate> importProcessingInfo = PCRImportConverter.getInstance().convert(importFile, new NullProgressMonitor());
		plate = importProcessingInfo.getProcessingResult();
	}

	@Test
	public void testDate() {

		LocalDateTime localDateTime = LocalDateTime.of(2011, Month.JANUARY, 9, 17, 04, 55);
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		Date date = java.util.Date.from(zonedDateTime.toInstant());
		assertEquals(date, plate.getDate());
	}

	@Test
	public void testWells() {

		assertEquals(90, plate.getWells().size());
		IWell well = plate.getWell(0);
		assertEquals("A1: gDNA", well.getLabel());
		IChannel channel = well.getChannels().get(0);
		assertEquals(0, channel.getId());
		assertEquals("SYBRGreen I", channel.getDetectionName());
		assertEquals(668.4299926757812, channel.getFluorescence().get(0), 0);
	}
}
