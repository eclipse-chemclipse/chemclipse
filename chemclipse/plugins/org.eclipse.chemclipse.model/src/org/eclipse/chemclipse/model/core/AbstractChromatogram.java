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
 * Christoph Läubrich - use getScans() everywhere to access the scan datastructure, modcount support, analysis segment support
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.baseline.BaselineModel;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.model.baseline.IChromatogramBaseline;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.implementation.ObservedPeakList;
import org.eclipse.chemclipse.model.implementation.TripleQuadMethod;
import org.eclipse.chemclipse.model.notifier.IChromatogramSelectionUpdateNotifier;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.updates.IChromatogramUpdateListener;
import org.eclipse.chemclipse.support.history.EditHistory;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;

public abstract class AbstractChromatogram extends AbstractMeasurementTarget implements IChromatogram {

	private static final long serialVersionUID = -2540103992883061431L;
	private static final Logger logger = Logger.getLogger(AbstractChromatogram.class);

	private static final String COLUMN_DETAILS = "Column Details";

	private boolean finalized = false;
	private String converterId = "";
	private File file = null; // The file object of the chromatogram.
	private int scanDelay = 4500;
	private int scanInterval = 1000; // 1000ms = 1 scan per second
	private final List<ChromatogramAnalysisSegment> analysisSegments = new ArrayList<>();
	/*
	 * This flag marks whether the chromatogram has been
	 * set to unload modus. It is used e.g. when loading
	 * the scan proxies in the background. If unload is true,
	 * no further actions are neccessary.
	 */
	private boolean unloaded = false;
	/*
	 * This is a list, which holds other participants that listen on changes in
	 * the chromatogram.<br/> It is important e.g. for the GUI to known when it
	 * has to update itself.
	 */
	private final List<IChromatogramUpdateListener> updateSupport = new ArrayList<>(5);
	/*
	 * EditHistory stores all information about the edit operations processed on
	 * the chromatogram.
	 */
	private final IEditHistory editHistory = new EditHistory();
	/*
	 * The baseline model map.
	 * A chromatogram could contain several baseline models.
	 */
	private String activeBaselineId = DEFAULT_BASELINE_ID;
	private Map<String, IBaselineModel> baselineModelMap = new HashMap<>();
	/*
	 * Store all scans in this list.<br/>
	 */
	private final List<IScan> scans = new ArrayList<>();
	/*
	 * Some vendors store several chromatograms in one file.
	 */
	private IChromatogram masterChromatogram = null;
	private final List<IChromatogram> referencedChromatograms = new ArrayList<>();
	/*
	 * Integration entries.
	 */
	private String integratorDescription = "";
	private List<IIntegrationEntry> chromatogramIntegrationEntries = new ArrayList<>();
	private List<IIntegrationEntry> backgroundIntegrationEntries = new ArrayList<>();

	private IMethod method = new TripleQuadMethod();
	private ISeparationColumnIndices separationColumnIndices = SeparationColumnFactory.getSeparationColumnIndices(SeparationColumnType.DEFAULT);
	/*
	 * This set contains all the peaks of the chromatogram.
	 * Specific chromatogram implementations might define
	 * specific peak types, which must extend from IPeak.
	 */
	private final ObservedPeakList<? extends IChromatogramPeak> peaks = new ObservedPeakList<>();

	private final Set<IIdentificationTarget> identificationTargets = new HashSet<>();
	private int modCount;
	/*
	 * Noise Calculator
	 * This can't be created directly here, as implementations are
	 * located at a higher level (including a MSD specific version).
	 */
	private INoiseCalculator noiseCalculator = null;
	/*
	 * Transient
	 */
	private Map<String, Object> processDataMap = new HashMap<>();

	/**
	 * Constructs a normal chromatogram.
	 * Several initialization will be performed.
	 */
	protected AbstractChromatogram() {

		baselineModelMap.put(DEFAULT_BASELINE_ID, new BaselineModel(this));
		putHeaderData(COLUMN_DETAILS, "");
		resetNoiseFactor();
	}

	@Override
	public String getColumnDetails() {

		return getHeaderData(COLUMN_DETAILS);
	}

	@Override
	public void setColumnDetails(String columnDetails) {

		putHeaderData(COLUMN_DETAILS, columnDetails != null ? columnDetails : "");
	}

	@Override
	public Map<String, Object> getProcessDataMap() {

		return processDataMap;
	}

