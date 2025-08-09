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
package org.eclipse.chemclipse.model.math;

import org.junit.Test;

public class IonRoundMethod_Performance_Test {

	@Test
	public void testActive() {

		executeActive();
	}

	@Test
	public void testDefault() {

		execute(IonRoundMethod.DEFAULT);
	}

	@Test
	public void testMinus00() {

		execute(IonRoundMethod.MINUS_00);
	}

	@Test
	public void testMinus01() {

		execute(IonRoundMethod.MINUS_01);
	}

	private void execute(IonRoundMethod method) {

		long start = System.currentTimeMillis();
		for(int i = 0; i < 5000000; i++) {
			method.round(Math.random());
		}
		long stop = System.currentTimeMillis();
		System.out.println(method.label() + ": " + (stop - start) + " ms");
	}

	private void executeActive() {

		long start = System.currentTimeMillis();
		for(int i = 0; i < 5000000; i++) {
			IonRoundMethod.getActive().round(Math.random());
		}
		long stop = System.currentTimeMillis();
		System.out.println(IonRoundMethod.getActive().label() + ": " + (stop - start) + " ms");
	}
}
