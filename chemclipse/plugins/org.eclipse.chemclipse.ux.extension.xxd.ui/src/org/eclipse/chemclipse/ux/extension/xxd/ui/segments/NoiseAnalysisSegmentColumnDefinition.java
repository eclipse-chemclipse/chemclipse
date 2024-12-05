/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring noise handling
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.segments;

import java.util.List;

import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.support.traces.TraceFactory;
import org.eclipse.chemclipse.support.traces.TraceGeneric;
import org.eclipse.chemclipse.support.ui.swt.columns.ColumnDefinition;
import org.eclipse.chemclipse.support.ui.swt.columns.SimpleColumnDefinition;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeNode;

public class NoiseAnalysisSegmentColumnDefinition extends AnalysisSegmentColumnDefinition {

	public NoiseAnalysisSegmentColumnDefinition(Runnable updateListener) {

		super(updateListener);
	}

	@Override
	public List<ColumnDefinition<?, ?>> getColumnDefinitions() {

		List<ColumnDefinition<?, ?>> definitions = super.getColumnDefinitions();
		definitions.add(new SimpleColumnDefinition<>("Ion", 100, new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof TreeNode treeNode) {
					element = treeNode.getValue();
				}
				//
				if(element instanceof INoiseSegment noiseSegment) {
					List<TraceGeneric> traces = TraceFactory.parseTraces(noiseSegment.getTraces(), TraceGeneric.class);
					if(!traces.isEmpty()) {
						double value = traces.get(0).getValue();
						if(value == IIon.TIC_ION) {
							return "TIC";
						}
						//
						return String.valueOf((int)value);
					}
				}
				return "-";
			}
		}));
		return definitions;
	}
}