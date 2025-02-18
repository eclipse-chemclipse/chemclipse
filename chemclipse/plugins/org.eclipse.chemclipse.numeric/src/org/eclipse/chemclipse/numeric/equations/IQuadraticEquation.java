/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.numeric.equations;

public interface IQuadraticEquation extends IEquation {

	/*
	 * Quadratic Term
	 */
	double getA();

	/*
	 * Linear Term
	 */
	double getB();

	/*
	 * Constant Term
	 */
	double getC();

	double getApexValueForX(Apex result);
}