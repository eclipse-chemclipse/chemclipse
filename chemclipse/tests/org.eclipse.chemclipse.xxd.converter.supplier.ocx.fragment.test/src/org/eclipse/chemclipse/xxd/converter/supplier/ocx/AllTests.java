/*******************************************************************************
 * Copyright (c) 2014, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx;

import org.eclipse.chemclipse.rcp.app.test.TestAssembler;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {

		TestAssembler testAssembler = new TestAssembler(Activator.getContext().getBundle().getBundleContext().getBundles());
		TestSuite suite = new TestSuite("Run all tests.");
		String bundleAndPackageName = "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse";
		testAssembler.assembleTests(suite, bundleAndPackageName, bundleAndPackageName, "*_Test"); // Unit
		testAssembler.assembleTests(suite, bundleAndPackageName, bundleAndPackageName, "*_ITest"); // Integration
		return suite;
	}
}