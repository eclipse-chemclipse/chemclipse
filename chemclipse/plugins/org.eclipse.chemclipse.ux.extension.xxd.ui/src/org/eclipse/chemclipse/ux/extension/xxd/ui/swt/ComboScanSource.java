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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import static org.eclipse.chemclipse.support.ui.swt.ControlBuilder.autoComplete;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Lists the sources of a scan, e.g. referenced chromatograms or library mass spectra.
 * A library contains several ten thousand entries, which a combo can't display at once,
 * hence only a limited number of matches is shown while typing.
 */
public class ComboScanSource extends Composite {

	private static final int MAX_ITEMS = 100;

	/*
	 * Whether a combo is read-only is a style flag, hence it can't be switched.
	 * Both variants are created and the matching one is put on top instead.
	 */
	private final StackLayout stackLayout = new StackLayout();
	private Combo comboReadOnly;
	private Combo comboEditable;
	private Combo combo;
	/*
	 * The sources are all available entries, the items are the displayed ones.
	 */
	private final List<Object> sources = new ArrayList<>();
	private final List<Object> items = new ArrayList<>();

	private Function<Object, String> labelFunction = String::valueOf;
	private Consumer<Object> selectionListener = null;

	public ComboScanSource(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setLabelFunction(Function<Object, String> labelFunction) {

		this.labelFunction = labelFunction;
	}

	public void setSelectionListener(Consumer<Object> selectionListener) {

		this.selectionListener = selectionListener;
	}

	public void setInput(List<?> sources) {

		this.sources.clear();
		this.sources.addAll(sources);
		setItems(filter(""));
	}

	/**
	 * Returns the selected source or null if none matches.
	 *
	 * @return {@link Object}
	 */
	public Object getSelection() {

		int index = combo.getSelectionIndex();
		if(index >= 0 && index < items.size()) {
			return items.get(index);
		}
		/*
		 * A content proposal sets the text only, hence the item is matched by its label.
		 */
		String text = combo.getText();
		for(Object item : items) {
			if(getLabel(item).equals(text)) {
				return item;
			}
		}

		return null;
	}

	public void selectFirst() {

		if(!items.isEmpty()) {
			combo.select(0);
		}
	}

	/**
	 * Moves the selection by the given offset, e.g. -1 for the previous source.
	 *
	 * @param moveIndex
	 */
	public void moveSelection(int moveIndex) {

		int itemCount = combo.getItemCount();
		if(itemCount > 0) {
			int index = combo.getSelectionIndex() + moveIndex;
			index = Math.max(0, Math.min(index, itemCount - 1));
			combo.select(index);
		}
	}

	public boolean hasPrevious() {

		return combo.getSelectionIndex() > 0;
	}

	public boolean hasNext() {

		return combo.getSelectionIndex() < combo.getItemCount() - 1;
	}

	/**
	 * Only a library offers enough entries to be searched through, hence typing
	 * is enabled on demand instead of permanently.
	 *
	 * @param editable
	 */
	public void setEditable(boolean editable) {

		Combo comboActive = editable ? comboEditable : comboReadOnly;
		if(comboActive != combo) {
			combo = comboActive;
			stackLayout.topControl = combo;
			layout(true);
			/*
			 * The items have been set on the combo that was on top before.
			 */
			setItems(new ArrayList<>(items));
		}
	}

	private void createControl() {

		setLayout(stackLayout);

		comboReadOnly = createCombo(this, SWT.READ_ONLY, "Select the source of the scan.");
		comboEditable = createCombo(this, SWT.NONE, "Select a source or type in a reference id.");
		enableAutoComplete(comboEditable);

		combo = comboReadOnly;
		stackLayout.topControl = combo;
	}

	private Combo createCombo(Composite parent, int style, String toolTipText) {

		Combo combo = new Combo(parent, style);
		combo.setToolTipText(toolTipText);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				fireSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				fireSelection();
			}
		});

		return combo;
	}

	private void enableAutoComplete(Combo combo) {

		IContentProposalProvider proposalProvider = (contents, _) -> {
			List<IContentProposal> proposals = new ArrayList<>();
			if(contents != null) {
				/*
				 * Dynamically add back the entries so they can be selected.
				 */
				String text = combo.getText();
				Point selection = combo.getSelection();
				setItems(filter(contents));
				combo.setText(text);
				combo.setSelection(selection);

				for(String item : combo.getItems()) {
					proposals.add(new ContentProposal(item));
				}
			}
			return proposals.toArray(new IContentProposal[0]);
		};

		ContentProposalAdapter contentProposalAdapter = autoComplete(combo, proposalProvider);
		contentProposalAdapter.addContentProposalListener((IContentProposalListener)_ -> fireSelection());
	}

	/**
	 * Returns at most {@value #MAX_ITEMS} sources, preferring the exact matches.
	 *
	 * @param contents
	 * @return {@link List}
	 */
	private List<Object> filter(String contents) {

		String search = (contents != null) ? contents.trim().toLowerCase() : "";
		List<Object> startsWith = new ArrayList<>();
		List<Object> contains = new ArrayList<>();

		for(Object source : sources) {
			if(search.isEmpty()) {
				startsWith.add(source);
			} else {
				String label = getLabel(source).toLowerCase();
				if(label.startsWith(search)) {
					startsWith.add(source);
				} else if(label.contains(search)) {
					contains.add(source);
				}
			}
			/*
			 * The exact matches are sufficient, no need to walk through the remaining sources.
			 */
			if(startsWith.size() >= MAX_ITEMS) {
				break;
			}
		}

		List<Object> filtered = new ArrayList<>(startsWith);
		for(Object source : contains) {
			if(filtered.size() >= MAX_ITEMS) {
				break;
			}
			filtered.add(source);
		}

		return filtered;
	}

	private void setItems(List<Object> filtered) {

		items.clear();
		items.addAll(filtered);

		String[] labels = new String[items.size()];
		for(int i = 0; i < items.size(); i++) {
			labels[i] = getLabel(items.get(i));
		}
		combo.setItems(labels);
	}

	private String getLabel(Object source) {

		String label = labelFunction.apply(source);
		return (label != null) ? label : "";
	}

	private void fireSelection() {

		if(selectionListener != null) {
			selectionListener.accept(getSelection());
		}
	}
}
