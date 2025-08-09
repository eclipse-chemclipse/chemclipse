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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.exceptions.PointIsNullException;
import org.junit.Test;

public class TwoPoints_2_Test {

	private IPoint p1;
	private IPoint p2;
	private ITwoPoints points;

	@Test
	public void testConstructor_1() throws PointIsNullException {

		p1 = new Point(5.0d, 10.0d);
		p2 = new Point(7.0d, 3.0d);
		points = new TwoPoints(p1, p2);
		assertNotNull(points);
	}

	@Test
	public void testConstructor_2() {

		assertThrows(PointIsNullException.class, () -> {
			p1 = null;
			p2 = new Point(7.0d, 3.0d);
			points = new TwoPoints(p1, p2);
		});
	}

	@Test
	public void testConstructor_3() {

		assertThrows(PointIsNullException.class, () -> {
			p1 = new Point(5.0d, 10.0d);
			p2 = null;
			points = new TwoPoints(p1, p2);
		});
	}

	@Test
	public void testConstructor_4() {

		assertThrows(PointIsNullException.class, () -> {
			p1 = null;
			p2 = null;
			points = new TwoPoints(p1, p2);
		});
	}
}
