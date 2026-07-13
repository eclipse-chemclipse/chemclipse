/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io.CalibrationFileReader;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io.CalibrationFileWriter;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io.StandardsReader;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.workbench.EditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChemClipseEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.calibration.ExtendedRetentionIndexListUI;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.osgi.service.event.EventHandler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class EditorCalibration implements IChemClipseEditor {

	public static final String ID = "org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.editors.editorCalibration";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui/org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.editors.EditorCalibration";
	public static final String ICON_URI = "platform:/plugin/org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui/icons/16x16/report.gif";
	public static final String TOOLTIP = "Retention Index Calibration";

	private static final Logger logger = Logger.getLogger(EditorCalibration.class);

	@Inject
	private MPart part;
	@Inject
	private MDirtyable dirtyable;
	@Inject
	private IEventBroker eventBroker;

	private ExtendedRetentionIndexListUI extendedRetentionIndexListUI;
	private File file;
	private ISeparationColumnIndices separationColumnIndices;

	private ArrayList<EventHandler> registeredEventHandler;
	private List<Object> objects = new ArrayList<>();

	public void registerEvent(String topic, String property) {

		registerEvent(topic, new String[]{property});
	}

	public void registerEvent(String topic, String[] properties) {

		if(eventBroker != null) {
			registeredEventHandler.add(registerEventHandler(eventBroker, topic, properties));
		}
	}

	public void registerEvents() {

		registerEvent(IChemClipseEvents.TOPIC_RI_LIBRARY_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
	}

	@Persist
	public void save() {

		if(file != null && separationColumnIndices != null) {
			CalibrationFileWriter calibrationFileWriter = new CalibrationFileWriter();
			calibrationFileWriter.write(file, separationColumnIndices);
			dirtyable.setDirty(false);
		}
	}

	@Override
	public boolean saveAs() {

		FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
		fileDialog.setText("Save the *.cal file.");
		fileDialog.setFilterExtensions("*.cal");
		fileDialog.setFilterNames("AMDIS Calibration *.cal");
		String pathRetentionIndexFile = fileDialog.open();
		if(pathRetentionIndexFile != null) {
			File file = new File(pathRetentionIndexFile);
			CalibrationFileWriter calibrationFileWriter = new CalibrationFileWriter();
			calibrationFileWriter.write(file, separationColumnIndices);
			dirtyable.setDirty(false);
			return true;
		}
		return false;
	}

	@Focus
	public void setFocus() {

		if(extendedRetentionIndexListUI != null) {
			extendedRetentionIndexListUI.setFocus();
		}
	}

	public void updateObjects(List<Object> objects) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(object instanceof Object[] array) {
				if(array.length == 2) {
					Object content = array[1];
					if(content instanceof ISeparationColumnIndices separationColumnIndices) {
						if(this.separationColumnIndices == separationColumnIndices) {
							dirtyable.setDirty(separationColumnIndices.isDirty());
						}
					}
				}
			}
		}
	}

	@PostConstruct
	private void createControl(Composite parent) {

		extendedRetentionIndexListUI = new ExtendedRetentionIndexListUI(parent, SWT.NONE);
		extendedRetentionIndexListUI.setInput(new StandardsReader().getStandardsList());
		loadCalibrationFile();
		registeredEventHandler = new ArrayList<>();
		registerEvents();
	}

	private void loadCalibrationFile() {

		Object object = part.getObject();
		if(object instanceof Map<?, ?> map) {
			file = new File((String)map.get(EditorSupport.MAP_FILE));
		}
		if(file != null) {
			String fileName = file.getName();
			if(fileName.length() > 4) {
				fileName = fileName.substring(0, fileName.length() - 4);
			}
			part.setLabel(fileName);
			CalibrationFileReader calibrationFileReader = new CalibrationFileReader();
			separationColumnIndices = calibrationFileReader.parse(file);
			extendedRetentionIndexListUI.setFile(file);
			extendedRetentionIndexListUI.setInput(separationColumnIndices);
		}
	}

	@PreDestroy
	private void preDestroy() {

		if(eventBroker != null) {
			for(EventHandler eventHandler : registeredEventHandler) {
				eventBroker.unsubscribe(eventHandler);
			}
		}
	}

	private EventHandler registerEventHandler(IEventBroker eventBroker, String topic, String[] properties) {

		EventHandler eventHandler = event -> {
			try {
				objects.clear();
				for(String property : properties) {
					Object object = event.getProperty(property);
					objects.add(object);
				}
				update();
			} catch(Exception e) {
				logger.warn(e + "\t" + event);
			}
		};
		eventBroker.subscribe(topic, eventHandler);
		return eventHandler;
	}

	private void update() {

		updateObjects(objects);
	}
}
