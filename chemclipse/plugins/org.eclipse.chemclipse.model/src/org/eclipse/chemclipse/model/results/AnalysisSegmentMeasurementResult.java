/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring Observer
 *******************************************************************************/
package org.eclipse.chemclipse.model.results;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.IScanRange;

public abstract class AnalysisSegmentMeasurementResult<T extends IAnalysisSegment> implements PropertyChangeListener, IMeasurementResult<List<T>> {

	private static final long serialVersionUID = 5118533547343273406L;
	private T selection;

	@Override
	public String getIdentifier() {

		return getClass().getName();
	}

	@Override
	public String getDescription() {

		return ""; //$NON-NLS-1$
	}

	public abstract Class<T> getType();

	/**
	 * the list of segments from this result in the given range
	 * 
	 * @param range
	 *            the range for which segments should be fetched
	 * @param includeBorders
	 *            if <code>true</code> segments that only partially match are included in the result (e.g. a segment starts outside the range but ends inside it)
	 * @return a possible empty list of segments that are available for the given range ordered by the start scan
	 */
	public List<T> getSegments(IScanRange range, boolean includeBorders) {

		return IAnalysisSegment.getSegments(range, includeBorders, getResult());
	}

	public T getSelection() {

		return selection;
	}

	public void setSelection(T selection) {

		if(this.selection != selection) {
			this.selection = selection;
			propertyChange(new PropertyChangeEvent(this, getDescription(), selection, selection));
		}
	}

	public boolean isSelected(T item) {

		return this.selection == item;
	}

	public void notifyListener() {

		propertyChange(new PropertyChangeEvent(this, getDescription(), selection, selection));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}