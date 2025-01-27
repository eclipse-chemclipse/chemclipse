/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.quantitation.IInternalStandard;
import org.eclipse.chemclipse.model.quantitation.InternalStandard;
import org.eclipse.chemclipse.model.quantitation.ResponseOption;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.support.validators.ConcentrationValidator;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.validation.CompensationFactorValidator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.validation.NameValidator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.PeakDataSupport;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class ExtendedInternalStandardsUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedInternalStandardsUI.class);
	//
	private static final String MENU_CATEGORY_ISTD = "Internal Standards";
	//
	private static final String ACTION_INITIALIZE = "ACTION_INITIALIZE";
	private static final String ACTION_CANCEL = "ACTION_CANCEL";
	private static final String ACTION_ADD = "ACTION_ADD";
	private static final String ACTION_DELETE = "ACTION_DELETE";
	private static final String ACTION_SELECT = "ACTION_SELECT";
	//
	private Composite toolbarInfo;
	private Composite toolbarModify;
	private Composite toolbarAdd;
	private Label labelPeak;
	private Label labelInputErrors;
	//
	private ComboViewer comboName;
	private Text textConcentration;
	private ComboViewer comboViewerFactor;
	private Text textFactor;
	private Button buttonInsert;
	//
	private NameValidator nameValidator;
	private ControlDecoration nameControlDecoration;
	private ConcentrationValidator concentrationValidator;
	private ControlDecoration concentrationControlDecoration;
	private CompensationFactorValidator compensationFactorValidator;
	private ControlDecoration compensationFactorControlDecoration;
	//
	private Button buttonCancel;
	private Button buttonAdd;
	private Button buttonDelete;
	//
	private InternalStandardsListUI internalStandardsListUI;
	private IPeak peak;
	//
	private PeakDataSupport peakDataSupport = new PeakDataSupport();

	public ExtendedInternalStandardsUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public boolean setFocus() {

		updatePeak();
		return true;
	}

	public void update(IPeak peak) {

		this.peak = peak;
		updatePeak();
	}

	private void updatePeak() {

		String editInformation = internalStandardsListUI.isEditEnabled() ? "Edit is enabled." : "Edit is disabled.";
		labelPeak.setText(peakDataSupport.getPeakLabel(peak) + " - " + editInformation);
		//
		if(peak != null) {
			internalStandardsListUI.setInput(peak.getInternalStandards());
			/*
			 * Extract the targets to set an ISTD more easily.
			 */
			float retentionIndex = peak.getPeakModel().getPeakMaximum().getRetentionIndex();
			List<IIdentificationTarget> targets = IIdentificationTarget.getTargetsSorted(peak.getTargets(), retentionIndex);
			List<String> names = new ArrayList<>();
			for(IIdentificationTarget target : targets) {
				String name = target.getLibraryInformation().getName();
				if(!names.contains(name)) {
					names.add(name);
				}
			}
			comboName.setInput(names);
		} else {
			internalStandardsListUI.setInput(null);
			comboName.setInput(Collections.emptyList());
		}
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		toolbarInfo = createToolbarInfo(this);
		toolbarModify = createToolbarModify(this);
		toolbarAdd = createToolbarAdd(this);
		createInternalStandardsList(this);
		//
		PartSupport.setCompositeVisibility(toolbarInfo, true);
		PartSupport.setCompositeVisibility(toolbarModify, false);
		PartSupport.setCompositeVisibility(toolbarAdd, false);
		//
		internalStandardsListUI.setEditEnabled(false);
		enableButtonFields(ACTION_INITIALIZE);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, false));
		//
		createButtonToggleToolbarInfo(composite);
		createButtonToggleToolbarModify(composite);
		createButtonToggleEditModus(composite);
	}

	private Composite createToolbarInfo(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));
		//
		labelPeak = new Label(composite, SWT.NONE);
		labelPeak.setText("");
		labelPeak.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return composite;
	}

	private Composite createToolbarModify(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(4, false));
		//
		createErrorLabel(composite);
		buttonCancel = createButtonCancel(composite);
		buttonAdd = createButtonAdd(composite);
		buttonDelete = createButtonDelete(composite);
		//
		return composite;
	}

	private void createErrorLabel(Composite parent) {

		labelInputErrors = new Label(parent, SWT.NONE);
		labelInputErrors.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private Button createButtonCancel(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Cancel Operation");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CANCEL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				enableButtonFields(ACTION_CANCEL);
				clearLabelInputErrors();
				PartSupport.setCompositeVisibility(toolbarAdd, false);
			}
		});
		return button;
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add Internal Standard");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(peak != null) {
					if(peak.getIntegratedArea() == 0) {
						setLabelInputError("The peak area is 0. Please integrate the peaks first.");
					} else {
						clearLabelInputErrors();
						enableButtonFields(ACTION_ADD);
						PartSupport.setCompositeVisibility(toolbarAdd, true);
					}
				} else {
					setLabelInputError("No peak has been selected yet.");
				}
			}
		});
		return button;
	}

	private Button createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete Internal Standard");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteInternalStandards(e.display.getActiveShell());
			}
		});
		return button;
	}

	private Composite createToolbarAdd(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(5, false));
		//
		createTextName(composite);
		createTextConcentration(composite);
		createComboViewerFactor(composite);
		createTextFactor(composite);
		buttonInsert = createButtonInsert(composite);
		//
		return composite;
	}

	private void createTextName(Composite parent) {

		comboName = new EnhancedComboViewer(parent, SWT.BORDER);
		Combo combo = comboName.getCombo();
		comboName.setContentProvider(ArrayContentProvider.getInstance());
		comboName.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				return element.toString();
			}
		});
		//
		combo.setText("");
		combo.setToolTipText("Name of the internal standard (ISTD).");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		nameValidator = new NameValidator();
		nameControlDecoration = new ControlDecoration(combo, SWT.LEFT | SWT.TOP);
		combo.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				validate(nameValidator, nameControlDecoration, comboName);
			}
		});
	}

	private void createTextConcentration(Composite parent) {

		textConcentration = new Text(parent, SWT.BORDER);
		textConcentration.setText("");
		textConcentration.setToolTipText("Concentration, e.g. 10 mg/L");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.minimumWidth = 80;
		textConcentration.setLayoutData(gridData);
		//
		concentrationValidator = new ConcentrationValidator();
		concentrationControlDecoration = new ControlDecoration(textConcentration, SWT.LEFT | SWT.TOP);
		textConcentration.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				validate(concentrationValidator, concentrationControlDecoration, textConcentration);
			}
		});
	}

	private void createComboViewerFactor(Composite parent) {

		comboViewerFactor = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewerFactor.getCombo();
		comboViewerFactor.setContentProvider(ArrayContentProvider.getInstance());
		comboViewerFactor.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ResponseOption responseOption) {
					return responseOption.label();
				}
				return null;
			}
		});
		//
		combo.setToolTipText("Response Option");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		comboViewerFactor.setInput(ResponseOption.values());
		comboViewerFactor.setSelection(new StructuredSelection(ResponseOption.COMPENSATION_FACTOR));
	}

	private void createTextFactor(Composite parent) {

		textFactor = new Text(parent, SWT.BORDER);
		textFactor.setText("1.0");
		textFactor.setToolTipText("Factor");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.minimumWidth = 50;
		textFactor.setLayoutData(gridData);
		//
		compensationFactorValidator = new CompensationFactorValidator();
		compensationFactorControlDecoration = new ControlDecoration(textFactor, SWT.LEFT | SWT.TOP);
		textFactor.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				validate(compensationFactorValidator, compensationFactorControlDecoration, textFactor);
			}
		});
	}

	private Button createButtonInsert(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Insert Internal Standard");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addInternalStandard(e.display.getActiveShell());
			}
		});
		//
		return button;
	}

	private Button createButtonToggleToolbarInfo(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle info toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarInfo);
				button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImageProvider.SIZE_16x16, visible));
			}
		});
		//
		return button;
	}

	private Button createButtonToggleToolbarModify(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setToolTipText("Toggle modify toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_DEFAULT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarModify);
				if(!visible) {
					/*
					 * Hide the add toolbar if the modify toolbar is set to hidden.
					 */
					PartSupport.setCompositeVisibility(toolbarAdd, false);
				}
				//
				if(visible) {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_ACTIVE, IApplicationImageProvider.SIZE_16x16));
				} else {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_DEFAULT, IApplicationImageProvider.SIZE_16x16));
				}
			}
		});
		//
		return button;
	}

	private Button createButtonToggleEditModus(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setToolTipText("Enable/disable to edit the table.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_ENTRY_DEFAULT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean editEnabled = !internalStandardsListUI.isEditEnabled();
				internalStandardsListUI.setEditEnabled(editEnabled);
				button.setImage(ApplicationImageFactory.getInstance().getImage((editEnabled) ? IApplicationImage.IMAGE_EDIT_ENTRY_ACTIVE : IApplicationImage.IMAGE_EDIT_ENTRY_DEFAULT, IApplicationImageProvider.SIZE_16x16));
				updatePeak();
			}
		});
		//
		return button;
	}

	private void createInternalStandardsList(Composite parent) {

		internalStandardsListUI = new InternalStandardsListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = internalStandardsListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				enableButtonFields(ACTION_SELECT);
			}
		});
		/*
		 * Add the delete targets support.
		 */
		Shell shell = internalStandardsListUI.getTable().getShell();
		ITableSettings tableSettings = internalStandardsListUI.getTableSettings();
		addDeleteMenuEntry(shell, tableSettings);
		addKeyEventProcessors(shell, tableSettings);
		internalStandardsListUI.applySettings(tableSettings);
	}

	private void addDeleteMenuEntry(Shell shell, ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Delete Internal Standards";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_ISTD;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				deleteInternalStandards(shell);
			}
		});
	}

	private void addKeyEventProcessors(Shell shell, ITableSettings tableSettings) {

		tableSettings.addKeyEventProcessor(new IKeyEventProcessor() {

			@Override
			public void handleEvent(ExtendedTableViewer extendedTableViewer, KeyEvent e) {

				if(e.keyCode == SWT.DEL) {
					/*
					 * DEL
					 */
					deleteInternalStandards(shell);
				}
			}
		});
	}

	private void deleteInternalStandards(Shell shell) {

		if(peak != null) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			messageBox.setText("Delete Internal Standards");
			messageBox.setMessage("Would you like to delete the selected internal standards?");
			if(messageBox.open() == SWT.YES) {
				/*
				 * Delete ISTD
				 */
				enableButtonFields(ACTION_DELETE);
				Iterator<?> iterator = internalStandardsListUI.getStructuredSelection().iterator();
				while(iterator.hasNext()) {
					Object object = iterator.next();
					if(object instanceof IInternalStandard internalStandard) {
						deleteInternalStandard(internalStandard);
					}
				}
				updatePeak();
			}
		}
	}

	private void deleteInternalStandard(IInternalStandard internalStandard) {

		if(peak != null) {
			peak.removeInternalStandard(internalStandard);
		}
	}

	private void addInternalStandard(Shell shell) {

		if(peak == null) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			messageBox.setText("Add Internal Standard (ISTD)");
			messageBox.setMessage("No peak has been selected.");
			messageBox.open();
		} else {
			try {
				boolean isInputValid = false;
				String name = "";
				double concentration = 0.0d;
				String concentrationUnit = "";
				double factor = 0.0d;
				//
				isInputValid = validate(nameValidator, nameControlDecoration, comboName);
				name = nameValidator.getName();
				//
				if(isInputValid) {
					isInputValid = validate(concentrationValidator, concentrationControlDecoration, textConcentration);
					concentration = concentrationValidator.getConcentration();
					concentrationUnit = concentrationValidator.getUnit();
				}
				//
				if(isInputValid) {
					isInputValid = validate(compensationFactorValidator, compensationFactorControlDecoration, textFactor);
					factor = compensationFactorValidator.getCompensationFactor();
				}
				/*
				 * Add
				 */
				if(isInputValid) {
					String chemicalClass = ""; // Use the edit modus to set it.
					double compensationFactor = getCompensationFactor(factor);
					IInternalStandard internalStandard = new InternalStandard(name, concentration, concentrationUnit, compensationFactor);
					internalStandard.setChemicalClass(chemicalClass);
					//
					if(peak.getInternalStandards().contains(internalStandard)) {
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
						messageBox.setText("Add Internal Standard (ISTD)");
						messageBox.setMessage("The Internal Standard (ISTD) exists already.");
						messageBox.open();
					} else {
						peak.addInternalStandard(internalStandard);
						comboName.getCombo().setText("");
						textConcentration.setText("");
						textFactor.setText(Double.toString(IInternalStandard.STANDARD_COMPENSATION_FACTOR));
						enableButtonFields(ACTION_INITIALIZE);
						updatePeak();
					}
				}
			} catch(Exception e1) {
				logger.warn(e1);
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
				messageBox.setText("Add Internal Standard (ISTD)");
				messageBox.setMessage("Please check the content, response factor and unit values.");
				messageBox.open();
			}
		}
	}

	private double getCompensationFactor(double factor) {

		double compensationFactor = factor;
		Object object = comboViewerFactor.getStructuredSelection().getFirstElement();
		if(object instanceof ResponseOption responseOption) {
			if(ResponseOption.RESPONSE_FACTOR.equals(responseOption)) {
				if(factor != 0) {
					compensationFactor = 1.0d / factor;
				}
			}
		}
		//
		return compensationFactor;
	}

	private void enableButtonFields(String action) {

		enableFields(false);
		switch(action) {
			case ACTION_INITIALIZE:
				buttonAdd.setEnabled(true);
				break;
			case ACTION_CANCEL:
				buttonAdd.setEnabled(true);
				break;
			case ACTION_ADD:
				buttonCancel.setEnabled(true);
				comboName.getCombo().setEnabled(true);
				textConcentration.setEnabled(true);
				textFactor.setEnabled(true);
				buttonInsert.setEnabled(true);
				break;
			case ACTION_DELETE:
				buttonAdd.setEnabled(true);
				break;
			case ACTION_SELECT:
				buttonAdd.setEnabled(true);
				buttonCancel.setEnabled(true);
				//
				if(internalStandardsListUI.getTable().getSelectionIndex() >= 0) {
					buttonDelete.setEnabled(true);
				} else {
					buttonDelete.setEnabled(false);
				}
				break;
		}
	}

	private void enableFields(boolean enabled) {

		buttonCancel.setEnabled(enabled);
		buttonDelete.setEnabled(enabled);
		buttonAdd.setEnabled(enabled);
		//
		comboName.getCombo().setEnabled(enabled);
		textConcentration.setEnabled(enabled);
		textFactor.setEnabled(enabled);
		buttonInsert.setEnabled(enabled);
	}

	private void clearLabelInputErrors() {

		labelInputErrors.setText("");
		labelInputErrors.setBackground(null);
	}

	private void setLabelInputError(String message) {

		labelInputErrors.setText(message);
		labelInputErrors.setBackground(Colors.LIGHT_YELLOW);
	}

	private boolean validate(IValidator<Object> validator, ControlDecoration controlDecoration, Text text) {

		return validate(validator, controlDecoration, text.getText());
	}

	private boolean validate(IValidator<Object> validator, ControlDecoration controlDecoration, ComboViewer combo) {

		return validate(validator, controlDecoration, combo.getCombo().getText());
	}

	private boolean validate(IValidator<Object> validator, ControlDecoration controlDecoration, String text) {

		IStatus status = validator.validate(text);
		if(status.isOK()) {
			controlDecoration.hide();
			clearLabelInputErrors();
			return true;
		} else {
			setLabelInputError(status.getMessage());
			controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
			controlDecoration.showHoverText("Input Error");
			controlDecoration.show();
			return false;
		}
	}
}