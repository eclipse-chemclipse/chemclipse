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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.ranges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.comparator.TimeRangeComparator;
import org.eclipse.chemclipse.model.ranges.TimeRange;
import org.eclipse.chemclipse.model.ranges.TimeRanges;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.validation.TimeRangeInputValidator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageTimeRanges;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TimeRangesUI extends Composite implements IExtendedPartUI {

	/*
	 * If no selection is active, then timeRange is null.
	 */
	private static final String NO_SELECTION = "--";

	private AtomicReference<Button> buttonPrevious = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerControl = new AtomicReference<>();
	private AtomicReference<Button> buttonNext = new AtomicReference<>();
	private AtomicReference<TimeRangeUI> timeRangeControl = new AtomicReference<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<Text> textTracesControl = new AtomicReference<>();
	/*
	 * TimeRanges is the object, that contains a map of ranges.
	 * TimeRange is the currently selected time range.
	 */
	private TimeRanges timeRanges = null;
	private TimeRange timeRange = null;
	private TimeRangeLabels timeRangeLabels = new TimeRangeLabels();

	private ITimeRangeUpdateListener updateListener = null;

	public TimeRangesUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public void update() {

		super.update();
		updateInput();
	}

	public void setTimeRangeLabels(TimeRangeLabels timeRangeLabels) {

		this.timeRangeLabels = timeRangeLabels;
	}

	public void setInput(TimeRanges timeRanges) {

		this.timeRanges = timeRanges;
		updateInput();
	}

	public void select(TimeRange timeRange) {

		this.timeRange = timeRange;
		Combo combo = comboViewerControl.get().getCombo();

		if(timeRange != null) {
			String[] items = combo.getItems();
			exitloop:
			for(int i = 0; i < items.length; i++) {
				if(items[i].equals(timeRange.getIdentifier())) {
					combo.select(i);
					break exitloop;
				}
			}
		} else {
			combo.select(0);
		}

		updateTimeRange(timeRange, false);
	}

	public void setUpdateListener(ITimeRangeUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(10, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);

		createButtonPrevious(this);
		createComboViewer(this);
		createButtonNext(this);
		createTimeRangeUI(this);
		createSeparator(this);
		createTextTraces(this);
		createSeparator(this);
		createButtonAdd(this);
		createButtonDelete(this);
		createButtonSettings(this);

		initialize();
	}

	private void initialize() {

		updateComboViewer();
	}

	private void createButtonPrevious(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Previous");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ARROW_BACKWARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				TimeRange timeRangePrevious = TimeRangeSupport.getTimeRangePrevious(timeRanges, timeRange);
				if(timeRangePrevious != null) {
					timeRange = timeRangePrevious;
					updateTimeRange(timeRange, true);
				}
			}
		});

		buttonPrevious.set(button);
	}

	private void createComboViewer(Composite composite) {

		ComboViewer comboViewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof TimeRange timeRange) {
					return timeRange.getIdentifier();
				} else if(element instanceof String text) {
					return text;
				}
				return null;
			}
		});
		/*
		 * Select the item.
		 */
		combo.setToolTipText("Select");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof TimeRange selectedTimeRange) {
					timeRange = selectedTimeRange;
					updateTimeRange(timeRange, true);
				} else {
					timeRange = null; // NO_SELECTION
					updateTimeRange(timeRange, true);
				}
			}
		});

		comboViewerControl.set(comboViewer);
	}

	private void createButtonNext(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Next");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ARROW_FORWARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				TimeRange timeRangeNext = TimeRangeSupport.getTimeRangeNext(timeRanges, timeRange);
				if(timeRangeNext != null) {
					timeRange = timeRangeNext;
					updateTimeRange(timeRange, true);
				}
			}
		});

		buttonNext.set(button);
	}

	private void createTimeRangeUI(Composite parent) {

		TimeRangeUI timeRangeUI = new TimeRangeUI(parent, SWT.NONE);
		timeRangeUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		timeRangeUI.setUpdateListener((TimeRange timeRange) -> {
			updateTimeRange(timeRange, true);
		});

		timeRangeControl.set(timeRangeUI);
	}

	private void createTextTraces(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setToolTipText("Modify traces for the given time range.");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(timeRange != null) {
					timeRange.setTraces(text.getText().trim());
					if(e.keyCode == SWT.LF || e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
						fireUpdate(timeRange);
					}
				}
			}

		});

		textTracesControl.set(text);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(timeRanges != null) {
					InputDialog dialog = new InputDialog(e.display.getActiveShell(), timeRangeLabels.getTitle(), timeRangeLabels.getCreateMessage(), timeRangeLabels.getCreateInitialValue(), new TimeRangeInputValidator(timeRanges.keySet(), timeRangeLabels));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						TimeRange timeRangeNew = timeRanges.extractTimeRange(item);
						if(timeRangeNew != null) {
							timeRanges.add(timeRangeNew);
							timeRange = timeRangeNew;
							updateComboViewer();
							updateTimeRange(timeRangeNew, true);
						}
					}
				}
			}
		});

		buttonAdd.set(button);
	}

	private Label createSeparator(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("|");
		label.setForeground(Colors.GRAY);

		return label;
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), timeRangeLabels.getTitle(), timeRangeLabels.getDeleteMessage())) {
					Object object = comboViewerControl.get().getStructuredSelection().getFirstElement();
					if(object instanceof TimeRange selectedTimeRange) {
						timeRanges.remove(selectedTimeRange);
						updateComboViewer();
						updateTimeRange(timeRange, true);
					}
				}
			}
		});

		buttonDelete.set(button);
	}

	private void createButtonSettings(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePageTimeRanges.class), display -> applySettings());
	}

	private void applySettings() {

		updateLabels();
	}

	private void updateLabels() {

		timeRangeControl.get().update();
		/*
		 * Layout the outer composites to
		 * enable more space for the labels.
		 */
		Composite parent = getParent();
		if(parent != null) {
			if(getParent() != null) {
				parent = getParent();
			}
		}

		if(parent != null && !parent.isDisposed()) {
			parent.layout(true);
			parent.redraw();
		}
	}

	private void updateComboViewer() {

		timeRange = null;
		ComboViewer comboViewer = comboViewerControl.get();

		if(timeRanges != null) {
			/*
			 * Sort
			 */
			buttonPrevious.get().setEnabled(true);
			buttonAdd.get().setEnabled(true);
			buttonNext.get().setEnabled(true);
			/*
			 * Create the sorted ranges and add the no selection
			 * entry at the beginning.
			 */
			List<TimeRange> timeRangesSorted = new ArrayList<>(timeRanges.values());
			Collections.sort(timeRangesSorted, new TimeRangeComparator());
			List<Object> timeRangesInput = new ArrayList<>();
			timeRangesInput.add(NO_SELECTION); // "No Selection"
			timeRangesInput.addAll(timeRangesSorted);

			Combo combo = comboViewer.getCombo();
			String currentSelection = combo.getText();
			comboViewer.setInput(timeRangesInput);

			int index = 0;
			if(combo.getItemCount() > 1) {
				buttonDelete.get().setEnabled(true);
				for(int i = 0; i < timeRangesSorted.size(); i++) {
					TimeRange timeRangeSorted = timeRangesSorted.get(i);
					if(timeRangeSorted.getIdentifier().equals(currentSelection)) {
						timeRange = timeRangeSorted;
						index = i + 1; // + 1 because the first entry is "No Selection"
					}
				}
			} else {
				buttonDelete.get().setEnabled(false);
			}
			combo.select(index);
		} else {
			buttonPrevious.get().setEnabled(false);
			buttonNext.get().setEnabled(false);
			buttonAdd.get().setEnabled(false);
			buttonDelete.get().setEnabled(false);
			comboViewer.setInput(null);
		}
	}

	private void updateInput() {

		updateComboViewer();
		updateTimeRange(timeRange, true);
		updateLabels();
	}

	private void updateTimeRange(TimeRange timeRange, boolean fireUpdate) {

		/*
		 * The time spinner act independently.
		 * If one range is modified, all other ranges
		 * shall be updated.
		 */
		boolean enabled = timeRange != null;
		timeRangeControl.get().setInput(timeRanges, timeRange);
		textTracesControl.get().setText(enabled ? timeRange.getTraces() : "");
		textTracesControl.get().setEnabled(enabled);
		buttonDelete.get().setEnabled(enabled);

		/*
		 * To prevent update cycle dead loops.
		 */
		if(fireUpdate) {
			fireUpdate(timeRange);
		}
	}

	private void fireUpdate(TimeRange timeRange) {

		if(updateListener != null) {
			updateListener.update(timeRange);
		}
	}
}