	@Override
	public void resetNoiseFactor() {

		String id = getNoiseCalculatorId();
		INoiseCalculator noiseCalculator = getNoiseCalculator();
		/*
		 * If the noise calculator has changed, create a new one.
		 * Otherwise, reset the status to trigger a new calculation.
		 */
		INoiseCalculator noiseCalculatorNew = null;
		if(noiseCalculator != null) {
			if(noiseCalculator.getId().equals(id)) {
				noiseCalculator.reset();
			} else {
				noiseCalculatorNew = createNoiseCalculator(id);
			}
		} else {
			noiseCalculatorNew = createNoiseCalculator(id);
		}
		/*
		 * Add the new calculator on demand.
		 */
		if(noiseCalculatorNew != null) {
			setNoiseCalculator(noiseCalculatorNew);
		}
	}

	protected abstract String getNoiseCalculatorId();

	protected abstract INoiseCalculator createNoiseCalculator(String id);

	@Override
	public INoiseCalculator getNoiseCalculator() {

		return noiseCalculator;
	}

	@Override
	public void setNoiseCalculator(INoiseCalculator noiseCalculator) {

		this.noiseCalculator = noiseCalculator;
	}

	@Override
	public float getSignalToNoiseRatio(float abundance) {

		if(noiseCalculator != null) {
			return noiseCalculator.getSignalToNoiseRatio(this, abundance);
		}

		return 0;
	}

	@Override
	public boolean isFinalized() {

		return finalized;
	}

	@Override
	public void setFinalized(boolean finalized) {

		this.finalized = finalized;
	}

	@Override
	public String getConverterId() {

		return finalized ? "" : converterId;
	}

	@Override
	public void setConverterId(String converterId) {

		this.converterId = converterId;
	}

	@Override
	public int getScanDelay() {

		return scanDelay;
	}

	@Override
	public void setScanDelay(int milliseconds) {

		if(scanDelay >= MIN_SCANDELAY && scanDelay <= MAX_SCANDELAY) {
			this.scanDelay = milliseconds;
		}
	}

	@Override
	public int getScanInterval() {

		return scanInterval;
	}

	@Override
	public void setScanInterval(int milliseconds) {

		if(milliseconds >= MIN_SCANINTERVAL && milliseconds <= MAX_SCANINTERVAL) {
			this.scanInterval = milliseconds;
		}
	}

	@Override
	public void setScanInterval(float scansPerSecond) {

		/*
		 * Calculates a new scanInterval for the given value scans per second.
		 */
		if(scansPerSecond >= MIN_SCANS_PER_SECOND && scansPerSecond <= MAX_SCANS_PER_SECOND) {
			int milliseconds = Math.round(1000 / scansPerSecond);
			setScanInterval(milliseconds);
		}
	}

	@Override
	public File getFile() {

		return this.file;
	}

	@Override
	public void setFile(File file) {

		this.file = file;
	}

	@Override
	public String getName() {

		if(file != null) {
			return FilenameUtils.getBaseName(file.getName());
		} else {
			String dataName = getDataName();
			if(dataName != null && !dataName.isEmpty()) {
				return dataName;
			}
			return DEFAULT_CHROMATOGRAM_NAME;
		}
	}

	// TODO optimize
	@Override
	public float getMinSignal() {

		float minSignal = Float.MAX_VALUE;
		if(getNumberOfScans() < 1) {
			return 0;
		}
		/*
		 * Do the for loop if at least one scan exists.
		 */
		for(IScan scan : getScans()) {
			float actSignal = scan.getTotalSignal();
			minSignal = (minSignal > actSignal) ? actSignal : minSignal;
		}
		return minSignal;
	}

	@Override
	public float getMaxSignal() {

		return getMaxSignal(containsScanCycles());
	}

	// TODO optimize
	@Override
	public float getMaxSignal(boolean condenseCycleNumberScans) {

		float maxSignal = 0;
		if(getNumberOfScans() >= 1) {
			/*
			 * Do the for loop if at least one scan exists.
			 */
			try {
				ITotalScanSignalExtractor totalIonSignalExtractor;
				totalIonSignalExtractor = new TotalScanSignalExtractor(this);
				ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(this, true, condenseCycleNumberScans);
				maxSignal = signals.getMaxSignal();
			} catch(ChromatogramIsNullException e) {
				logger.warn(e);
			}
		}
		return maxSignal;
	}

	@Override
	public int getStartRetentionTime() {

		int lastScan = getNumberOfScans();
		if(lastScan == 0) {
			return 0;
		}
		return getScan(1).getRetentionTime();
	}

	@Override
	public int getStopRetentionTime() {

		int lastScan = getNumberOfScans();
		if(lastScan == 0) {
			return 0;
		}
		return getScan(lastScan).getRetentionTime();
	}

	@Override
	public int getNumberOfScans() {

		return getScans().size();
	}

