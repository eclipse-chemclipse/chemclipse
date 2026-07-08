/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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

import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.getCalculationType;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.getSessionSubtractMassSpectrum;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.isUseNormalizedScan;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.isUsePeaksInsteadOfScans;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.loadSessionSubtractMassSpectrum;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.setSessionSubtractMassSpectrum;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.storeSessionSubtractMassSpectrum;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.model.targets.TargetSupport;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.support.FilterSupport;
import org.eclipse.chemclipse.msd.model.support.MassSpectrumIO;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageSubtract;
import org.eclipse.chemclipse.ux.extension.xxd.ui.runnables.LibraryServiceRunnable;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import jakarta.inject.Inject;

public class ExtendedSubtractScanUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedSubtractScanUI.class);

	private AtomicReference<TabFolder> tabFolderControl = new AtomicReference<>();
	private AtomicReference<ScanChartUI> scanChartControl = new AtomicReference<>();
	private AtomicReference<ExtendedScanTableUI> extendedScanTableControl = new AtomicReference<>();
	private AtomicReference<Button> buttonSelectedScanControl = new AtomicReference<>();
	private AtomicReference<Button> buttonCombinedScanControl = new AtomicReference<>();
	private AtomicReference<Button> buttonComparisonScanControl = new AtomicReference<>();

	private IScanMSD scanMSD = null;
	private IPeakMSD peakMSD = null;
	private IChromatogramSelectionMSD chromatogramSelectionMSD = null;

	@Inject
	public ExtendedSubtractScanUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	@Focus
	public boolean setFocus() {

		boolean focus = super.setFocus();
		updateScanData(scanMSD);

		return focus;
	}

	public void update(Object object) {

		if(object instanceof IChromatogramSelectionMSD chromatogramSelectionMSD) {
			this.chromatogramSelectionMSD = chromatogramSelectionMSD;
		} else if(object instanceof IScanMSD scanMSD) {
			this.scanMSD = scanMSD;
		} else if(object instanceof IPeakMSD peakMSD) {
			this.peakMSD = peakMSD;
		} else if(object == null) {
			chromatogramSelectionMSD = null;
		}

		updateScanData(scanMSD);
		updateWidgets();
	}

	private void createControl() {

		setLayout(new FillLayout());

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		createToolbarMain(composite);
		createScanTabFolderSection(composite);

		loadSessionMassSpectrum(composite.getDisplay());
		updateWidgets();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));

		createAddSelectedScanButton(composite);
		createAddCombinedScanButton(composite);
		createAddComparisonScanButton(composite);
		createClearSessionButton(composite);
		createButtonCopyTracesClipboard(composite);
		createSaveButton(composite);
		createSettingsButton(composite);
	}

	private void createScanTabFolderSection(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(1, true));

		TabFolder tabFolder = new TabFolder(composite, SWT.BOTTOM);
		tabFolder.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateScanData(scanMSD);
			}
		});

		tabFolderControl.set(tabFolder);
		createScanChart(tabFolder);
		createScanTable(tabFolder);
	}

	private void createScanChart(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Chart");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		tabItem.setControl(composite);

		ScanChartUI scanChartUI = new ScanChartUI(composite, SWT.BORDER);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		scanChartControl.set(scanChartUI);
	}

	private void createScanTable(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Table");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite.setLayout(new GridLayout(1, true));
		tabItem.setControl(composite);

		ExtendedScanTableUI extendedScanTableUI = new ExtendedScanTableUI(composite, SWT.NONE);
		extendedScanTableUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		ScanTableUI scanTableUI = extendedScanTableUI.getScanTableUI();
		scanTableUI.getTable().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(e.keyCode == SWT.DEL) {
					if(scanMSD != null) {
						for(Object object : scanTableUI.getStructuredSelection().toArray()) {
							if(object instanceof IIon ion) {
								scanMSD.removeIon(ion);
							}
						}
						saveSessionMassSpectrum(null, scanMSD);
						updateScanData(scanMSD);
					}
				}
			}

		});

		extendedScanTableControl.set(extendedScanTableUI);
	}

	private void createAddSelectedScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add selected scan to subtract spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_ADD_SELECTED_SCAN, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogramSelectionMSD != null && chromatogramSelectionMSD.getSelectedScan() != null) {
					/*
					 * Add the selected scan to the session MS.
					 */
					IScanMSD massSpectrum1 = getSessionSubtractMassSpectrum();
					CalculationType calculationType = getCalculationType();
					IScanMSD massSpectrum2 = chromatogramSelectionMSD.getSelectedScan();
					boolean useNormalize = isUseNormalizedScan();
					IScanMSD subtractMassSpectrum = FilterSupport.getCombinedMassSpectrum(massSpectrum1, massSpectrum2, null, useNormalize, calculationType);
					saveSessionMassSpectrum(e.display, subtractMassSpectrum);
				}
			}
		});

		buttonSelectedScanControl.set(button);
	}

	private void createAddCombinedScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add combined scan to subtract spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_ADD_COMBINED_SCAN, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogramSelectionMSD != null) {
					boolean useNormalize = isUseNormalizedScan();
					CalculationType calculationType = getCalculationType();
					boolean usePeaksInsteadOfScans = isUsePeaksInsteadOfScans();
					IScanMSD massSpectrum1 = getSessionSubtractMassSpectrum();
					IScanMSD massSpectrum2 = FilterSupport.getCombinedMassSpectrum(chromatogramSelectionMSD, null, useNormalize, calculationType, usePeaksInsteadOfScans);
					IScanMSD subtractMassSpectrum = FilterSupport.getCombinedMassSpectrum(massSpectrum1, massSpectrum2, null, useNormalize, calculationType);
					saveSessionMassSpectrum(e.display, subtractMassSpectrum);
				}
			}
		});

		buttonCombinedScanControl.set(button);
	}

	private void createAddComparisonScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add comparison scan to subtract spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COMPARISON_SCAN, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(isValidComparisonScanPeak()) {
					/*
					 * Retrieve the comparison mass spectrum of the best target.
					 */
					IIdentificationTarget identificationTarget = TargetSupport.getBestIdentificationTarget(peakMSD);
					LibraryServiceRunnable runnable = new LibraryServiceRunnable(identificationTarget, referenceMassSpectrum -> saveSessionMassSpectrum(e.display, referenceMassSpectrum));
					try {
						if(runnable.requireProgressMonitor()) {
							DisplayUtils.executeInUserInterfaceThread(() -> {
								ProgressMonitorDialog monitor = new ProgressMonitorDialog(getShell());
								monitor.run(true, true, runnable);
								return null;
							});
						} else {
							DisplayUtils.executeBusy(() -> {
								runnable.run(new NullProgressMonitor());
								return null;
							});
						}
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					} catch(ExecutionException ex) {
						ILog.get().error("Fetch comparison scan failed.", ex);
					}
				}
			}
		});

		buttonComparisonScanControl.set(button);
	}

	private void createClearSessionButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Clear the session spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_CLEAR_SESSION_MASS_SPECTRUM, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText("Clear Session");
				messageBox.setMessage("Would you like to clear the session subtract scan?");
				if(messageBox.open() == SWT.YES) {
					scanMSD = null;
					updateScanData(scanMSD);
					saveSessionMassSpectrum(e.display, null);
				}
			}
		});
	}

	private Button createButtonCopyTracesClipboard(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Copy the traces to clipboard.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COPY_CLIPBOARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String traces = MassSpectrumIO.getMassSpectrum(scanMSD);
				TextTransfer textTransfer = TextTransfer.getInstance();
				Object[] data = new Object[]{traces};
				Transfer[] dataTypes = new Transfer[]{textTransfer};
				Clipboard clipboard = new Clipboard(e.widget.getDisplay());
				clipboard.setContents(data, dataTypes);
				clipboard.dispose();
			}
		});

		return button;
	}

	private Button createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the subtract scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(scanMSD != null) {
						DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), scanMSD, "SubtractMS");
					}
				} catch(NoConverterAvailableException e1) {
					logger.warn(e1);
				}
			}
		});
		return button;
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePageScans.class, PreferencePageSubtract.class), display -> applySettings());
	}

	private void applySettings() {

		updateScanData(scanMSD);
	}

	private void updateScanData(IScanMSD scanMSD) {

		/*
		 * Chart
		 */
		ScanChartUI scanChartUI = scanChartControl.get();
		if(scanMSD == null) {
			scanChartUI.deleteSeries();
			scanChartUI.getBaseChart().redraw();
		} else {
			scanChartUI.setInput(scanMSD);
		}
		/*
		 * Table
		 */
		ExtendedScanTableUI extendedScanTableUI = extendedScanTableControl.get();
		if(extendedScanTableUI.isVisible()) {
			extendedScanTableUI.setInput(scanMSD);
		}

		updateWidgets();
	}

	private void loadSessionMassSpectrum(Display display) {

		loadSessionSubtractMassSpectrum();
		fireUpdateEvent(display);
	}

	/**
	 * If the display is set to null, no event is fired.
	 * 
	 * @param display
	 * @param scanMSD
	 */
	private void saveSessionMassSpectrum(Display display, IScanMSD scanMSD) {

		setSessionSubtractMassSpectrum(scanMSD);
		storeSessionSubtractMassSpectrum();

		if(display != null) {
			fireUpdateEvent(display);
		}
	}

	private void updateWidgets() {

		boolean enabled = chromatogramSelectionMSD != null;
		buttonSelectedScanControl.get().setEnabled(enabled);
		buttonCombinedScanControl.get().setEnabled(enabled);
		buttonComparisonScanControl.get().setEnabled(isValidComparisonScanPeak());
	}

	private boolean isValidComparisonScanPeak() {

		return peakMSD != null && !peakMSD.getTargets().isEmpty();
	}

	private void fireUpdateEvent(Display display) {

		UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_UPDATE_SESSION_SUBTRACT_MASS_SPECTRUM, true);
	}
}