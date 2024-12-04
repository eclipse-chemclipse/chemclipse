/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.model;

public class Probability {

	private byte[] adenine;
	private byte[] thymine;
	private byte[] guanine;
	private byte[] cytosine;

	public Probability(byte[] adenine, byte[] cytosine, byte[] guanine, byte[] thymine) {

		this.adenine = adenine;
		this.cytosine = cytosine;
		this.guanine = guanine;
		this.thymine = thymine;
	}

	public byte[] getAdenine() {

		return adenine;
	}

	public byte[] getCytosine() {

		return cytosine;
	}

	public byte[] getGuanine() {

		return guanine;
	}

	public byte[] getThymine() {

		return thymine;
	}
}