	@Override
	public int getScanNumber(float retentionTime) {

		int milliseconds = (int)(retentionTime * 1000 * 60);
		return getScanNumber(milliseconds);
	}

	@Override
	public int getScanNumber(int retentionTime) {

		/*
		 * Check if the retention time is out of limits.
		 */
		if(retentionTime < getStartRetentionTime() || retentionTime > getStopRetentionTime()) {
			return 0;
		}
		/*
		 * If the scan interval is null, there is no scan stored.
		 */
		int scanInterval = getScanInterval();
		if(scanInterval == 0) {
			return 0;
		}
		/*
		 * If the given retention time fits the last scan, return the scan
		 * number of the last scan.
		 */
		if(retentionTime == getStopRetentionTime()) {
			return getNumberOfScans();
		}
		/*
		 * Calculate the scan number starting point to not iterate through all
		 * getScans().
		 */
		// TODO optimize!! It doesn't work if the scan intervals are not equal of length!!
		/*
		 * int startScan = (retentionTime - getScanDelay()) / scanInterval - 1;
		 * if(startScan < 1) {
		 * startScan = 1;
		 * }
		 */
		// TODO optimieren? Collections.binarySearch?
		for(int scan = 1; scan <= getNumberOfScans(); scan++) {
			if(getScan(scan).getRetentionTime() > retentionTime) {
				return --scan;
			}
		}
		/*
		 * If there was no fit, return 0.
		 */
		return 0;
	}

	@Override
	public float getTotalSignal() {

		float totalSignal = 0.0f;
		/*
		 * If there is no scan stored, return 0;
		 */
		if(getNumberOfScans() < 1) {
			return 0;
		}
		/*
		 * Do the for loop if at least one scan exists.
		 */
		for(IScan scan : getScans()) {
			totalSignal += scan.getTotalSignal();
		}
		return totalSignal;
	}

	@Override
	public void recalculateScanNumbers() {

		int scanNumber = 1;
		for(IScan scan : getScans()) {
			scan.setScanNumber(scanNumber);
			scanNumber++;
		}
	}

	@Override
	public void recalculateRetentionTimes() {

		int actual = getScanDelay();
		for(IScan scan : getScans()) {
			scan.setRetentionTime(actual);
			actual += getScanInterval();
		}
		/*
		 * Forces all listeners to be updated.
		 */
		fireUpdateChange(true);
	}

	@Override
	public void addScan(IScan scan) {

		scan.setParentChromatogram(this);
		List<IScan> list = getScans();
		int lastScan = list.size();
		scan.setScanNumber(++lastScan);
		list.add(scan);
	}

	@Override
	public void addScans(List<IScan> scans) {

		for(IScan scan : scans) {
			addScan(scan);
		}
	}

	@Override
	public IScan getScan(int scan) {

		int position = scan;
		List<IScan> list = getScans();
		if(position > 0 && position <= list.size()) {
			return list.get(--position);
		}
		return null;
	}

	@Override
	public List<IScan> getScans() {

		return scans;
	}

	@Override
	public void removeScan(final int scan) {

		int position = scan;
		List<IScan> list = getScans();
		if(position > 0 && position <= list.size()) {
			list.remove(--position);
		}
	}

	@Override
	public void removeScans(int from, int to) {

		int start;
		int end;
		if(from > to) {
			start = to;
			end = from;
		} else {
			start = from;
			end = to;
		}
		for(int i = start; i <= end; i++) {
			removeScan(start);
		}
		/*
		 * Forces all listeners to be updated.
		 */
		// TODO Test updateChange
		// fireUpdateChange(true);
	}

