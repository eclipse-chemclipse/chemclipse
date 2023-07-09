/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.processors.Processor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.support.PreferencesProcessSupport;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ProcessorToolbarUI extends Composite {

	private PreferencesProcessSupport preferencesSupport;
	private BiConsumer<IProcessSupplier<?>, IProcessSupplierContext> executionListener;
	//
	private List<Processor> processors = new ArrayList<>();
	private List<Button> buttons = new ArrayList<>();
	private Composite control;
	//
	private IProcessSupplierContext context = new ProcessTypeSupport();

	public ProcessorToolbarUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public void update() {

		super.update();
		updateInput();
	}

	public void setInput(PreferencesProcessSupport preferencesSupport, BiConsumer<IProcessSupplier<?>, IProcessSupplierContext> executionListener) {

		this.preferencesSupport = preferencesSupport;
		this.executionListener = executionListener;
		updateInput();
	}

	private void createControl() {

		setLayout(new FillLayout());
		//
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(1, true));
		//
		createToolbarMain(composite);
		//
		initialize();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));
		/*
		 * The toolbar buttons are set dynamically.
		 */
		this.control = composite;
	}

	private void initialize() {

		updateInput();
	}

	private void updateInput() {

		clearElements();
		createProcessorButtons(control);
		layoutControl(control);
	}

	private void clearElements() {

		processors.clear();
		for(Button button : buttons) {
			if(!button.isDisposed()) {
				button.dispose();
			}
		}
		//
		buttons.clear();
	}

	private void createProcessorButtons(Composite parent) {

		if(preferencesSupport != null) {
			processors.addAll(preferencesSupport.getStoredProcessors());
			for(Processor processor : processors) {
				if(processor != null && processor.isActive()) {
					buttons.add(createButton(parent, processor, executionListener, context));
				}
			}
		}
	}

	private void layoutControl(Composite parent) {

		if(parent.getLayout() instanceof GridLayout gridLayout) {
			gridLayout.numColumns = buttons.size();
			parent.getParent().layout(true);
		}
	}

	private Button createButton(Composite parent, Processor processor, BiConsumer<IProcessSupplier<?>, IProcessSupplierContext> executionListener, IProcessSupplierContext processSupplierContext) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(getToolTipText(processor));
		button.setImage(ApplicationImageFactory.getInstance().getImage(processor.getImageFileName(), IApplicationImageProvider.SIZE_16x16));
		//
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				executionListener.accept(processor.getProcessSupplier(), processSupplierContext);
			}
		});
		//
		return button;
	}

	private String getToolTipText(Processor processor) {

		StringBuilder builder = new StringBuilder();
		IProcessSupplier<?> processSupplier = processor.getProcessSupplier();
		builder.append(processSupplier.getName());
		String description = processSupplier.getDescription();
		if(description != null && !description.isEmpty()) {
			builder.append(": ");
			builder.append(description);
		}
		//
		return builder.toString();
	}
}