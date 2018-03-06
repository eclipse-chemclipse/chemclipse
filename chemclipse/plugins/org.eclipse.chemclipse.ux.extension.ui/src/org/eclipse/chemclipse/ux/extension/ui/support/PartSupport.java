/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.support;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.addons.ModelSupportAddon;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MArea;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class PartSupport {

	public static final String PERSPECTIVE_DATA_ANALYSIS = "org.eclipse.chemclipse.ux.extension.xxd.ui.perspective.main";
	//
	public static final String AREA = "org.eclipse.chemclipse.rcp.app.ui.editor";
	//
	public static final String PARTDESCRIPTOR_CHROMATOGRAM_OVERLAY = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramOverlayPartDescriptor";
	public static final String PARTDESCRIPTOR_CHROMATOGRAM_HEADER = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramHeaderPartDescriptor";
	public static final String PARTDESCRIPTOR_CHROMATOGRAM_OVERVIEW = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramOverviewPartDescriptor";
	public static final String PARTDESCRIPTOR_SCAN_CHART = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.scanChartPartDescriptor";
	public static final String PARTDESCRIPTOR_SCAN_TABLE = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.scanTablePartDescriptor";
	public static final String PARTDESCRIPTOR_SCAN_LIST = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.scanListPartDescriptor";
	public static final String PARTDESCRIPTOR_TARGETS = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.targetsPartDescriptor";
	public static final String PARTDESCRIPTOR_PEAK_CHART = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.peakChartPartDescriptor";
	public static final String PARTDESCRIPTOR_PEAK_DETAILS = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.peakDetailsPartDescriptor";
	public static final String PARTDESCRIPTOR_PEAK_DETECTOR = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.peakDetectorPartDescriptor";
	public static final String PARTDESCRIPTOR_PEAK_LIST = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.peakListPartDescriptor";
	public static final String PARTDESCRIPTOR_SUBTRACT_SCAN = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.subtractScanPartDescriptor";
	public static final String PARTDESCRIPTOR_COMBINED_SCAN = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.combinedScanPartDescriptor";
	public static final String PARTDESCRIPTOR_COMPARISON_SCAN = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.comparisonScanPartDescriptor";
	public static final String PARTDESCRIPTOR_INTEGRATION_AREA = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.integrationAreaPartDescriptor";
	public static final String PARTDESCRIPTOR_QUANTITATION = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.quantitationPartDescriptor";
	public static final String PARTDESCRIPTOR_INTERNAL_STANDARDS = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.internalStandardsPartDescriptor";
	//
	public static final String PARTSTACK_NONE = "";
	public static final String PARTSTACK_QUICKACCESS = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.quickaccess";
	public static final String PARTSTACK_LEFT_TOP = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.left.top";
	public static final String PARTSTACK_LEFT_CENTER = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.left.center";
	public static final String PARTSTACK_RIGHT_TOP = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.right.top";
	public static final String PARTSTACK_BOTTOM_LEFT = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.bottom.left";
	public static final String PARTSTACK_BOTTOM_CENTER = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.bottom.center";
	public static final String PARTSTACK_BOTTOM_RIGHT = "org.eclipse.chemclipse.ux.extension.xxd.ui.partstack.bottom.right";
	//
	private static MApplication application = ModelSupportAddon.getApplication();
	private static EModelService modelService = ModelSupportAddon.getModelService();
	private static EPartService partService = ModelSupportAddon.getPartService();
	//
	private static Map<String, String> partMap = new HashMap<String, String>();
	private static IEventBroker eventBroker = ModelSupportAddon.getEventBroker();

	public static MPart getPart(String partId, String partStackId) {

		MPart part = null;
		MUIElement element = modelService.find(partId, application);
		if(element instanceof MPart) {
			/*
			 * Get the part or create it.
			 */
			part = (MPart)element;
			if(!partService.getParts().contains(part)) {
				partService.createPart(part.getElementId());
			}
		} else {
			part = partService.createPart(partId);
			MPartStack partStack = (MPartStack)modelService.find(partStackId, application);
			partStack.getChildren().add(part);
		}
		//
		return part;
	}

	/***
	 * Returns true if the part is visible after running the method.
	 * 
	 * @param part
	 * @param partStackId
	 * @return boolean
	 */
	public static boolean togglePartVisibility(MPart part, String partStackId) {

		boolean isVisible = false;
		if(part != null) {
			if(part.isVisible()) {
				part.setVisible(false);
				partService.hidePart(part);
			} else {
				part.setVisible(true);
				partService.showPart(part, PartState.ACTIVATE);
				isVisible = true;
			}
		}
		return isVisible;
	}

	public static boolean isChildrenOfPartStack(MPartStack partStack, String elementId) {

		if(partStack != null) {
			for(MStackElement stackElement : partStack.getChildren()) {
				if(stackElement.getElementId().equals(elementId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isPartVisible(MPart part) {

		if(part != null) {
			if(partService.isPartVisible(part)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean isPartVisible(String partId, String partStackId) {

		MPart part = getPart(partId, partStackId);
		if(part != null) {
			if(partService.isPartVisible(part)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static void showPart(String partId, String partStackId) {

		MPart part = getPart(partId, partStackId);
		showPart(part);
	}

	public static void showPart(MPart part) {

		if(part != null) {
			partService.showPart(part, PartState.ACTIVATE);
		}
	}

	public static void hidePart(MPart part) {

		if(part != null) {
			partService.hidePart(part);
		}
	}

	public static void setPartVisibility(String partId, String partStackId, boolean visible) {

		MPart part = getPart(partId, partStackId);
		if(part != null) {
			part.setVisible(visible);
		}
	}

	public static MPartStack getPartStack(String partStackId) {

		return (MPartStack)modelService.find(partStackId, application);
	}

	public static void setAreaVisibility(String areaId, boolean visible) {

		MArea area = (MArea)modelService.find(areaId, application);
		if(area != null) {
			area.setVisible(visible);
		}
	}

	public static boolean toggleCompositeVisibility(Composite composite) {

		boolean visible = !composite.isVisible();
		setCompositeVisibility(composite, visible);
		return visible;
	}

	public static void setCompositeVisibility(Composite composite, boolean visible) {

		composite.setVisible(visible);
		GridData gridData = (GridData)composite.getLayoutData();
		if(gridData != null) {
			gridData.exclude = !visible;
		}
		Composite parent = composite.getParent();
		parent.layout(true);
		parent.redraw();
	}

	public static void setControlVisibility(Control control, boolean visible) {

		control.setVisible(visible);
		GridData gridData = (GridData)control.getLayoutData();
		if(gridData != null) {
			gridData.exclude = !visible;
		}
		//
		Composite parent = control.getParent();
		Composite parentParent = parent.getParent();
		if(parentParent != null) {
			parent = parentParent;
		}
		//
		parent.layout(true);
		parent.redraw();
	}

	/**
	 * Prefer to use this method if you'd like to toggle the part visibility.
	 * 
	 * @param partId
	 * @param partStackId
	 */
	public static void togglePartVisibility(String partId, String partStackId) {

		if(PartSupport.PARTSTACK_NONE.equals(partStackId)) {
			/*
			 * Hide the part if it is visible.
			 */
			String currentPartStackId = partMap.get(partId);
			if(currentPartStackId != null) {
				setPartVisibility(partId, currentPartStackId, false);
			}
		} else {
			/*
			 * Initialize the part status if the user
			 * has chosen another than the initial position.
			 */
			String currentPartStackId = partMap.get(partId);
			if(currentPartStackId == null) {
				/*
				 * Initialize the part.
				 */
				setPartVisibility(partId, partStackId, false);
			} else {
				/*
				 * Move the part to another part stack.
				 */
				if(!partStackId.equals(currentPartStackId)) {
					MPart part = PartSupport.getPart(partId, currentPartStackId);
					MPartStack defaultPartStack = PartSupport.getPartStack(currentPartStackId);
					MPartStack partStack = PartSupport.getPartStack(partStackId);
					defaultPartStack.getChildren().remove(part);
					partStack.getChildren().add(part);
				}
			}
			partMap.put(partId, partStackId);
			/*
			 * Toggle visibility and send an event.
			 * E.g. the icons in the toolbar "TaskQuickAccessPart.java" will be modified.
			 */
			MPart part = getPart(partId, partStackId);
			if(togglePartVisibility(part, partStackId)) {
				eventBroker.post(IChemClipseEvents.TOPIC_TOGGLE_PART_VISIBILITY_TRUE, partId);
			} else {
				eventBroker.post(IChemClipseEvents.TOPIC_TOGGLE_PART_VISIBILITY_FALSE, partId);
			}
		}
	}

	/**
	 * Prefer to use this method if you'd like to toggle the part stack visibility.
	 * 
	 * @param partId
	 * @param partStackId
	 */
	public static void setPartStackVisibility(String partStackId, boolean visible) {

		MPartStack partStack = getPartStack(partStackId);
		if(partStack != null) {
			partStack.setVisible(visible);
			if(visible) {
				eventBroker.post(IChemClipseEvents.TOPIC_TOGGLE_PARTSTACK_VISIBILITY_TRUE, partStackId);
			} else {
				eventBroker.post(IChemClipseEvents.TOPIC_TOGGLE_PARTSTACK_VISIBILITY_FALSE, partStackId);
			}
		}
	}
}
