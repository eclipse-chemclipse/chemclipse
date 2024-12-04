/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - support file selection, refactor for new settings model, use validators, support for longs
 * Matthias Mailänder - add labeled combo boxes
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.methods;

import java.io.File;

import org.eclipse.chemclipse.model.settings.AbstractProcessSettings;
import org.eclipse.chemclipse.support.settings.ComboSettingsProperty.ComboSupplier;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;
import org.eclipse.chemclipse.support.settings.parser.InputValue;
import org.eclipse.chemclipse.support.settings.validation.InputValidator;
import org.eclipse.chemclipse.support.text.ILabel;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.provider.AdapterLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class WidgetItem {

	private final InputValue inputValue;
	//
	private InputValidator inputValidator;
	private ControlDecoration controlDecoration;
	private Control control;
	private Object currentSelection;

	public WidgetItem(InputValue inputValue, Object currentSelection) {

		this.inputValue = inputValue;
		this.currentSelection = currentSelection;
	}

	public InputValue getInputValue() {

		return inputValue;
	}

	public void restoreDefaults() {

		if(control instanceof Text text) {
			text.setText((String)inputValue.getDefaultValue());
		} else if(control instanceof Button button) {
			button.setSelection(Boolean.valueOf((String)inputValue.getDefaultValue()));
		} else if(control instanceof Combo combo) {
			Object enumValue = getEnumFrom(inputValue.getDefaultValue(), inputValue.getRawType());
			if(enumValue instanceof ILabel labelledEnum) {
				setSelection(combo, labelledEnum.label());
			} else {
				setSelection(combo, enumValue.toString());
			}
		} else if(control instanceof Composite composite) {
			for(Control child : composite.getChildren()) {
				if(child instanceof Text text) {
					text.setText((String)inputValue.getDefaultValue());
				}
			}
		}
	}

	private static Object getEnumFrom(Object defaultValue, Class<?> rawType) {

		if(rawType.isEnum()) {
			for(Object constant : rawType.getEnumConstants()) {
				if(constant.toString().equals(defaultValue)) {
					return constant;
				}
			}
		}
		return defaultValue;
	}

	private void setSelection(Combo combo, String selection) {

		String[] items = combo.getItems();
		int selectedIndex = -1;
		for(int i = 0; i < items.length; i++) {
			if(items[i].equals(selection)) {
				selectedIndex = i;
				break;
			}
		}
		combo.select(selectedIndex);
	}

	public ControlDecoration getControlDecoration() {

		return controlDecoration;
	}

	public Control getControl() {

		return control;
	}

	public void initializeControl(Composite parent) {

		control = createControl(parent);
		inputValidator = new InputValidator(inputValue);
		controlDecoration = new ControlDecoration(control, SWT.LEFT | SWT.TOP);
	}

	public IStatus validate() {

		IStatus status = getStatus();
		if(status.isOK()) {
			controlDecoration.hide();
		} else {
			controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
			controlDecoration.showHoverText(status.getMessage());
			controlDecoration.show();
		}
		return status;
	}

	private IStatus getStatus() {

		Object currentValue;
		try {
			currentValue = getValue();
		} catch(RuntimeException e) {
			currentValue = null;
		}
		for(IValidator<Object> validator : inputValue.getValidators()) {
			IStatus validate = validator.validate(currentValue);
			if(!validate.isOK()) {
				return validate;
			}
		}
		return inputValidator.validate(currentValue);
	}

	public Object getValue() {

		Class<?> rawType = inputValue.getRawType();
		if(rawType != null) {
			/*
			 * File
			 */
			if(rawType == File.class) {
				return currentSelection;
			}
			/*
			 * Text
			 */
			if(control instanceof Text text) {
				/*
				 * Text
				 */
				String textValue = text.getText().trim();
				//
				if(rawType == int.class || rawType == Integer.class) {
					if(textValue.isEmpty()) {
						return 0;
					}
					return Integer.parseInt(textValue);
				} else if(rawType == long.class || rawType == Long.class) {
					if(textValue.isEmpty()) {
						return (long)0;
					}
					return Long.parseLong(textValue);
				} else if(rawType == float.class || rawType == Float.class) {
					if(textValue.isEmpty()) {
						return 0f;
					}
					return Float.parseFloat(textValue);
				} else if(rawType == double.class || rawType == Double.class) {
					if(textValue.isEmpty()) {
						return 0d;
					}
					return Double.parseDouble(textValue);
				} else if(rawType == byte.class || rawType == Byte.class) {
					if(textValue.isEmpty()) {
						return (byte)0;
					}
					return Byte.parseByte(textValue);
				} else if(rawType == short.class || rawType == Short.class) {
					if(textValue.isEmpty()) {
						return (short)0;
					}
					return Short.parseShort(textValue);
				} else {
					return textValue;
				}
			} else if(control instanceof Button button) {
				/*
				 * Checkbox
				 */
				return button.getSelection();
			} else if(control instanceof Combo) {
				/*
				 * Combo
				 */
				ComboSupplier<?> comboSupplier = inputValue.getComboSupplier();
				if(comboSupplier != null) {
					return getValueAsString(comboSupplier);
				} else if(rawType.isEnum()) {
					return currentSelection.toString();
				}
			} else {
				/*
				 * Specific controls
				 */
				for(Object object : Activator.getDefault().getAnnotationWidgetServices()) {
					if(object instanceof IAnnotationWidgetService annotationWidgetService) {
						Class<?> supportedClass = annotationWidgetService.getSupportedClass();
						if(supportedClass.equals(rawType)) {
							return annotationWidgetService.getValue(currentSelection);
						}
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> String getValueAsString(ComboSupplier<T> comboSupplier) {

		try {
			return comboSupplier.asString((T)currentSelection);
		} catch(ClassCastException cce) {
			return null;
		}
	}

	private Control createControl(Composite parent) {

		Class<?> rawType = inputValue.getRawType();
		if(rawType != null) {
			if(inputValue.getComboSupplier() != null) {
				/*
				 * Combo Supplier
				 */
				ComboViewer viewer = createGenericCombo(parent, inputValue.getComboSupplier());
				return viewer.getControl();
			} else if(rawType == int.class || rawType == Integer.class) {
				return createTextWidgetNormal(parent);
			} else if(rawType == long.class || rawType == Long.class) {
				return createTextWidgetNormal(parent);
			} else if(rawType == float.class || rawType == Float.class) {
				return createTextWidgetNormal(parent);
			} else if(rawType == double.class || rawType == Double.class) {
				return createTextWidgetNormal(parent);
			} else if(rawType == short.class || rawType == Short.class) {
				return createTextWidgetNormal(parent);
			} else if(rawType == byte.class || rawType == Byte.class) {
				return createTextWidgetNormal(parent);
			} else if(rawType == String.class) {
				if(inputValue.isMultiLine()) {
					return createTextWidgetMultiLine(parent);
				} else {
					return createTextWidgetNormal(parent);
				}
			} else if(rawType == boolean.class || rawType == Boolean.class) {
				/*
				 * Checkbox
				 */
				return createCheckboxWidget(parent);
			} else if(rawType.isEnum()) {
				/*
				 * Combo
				 */
				if(rawType.getEnumConstants() instanceof Enum[] enums) {
					ComboViewer viewer = createLabeledEnumComboViewerWidget(parent, enums);
					return viewer.getControl();
				}
			} else if(rawType == File.class) {
				return createFileWidget(parent);
			} else {
				/*
				 * Specific controls
				 */
				Control control = getSpecificControl(parent);
				if(control != null) {
					return control;
				} else {
					Label label = new Label(parent, SWT.NONE);
					label.setText("The type is not supported: " + rawType);
					return label;
				}
			}
		}
		return null;
	}

	private Control getSpecificControl(Composite parent) {

		Class<?> rawType = inputValue.getRawType();
		for(Object object : Activator.getDefault().getAnnotationWidgetServices()) {
			if(object instanceof IAnnotationWidgetService annotationWidgetService) {
				Class<?> supportedClass = annotationWidgetService.getSupportedClass();
				if(supportedClass.equals(rawType)) {
					return annotationWidgetService.createWidget(parent, inputValue.getDescription(), currentSelection);
				}
			}
		}
		//
		return null;
	}

	private ComboViewer createGenericCombo(Composite parent, ComboSupplier<?> comboSupplier) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AdapterLabelProvider());
		combo.setToolTipText(inputValue.getDescription());
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		comboViewer.setInput(comboSupplier.items());
		//
		if(currentSelection instanceof String stringSelection) {
			Object currentValue = comboSupplier.fromString(stringSelection);
			if(currentValue != null) {
				comboViewer.setSelection(new StructuredSelection(currentValue));
			} else {
				comboViewer.setSelection(StructuredSelection.EMPTY);
			}
			currentSelection = currentValue;
		}
		//
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				currentSelection = comboViewer.getStructuredSelection().getFirstElement();
			}
		});
		//
		return comboViewer;
	}

	private Control createFileWidget(Composite parent) {

		FileSettingProperty fileSettingProperty = inputValue.getFileSettingProperty();
		//
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		//
		Text text = new Text(composite, SWT.BORDER);
		text.setToolTipText(inputValue.getDescription());
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		text.setText(AbstractProcessSettings.getCleanedFileValue(getValueAsString()));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				currentSelection = new File(text.getText());
			}
		});
		//
		Button button = new Button(composite, SWT.PUSH);
		button.setText(" ... ");
		button.setToolTipText(ExtensionMessages.chooseLocation);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean filechooser;
				int style;
				/*
				 * Determine whether to use a File or Directory Dialog
				 * in open or save modus.
				 */
				if(fileSettingProperty != null) {
					filechooser = !fileSettingProperty.onlyDirectory();
					if(fileSettingProperty.dialogType() == DialogType.OPEN_DIALOG) {
						style = SWT.OPEN;
					} else {
						style = SWT.SAVE;
					}
				} else {
					filechooser = true;
					style = SWT.OPEN;
				}
				/*
				 * 
				 */
				if(filechooser) {
					/*
					 * Navigate to the currently selected directory if available.
					 */
					FileDialog dialog = new FileDialog(button.getShell(), style);
					if(currentSelection != null) {
						File file = new File(currentSelection.toString());
						if(file.exists()) {
							String filterPath;
							if(file.isFile()) {
								filterPath = file.getParentFile().getAbsolutePath();
							} else {
								filterPath = file.getAbsolutePath();
							}
							dialog.setFilterPath(filterPath);
						}
					}
					/*
					 * Show Extensions
					 */
					if(fileSettingProperty != null) {
						String[] extensions = fileSettingProperty.validExtensions();
						String[] extensionNames = fileSettingProperty.extensionNames();
						if(extensions.length > 0 && extensionNames.length == extensions.length) {
							dialog.setFilterExtensions(extensions);
							dialog.setFilterNames(extensionNames);
						}
					}
					/*
					 * Open File Dialog
					 */
					String open = dialog.open();
					if(open != null) {
						text.setText(open);
						currentSelection = new File(open);
					}
				} else {
					/*
					 * Navigate to the currently selected directory if available.
					 */
					DirectoryDialog dialog = new DirectoryDialog(button.getShell(), style);
					if(currentSelection != null) {
						File file = new File(currentSelection.toString());
						if(file.exists()) {
							dialog.setFilterPath(file.getAbsolutePath());
						}
					}
					/*
					 * Open Directory Dialog
					 */
					String open = dialog.open();
					if(open != null) {
						text.setText(open);
						currentSelection = new File(open);
					}
				}
				/*
				 * Handle the events.
				 */
				Listener[] listeners = composite.getListeners(SWT.Selection);
				for(Listener listener : listeners) {
					listener.handleEvent(new Event());
				}
			}
		});
		//
		return composite;
	}

	private Control createTextWidgetNormal(Composite parent) {

		return createTextWidget(parent, SWT.BORDER, new GridData(GridData.FILL_HORIZONTAL));
	}

	private Control createTextWidgetMultiLine(Composite parent) {

		return createTextWidget(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI, new GridData(GridData.FILL_BOTH));
	}

	private Control createTextWidget(Composite parent, int style, GridData gridData) {

		Text text = new Text(parent, style);
		text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TRANSPARENT)); // workaround
		text.setData("org.eclipse.e4.ui.css.id", "MethodWidgetItem");
		text.setText(getValueAsString());
		text.setToolTipText(inputValue.getDescription());
		text.setLayoutData(gridData);
		return text;
	}

	private Control createCheckboxWidget(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText("");
		button.setSelection(getValueAsBoolean());
		button.setToolTipText(inputValue.getDescription());
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return button;
	}

	private ComboViewer createLabeledEnumComboViewerWidget(Composite parent, Enum<?>[] input) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ILabel label) {
					return label.label();
				}
				return element.toString();
			}
		});
		//
		combo.setToolTipText(inputValue.getDescription());
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		//
		comboViewer.setInput(input);
		Enum<?> initialSelection = Enum.valueOf(input[0].getDeclaringClass(), getValueAsString());
		comboViewer.setSelection(new StructuredSelection(initialSelection));
		//
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				currentSelection = comboViewer.getStructuredSelection().getFirstElement();
			}
		});
		//
		return comboViewer;
	}

	private boolean getValueAsBoolean() {

		if(currentSelection instanceof Boolean booleanSelection) {
			return booleanSelection.booleanValue();
		}
		//
		if(currentSelection instanceof String stringSelection) {
			return Boolean.valueOf(stringSelection);
		}
		//
		return false;
	}

	private String getValueAsString() {

		if(currentSelection == null) {
			return "";
		}
		//
		if(currentSelection instanceof String stringSelection) {
			return stringSelection;
		} else {
			return currentSelection.toString();
		}
	}
}