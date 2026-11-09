/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - group by retention index
 *******************************************************************************/
package org.eclipse.chemclipse.model.statistics;

public interface IRetentionIndex extends IVariable {

	String TYPE = "Retention Index"; //$NON-NLS-1$

	int getRetentionIndex();

	void setRetentionIndex(int retentionIndex);
}
