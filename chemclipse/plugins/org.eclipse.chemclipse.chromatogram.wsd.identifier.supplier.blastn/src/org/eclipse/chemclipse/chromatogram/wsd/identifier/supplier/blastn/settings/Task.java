/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings;

import org.eclipse.chemclipse.support.text.ILabel;

public enum Task implements ILabel {

	BLASTN("blastn", "BLASTN"), //
	BLASTN_SHORT("blastn-short", "short sequence BLASTN"), //
	MEGABLAST("megablast", "MegaBLAST"), //
	DC_MEGABLAST("dc-megablast", "discontiguous MegaBLAST"); //

	private String label = "";
	private String value = "";

	private Task(String value, String label) {

		this.value = value;
		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}

	public String value() {

		return value;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}