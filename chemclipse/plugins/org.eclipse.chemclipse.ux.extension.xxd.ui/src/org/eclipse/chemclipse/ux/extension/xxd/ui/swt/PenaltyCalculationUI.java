/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.identifier.IPenaltyCalculationSettings;
import org.eclipse.chemclipse.model.identifier.PenaltyCalculation;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.model.PenaltyCalculationModel;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class PenaltyCalculationUI extends Composite {

	private AtomicReference<Button> buttonClipboard = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerPenaltyCalculation = new AtomicReference<>();
	private AtomicReference<Text> textReferenceValue = new AtomicReference<>();
	private AtomicReference<Text> textPenaltyWindow = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerPenaltyLevelFactor = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerMaxPenalty = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerPenaltyMissingReference = new AtomicReference<>();
	private AtomicReference<Button> buttonCalculate = new AtomicReference<>();

	private PenaltyCalculationModel penaltyCalculationModel = new PenaltyCalculationModel();
	private IUpdateListener updateListener;

	public PenaltyCalculationUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public PenaltyCalculationModel getPenaltyCalculationModel() {

		return penaltyCalculationModel;
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(8, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);

		createButtonClipboard(this);
		createComboViewer(this);
		createTextReferenceValue(this);
		createTextPenaltyWindow(this);
		createSpinnerPenaltyLevelFactor(this);
		createSpinnerMaxPenalty(this);
		createSpinnerPenaltyMissingReference(this);
		createButtonCalculate(this);

		initialize();
	}

	private void initialize() {

		ComboViewer comboViewer = comboViewerPenaltyCalculation.get();
		comboViewer.setInput(PenaltyCalculation.values());
		comboViewer.getCombo().select(0);

		updateWidgets();
	}

	private void createButtonClipboard(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Copy settings to clipboard");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COPY_CLIPBOARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String lineDelimiter = OperatingSystemUtils.getLineDelimiter();
				StringBuilder builder = new StringBuilder();
				PenaltyCalculation penaltyCalculation = getPenaltySelection();

				builder.append("Penalty Calculation: ");
				builder.append(penaltyCalculation.label());
				builder.append(lineDelimiter);

				builder.append("Penalty Window: ");
				String penaltyWindow;
				switch(penaltyCalculation) {
					case RETENTION_TIME_MS:
						int retentionTimeMilliseconds = (int)getValueText(textPenaltyWindow);
						penaltyWindow = Integer.toString(retentionTimeMilliseconds);
						break;
					case RETENTION_TIME_MIN:
						double retentionTimeMinutes = getValueText(textPenaltyWindow);
						penaltyWindow = Double.toString(retentionTimeMinutes);
						break;
					case RETENTION_INDEX:
						double retentionIndex = getValueText(textPenaltyWindow);
						penaltyWindow = Double.toString(retentionIndex);
						break;
					default:
						penaltyWindow = "0";
						break;
				}
				builder.append(penaltyWindow);
				builder.append(lineDelimiter);

				builder.append("Penalty Calculation Level Factor: ");
				builder.append(getValueSpinner(spinnerPenaltyLevelFactor));
				builder.append(lineDelimiter);

				builder.append("Max Penalty: ");
				builder.append(getValueSpinner(spinnerMaxPenalty));

				builder.append("Penalty Missing Reference: ");
				builder.append(getValueSpinner(spinnerPenaltyMissingReference));

				Object[] data = new Object[]{builder.toString()};

				TextTransfer textTransfer = TextTransfer.getInstance();
				Transfer[] dataTypes = new Transfer[]{textTransfer};
				Clipboard clipboard = new Clipboard(Display.getDefault());
				clipboard.setContents(data, dataTypes);
				clipboard.dispose();
			}
		});

		buttonClipboard.set(button);
	}

	private void createComboViewer(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof PenaltyCalculation penaltyCalculation) {
					return penaltyCalculation.label();
				}
				return null;
			}
		});

		combo.setToolTipText("Select the penalty calculation type.");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateWidgets();
				calculate();
			}
		});

		comboViewerPenaltyCalculation.set(comboViewer);
	}

	private void createTextReferenceValue(Composite parent) {

		textReferenceValue.set(createText(parent, "Reference", 0.0d));
	}

	private void createTextPenaltyWindow(Composite parent) {

		textPenaltyWindow.set(createText(this, "Penalty Window", 10.0d));
	}

	private Text createText(Composite parent, String tooltip, double selection) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText(Double.toString(selection));
		text.setToolTipText(tooltip);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				updateButtons();
				if(e.keyCode == SWT.LF || e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					calculate();
				}
			}
		});

		return text;
	}

	private void createSpinnerPenaltyLevelFactor(Composite parent) {

		int min = (int)IPenaltyCalculationSettings.MIN_PENALTY_LEVEL_FACTOR;
		int max = (int)IPenaltyCalculationSettings.MAX_PENALTY_LEVEL_FACTOR;
		int selection = (int)IPenaltyCalculationSettings.DEF_PENALTY_LEVEL_FACTOR;
		spinnerPenaltyLevelFactor.set(createSpinner(parent, "Penalty Level Factor", min, max, selection));
	}

	private void createSpinnerMaxPenalty(Composite parent) {

		int min = (int)IPenaltyCalculationSettings.MIN_PENALTY_MATCH_FACTOR;
		int max = (int)IPenaltyCalculationSettings.MAX_PENALTY_MATCH_FACTOR;
		int selection = (int)IPenaltyCalculationSettings.DEF_PENALTY_MATCH_FACTOR;
		spinnerMaxPenalty.set(createSpinner(parent, "Max Penalty", min, max, selection));
	}

	private void createSpinnerPenaltyMissingReference(Composite parent) {

		int min = (int)IPenaltyCalculationSettings.MIN_PENALTY_MATCH_FACTOR;
		int max = (int)IPenaltyCalculationSettings.MAX_PENALTY_MATCH_FACTOR;
		int selection = 0;
		spinnerPenaltyMissingReference.set(createSpinner(parent, "Penalty Missing Reference", min, max, selection));
	}

	private Spinner createSpinner(Composite parent, String tooltip, int min, int max, int selection) {

		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setToolTipText(tooltip);
		spinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinner.setIncrement(1);
		spinner.setMinimum(min);
		spinner.setMaximum(max);
		spinner.setSelection(selection);

		spinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				calculate();
			}
		});

		spinner.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				updateButtons();
				if(e.keyCode == SWT.LF || e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					calculate();
				}
			}
		});

		return spinner;
	}

	private void createButtonCalculate(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Calculate");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				calculate();
			}
		});

		buttonCalculate.set(button);
	}

	private PenaltyCalculation getPenaltySelection() {

		Object object = comboViewerPenaltyCalculation.get().getStructuredSelection().getFirstElement();
		if(object instanceof PenaltyCalculation penaltyCalculation) {
			return penaltyCalculation;
		}

		return PenaltyCalculation.NONE;
	}

	private boolean validate() {

		return getValueText(textReferenceValue) != -1 && //
				getValueText(textPenaltyWindow) != -1 && //
				getValueSpinner(spinnerPenaltyLevelFactor) != -1 && //
				getValueSpinner(spinnerMaxPenalty) != -1 && //
				getValueSpinner(spinnerPenaltyMissingReference) != -1;
	}

	private void updateWidgets() {

		boolean enabled = true;
		PenaltyCalculation penaltyCalculation = getPenaltySelection();

		switch(penaltyCalculation) {
			case RETENTION_TIME_MS:
				textReferenceValue.get().setToolTipText("Retention Time Reference [min]");
				break;
			case RETENTION_TIME_MIN:
				textReferenceValue.get().setToolTipText("Retention Time Reference [min]");
				break;
			case RETENTION_INDEX:
				textReferenceValue.get().setToolTipText("Retention Index Reference");
				break;
			default:
				enabled = false;
				break;
		}

		updateWidgets(enabled);
		updateButtons();
	}

	private void updateWidgets(boolean enabled) {

		buttonClipboard.get().setEnabled(enabled);
		textReferenceValue.get().setEnabled(enabled);
		textPenaltyWindow.get().setEnabled(enabled);
		spinnerPenaltyLevelFactor.get().setEnabled(enabled);
		spinnerMaxPenalty.get().setEnabled(enabled);
		spinnerPenaltyMissingReference.get().setEnabled(enabled);
		buttonCalculate.get().setEnabled(enabled);
	}

	private boolean updateButtons() {

		boolean enabled = validate();
		buttonClipboard.get().setEnabled(enabled);
		buttonCalculate.get().setEnabled(enabled);
		return enabled;
	}

	private void calculate() {

		if(updateButtons()) {
			penaltyCalculationModel.setReferenceValue(getValueText(textReferenceValue));
			IPenaltyCalculationSettings penaltyCalculationSettings = penaltyCalculationModel.getPenaltyCalculationSettings();
			penaltyCalculationSettings.setPenaltyCalculation(getPenaltySelection());
			penaltyCalculationSettings.setPenaltyWindow(getValueText(textPenaltyWindow));
			penaltyCalculationSettings.setPenaltyLevelFactor(getValueSpinner(spinnerPenaltyLevelFactor));
			penaltyCalculationSettings.setMaxPenalty(getValueSpinner(spinnerMaxPenalty));
			penaltyCalculationSettings.setPenaltyMissingReference(getValueSpinner(spinnerPenaltyMissingReference));
			fireUpdate();
		}
	}

	private float getValueText(AtomicReference<Text> text) {

		try {
			return (float)Double.parseDouble(text.get().getText().trim());
		} catch(NumberFormatException e) {
			return -1;
		}
	}

	private float getValueSpinner(AtomicReference<Spinner> spinner) {

		return (float)spinner.get().getSelection();
	}

	private void fireUpdate() {

		if(updateListener != null) {
			updateListener.update();
		}
	}
}