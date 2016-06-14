/*******************************************************************************
 * Copyright (c) 2016 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.converter.processing.report;

import org.eclipse.chemclipse.converter.model.IReportRowModel;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;

public interface IReportImportConverterProcessingInfo extends IProcessingInfo {

	IReportRowModel getReportRowModel() throws TypeCastException;

	void setReportRowModel(IReportRowModel reportRowModel);
}
