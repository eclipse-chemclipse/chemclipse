/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.swt.ui.support.MassSpectrumFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.addons.ModelSupportAddon;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuCategories;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.support.ScanSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.parts.ScanTablePart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.wsd.model.core.IPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ScanSignalWSD;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class ExtendedScanTableUI {

	private static final Logger logger = Logger.getLogger(ScanTablePart.class);
	//
	private Label labelInfo;
	private Composite toolbarInfo;
	private Composite toolbarEdit;
	private Button buttonSaveScan;
	private Button buttonOptimizedScan;
	private Label labelX;
	private Text textX;
	private Label labelY;
	private Text textY;
	//
	private ScanListUI scanListUI;
	//
	private Object object;
	private IScanMSD optimizedMassSpectrum;

	@Inject
	public ExtendedScanTableUI(Composite parent) {
		initialize(parent);
	}

	@Focus
	public void setFocus() {

		updateObject();
	}

	public void update(Object object) {

		this.object = object;
		updateObject();
	}

	private void updateObject() {

		IScan scan = null;
		if(object instanceof IScan) {
			scan = (IScan)object;
		} else if(object instanceof IPeak) {
			IPeak peak = (IPeak)object;
			scan = peak.getPeakModel().getPeakMaximum();
		}
		//
		labelInfo.setText(ScanSupport.getScanLabel(scan));
		scanListUI.setInput(scan);
		/*
		 * Fields
		 */
		List<TableViewerColumn> tableViewerColumns = scanListUI.getTableViewerColumns();
		if(tableViewerColumns.size() == 2) {
			//
			String titleX = tableViewerColumns.get(0).getColumn().getText();
			labelX.setText(titleX + ":");
			textX.setToolTipText(titleX);
			//
			String titleY = tableViewerColumns.get(1).getColumn().getText();
			labelY.setText(titleY + ":");
			textY.setToolTipText(titleY);
			//
			toolbarEdit.layout(true);
		}
		/*
		 * Optimized Scan
		 */
		optimizedMassSpectrum = null;
		buttonOptimizedScan.setEnabled(ScanSupport.containsOptimizedScan(scan));
		buttonSaveScan.setEnabled((object instanceof IScanMSD) ? true : false);
	}

	private void initialize(Composite parent) {

		parent.setLayout(new GridLayout(1, true));
		//
		createToolbarMain(parent);
		toolbarInfo = createToolbarInfo(parent);
		toolbarEdit = createToolbarEdit(parent);
		createTable(parent);
		//
		PartSupport.setCompositeVisibility(toolbarInfo, true);
		PartSupport.setCompositeVisibility(toolbarEdit, false);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridDataStatus = new GridData(GridData.FILL_HORIZONTAL);
		gridDataStatus.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridDataStatus);
		composite.setLayout(new GridLayout(6, false));
		//
		createButtonToggleToolbarInfo(composite);
		createButtonToggleToolbarEdit(composite);
		createResetButton(composite);
		buttonSaveScan = createSaveButton(composite);
		buttonOptimizedScan = createOptimizedScanButton(composite);
		createSettingsButton(composite);
	}

	private Button createButtonToggleToolbarInfo(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle info toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarInfo);
				if(visible) {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImage.SIZE_16x16));
				} else {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImage.SIZE_16x16));
				}
			}
		});
		//
		return button;
	}

	private Button createButtonToggleToolbarEdit(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle edit toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarEdit);
				if(visible) {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImage.SIZE_16x16));
				} else {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImage.SIZE_16x16));
				}
			}
		});
		//
		return button;
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the scan chart.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				reset();
			}
		});
	}

	private Button createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(object instanceof IScanMSD) {
						IScanMSD massSpectrum;
						if(optimizedMassSpectrum != null) {
							massSpectrum = optimizedMassSpectrum;
						} else {
							massSpectrum = (IScanMSD)object;
						}
						MassSpectrumFileSupport.saveMassSpectrum(massSpectrum);
					}
				} catch(NoConverterAvailableException e1) {
					logger.warn(e1);
				}
			}
		});
		return button;
	}

	private Button createOptimizedScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Show optimized scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PLUS, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(object instanceof IScanMSD) {
					IScanMSD scanMSD = (IScanMSD)object;
					optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
					if(optimizedMassSpectrum != null) {
						scanListUI.setInput(optimizedMassSpectrum);
						labelInfo.setText(ScanSupport.getScanLabel(optimizedMassSpectrum));
						button.setEnabled(false);
					}
				}
			}
		});
		return button;
	}

	private void createSettingsButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Open the Settings");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CONFIGURE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IPreferencePage preferencePage = new PreferencePageScans();
				preferencePage.setTitle("Scan Settings");
				PreferenceManager preferenceManager = new PreferenceManager();
				preferenceManager.addToRoot(new PreferenceNode("1", preferencePage));
				//
				PreferenceDialog preferenceDialog = new PreferenceDialog(Display.getDefault().getActiveShell(), preferenceManager);
				preferenceDialog.create();
				preferenceDialog.setMessage("Settings");
				if(preferenceDialog.open() == PreferenceDialog.OK) {
					try {
						applySettings();
					} catch(Exception e1) {
						MessageDialog.openError(Display.getDefault().getActiveShell(), "Settings", "Something has gone wrong to apply the chart settings.");
					}
				}
			}
		});
	}

	private Composite createToolbarInfo(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));
		//
		labelInfo = new Label(composite, SWT.NONE);
		labelInfo.setText("");
		labelInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return composite;
	}

	private Composite createToolbarEdit(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		labelX = createLabelX(composite);
		textX = createTextX(composite);
		labelY = createLabelY(composite);
		textY = createTextY(composite);
		createButtonAdd(composite);
		createButtonDelete(composite);
		//
		return composite;
	}

	private Label createLabelX(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("");
		return label;
	}

	private Text createTextX(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		return text;
	}

	private Label createLabelY(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("");
		return label;
	}

	private Text createTextY(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		return text;
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add the scan signal.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addSignal();
			}
		});
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the scan signal.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteSignals();
			}
		});
	}

	private void createTable(Composite parent) {

		scanListUI = new ScanListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		scanListUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		/*
		 * Add the delete targets support.
		 */
		ITableSettings tableSettings = scanListUI.getTableSettings();
		addDeleteMenuEntry(tableSettings);
		addKeyEventProcessors(tableSettings);
		scanListUI.applySettings(tableSettings);
	}

	private void addDeleteMenuEntry(ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Delete Signal(s)";
			}

			@Override
			public String getCategory() {

				return ITableMenuCategories.STANDARD_OPERATION;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				deleteSignals();
			}
		});
	}

	private void addKeyEventProcessors(ITableSettings tableSettings) {

		tableSettings.addKeyEventProcessor(new IKeyEventProcessor() {

			@Override
			public void handleEvent(ExtendedTableViewer extendedTableViewer, KeyEvent e) {

				if(e.keyCode == SWT.DEL) {
					/*
					 * DEL
					 */
					deleteSignals();
				}
			}
		});
	}

	private void applySettings() {

		updateObject();
	}

	private void reset() {

		updateObject();
	}

	@SuppressWarnings("rawtypes")
	private void deleteSignals() {

		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setText("Delete Signal(s)");
		messageBox.setMessage("Would you like to delete the selected signal(s)?");
		if(messageBox.open() == SWT.YES) {
			/*
			 * Delete the signal
			 */
			Iterator iterator = scanListUI.getStructuredSelection().iterator();
			while(iterator.hasNext()) {
				Object object = iterator.next();
				deleteSignal(object);
			}
			//
			fireScanUpdate();
			//
		}
	}

	private void deleteSignal(Object signal) {

		if(object instanceof IScanMSD) {
			IScanMSD scanMSD = (IScanMSD)object;
			if(signal instanceof IIon) {
				scanMSD.removeIon((IIon)signal);
			}
		} else if(object instanceof IScanCSD) {
			IScanCSD scanCSD = (IScanCSD)object;
			scanCSD.adjustTotalSignal(0.0f);
		} else if(object instanceof IScanWSD) {
			IScanWSD scanWSD = (IScanWSD)object;
			if(signal instanceof IScanSignalWSD) {
				scanWSD.removeScanSignal((IScanSignalWSD)signal);
			}
		} else if(object instanceof IPeakMSD) {
			IPeakMSD peakMSD = (IPeakMSD)object;
			if(signal instanceof IIon) {
				peakMSD.getExtractedMassSpectrum().removeIon((IIon)signal);
			}
		} else if(object instanceof IPeakCSD) {
			IPeakCSD peakCSD = (IPeakCSD)object;
			if(signal instanceof IScanCSD) {
				IScan scan = peakCSD.getPeakModel().getPeakMaximum();
				if(scan instanceof IScanCSD) {
					IScanCSD scanCSD = (IScanCSD)scan;
					scanCSD.adjustTotalSignal(0);
				}
			}
		} else if(object instanceof IPeakWSD) {
			IPeakWSD peakWSD = (IPeakWSD)object;
			if(signal instanceof IScanSignalWSD) {
				IScan scan = (IScan)peakWSD.getPeakModel().getPeakMaximum();
				if(scan instanceof IScanWSD) {
					IScanWSD scanWSD = (IScanWSD)scan;
					if(signal instanceof IScanSignalWSD) {
						scanWSD.removeScanSignal((IScanSignalWSD)signal);
					}
				}
			}
		}
	}

	private void addSignal() {

		String x = textX.getText().trim();
		String y = textY.getText().trim();
		//
		if("".equals(x) || "".equals(y)) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Add Signal", "The values must be not empty.");
		} else {
			try {
				/*
				 * Add the signal.
				 */
				double valueX = Double.parseDouble(x);
				float valueY = Float.parseFloat(y);
				//
				if(object instanceof IScanMSD) {
					IScanMSD scanMSD = (IScanMSD)object;
					scanMSD.addIon(new Ion(valueX, valueY));
				} else if(object instanceof IScanCSD) {
					IScanCSD scanCSD = (IScanCSD)object;
					scanCSD.adjustTotalSignal(valueY);
				} else if(object instanceof IScanWSD) {
					IScanWSD scanWSD = (IScanWSD)object;
					scanWSD.addScanSignal(new ScanSignalWSD(valueX, valueY));
				} else if(object instanceof IPeakMSD) {
					IPeakMSD peakMSD = (IPeakMSD)object;
					peakMSD.getExtractedMassSpectrum().addIon(new Ion(valueX, valueY));
				} else if(object instanceof IPeakCSD) {
					IPeakCSD peakCSD = (IPeakCSD)object;
					IScan scan = peakCSD.getPeakModel().getPeakMaximum();
					if(scan instanceof IScanCSD) {
						IScanCSD scanCSD = (IScanCSD)scan;
						scanCSD.adjustTotalSignal(valueY);
					}
				} else if(object instanceof IPeakWSD) {
					IPeakWSD peakWSD = (IPeakWSD)object;
					IScan scan = (IScan)peakWSD.getPeakModel().getPeakMaximum();
					if(scan instanceof IScanWSD) {
						IScanWSD scanWSD = (IScanWSD)scan;
						scanWSD.addScanSignal(new ScanSignalWSD(valueX, valueY));
					}
				}
				//
				textX.setText("");
				textY.setText("");
				fireScanUpdate();
				//
			} catch(Exception e) {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Add Signal", "Something has gone wrong to add the signal.");
			}
		}
	}

	private void fireScanUpdate() {

		/*
		 * Fire an update.
		 */
		IEventBroker eventBroker = ModelSupportAddon.getEventBroker();
		if(object instanceof IScan) {
			eventBroker.send(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION, object);
		} else if(object instanceof IPeak) {
			eventBroker.send(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION, object);
		}
	}
}
