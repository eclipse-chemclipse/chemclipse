/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support;

import java.util.List;

import org.eclipse.chemclipse.converter.io.support.IArrayReader;

public interface ITraceArrayReader extends IArrayReader {

	int getSamples();

	List<Integer> getAdenine();

	List<Integer> getCytosine();

	List<Integer> getGuanine();

	List<Integer> getThymine();
}
