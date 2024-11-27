/*******************************************************************************
 * Copyright (c) 2021, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;

public interface IUnknownSettings extends IIdentifierSettings {

	String getTargetName();

	void setTargetName(String targetName);

	float getMatchQuality();

	void setMatchQuality(float matchQuality);

	String getMarkerStart();

	void setMarkerStart(String markerStart);

	String getMarkerStop();

	void setMarkerStop(String markerStop);

	boolean isIncludeRetentionTime();

	void setIncludeRetentionTime(boolean includeRetentionTime);

	boolean isIncludeRetentionIndex();

	void setIncludeRetentionIndex(boolean includeRetentionIndex);
}
