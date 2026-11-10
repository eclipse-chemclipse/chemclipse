/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.core;

import java.io.Serializable;
import java.util.Map;

import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Scans are data points per interval from the detector.
 * 
 * Hence, a scan could represent various types.
 * Or it could be a chromatogram containing subsequent scans too.
 * 
 */
public interface IScan extends ISignal, IAdaptable, Serializable, ITargetSupplier {

	/**
	 * Returns the chromatogram, where the scan is stored.<br/>
	 * If there is no parent chromatogram, it will return null.
	 */
	IChromatogram getParentChromatogram();

	/**
	 * Sets the chromatogram which stores the scan.
	 */
	void setParentChromatogram(IChromatogram parentChromatogram);

	/**
	 * Returns the scan number of the actual scan.<br/>
	 * If it is not part if a chromatogram, the scan number is 0.
	 */
	int getScanNumber();

	/**
	 * Sets the scan Number of the actual scan.<br/>
	 * Only values >= 0 are allowed.
	 */
	void setScanNumber(int scanNumber);

	/**
	 * Returns the retention time of the scan.<br/>
	 * Retention time is stored as milliseconds.
	 * 
	 * @return int - retention time
	 */
	int getRetentionTime();

	/**
	 * Sets a new retention time.<br/>
	 * Retention time in milliseconds.<br/>
	 * Only values >= 0 are allowed.
	 * 
	 * @param retentionTime
	 *            - new retention time
	 */
	void setRetentionTime(int retentionTime);

	/**
	 * Get the retention time for column 1.
	 * This is used for multidimensional chromatography.
	 */
	int getRetentionTimeColumn1();

	/**
	 * Set the retention time for column 1.
	 * This is used for multidimensional chromatography.
	 */
	void setRetentionTimeColumn1(int retentionTimeColumn1);

	/**
	 * Get the retention time for column 2.
	 * This is used for multidimensional chromatography.
	 */
	int getRetentionTimeColumn2();

	/**
	 * Set the retention time for column 2.
	 * This is used for multidimensional chromatography.
	 */
	void setRetentionTimeColumn2(int retentionTimeColumn2);

	/**
	 * RRT
	 * Returns the relative retention time of the scan.<br/>
	 * Relative retention time is stored as milliseconds.
	 * 
	 * @return int - relative retention time
	 */
	int getRelativeRetentionTime();

	/**
	 * RRT
	 * Sets a new relative retention time.<br/>
	 * Relative retention time in milliseconds.<br/>
	 * Only values >= 0 are allowed.
	 */
	void setRelativeRetentionTime(int relativeRetentionTime);

	/**
	 * Returns the retention index.
	 * 
	 * @return float - retention index
	 */
	float getRetentionIndex();

	/**
	 * Sets the retention index. Only values >= 0 are allowed.<br/>
	 */
	void setRetentionIndex(float retentionIndex);

	/**
	 * Returns whether this scan stores additional RI data.
	 */
	boolean hasAdditionalRetentionIndices();

	/**
	 * Returns the retention index given by the RetentionIndexType.
	 * If none is available, 0 will be returned.
	 */
	float getRetentionIndex(SeparationColumnType separationColumnType);

	Map<SeparationColumnType, Float> getRetentionIndicesTyped();

	/**
	 * Sets the retention index. Only values >= 0 are allowed.<br/>
	 * Set a retention index for a certain column type.
	 */
	void setRetentionIndex(SeparationColumnType separationColumnType, float retentionIndex);

	/**
	 * Returns the total signal.<br/>
	 * If no signal is stored, 0 will be returned.
	 * 
	 * @return float - signal
	 */
	float getTotalSignal();

	int getTimeSegmentId();

	void setTimeSegmentId(int timeSegmentId);

	/**
	 * The default cycle number is 1.
	 * Cycle numbers are used to display several scans of
	 * one cycle number as one summed TIC.
	 */
	int getCycleNumber();

	void setCycleNumber(int cycleNumber);

	/**
	 * This flag marks if a this scan has been edited.
	 */
	boolean isDirty();

	/**
	 * This flag marks if this scan has been edited.<br/>
	 * It will only be saved if it is dirty. It should save a little bit of
	 * process time.
	 */
	void setDirty(boolean isDirty);

	/**
	 * Returns the identifier of the scan.
	 * It is used, e.g. to select and find a specifically marked scan in the list of scans.
	 */
	String getIdentifier();

	/**
	 * Sets an identifier.
	 */
	void setIdentifier(String identifier);

	/**
	 * Adjusts the scan to the given total signal.<br/>
	 * It means that all will be shifted so that the total signal
	 * will be the given total signal.
	 * Be aware of that some supplier support not the whole
	 * available signal range.
	 */
	void adjustTotalSignal(float totalSignal);

	boolean isEmpty();
}