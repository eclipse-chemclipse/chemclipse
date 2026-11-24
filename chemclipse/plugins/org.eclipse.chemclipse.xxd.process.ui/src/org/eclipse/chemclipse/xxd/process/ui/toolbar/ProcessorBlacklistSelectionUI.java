/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.ui.toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.xxd.process.ui.l10n.ProcessMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Select processors that shall be hidden from the menus.
 * Active processors are blacklisted. Unlike the quick-access toolbar,
 * the blacklisted processors have no order and no custom image.
 */
public class ProcessorBlacklistSelectionUI extends Composite {

	private Text textSearch;
	private ProcessSupplierListUI processorListAvailable;
	private ProcessSupplierListUI processorListBlacklisted;

	private List<Processor> processors = new ArrayList<>();

	public ProcessorBlacklistSelectionUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public List<Processor> getProcessors() {

		return Collections.unmodifiableList(processors);
	}

	public void setInput(List<Processor> processors) {

		this.processors.clear();
		if(processors != null) {
			this.processors.addAll(processors);
		}
		updateProcessorLists();
	}

	private void createControl() {

		setLayout(new GridLayout(3, false));

		createToolbarTop(this);
		createListSection(this);
	}

	private void createToolbarTop(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));

		textSearch = createTextSearch(composite);
		createButtonSearch(composite);
	}

	private Text createTextSearch(Composite parent) {

		Text text = new Text(parent, SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
		text.setToolTipText(ProcessMessages.searchAvailableProcessorItems);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		/*
		 * Listen to search key event.
		 */
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				runSearch();
			}
		});
		/*
		 * Prevent that the dialog is closed on enter search.
		 */
		text.addTraverseListener(e -> {
			if(e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
			}
		});
		/*
		 * Click on the icons.
		 */
		text.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				if(e.detail == SWT.ICON_CANCEL) {
					text.setText(""); //$NON-NLS-1$
					runSearch();
				} else if(e.detail == SWT.ICON_SEARCH) {
					runSearch();
				}
			}
		});

		return text;
	}

	private Button createButtonSearch(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SEARCH, IApplicationImageProvider.SIZE_16x16));

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				runSearch();
			}
		});

		return button;
	}

	private void createListSection(Composite parent) {

		processorListAvailable = createProcessSupplierListUI(parent);
		createToolbarSelect(parent);
		processorListBlacklisted = createProcessSupplierListUI(parent);
	}

	private ProcessSupplierListUI createProcessSupplierListUI(Composite parent) {

		ProcessSupplierListUI processSupplierListUI = new ProcessSupplierListUI(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 300;
		gridData.widthHint = 100;
		processSupplierListUI.enableSorting(true);
		Table table = processSupplierListUI.getTable();
		table.setLayoutData(gridData);

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				Object object = processSupplierListUI.getStructuredSelection().getFirstElement();
				if(object instanceof Processor processor) {
					processor.setActive(!processor.isActive());
					updateProcessorLists();
				}
			}
		});

		return processSupplierListUI;
	}

	private void createToolbarSelect(Composite parent) {

		ToolBar toolBar = new ToolBar(parent, SWT.VERTICAL | SWT.FLAT);

		createToolItemAdd(toolBar);
		createToolItemRemove(toolBar);
	}

	private void createToolItemAdd(ToolBar toolBar) {

		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		toolItem.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ARROW_FORWARD, IApplicationImageProvider.SIZE_16x16));

		toolItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				modifySelectedProcessors(processorListAvailable, true);
				updateProcessorLists();
			}
		});
	}

	private void createToolItemRemove(ToolBar toolBar) {

		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		toolItem.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ARROW_BACKWARD, IApplicationImageProvider.SIZE_16x16));

		toolItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				modifySelectedProcessors(processorListBlacklisted, false);
				updateProcessorLists();
			}
		});
	}

	private void modifySelectedProcessors(ProcessSupplierListUI listUI, boolean blacklist) {

		for(Object object : listUI.getStructuredSelection().toArray()) {
			if(object instanceof Processor processor) {
				processor.setActive(blacklist);
			}
		}
	}

	private void runSearch() {

		String searchText = textSearch.getText().trim();
		processorListAvailable.setSearchText(searchText, false);
	}

	private void updateProcessorLists() {

		processorListAvailable.setInput(processors.stream().filter(processor -> !processor.isActive()).toList());
		processorListBlacklisted.setInput(processors.stream().filter(Processor::isActive).toList());
	}
}
