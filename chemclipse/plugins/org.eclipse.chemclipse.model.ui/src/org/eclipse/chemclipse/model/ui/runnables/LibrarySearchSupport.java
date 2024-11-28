/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.ui.runnables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.services.IRetentionIndexLibraryService;
import org.eclipse.chemclipse.model.ui.Activator;

public class LibrarySearchSupport {

	public static List<IRetentionIndexLibraryService> getRetentionIndexLibraryServices() {

		List<IRetentionIndexLibraryService> retentionIndexLibraryServices = new ArrayList<>();
		//
		Object[] services = Activator.getDefault().getRetentionIndexLibraryServices();
		if(services != null) {
			for(Object service : services) {
				if(service instanceof IRetentionIndexLibraryService retentionIndexLibraryService) {
					retentionIndexLibraryServices.add(retentionIndexLibraryService);
				}
			}
		}
		//
		return retentionIndexLibraryServices;
	}
}