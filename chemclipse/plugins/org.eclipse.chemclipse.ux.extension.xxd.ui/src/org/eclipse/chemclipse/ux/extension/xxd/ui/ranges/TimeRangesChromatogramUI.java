/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.ranges;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.ranges.TimeRange;
import org.eclipse.chemclipse.model.ranges.TimeRanges;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;

public class TimeRangesChromatogramUI extends Composite implements IExtendedPartUI {

	private AtomicReference<TimeRangesUI> rangesControl = new AtomicReference<>();
	private AtomicReference<TimeRangesChart> chartControl = new AtomicReference<>();
	//
	private TimeRangeAdjustmentHandler timeRangeAdjustmentHandler = new TimeRangeAdjustmentHandler();
	private TimeRangeSelectionHandler timeRangeSelectionHandler = new TimeRangeSelectionHandler();
	//
	private TimeRanges timeRanges = null;
	private TimeRangeMarker timeRangeMarker;
	private ITimeRangeUpdateListener updateListener;

	public TimeRangesChromatogramUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public void update() {

		super.update();
		rangesControl.get().setInput(timeRanges);
	}

	public TimeRangeModus getTimeRangeModus() {

		return chartControl.get().getTimeRangeModus();
	}

	public void setTimeRangeModus(TimeRangeModus timeRangeModus) {

		chartControl.get().setTimeRangeModus(timeRangeModus);
	}

	public void setTimeRangeLabels(TimeRangeLabels timeRangeLabels) {

		rangesControl.get().setTimeRangeLabels(timeRangeLabels);
		chartControl.get().setTimeRangeLabels(timeRangeLabels);
	}

