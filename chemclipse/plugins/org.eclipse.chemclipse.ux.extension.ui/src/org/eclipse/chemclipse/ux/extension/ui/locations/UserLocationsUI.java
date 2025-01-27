/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.locations;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.locations.UserLocation;
import org.eclipse.chemclipse.model.locations.UserLocations;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.support.validators.PathValidator;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferenceSupplier;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

public class UserLocationsUI extends Composite {

	private static final String TOOLTIP_TEXT = "Enter/modify the user locations.";
	//
	public static final String IMPORT_TITLE = "Import " + UserLocations.DESCRIPTION;
	public static final String EXPORT_TITLE = "Export " + UserLocations.DESCRIPTION;
	public static final String MESSAGE_IMPORT_SUCCESSFUL = "User locations have been imported successfully.";
	public static final String MESSAGE_EXPORT_SUCCESSFUL = "User locations have been exported successfully.";
	public static final String MESSAGE_EXPORT_FAILED = "Failed to export the user locations.";
	//
	private ComboViewer comboViewer;
	private Text textPath;
	private Button buttonAdd;
	private Button buttonDelete;
	private Button buttonImport;
	private Button buttonExport;
	//
	private UserLocations userLocations = null;
	private UserLocation userLocation = null;
	//
	private IUpdateListener updateListener = null;

	public UserLocationsUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(UserLocations userLocations) {

		this.userLocations = userLocations;
		updateInput(null);
	}

	@Override
	public void update() {

		super.update();
		updateUserLocation();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public String[] getItems() {

		return comboViewer.getCombo().getItems();
	}

	public void select(int index) {

		if(index >= 0 && index < getItems().length) {
			comboViewer.getCombo().select(index);
			Object object = comboViewer.getStructuredSelection().getFirstElement();
			if(object instanceof UserLocation selectedUserLocation) {
				userLocation = selectedUserLocation;
				updateUserLocation();
			}
		}
	}

	public UserLocations getUserLocations() {

		return userLocations;
	}

	public UserLocation getUserLocation() {

		return userLocation;
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);
		//
		comboViewer = createComboViewer(this);
		textPath = createText(this);
		buttonAdd = createButtonAdd(this);
		buttonDelete = createButtonDelete(this);
		buttonImport = createButtonImport(this);
		buttonExport = createButtonExport(this);
	}

