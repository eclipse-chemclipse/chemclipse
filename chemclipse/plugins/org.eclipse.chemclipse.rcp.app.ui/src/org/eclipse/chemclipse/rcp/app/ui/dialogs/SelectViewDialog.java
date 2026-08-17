/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.rcp.app.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.rcp.app.ui.provider.SelectViewContentProvider;
import org.eclipse.chemclipse.rcp.app.ui.provider.SelectViewFilter;
import org.eclipse.chemclipse.rcp.app.ui.provider.SelectViewLabelProvider;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import jakarta.inject.Inject;
import jakarta.inject.Named;

public class SelectViewDialog extends Dialog implements ISelectionChangedListener {

	/*
	 * Initial table height and weight
	 */
	private static final int LIST_HEIGHT = 300;
	private static final int LIST_WIDTH = 300;
	/*
	 * The SWT elements
	 */
	private TableViewer tableViewer;
	private SelectViewFilter selectViewFilter;
	private Text text;
	/*
	 * Context and services
	 */
	@Inject
	private IEclipseContext eclipseContext;
	@Inject
	private MApplication application;
	@Inject
	private EModelService modelService;
	@Inject
	private EPartService partService;
	private List<MPart> selectedParts = new ArrayList<>();

	@Inject
	public SelectViewDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {

		super(shell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Select View");
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		selectedParts.clear();
		if(event.getSelection() instanceof IStructuredSelection selection) {
			for(Object element : selection.toList()) {
				if(element instanceof MPart part) {
					selectedParts.add(part);
				}
			}
		}
		validateSelection();
	}

	/**
	 * Creates and returns the contents of the upper part of this dialog (above
	 * the button bar).
	 *
	 * @param parent
	 *            the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite)super.createDialogArea(parent);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(1, true));
		/*
		 * Create the SWT elements
		 */
		createViewSearchTextField(composite);
		createViewList(composite);
		/*
		 * Enable / disable the OK button.
		 */
		validateSelection();

		return composite;
	}

	@Override
	protected void okPressed() {

		super.okPressed();
		selectAndActivatePart();
	}

	private void selectAndActivatePart() {

		MPart lastPart = null;
		for(MPart part : selectedParts) {
			if(!partService.getParts().contains(part)) {
				partService.createPart(part.getElementId());
			}
			partService.showPart(part, PartState.VISIBLE);
			lastPart = part;
		}
		if(lastPart != null) {
			partService.showPart(lastPart, PartState.ACTIVATE);
		}
	}

	/**
	 * Creates a text field to search the list of perspectives.
	 *
	 * @param parent
	 */
	private void createViewSearchTextField(Composite parent) {

		text = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				selectViewFilter.setSearchPattern(text.getText());
				tableViewer.refresh();
				validateSelection();
			}
		});
		text.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				selectViewFilter.setSearchPattern(text.getText());
				tableViewer.refresh();
				validateSelection();
			}
		});
	}

	/**
	 * Creates the list of available perspectives.
	 *
	 * @param parent
	 */
	private void createViewList(Composite parent) {

		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = LIST_WIDTH;
		gridData.heightHint = LIST_HEIGHT;
		Control control = tableViewer.getControl();
		control.setLayoutData(gridData);
		/*
		 * Label and content provider
		 */
		tableViewer.setLabelProvider(ContextInjectionFactory.make(SelectViewLabelProvider.class, eclipseContext));
		tableViewer.setContentProvider(new SelectViewContentProvider());
		selectViewFilter = new SelectViewFilter();
		selectViewFilter.setCaseInsensitive(true);
		tableViewer.addFilter(selectViewFilter);
		tableViewer.addSelectionChangedListener(this);
		/*
		 * Select the perspective in double click.
		 */
		tableViewer.addDoubleClickListener(_ -> okPressed());
		/*
		 * Input (Sorted)
		 */
		Map<String, MPart> partMap = new HashMap<>();
		for(MPart part : modelService.findElements(application, null, MPart.class)) {
			partMap.put(part.getElementId(), part);
		}
		List<MPart> parts = new ArrayList<>(partMap.values());
		Collections.sort(parts, (p1, p2) -> getPartLabel(p1).compareTo(getPartLabel(p2)));
		tableViewer.setInput(parts);
	}

	private String getPartLabel(MPart part) {

		String label = part.getLabel();
		return label != null ? label : "";
	}

	/**
	 * Validates whether the OK button is enabled or not.
	 */
	private void validateSelection() {

		Button buttonOK = getButton(IDialogConstants.OK_ID);
		if(buttonOK == null) {
			return;
		}
		buttonOK.setEnabled(!selectedParts.isEmpty());
	}
}