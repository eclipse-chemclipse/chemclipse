/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - adapted for PCR
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.support;

import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.model.core.IMeasurementInfo;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.pcr.converter.core.PlateConverterPCR;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.pcr.ui.Activator;
import org.eclipse.chemclipse.ux.extension.pcr.ui.editors.PlateEditorPCR;
import org.eclipse.chemclipse.ux.extension.ui.editors.EditorDescriptor;
import org.eclipse.chemclipse.ux.extension.ui.provider.AbstractSupplierFileEditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierEditorSupport;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

public class SupplierEditorSupport extends AbstractSupplierFileEditorSupport implements ISupplierEditorSupport {

	private static final Object NO_EXECUTE_METHOD = new Object();
	private String type = "";
	//
	private String elementId = "";
	private String contributionURI = "";
	private String iconURI = "";
	private String tooltip = "";
	private String topicUpdateRawfile = "";
	private String topicUpdateOverview = "";
	private final Supplier<IEclipseContext> contextSupplier;

	public SupplierEditorSupport(Supplier<IEclipseContext> contextSupplier) {

		super(PlateConverterPCR.getScanConverterSupport().getSupplier());
		this.contextSupplier = contextSupplier;
		refreshEditorReferences();
	}

	@Override
	public String getType() {

		return type;
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, boolean batch) {

		if(isSupplierFile(file)) {
			refreshEditorReferences();
			openEditor(file, null, elementId, contributionURI, iconURI, tooltip, headerMap, batch);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, ISupplier supplier) {

		IEclipseContext eclipseContext = contextSupplier.get();
		IEclipseContext parameterContext = EclipseContextFactory.create();
		try {
			parameterContext.set(File.class, file);
			parameterContext.set(ISupplier.class, supplier);
			Object[] executables = {Adapters.adapt(supplier, EditorDescriptor.class), supplier};
			for(Object executable : executables) {
				if(executable == null) {
					continue;
				}
				Object invoke = ContextInjectionFactory.invoke(executable, Execute.class, eclipseContext, parameterContext, NO_EXECUTE_METHOD);
				if(NO_EXECUTE_METHOD != invoke) {
					if(invoke instanceof Boolean booleanInvoke) {
						return booleanInvoke.booleanValue();
					}
					return true;
				}
			}
		} finally {
			parameterContext.dispose();
		}
		return openEditor(file, headerMap, false);
	}

	@Override
	public void openEditor(IMeasurement measurement) {

		refreshEditorReferences();
		openEditor(null, measurement, elementId, contributionURI, iconURI, tooltip);
	}

	@Override
	public void openOverview(final File file) {

		if(isSupplierFile(file)) {
			IEventBroker eventBroker = Activator.getDefault().getEventBroker();
			eventBroker.send(topicUpdateRawfile, file);
		}
	}

	@Override
	public void openOverview(IMeasurementInfo measurementInfo) {

		IEventBroker eventBroker = Activator.getDefault().getEventBroker();
		eventBroker.send(topicUpdateOverview, measurementInfo);
	}

	private void refreshEditorReferences() {

		type = TYPE_PCR;
		elementId = PlateEditorPCR.ID;
		contributionURI = PlateEditorPCR.CONTRIBUTION_URI;
		iconURI = PlateEditorPCR.ICON_URI;
		tooltip = PlateEditorPCR.TOOLTIP;
		topicUpdateRawfile = IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_RAWFILE;
		topicUpdateOverview = IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_OVERVIEW;
	}
}