	private ComboViewer createComboViewer(Composite composite) {

		ComboViewer comboViewer = new EnhancedComboViewer(composite, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof UserLocation userLocation) {
					return userLocation.getName();
				}
				return null;
			}
		});
		/*
		 * Select the item.
		 */
		combo.setToolTipText("Select a user location.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof UserLocation selectedUserLocation) {
					userLocation = selectedUserLocation;
					updateUserLocation();
					fireUpdate();
				}
			}
		});
		//
		return comboViewer;
	}

	private Text createText(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setToolTipText(TOOLTIP_TEXT);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		PathValidator pathValidator = new PathValidator(true);
		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		//
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {

				if(userLocation != null) {
					if(validate(pathValidator, controlDecoration, text)) {
						userLocation.setPath(pathValidator.getPath());
						fireUpdate();
					}
				}
			}
		});
		//
		return text;
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add a new user location.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(userLocations != null) {
					InputDialog dialog = new InputDialog(e.display.getActiveShell(), "User Location", "Create a new user location.", "Project | path", new UserLocationInputValidator(userLocations.keySet()));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						UserLocation userLocationNew = userLocations.extractUserLocation(item);
						if(userLocationNew != null) {
							userLocations.add(userLocationNew);
							userLocation = userLocationNew;
							updateInput(userLocation.getName());
							fireUpdate();
						}
					}
				}
			}
		});
		return button;
	}

	private Button createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the selected user location.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), "User Location", "Would you like to delete the selected user location?")) {
					Object object = comboViewer.getStructuredSelection().getFirstElement();
					if(object instanceof UserLocation selectedUserLocation) {
						userLocation = null;
						userLocations.remove(selectedUserLocation);
						updateInput(null);
						fireUpdate();
					}
				}
			}
		});
		return button;
	}

	private Button createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Import user locations.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(userLocations != null) {
					FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.READ_ONLY);
					fileDialog.setText(IMPORT_TITLE);
					fileDialog.setFilterExtensions(new String[]{UserLocations.FILTER_EXTENSION});
					fileDialog.setFilterNames(new String[]{UserLocations.FILTER_NAME});
					fileDialog.setFilterPath(PreferenceSupplier.getUserLocationsTemplateFolder());
					String path = fileDialog.open();
					if(path != null) {
						PreferenceSupplier.setUserLocationsTemplateFolder(fileDialog.getFilterPath());
						File file = new File(path);
						userLocations.importItems(file);
						MessageDialog.openInformation(e.display.getActiveShell(), IMPORT_TITLE, MESSAGE_IMPORT_SUCCESSFUL);
						updateInput(null);
						fireUpdate();
					}
				}
			}
		});
		return button;
	}

	private Button createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Export user locations.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(userLocations != null) {
					FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
					fileDialog.setOverwrite(true);
					fileDialog.setText(EXPORT_TITLE);
					fileDialog.setFilterExtensions(new String[]{UserLocations.FILTER_EXTENSION});
					fileDialog.setFilterNames(new String[]{UserLocations.FILTER_NAME});
					fileDialog.setFileName(UserLocations.FILE_NAME);
					fileDialog.setFilterPath(PreferenceSupplier.getUserLocationsTemplateFolder());
					String path = fileDialog.open();
					if(path != null) {
						PreferenceSupplier.setUserLocationsTemplateFolder(fileDialog.getFilterPath());
						File file = new File(path);
						if(userLocations.exportItems(file)) {
							MessageDialog.openInformation(e.display.getActiveShell(), EXPORT_TITLE, MESSAGE_EXPORT_SUCCESSFUL);
						} else {
							MessageDialog.openWarning(e.display.getActiveShell(), EXPORT_TITLE, MESSAGE_EXPORT_FAILED);
						}
					}
				}
			}
		});
		return button;
	}

	private void updateInput(String identifier) {

		userLocation = null;
		if(userLocations != null) {
			/*
			 * Sort the user locations.
			 */
			List<UserLocation> userLocationsSorted = new ArrayList<>(userLocations.values());
			Collections.sort(userLocationsSorted, (l1, l2) -> l1.getName().compareTo(l2.getName()));
			/*
			 * Set the selection
			 */
			UserLocation userLocation = null;
			if(comboViewer.getStructuredSelection().getFirstElement() instanceof UserLocation ul) {
				userLocation = ul;
			}
			//
			comboViewer.setInput(userLocationsSorted);
			if(userLocation != null && userLocationsSorted.contains(userLocation)) {
				comboViewer.setSelection(new StructuredSelection(userLocation));
			}
			//
			buttonAdd.setEnabled(true);
			buttonDelete.setEnabled(!userLocationsSorted.isEmpty());
			buttonImport.setEnabled(true);
			buttonExport.setEnabled(true);
		} else {
			/*
			 * Settings
			 */
			buttonAdd.setEnabled(false);
			buttonDelete.setEnabled(false);
			buttonImport.setEnabled(false);
			buttonExport.setEnabled(false);
			comboViewer.setInput(null);
		}
		//
		updateUserLocation();
	}

	private void updateUserLocation() {

		textPath.setText(userLocation != null ? userLocation.getPath() : "");
		buttonDelete.setEnabled(userLocation != null);
	}

	private boolean validate(IValidator<Object> validator, ControlDecoration controlDecoration, Text text) {

		IStatus status = validator.validate(text.getText().trim());
		if(status.isOK()) {
			controlDecoration.hide();
			text.setToolTipText(TOOLTIP_TEXT);
			return true;
		} else {
			controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
			controlDecoration.showHoverText(status.getMessage());
			controlDecoration.show();
			text.setToolTipText(status.getMessage());
			return false;
		}
	}

	private void fireUpdate() {

		if(updateListener != null) {
			getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {

					updateListener.update();
				}
			});
		}
	}
}