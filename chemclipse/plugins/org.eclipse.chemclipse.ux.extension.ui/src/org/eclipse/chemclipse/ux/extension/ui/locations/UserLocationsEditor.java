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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.locations.UserLocation;
import org.eclipse.chemclipse.model.locations.UserLocations;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;

public class UserLocationsEditor extends Composite {

	private UserLocationsUI userLocationsUI;
	private UserLocationsListUI userLocationsListUI;
	//
	private UserLocations userLocations;
	private IUpdateListener updateListener = null;

	public UserLocationsEditor(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(UserLocations userLocations) {

		this.userLocations = userLocations;
		updateUserLocationsUI();
		updateUserLocationsTable();
		fireUpdate();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public UserLocation geUserLocation() {

		return userLocationsUI.getUserLocation();
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);
		//
		userLocationsUI = createUserLocationsUI(this);
		userLocationsListUI = createUserLocationsListUI(this);
	}

	private UserLocationsUI createUserLocationsUI(Composite parent) {

		UserLocationsUI userLocationsUI = new UserLocationsUI(parent, SWT.NONE);
		userLocationsUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		userLocationsUI.setUpdateListener(new IUpdateListener() {

			@Override
			public void update() {

				updateUserLocationsTable();
				fireUpdate();
			}
		});
		//
		return userLocationsUI;
	}

	private UserLocationsListUI createUserLocationsListUI(Composite parent) {

		UserLocationsListUI userLocationsListUI = new UserLocationsListUI(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		Table table = userLocationsListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		userLocationsListUI.setEditEnabled(false);
		userLocationsListUI.setSortEnabled(true);
		/*
		 * Selection
		 */
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = userLocationsListUI.getStructuredSelection().getFirstElement();
				if(object instanceof UserLocation userLocation) {
					String[] items = userLocationsUI.getItems();
					exitloop:
					for(int i = 0; i < items.length; i++) {
						if(items[i].equals(userLocation.getName())) {
							userLocationsUI.select(i);
							break exitloop;
						}
					}
				}
			}
		});
		/*
		 * Delete items
		 */
		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(userLocations != null) {
					if(e.keyCode == SWT.DEL) {
						MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						messageBox.setText("Delete user location");
						messageBox.setMessage("Would you like to delete the user location?");
						if(messageBox.open() == SWT.YES) {
							/*
							 * Collect
							 */
							List<UserLocation> deleteItems = new ArrayList<>();
							for(Object object : userLocationsListUI.getStructuredSelection().toList()) {
								if(object instanceof UserLocation userLocation) {
									deleteItems.add(userLocation);
								}
							}
							/*
							 * Delete
							 */
							delete(deleteItems);
							/*
							 * Update
							 */
							updateUserLocationsUI();
							updateUserLocationsTable();
							fireUpdate();
						}
					}
				}
			}
		});
		//
		return userLocationsListUI;
	}

	private void delete(List<UserLocation> deleteItems) {

		if(userLocations != null) {
			for(UserLocation deleteItem : deleteItems) {
				userLocations.remove(deleteItem.getName());
			}
		}
	}

	private void updateUserLocationsUI() {

		userLocationsUI.setInput(userLocations);
	}

	private void updateUserLocationsTable() {

		if(userLocations != null) {
			List<UserLocation> list = new ArrayList<>(userLocations.values());
			Collections.sort(list, (t1, t2) -> t1.getName().compareTo(t2.getName()));
			userLocationsListUI.setInput(list);
		} else {
			userLocationsListUI.setInput(null);
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