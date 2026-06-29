/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.IWaveSpectrumIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.wavespectrum.IWaveSpectrumIdentifierSupplier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.wavespectrum.IWaveSpectrumIdentifierSupport;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.wavespectrum.WaveSpectrumIdentifier;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.identifier.IMassSpectrumIdentifierSupplier;
import org.eclipse.chemclipse.msd.identifier.IMassSpectrumIdentifierSupport;
import org.eclipse.chemclipse.msd.identifier.MassSpectrumIdentifier;
import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.updates.IUpdateListenerUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables.MassSpectrumIdentifierRunnable;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables.WaveSpectrumIdentifierRunnable;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.ProcessorSettingsSupport;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ScanIdentifierUI extends Composite {

	private static final Logger logger = Logger.getLogger(ScanIdentifierUI.class);
	private static final String KEY_MENU_DATA = "keyMenuData";

	private AtomicReference<Button> buttonExecute = new AtomicReference<>();
	private AtomicReference<Menu> menuMSD = new AtomicReference<>();
	private AtomicReference<Menu> menuWSD = new AtomicReference<>();

	private List<IScan> scans = new ArrayList<>();
	private IUpdateListenerUI updateListener = null;

	private List<IMassSpectrumIdentifierSupplier> identifierSuppliersMSD = getIdentifierSuppliersMSD();
	private List<IWaveSpectrumIdentifierSupplier> identifierSuppliersWSD = getIdentifierSuppliersWSD();
	private IMassSpectrumIdentifierSupplier massSpectrumIdentifierSupplier;
	private IWaveSpectrumIdentifierSupplier waveSpectrumIdentifierSupplier;

	public ScanIdentifierUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public static String[][] getScanIdentifierMSD() {

		List<IMassSpectrumIdentifierSupplier> identifierSuppliersMSD = getIdentifierSuppliersMSD();
		String[][] scanIdentifierMSD = new String[identifierSuppliersMSD.size()][2];
		for(int i = 0; i < identifierSuppliersMSD.size(); i++) {
			IMassSpectrumIdentifierSupplier identifierSupplier = identifierSuppliersMSD.get(i);
			scanIdentifierMSD[i] = new String[]{identifierSupplier.getIdentifierName(), identifierSupplier.getId()};
		}

		return scanIdentifierMSD;
	}

	public static String[][] getScanIdentifierWSD() {

		List<IWaveSpectrumIdentifierSupplier> identifierSuppliersWSD = getIdentifierSuppliersWSD();
		String[][] scanIdentifierWSD = new String[identifierSuppliersWSD.size()][2];
		for(int i = 0; i < identifierSuppliersWSD.size(); i++) {
			IWaveSpectrumIdentifierSupplier identifierSupplier = identifierSuppliersWSD.get(i);
			scanIdentifierWSD[i] = new String[]{identifierSupplier.getIdentifierName(), identifierSupplier.getId()};
		}

		return scanIdentifierWSD;
	}

	@Override
	public void setEnabled(boolean enabled) {

		super.setEnabled(enabled);
		buttonExecute.get().setEnabled(enabled);
	}

	public void setInput(IScan scan) {

		setInput(Arrays.asList(scan));
	}

	public void setInput(List<IScan> scans) {

		this.scans.clear();
		this.scans.addAll(scans);
		updateMenu();
	}

	public void setUpdateListener(IUpdateListenerUI updateListener) {

		this.updateListener = updateListener;
	}

	public void updateIdentifier() {

		updateMenu();
	}

	public void runIdentification() {

		Menu menu = buttonExecute.get().getMenu();
		if(menu != null) {

			DataType dataType = (DataType)menu.getData(KEY_MENU_DATA);
			List<IScanMSD> scansMSD = new ArrayList<>();
			List<IScanWSD> scansWSD = new ArrayList<>();

			for(IScan scan : scans) {
				if(scan instanceof IScanMSD scanMSD && DataType.MSD.equals(dataType)) {
					IScanMSD optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
					IScanMSD massSpectrum = (optimizedMassSpectrum != null) ? optimizedMassSpectrum : scanMSD;
					scansMSD.add(massSpectrum);
				} else if(scan instanceof IScanWSD scanWSD && DataType.WSD.equals(dataType)) {
					scansWSD.add(scanWSD);
				}
			}

			runIdentification(getDisplay(), scansMSD, scansWSD, true);
		}
	}

	@Override
	public void dispose() {

		if(menuMSD.get() != null) {
			menuMSD.get().dispose();
		}

		if(menuWSD.get() != null) {
			menuWSD.get().dispose();
		}
	}

	private void createControl() {

		setLayout(new FillLayout());

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout(gridLayout);

		createButtonExecute(composite);

		initialize();
	}

	private void initialize() {

		setEnabled(false);
	}

	private void createButtonExecute(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IDENTIFY_MASS_SPECTRUM, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				runIdentification();
			}
		});

		buttonExecute.set(button);
	}

	private void updateMenu() {

		setEnabled(false);

		if(!scans.isEmpty()) {
			/*
			 * Get the first scan to determine whether to use MSD or WSD.
			 */
			IScan scan = scans.get(0);
			if(scan instanceof IScanMSD) {
				/*
				 * MSD
				 */
				activateDefaultIdentifierMSD(identifierSuppliersMSD);
				if(menuMSD.get() == null) {
					createMenuIdentifierMSD(buttonExecute.get(), identifierSuppliersMSD);
				}
				buttonExecute.get().setMenu(menuMSD.get());
				setEnabled(true);
			} else if(scan instanceof IScanWSD) {
				/*
				 * WSD
				 */
				activateDefaultIdentifierWSD(identifierSuppliersWSD);
				if(menuWSD.get() == null) {
					createMenuIdentifierWSD(buttonExecute.get(), identifierSuppliersWSD);
				}
				buttonExecute.get().setMenu(menuWSD.get());
				setEnabled(true);
			}
		}
	}

	private void activateDefaultIdentifierMSD(List<IMassSpectrumIdentifierSupplier> identifierSuppliers) {

		/*
		 * Try to set the selected identifier.
		 */
		String identifier = PreferenceSupplier.getScanIdentifierMSD();
		if(!identifier.isEmpty()) {
			exitloop:
			for(IMassSpectrumIdentifierSupplier identifierSupplier : identifierSuppliers) {
				if(identifier.equals(identifierSupplier.getId())) {
					massSpectrumIdentifierSupplier = identifierSupplier;
					break exitloop;
				}
			}
		}
		/*
		 * Get a default identifier.
		 */
		if(massSpectrumIdentifierSupplier == null && !identifierSuppliers.isEmpty()) {
			massSpectrumIdentifierSupplier = identifierSuppliers.get(0);
		}
		/*
		 * Set the tooltip.
		 */
		if(massSpectrumIdentifierSupplier != null) {
			buttonExecute.get().setToolTipText(massSpectrumIdentifierSupplier.getIdentifierName());
		}
	}

	private void activateDefaultIdentifierWSD(List<IWaveSpectrumIdentifierSupplier> identifierSuppliers) {

		/*
		 * Try to set the selected identifier.
		 */
		String identifier = PreferenceSupplier.getScanIdentifierWSD();
		if(!identifier.isEmpty()) {
			exitloop:
			for(IWaveSpectrumIdentifierSupplier identifierSupplier : identifierSuppliers) {
				if(identifier.equals(identifierSupplier.getId())) {
					waveSpectrumIdentifierSupplier = identifierSupplier;
					break exitloop;
				}
			}
		}
		/*
		 * Get a default identifier.
		 */
		if(waveSpectrumIdentifierSupplier == null) {
			if(!identifierSuppliers.isEmpty()) {
				waveSpectrumIdentifierSupplier = identifierSuppliers.get(0);
			}
		}
		/*
		 * Set the tooltip.
		 */
		if(waveSpectrumIdentifierSupplier != null) {
			buttonExecute.get().setToolTipText(waveSpectrumIdentifierSupplier.getIdentifierName());
		}
	}

	private void createMenuIdentifierMSD(Button button, List<IMassSpectrumIdentifierSupplier> identifierSuppliers) {

		Menu menu = new Menu(button);
		menu.setData(KEY_MENU_DATA, DataType.MSD);

		for(IMassSpectrumIdentifierSupplier identifierSupplier : identifierSuppliers) {
			/*
			 * Identifier Handler
			 */
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText(identifierSupplier.getIdentifierName());
			menuItem.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					button.setToolTipText(identifierSupplier.getIdentifierName());
					PreferenceSupplier.setScanIdentifierMSD(identifierSupplier.getId());
					massSpectrumIdentifierSupplier = identifierSupplier;
					runIdentification();
				}
			});
		}

		menuMSD.set(menu);
	}

	private void createMenuIdentifierWSD(Button button, List<IWaveSpectrumIdentifierSupplier> identifierSuppliers) {

		Menu menu = new Menu(button);
		menu.setData(KEY_MENU_DATA, DataType.WSD);

		for(IWaveSpectrumIdentifierSupplier identifierSupplier : identifierSuppliers) {
			/*
			 * Identifier Handler
			 */
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText(identifierSupplier.getIdentifierName());
			menuItem.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					button.setToolTipText(identifierSupplier.getIdentifierName());
					PreferenceSupplier.setScanIdentifierWSD(identifierSupplier.getId());
					waveSpectrumIdentifierSupplier = identifierSupplier;
					runIdentification();
				}
			});
		}

		menuWSD.set(menu);
	}

	private static List<IMassSpectrumIdentifierSupplier> getIdentifierSuppliersMSD() {

		IMassSpectrumIdentifierSupport massSpectrumIdentifierSupport = MassSpectrumIdentifier.getMassSpectrumIdentifierSupport();
		List<IMassSpectrumIdentifierSupplier> identifierSuppliers = new ArrayList<>(massSpectrumIdentifierSupport.getSuppliers());
		Collections.sort(identifierSuppliers, (s1, s2) -> s1.getIdentifierName().compareTo(s2.getIdentifierName()));

		return identifierSuppliers;
	}

	private static List<IWaveSpectrumIdentifierSupplier> getIdentifierSuppliersWSD() {

		IWaveSpectrumIdentifierSupport waveSpectrumIdentifierSupport = WaveSpectrumIdentifier.getWaveSpectrumIdentifierSupport();
		List<IWaveSpectrumIdentifierSupplier> identifierSuppliers = new ArrayList<>(waveSpectrumIdentifierSupport.getSuppliers());
		Collections.sort(identifierSuppliers, (s1, s2) -> s1.getIdentifierName().compareTo(s2.getIdentifierName()));

		return identifierSuppliers;
	}

	/**
	 * Identify the scan by calling the selected identifier.
	 * 
	 * @param display
	 * @param scanMSD
	 */
	private void runIdentification(Display display, List<IScanMSD> massSpectra, List<IScanWSD> waveSpectra, boolean update) {

		runIdentificationMSD(display, massSpectra, update);
		runIdentificationWSD(display, waveSpectra, update);
	}

	private void runIdentificationMSD(Display display, List<IScanMSD> massSpectra, boolean update) {

		if(!massSpectra.isEmpty() && massSpectrumIdentifierSupplier != null) {
			/*
			 * Run the identification using the dynamic settings.
			 */
			try {
				IMassSpectrumIdentifierSettings identifierSettings = ProcessorSettingsSupport.getSettings(getShell(), massSpectrumIdentifierSupplier.getId());
				if(identifierSettings != null) {
					IRunnableWithProgress runnable = new MassSpectrumIdentifierRunnable(massSpectra, massSpectrumIdentifierSupplier.getId(), identifierSettings);
					ProgressMonitorDialog monitor = new ProgressMonitorDialog(display.getActiveShell());
					monitor.run(false, true, runnable);
					if(update) {
						fireUpdate(display);
					}
				}
			} catch(InvocationTargetException e) {
				logger.warn(e);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				logger.warn(e);
			}
		}
	}

	private void runIdentificationWSD(Display display, List<IScanWSD> waveSpectra, boolean update) {

		if(!waveSpectra.isEmpty() && waveSpectrumIdentifierSupplier != null) {
			/*
			 * Run the identification using the dynamic settings.
			 */
			try {
				IWaveSpectrumIdentifierSettings identifierSettings = ProcessorSettingsSupport.getSettings(getShell(), waveSpectrumIdentifierSupplier.getId());
				IRunnableWithProgress runnable = new WaveSpectrumIdentifierRunnable(waveSpectra, waveSpectrumIdentifierSupplier.getId(), identifierSettings);
				ProgressMonitorDialog monitor = new ProgressMonitorDialog(display.getActiveShell());
				monitor.run(true, true, runnable);
				if(update) {
					fireUpdate(display);
				}
			} catch(InvocationTargetException e) {
				logger.warn(e);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				logger.warn(e);
			}
		}
	}

	private void fireUpdate(Display display) {

		if(updateListener != null) {
			updateListener.update(display);
		}
	}
}