	public void setUpdateListener(ITimeRangeUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void setInput(TimeRanges timeRanges) {

		this.timeRanges = timeRanges;
		updateInput();
	}

	public void select(TimeRange timeRange) {

		updateTimeRangeMarker(timeRange);
	}

	public AtomicReference<TimeRangesUI> getTimeRangesControl() {

		return rangesControl;
	}

	public AtomicReference<TimeRangesChart> getChromatogramChartControl() {

		return chartControl;
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createTimeRangesUI(this);
		createTimeRangesChart(this);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(rangesControl, true);
		//
		timeRangeAdjustmentHandler.setUpdateListener(new ITimeRangeUpdateListener() {

			@Override
			public void update(TimeRange timeRange) {

				updateTimeRangeMarker(timeRange);
				fireUpdate(timeRange);
			}
		});
		//
		timeRangeSelectionHandler.setUpdateListener(new ITimeRangeUpdateListener() {

			@Override
			public void update(TimeRange timeRange) {

				updateTimeRangeMarker(timeRange);
				fireUpdate(timeRange);
			}
		});
	}

	private void createTimeRangesUI(Composite parent) {

		TimeRangesUI timeRangesUI = new TimeRangesUI(parent, SWT.NONE);
		timeRangesUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		timeRangesUI.setUpdateListener(new ITimeRangeUpdateListener() {

			@Override
			public void update(TimeRange timeRange) {

				chartControl.get().updateTimeRangeMarker(timeRange);
				updateTimeRangeMarker(timeRange);
				fireUpdate(timeRange);
			}
		});
		//
		rangesControl.set(timeRangesUI);
	}

	private void createTimeRangesChart(Composite parent) {

		TimeRangesChart timeRangesChart = new TimeRangesChart(parent, SWT.NONE);
		timeRangesChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		timeRangeMarker = addTimeRangeMarker(timeRangesChart);
		//
		IChartSettings chartSettings = timeRangesChart.getChartSettings();
		chartSettings.addHandledEventProcessor(timeRangeAdjustmentHandler);
		chartSettings.addHandledEventProcessor(timeRangeSelectionHandler);
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IKeyboardSupport.EVENT_KEY_UP;
			}

			@Override
			public int getButton() {

				return IKeyboardSupport.KEY_CODE_LC_L;
			}

			@Override
			public int getStateMask() {

				return SWT.NONE;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				TimeRange timeRange = timeRangeMarker.getTimeRangeSelection();
				if(timeRange != null) {
					TimeRangeSupport.toggleUpdateLock(timeRanges, timeRange);
					updateTimeRangeMarker(timeRange);
					fireUpdate(timeRange);
				}
			}
		});
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IKeyboardSupport.EVENT_KEY_UP;
			}

			@Override
			public int getButton() {

				return SWT.DEL;
			}

			@Override
			public int getStateMask() {

				return SWT.NONE;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				TimeRange timeRange = timeRangeMarker.getTimeRangeSelection();
				if(timeRange != null) {
					if(MessageDialog.openQuestion(event.display.getActiveShell(), "Time Range", "Would you like to delete '" + timeRange.getIdentifier() + "'?")) {
						timeRanges.remove(timeRange);
						updateInput();
						fireUpdate(null);
					}
				}
			}
		});
		timeRangesChart.applySettings(chartSettings);
		//
		timeRangesChart.setUpdateListener(new ITimeRangeUpdateListener() {

			@Override
			public void update(TimeRange timeRange) {

				TimeRangesUI timeRangeUI = rangesControl.get();
				timeRangeUI.update();
				timeRangeUI.select(timeRange);
				chartControl.get().updateTimeRangeMarker(timeRange);
				updateTimeRangeMarker(timeRange);
				fireUpdate(timeRange);
			}
		});
		//
		BaseChart baseChart = timeRangesChart.getBaseChart();
		IPlotArea plotArea = baseChart.getPlotArea();
		plotArea.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {

				TimeRange timeRange = TimeRangeSelector.selectRange(baseChart, e.x, -1, -1, timeRanges);
				if(timeRange != null) {
					int hoverPositionX = (int)baseChart.getSelectedPrimaryAxisValue(e.x, IExtendedChart.X_AXIS);
					timeRangeMarker.setTimeRangeHover(timeRange, hoverPositionX);
				} else {
					timeRangeMarker.clearTimeRangeHover();
				}
			}
		});
		//
		chartControl.set(timeRangesChart);
	}

	private TimeRangeMarker addTimeRangeMarker(ScrollableChart scrollableChart) {

		BaseChart baseChart = scrollableChart.getBaseChart();
		IPlotArea plotArea = baseChart.getPlotArea();
		TimeRangeMarker timeRangeMarker = new TimeRangeMarker(baseChart);
		plotArea.addCustomPaintListener(timeRangeMarker);
		//
		return timeRangeMarker;
	}

	private void setTimeRangesVisible(boolean visible) {

		TimeRangesUI timeRangesUI = rangesControl.get();
		if(timeRangesUI != null) {
			timeRangesUI.setVisible(visible);
			Object layoutData = timeRangesUI.getLayoutData();
			if(layoutData instanceof GridData gridData) {
				gridData.exclude = !visible;
			}
			Composite parent = timeRangesUI.getParent();
			parent.layout(true);
			parent.redraw();
		}
	}

	private void updateInput() {

		rangesControl.get().setInput(timeRanges);
		timeRangeAdjustmentHandler.setInput(timeRanges);
		timeRangeSelectionHandler.setInput(timeRanges);
		updateTimeRangeMarker(null);
		chartControl.get().setInput(timeRanges);
	}

	private void updateTimeRangeMarker(TimeRange timeRange) {

		timeRangeMarker.getTimeRanges().clear();
		if(timeRanges != null) {
			/*
			 * Update the time range composite.
			 * Don't set the time ranges visible, e.g. if the user has set
			 * the composite invisible. The time ranges can be toggled via
			 * IExtendedPartUI & AtomicReference<TimeRangesUI> getTimeRangesControl().
			 * setTimeRangesVisible(true);
			 */
			chartControl.get().updateTimeRangeMarker(timeRange);
			rangesControl.get().select(timeRange);
			timeRangeMarker.getTimeRanges().addAll(timeRanges.values());
			timeRangeMarker.setTimeRangeSelection(timeRange);
		} else {
			/*
			 * Hide the time range composite.
			 */
			setTimeRangesVisible(false);
		}
		//
		chartControl.get().getBaseChart().redraw();
	}

	private void fireUpdate(TimeRange timeRange) {

		if(updateListener != null) {
			updateListener.update(timeRange);
		}
	}
}