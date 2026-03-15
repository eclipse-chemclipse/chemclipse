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
 * Christoph Läubrich - allow chromatogram to be marked as dirty, support for analysis segments
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.baseline.IChromatogramBaseline;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.targets.ITargetDisplaySettings;
import org.eclipse.chemclipse.model.updates.IUpdater;
import org.eclipse.chemclipse.support.history.ISupplierEditHistory;

/**
 * A chromatogram consists of several scans. This is the detector
 * independent part of it.
 */
public interface IChromatogram extends SegmentedMeasurement, IChromatogramOverview, IChromatogramPeaks, ISupplierEditHistory, IChromatogramBaseline, IUpdater, IChromatogramIntegrationSupport, IChromatogramProcessorSupport, ITargetSupplier, ITargetDisplaySettings {

	String DEFAULT_CHROMATOGRAM_NAME = "Chromatogram";
	int MIN_SCANDELAY = 0;
	int MAX_SCANDELAY = 216000000; // 1h = 1000ms * 60 (-> 1sec) * 60 (-> 1min) * 60 (-> 1h)
	int MIN_SCANINTERVAL = 1;
	int MAX_SCANINTERVAL = 3600000; // 1min = 1000ms * 60 (-> 1sec) * 60 (-> 1min)
	float MIN_SCANS_PER_SECOND = 0.1f;
	float MAX_SCANS_PER_SECOND = 20.0f;

	String getColumnDetails();

	void setColumnDetails(String columnDetails);

	/*
	 * By default, the separation column could be
	 * parsed on import. If that shall be suppressed
	 * the return must be overridden to false.
	 */
	default boolean isParseSeparationColumnEnabled() {

		return true;
	}

	/**
	 * Use this map to store references to objects that are
	 * related to the chromatogram by external processes.
	 * The data is not saved and only valid for the current session.
	 */
	Map<String, Object> getProcessDataMap();

	boolean isFinalized();

	/**
	 * Set the status of the chromatogram.
	 * It's not possible to overwrite finalized chromatograms
	 * by clicking on save. Simply, the converter id
	 * is set to "", so that the software asks how to save
	 * the chromatogram.
	 */
	void setFinalized(boolean finalized);

	/**
	 * The converter sets its id if the chromatogram is writable by the
	 * converter.
	 */
	void setConverterId(String id);

	/**
	 * Returns the converter id. If the chromatogram is not writable, null or ""
	 * will be returned.
	 */
	String getConverterId();

	/**
	 * Adds a scan to the chromatogram.
	 */
	void addScan(IScan scan);

	/**
	 * Adds the scans to the chromatogram.
	 */
	void addScans(List<IScan> scans);

	/**
	 * Returns the scan with the given scan number from the chromatogram. If the
	 * retention times should be recalculated, call
	 * chromatogram.recalculateRetentionTimes().<br/>
	 * If no scan was available, null will be returned.
	 */
	IScan getScan(int scan);

	/**
	 * Returns the list of stored scans.
	 * Please use it only to iterate the stored scans.
	 * Modifications on the list may cause problems.
	 */
	List<IScan> getScans();

	/**
	 * Removes the scan with the given scan number from the chromatogram.<br/>
	 * If the retention times should be recalculated, call
	 * chromatogram.recalculateRetentionTimes().<br/>
	 * You can also add several scans, and call
	 * chromatogram.recalculateRetentionTimes() afterwards.
	 */
	void removeScan(int scan);

	/**
	 * Removes the scan range from the chromatogram.<br/>
	 * The value from and to are included.<br/>
	 * For example, if you want to remove 10 scans beginning from the scan 500
	 * call:<br/>
	 * removeScans(500, 509)<br/>
	 * <br/>
	 * If the retention times should be recalculated, call
	 * chromatogram.recalculateRetentionTimes().<br/>
	 * You can also remove several scans, and call
	 * chromatogram.recalculateRetentionTimes() afterwards.
	 */
	void removeScans(int from, int to);

	/**
	 * Replace all scans with the given list.
	 * Handle with care!
	 */
	void replaceAllScans(List<IScan> scans);

	/**
	 * Returns the currently used noise calculator.
	 */
	INoiseCalculator getNoiseCalculator();

	/**
	 * Sets the currently used noise calculator.
	 */
	void setNoiseCalculator(INoiseCalculator noiseCalculator);

	/**
	 * Recalculates the noise factor.
	 */
	void resetNoiseFactor();

	/**
	 * Calculates the signal to noise (S/N) ratio of the given abundance.
	 */
	float getSignalToNoiseRatio(float abundance);

	/**
	 * Returns the chromatogram name from a directory.
	 * E.g., if the filename is /.../chrom.D/DATA.MS, chrom will be returned.
	 */
	String extractNameFromDirectory(String nameDefault, String directoryExtension);

	/**
	 * Returns the chromatogram name from a file.
	 * E.g., if the filename is /.../chrom.csv, chrom will be returned.
	 */
	String extractNameFromFile(String nameDefault);

	/**
	 * Returns the master chromatogram if it is set.
	 * This method may return null.
	 */
	IChromatogram getMasterChromatogram();

	/**
	 * Stores a list of referenced chromatograms.
	 * Some vendors store more than one chromatogram in one file.
	 * This should be part of further improvements how to handle this issue.
	 */
	List<IChromatogram> getReferencedChromatograms();

	/**
	 * Add a referenced chromatogram.
	 */
	void addReferencedChromatogram(IChromatogram chromatogram);

	/**
	 * Removes a referenced chromatogram.
	 */
	void removeReferencedChromatogram(IChromatogram chromatogram);

	void removeAllReferencedChromatograms();

	/**
	 * Sets a flag that this chromatogram has been unloaded.
	 */
	void setUnloaded();

	/**
	 * Marks that this chromatogram has been set to unload modus.
	 */
	boolean isUnloaded();

	/**
	 * This methods checks whether different scan cycles are contained.
	 * All scans of one cycle shall be displayed in TIC mode with the
	 * summed signal.
	 */
	boolean containsScanCycles();

	/**
	 * Returns the scans identified by the scan cycle id.
	 */
	List<IScan> getScanCycleScans(int cycleNumber);

	/**
	 * Returns the chromatogram method.
	 */
	IMethod getMethod();

	ISeparationColumnIndices getSeparationColumnIndices();

	void setSeparationColumnIndices(ISeparationColumnIndices separationColumnIndices);

	@Override
	default File getFile() {

		return null;
	}

	void setDirty(boolean dirty);

	default void defineAnalysisSegment(IScanRange range) {

		defineAnalysisSegment(range, Collections.emptyList());
	}

	void defineAnalysisSegment(IScanRange range, Collection<? extends IAnalysisSegment> childs);

	void removeAnalysisSegment(IAnalysisSegment segment);

	void updateAnalysisSegment(IAnalysisSegment segment, IScanRange range);

	default void clearAnalysisSegments() {

		IAnalysisSegment[] segments = getAnalysisSegments().toArray(new IAnalysisSegment[0]);
		for(IAnalysisSegment segment : segments) {
			removeAnalysisSegment(segment);
		}
	}
}