	@Override
	public void replaceAllScans(List<IScan> scans) {

		List<IScan> list = getScans();
		list.clear();

		int scanNumber = 1;
		for(IScan scan : scans) {
			scan.setParentChromatogram(this);
			scan.setScanNumber(scanNumber);
			scanNumber++;
		}

		list.addAll(scans);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {

		return Adapters.adapt(this, adapter);
	}

	@Override
	public IEditHistory getEditHistory() {

		return editHistory;
	}

	@Override
	public IBaselineModel getBaselineModel() {

		IBaselineModel baselineModel = baselineModelMap.get(activeBaselineId);
		if(baselineModel == null) {
			baselineModel = new BaselineModel(this);
			baselineModelMap.put(activeBaselineId, baselineModel);
		}

		return baselineModel;
	}

	@Override
	public Set<String> getBaselineIds() {

		return Collections.unmodifiableSet(baselineModelMap.keySet());
	}

	@Override
	public String getActiveBaseline() {

		return activeBaselineId;
	}

	@Override
	public void setActiveBaselineDefault() {

		setActiveBaseline(IChromatogramBaseline.DEFAULT_BASELINE_ID);
	}

	@Override
	public void setActiveBaseline(String id) {

		if(id != null && !id.isEmpty()) {
			/*
			 * Activate the id and create a new baseline model.
			 */
			this.activeBaselineId = id;
			getBaselineModel();
		}
	}

	@Override
	public void removeBaseline(String id) {

		if(id != null && !id.isEmpty()) {
			if(!DEFAULT_BASELINE_ID.equals(id)) {
				baselineModelMap.remove(id);
				setActiveBaseline(DEFAULT_BASELINE_ID);
			}
		}
	}

	@Override
	public void addChromatogramUpdateListener(IChromatogramUpdateListener listener) {

		updateSupport.add(listener);
	}

	@Override
	public void removeChromatogramUpdateListener(IChromatogramUpdateListener listener) {

		updateSupport.remove(listener);
	}

	/**
	 * When a chromatogram value has changed, call fireUpdateChange() to inform
	 * all registered listeners ({@link IChromatogramUpdateListener}). See {@link IChromatogramSelectionUpdateNotifier} for the explanation of forceReload.
	 */
	protected void fireUpdateChange(final boolean forceReload) {

		/**
		 * Inform all listeners if a chromatogram value has changed, for example
		 * a mass spectrum.<br/>
		 * The update action is encapsulated in a ISafeRunnable object to handle
		 * failures of the implementing listeners.
		 */
		for(final IChromatogramUpdateListener listener : updateSupport) {
			ISafeRunnable runnable = new ISafeRunnable() {

				@Override
				public void handleException(Throwable exception) {

					logger.warn(exception);
				}

				@Override
				public void run() throws Exception {

					listener.update(forceReload);
				}
			};
			SafeRunner.run(runnable);
		}
	}

	@Override
	public IChromatogram getMasterChromatogram() {

		return masterChromatogram;
	}

	/**
	 * Sets the master chromatogram. If null is provided, the link
	 * to the master chromatogram is removed. This method is
	 * primarily used when adding or removing reference chromatograms.
	 *
	 * @param masterChromatogram
	 */
	private void setMasterChromatogram(IChromatogram chromatogram, IChromatogram masterChromatogram) {

		if(chromatogram instanceof AbstractChromatogram abstractChromatogram) {
			abstractChromatogram.masterChromatogram = masterChromatogram;
		}
	}

	@Override
	public List<IChromatogram> getReferencedChromatograms() {

		return referencedChromatograms;
	}

	@Override
	public void addReferencedChromatogram(IChromatogram chromatogram) {

		setMasterChromatogram(chromatogram, this);
		referencedChromatograms.add(chromatogram);
	}

	@Override
	public void removeReferencedChromatogram(IChromatogram chromatogram) {

		setMasterChromatogram(chromatogram, null);
		referencedChromatograms.remove(chromatogram);
	}

	@Override
	public void removeAllReferencedChromatograms() {

		for(IChromatogram chromatogram : referencedChromatograms) {
			setMasterChromatogram(chromatogram, null);
		}
		referencedChromatograms.clear();
	}

	@Override
	public String getIntegratorDescription() {

		return integratorDescription;
	}

	@Override
	public void setIntegratorDescription(String integratorDescription) {

		if(integratorDescription != null) {
			this.integratorDescription = integratorDescription;
		}
	}

	@Override
	public double getPeakIntegratedArea() {

		double integratedArea = 0.0d;
		for(IPeak peak : getPeaks()) {
			integratedArea += peak.getIntegratedArea();
		}
		return integratedArea;
	}

	@Override
	public double getChromatogramIntegratedArea() {

		return getIntegratedArea(chromatogramIntegrationEntries);
	}

	@Override
	public void setIntegratedArea(List<IIntegrationEntry> chromatogramIntegrationEntries, List<IIntegrationEntry> backgroundIntegrationEntries, String integratorDescription) {

		setIntegratorDescription(integratorDescription);

		if(chromatogramIntegrationEntries != null) {
			this.chromatogramIntegrationEntries = chromatogramIntegrationEntries;
		}

		if(backgroundIntegrationEntries != null) {
			this.backgroundIntegrationEntries = backgroundIntegrationEntries;
		}
	}

	@Override
	public List<IIntegrationEntry> getChromatogramIntegrationEntries() {

		return chromatogramIntegrationEntries;
	}

	@Override
	public double getBackgroundIntegratedArea() {

		return getIntegratedArea(backgroundIntegrationEntries);
	}

	@Override
	public List<IIntegrationEntry> getBackgroundIntegrationEntries() {

		return backgroundIntegrationEntries;
	}

	private double getIntegratedArea(List<IIntegrationEntry> integrationEntries) {

		double integratedArea = 0.0d;
		if(!integrationEntries.isEmpty()) {
			for(IIntegrationEntry integrationEntry : integrationEntries) {
				integratedArea += integrationEntry.getIntegratedArea();
			}
		}
		return integratedArea;
	}

	@Override
	public void removeAllBackgroundIntegrationEntries() {

		backgroundIntegrationEntries.clear();
	}

	@Override
	public void removeAllChromatogramIntegrationEntries() {

		chromatogramIntegrationEntries.clear();
	}

	@Override
	public void setUnloaded() {

		unloaded = true;
	}

	@Override
	public boolean isUnloaded() {

		return unloaded;
	}

	@Override
	public boolean containsScanCycles() {

		int defaultCycleNumber = 1;
		for(IScan scan : getScans()) {
			if(scan.getCycleNumber() != defaultCycleNumber) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IScan> getScanCycleScans(int scanCycle) {

		List<IScan> scanCycleScans = new ArrayList<>();
		if(scanCycle > 0) {
			if(containsScanCycles()) {
				/*
				 * Yes, there are cycle scan available.
				 */
				for(IScan scan : getScans()) {
					if(scan.getCycleNumber() == scanCycle) {
						scanCycleScans.add(scan);
					}
				}
			} else {
				/*
				 * If the cycle is 1, return the complete list.
				 * Otherwise do nothing.
				 */
				if(scanCycle == 1) {
					scanCycleScans.addAll(getScans());
				}
			}
		}
		return scanCycleScans;
	}

	@Override
	public IMethod getMethod() {

		return method;
	}

	@Override
	public ISeparationColumnIndices getSeparationColumnIndices() {

		return separationColumnIndices;
	}

	@Override
	public void setSeparationColumnIndices(ISeparationColumnIndices separationColumnIndices) {

		if(separationColumnIndices != null) {
			this.separationColumnIndices = separationColumnIndices;
		}
	}

	@Override
	public List<? extends IChromatogramPeak> getPeaks() {

		return peaks;
	}

	@Override
	public List<? extends IChromatogramPeak> getPeaks(int startRetentionTime, int stopRetentionTime) {

		return peaks.getRetentionTimeMap().getPeaks(startRetentionTime, stopRetentionTime);
	}

	@Override
	public Set<IIdentificationTarget> getTargets() {

		return identificationTargets;
	}

	@Override
	public int getModCount() {

		return modCount;
	}

	@Override
	public boolean isDirty() {

		return modCount != 0;
	}

	@Override
	public void setDirty(boolean dirty) {

		if(dirty) {
			modCount++;
		} else {
			modCount = 0;
		}
	}

	@Override
	public List<IAnalysisSegment> getAnalysisSegments() {

		return Collections.unmodifiableList(analysisSegments);
	}

	@Override
	public void defineAnalysisSegment(IScanRange range, Collection<? extends IAnalysisSegment> childs) {

		analysisSegments.add(new ChromatogramAnalysisSegment(range, this, childs));
	}

	@Override
	public void removeAnalysisSegment(IAnalysisSegment segment) {

	}

	@Override
	public void updateAnalysisSegment(IAnalysisSegment segment, IScanRange range) {

	}

	@Override
	public boolean equals(Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		IChromatogram other = (IChromatogram)otherObject;
		return getNumberOfScans() == other.getNumberOfScans() && getTotalSignal() == other.getTotalSignal() && getMinSignal() == other.getMinSignal() && getMaxSignal() == other.getMaxSignal() && getStartRetentionTime() == other.getStartRetentionTime() && getStopRetentionTime() == other.getStopRetentionTime();
	}

	@Override
	public int hashCode() {

		// for performance reason we here only take some basic information into account
		return 7 * Integer.hashCode(getNumberOfScans()) + 7 * Integer.hashCode(getStartRetentionTime()) + 9 * Integer.hashCode(getStopRetentionTime());
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("numberOfScans=" + getNumberOfScans());
		builder.append(",");
		builder.append("totalSignal=" + getTotalSignal());
		builder.append(",");
		builder.append("minSignal=" + getMinSignal());
		builder.append(",");
		builder.append("maxSignal=" + getMaxSignal());
		builder.append(",");
		builder.append("startRetentionTime=" + getStartRetentionTime());
		builder.append(",");
		builder.append("stopRetentionTime=" + getStopRetentionTime());
		builder.append("]");
		return builder.toString();
	}
}