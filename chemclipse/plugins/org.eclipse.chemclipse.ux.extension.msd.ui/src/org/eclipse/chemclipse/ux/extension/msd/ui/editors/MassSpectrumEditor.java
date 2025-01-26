/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - update upon events
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.editors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.msd.converter.database.DatabaseConverter;
import org.eclipse.chemclipse.msd.converter.exceptions.NoMassSpectrumConverterAvailableException;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.support.ui.workbench.EditorSupport;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.support.MassSpectrumImportRunnable;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.IMassSpectrumChart;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.MassSpectraSelectionUI;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.MassSpectrumChartCentroid;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.MassSpectrumChartProfile;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class MassSpectrumEditor implements IMassSpectrumEditor {

	public static final String ID = "org.eclipse.chemclipse.ux.extension.msd.ui.part.massSpectrumEditor";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.msd.ui/org.eclipse.chemclipse.ux.extension.msd.ui.editors.MassSpectrumEditor";
	public static final String ICON_URI = ApplicationImageFactory.getInstance().getURI(IApplicationImage.IMAGE_MASS_SPECTRUM_FILE, IApplicationImageProvider.SIZE_16x16);
	public static final String TOOLTIP = "Mass Spectrum - Detector Type: MSD";
	//
	private static final Logger logger = Logger.getLogger(MassSpectrumEditor.class);
	/*
	 * Injected member in constructor
	 */
	@Inject
	private MPart part;
	@Inject
	private MDirtyable dirtyable;
	@Inject
	private MApplication application;
	@Inject
	private EModelService modelService;
	@Inject
	private IEventBroker eventBroker;
	/*
	 * Mass spectrum selection and the GUI element.
	 */
	private File massSpectrumFile;
	private IMassSpectra massSpectra;
	private IScanMSD massSpectrum;
	private ArrayList<EventHandler> registeredEventHandler;
	private IMassSpectrumChart massSpectrumChart;
	private List<Object> objects = new ArrayList<>();
	private AtomicReference<Composite> toolbarMainControl = new AtomicReference<>();
	private AtomicReference<MassSpectraSelectionUI> toolbarSelectionControl = new AtomicReference<>();
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	@PostConstruct
	private void createControl(Composite parent) {

		loadMassSpectra();
		createPages(parent);
		registeredEventHandler = new ArrayList<>();
		registerEvents();
	}

	@Focus
	public void setFocus() {

		/*
		 * Fire an update if a loaded mass spectrum has been selected.
		 */
		if(massSpectrum != null) {
			UpdateNotifier.update(massSpectrum);
		}
	}

	@PreDestroy
	private void preDestroy() {

		IScan scan = null;
		UpdateNotifierUI.update(Display.getDefault(), scan);
		/*
		 * Remove the editor from the listed parts.
		 */
		if(modelService != null) {
			MPartStack partStack = (MPartStack)modelService.find(IPerspectiveAndViewIds.EDITOR_PART_STACK_ID, application);
			part.setToBeRendered(false);
			part.setVisible(false);
			partStack.getChildren().remove(part);
		}
		/*
		 * Run the garbage collector.
		 */
		System.gc();
	}

	@Persist
	public boolean save() {

		Shell shell = DisplayUtils.getShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

				try {
					monitor.beginTask("Save Mass Spectra", IProgressMonitor.UNKNOWN);
					try {
						saveMassSpectra(monitor, shell);
					} catch(NoMassSpectrumConverterAvailableException e) {
						throw new InvocationTargetException(e);
					}
				} finally {
					monitor.done();
				}
			}
		};
		/*
		 * Run the export
		 */
		try {
			/*
			 * True to show the moving progress bar. False, a mass spectrum
			 * should be imported as a whole.
			 */
			dialog.run(true, false, runnable);
		} catch(InvocationTargetException e) {
			return saveAs();
		} catch(InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}

	private void saveMassSpectra(IProgressMonitor monitor, Shell shell) throws NoMassSpectrumConverterAvailableException {

		/*
		 * Try to save the mass spectrum.
		 */
		if(massSpectrumFile != null && massSpectra != null && shell != null) {
			/*
			 * Convert the mass spectra.
			 */
			String converterId = massSpectra.getConverterId();
			if(converterId != null && !converterId.equals("")) {
				/*
				 * Try to save the mass spectrum.
				 */
				monitor.subTask("Save Mass Spectrum");
				IProcessingInfo<File> processingInfo = DatabaseConverter.convert(massSpectrumFile, massSpectra, false, converterId, monitor);
				try {
					/*
					 * If no failures have occurred, set the dirty status to
					 * false.
					 */
					processingInfo.getProcessingResult();
					dirtyable.setDirty(false);
				} catch(TypeCastException e) {
					logger.warn(e);
				}
			} else {
				throw new NoMassSpectrumConverterAvailableException();
			}
		}
	}

	@Override
	public boolean saveAs() {

		boolean saveSuccessful = false;
		if(massSpectra != null) {
			try {
				saveSuccessful = DatabaseFileSupport.saveMassSpectra(massSpectra);
				dirtyable.setDirty(!saveSuccessful);
			} catch(NoConverterAvailableException e) {
				logger.warn(e);
			}
		}
		return saveSuccessful;
	}

	private void loadMassSpectra() {

		try {
			/*
			 * Import the mass spectrum without showing it on the user interface.
			 * The GUI will take care itself of this action.
			 */
			Object object = part.getObject();
			if(object instanceof Map<?, ?> map) {
				/*
				 * String
				 */
				File file = new File((String)map.get(EditorSupport.MAP_FILE));
				boolean batch = (boolean)map.get(EditorSupport.MAP_BATCH);
				importMassSpectrum(file, batch);
			} else if(object instanceof String path) {
				/*
				 * Legacy ... Deprecated
				 */
				File file = new File(path);
				importMassSpectrum(file, true);
			}
		} catch(Exception e) {
			logger.warn(e);
		}
	}

	private void importMassSpectrum(File file, boolean batch) throws ChromatogramIsNullException {

		/*
		 * Import the mass spectrum here, but do not set to the mass spectrum UI,
		 * as it must be initialized first.
		 */
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(DisplayUtils.getShell());
		MassSpectrumImportRunnable runnable = new MassSpectrumImportRunnable(file);
		try {
			/*
			 * No fork, otherwise it might crash when loading the data takes too long.
			 */
			boolean fork = !batch;
			dialog.run(fork, false, runnable);
		} catch(InvocationTargetException e) {
			logger.warn(e);
			logger.warn(e.getCause());
		} catch(InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
		}
		massSpectra = runnable.getMassSpectra();
		if(toolbarSelectionControl.get() != null) {
			toolbarSelectionControl.get().update(massSpectra);
		}
		massSpectrumFile = file;
	}

	private void createPages(Composite parent) {

		if(massSpectra != null && massSpectra.getMassSpectrum(1) != null) {
			createMassSpectrumPage(parent);
		} else {
			createErrorMessagePage(parent);
		}
	}

	private void createMassSpectrumPage(Composite parent) {

		setPartLabel();
		//
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		//
		createToolbarMain(composite);
		createToolbarSelection(composite);
		createMassSpectrumChart(composite);
	}

	private void setPartLabel() {

		String name = ("".equals(massSpectra.getName())) ? "NoName" : massSpectra.getName();
		massSpectrum = massSpectra.getMassSpectrum(1);
		massSpectrum.setDirty(false);
		if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			name = standaloneMassSpectrum.getName();
		} else if(massSpectrum instanceof IRegularLibraryMassSpectrum regularLibraryMassSpectrum) {
			ILibraryInformation libraryInformation = regularLibraryMassSpectrum.getLibraryInformation();
			if(libraryInformation != null) {
				name = libraryInformation.getName();
			}
		}
		part.setLabel(name);
	}

	private void createMassSpectrumChart(Composite composite) {

		if(isProfile()) {
			massSpectrumChart = new MassSpectrumChartProfile(composite, SWT.BORDER);
		} else {
			massSpectrumChart = new MassSpectrumChartCentroid(composite, SWT.BORDER);
		}
		massSpectrumChart.update(massSpectrum);
	}

	private boolean isProfile() {

		if(massSpectrum instanceof IRegularMassSpectrum regularLibraryMassSpectrum) {
			return regularLibraryMassSpectrum.getMassSpectrumType() == MassSpectrumType.PROFILE;
		} else {
			return PreferenceSupplier.useProfileMassSpectrumView();
		}
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(4, false));
		//
		createButtonToggleSelection(composite);
		//
		toolbarMainControl.set(composite);
	}

	private void createToolbarSelection(Composite parent) {

		MassSpectraSelectionUI massSpectraSelectionUI = new MassSpectraSelectionUI(parent, SWT.NONE);
		massSpectraSelectionUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		massSpectraSelectionUI.setVisible(PreferenceSupplier.isSelectionComboVisible());
		toolbarSelectionControl.set(massSpectraSelectionUI);
		massSpectraSelectionUI.update(massSpectra);
		massSpectraSelectionUI.addSelectionChangeListener(createSelectionChangedListener());
	}

	private void createErrorMessagePage(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		Label label = new Label(composite, SWT.NONE);
		label.setText("The mass spectrum couldn't be loaded.");
	}

	public void registerEvent(String topic, String property) {

		registerEvent(topic, new String[]{property});
	}

	public void registerEvent(String topic, String[] properties) {

		if(eventBroker != null) {
			registeredEventHandler.add(registerEventHandler(eventBroker, topic, properties));
		}
	}

	private EventHandler registerEventHandler(IEventBroker eventBroker, String topic, String[] properties) {

		EventHandler eventHandler = new EventHandler() {

			@Override
			public void handleEvent(Event event) {

				try {
					objects.clear();
					for(String property : properties) {
						Object object = event.getProperty(property);
						objects.add(object);
					}
					update(topic);
				} catch(Exception e) {
					logger.warn(e + "\t" + event);
				}
			}
		};
		eventBroker.subscribe(topic, eventHandler);
		return eventHandler;
	}

	private void update(String topic) {

		if(massSpectrumChart.isVisible()) {
			updateObjects(objects, topic);
		}
	}

	public void registerEvents() {

		registerEvent(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
	}

	public void updateObjects(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(object instanceof IScanMSD scanMSD) {
				if(object != massSpectrum) {
					massSpectrumChart.update(scanMSD);
				} else {
					dirtyable.setDirty(massSpectrum.isDirty());
				}
			}
		}
	}

	@Override
	public IScanMSD getScanSelection() {

		return massSpectrum;
	}

	private Button createButtonToggleSelection(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSelectionControl, IApplicationImage.IMAGE_EXPAND_ALL, "Selection toolbar.");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				preferenceStore.setValue(PreferenceSupplier.P_SHOW_MASS_SPECTRUM_SELECTION_COMBO, toolbarSelectionControl.get().isVisible());
			}
		});
		return button;
	}

	private Button createButtonToggleToolbar(Composite parent, AtomicReference<? extends Composite> toolbar, String image, String tooltip) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		setButtonImage(button, image, tooltip, false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Composite composite = toolbar.get();
				if(composite != null) {
					boolean active = PartSupport.toggleCompositeVisibility(composite);
					setButtonImage(button, image, tooltip, active);
				}
			}
		});
		return button;
	}

	void setButtonImage(Button button, String image, String tooltip, boolean enabled) {

		if(isToggleButton(button)) {
			button.setSelection(enabled);
			if(button.getImage() == null) {
				button.setImage(ApplicationImageFactory.getInstance().getImage(image, IApplicationImageProvider.SIZE_16x16));
			}
		} else {
			button.setImage(ApplicationImageFactory.getInstance().getImage(image, IApplicationImageProvider.SIZE_16x16, enabled));
		}
		button.setToolTipText(tooltip);
	}

	private boolean isToggleButton(Button button) {

		return (button.getStyle() & SWT.TOGGLE) == SWT.TOGGLE;
	}

	private ISelectionChangedListener createSelectionChangedListener() {

		return new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				if(event.getSelection() instanceof IStructuredSelection selection) {
					if(selection.getFirstElement() instanceof IScanMSD scanMSD) {
						massSpectrum = scanMSD;
						massSpectrumChart.update(massSpectrum);
						UpdateNotifier.update(massSpectrum);
					}
				}
			}
		};
	}
}
