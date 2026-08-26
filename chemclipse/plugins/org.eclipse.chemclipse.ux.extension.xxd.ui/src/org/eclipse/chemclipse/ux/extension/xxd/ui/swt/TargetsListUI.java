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
 * Christoph Läubrich - make more generic useable
 * Matthias Mailänder - display the metrics of the identification algorithm
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.provider.TargetListFilter;
import org.eclipse.chemclipse.ux.extension.ui.provider.TargetsColumns;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TargetsComparator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TargetsEditingSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TargetsLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

public class TargetsListUI extends ExtendedTableViewer {

	private static final String[] TITLES = TargetsLabelProvider.TITLES;
	private static final int[] BOUNDS = TargetsLabelProvider.BOUNDS;

	private final TargetsLabelProvider labelProvider = new TargetsLabelProvider();
	private final TargetsComparator targetsComparator = new TargetsComparator();
	private final TargetListFilter targetListFilter = new TargetListFilter();

	private Integer retentionTime = null;
	private Float retentionIndex = null;

	private TargetsColumns targetsColumns = null;
	private boolean dynamicColumns = false;
	private boolean editingSupport = false;

	private IUpdateListener updateListener;

	public TargetsListUI(Composite parent, int style) {

		this(parent, TITLES, style);
		dynamicColumns = true;
	}

	public void setComparator(boolean active) {

		if(active) {
			setComparator(targetsComparator);
			sortTable();
		} else {
			setComparator(null);
			refresh();
		}
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void updateContent() {

		if(updateListener != null) {
			updateListener.update();
		}
	}

	public TargetsListUI(Composite parent, String[] alternativeTitles, int style) {

		super(parent, style | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		createColumns(alternativeTitles, BOUNDS);
		setLabelProvider(labelProvider); // order is important
		setContentProvider(new ListContentProvider());
		setComparator(false);
		setFilters(targetListFilter);
		setCellColorProvider();
		createDragAndDropProvider();
	}

	@Override
	protected void inputChanged(Object input, Object oldInput) {

		updateColumns(input);
		super.inputChanged(input, oldInput);
	}

	private void updateColumns(Object input) {

		if(!dynamicColumns) {
			return;
		}

		Collection<?> leadingColums = input instanceof Collection<?> collection ? collection : null;
		TargetsColumns columns = TargetsColumns.create(leadingColums, TargetsLabelProvider.TRAILING_COLUMNS);
		if(columns.matches(targetsColumns)) {
			return;
		}

		targetsColumns = columns;
		labelProvider.setColumns(columns);
		targetsComparator.setColumns(columns);
		createColumns(columns.getTitles(), columns.getBounds());

		setLabelProvider(labelProvider);
		setCellColorProvider();
		if(editingSupport) {
			setEditingSupport();
		}
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		targetListFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void updateSourceRange(Integer retentionTime, Float retentionIndex) {

		this.retentionTime = retentionTime;
		this.retentionIndex = retentionIndex;
		refresh();
	}

	public void clear() {

		setInput(null);
		updateSourceRange(null, null);
	}

	public void sortTable() {

		if(getComparator() != null) {
			int column = 0;
			int sortOrder = IRecordTableComparator.DESCENDING;

			targetsComparator.setColumn(column);
			targetsComparator.setDirection(sortOrder);
			refresh();
			targetsComparator.setDirection(1 - sortOrder);
			targetsComparator.setColumn(column);
		}
	}

	public void setEditingSupport() {

		editingSupport = true;
		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			if(label.equals(TargetsLabelProvider.VERIFIED)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.NAME)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.CAS)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.COMMENTS)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.FORMULA)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.SMILES)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.INCHI)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.INCHI_KEY)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.CONTRIBUTOR)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			} else if(label.equals(TargetsLabelProvider.REFERENCE_ID)) {
				tableViewerColumn.setEditingSupport(new TargetsEditingSupport(this, label));
			}
		}
	}

	private void setCellColorProvider() {

		setColorProviderRetentionTime();
		setColorProviderRetentionIndex();
	}

	private void setColorProviderRetentionTime() {

		TableViewerColumn tableViewerColumn = getTableViewerColumn(TargetsLabelProvider.RETENTION_TIME);
		if(tableViewerColumn != null) {
			tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {

					if(cell != null) {
						Object element = cell.getElement();
						if(element instanceof IIdentificationTarget identificationTarget) {
							ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
							int retentionTimeTarget = libraryInformation.getRetentionTime();

							if(retentionTime != null && retentionTimeTarget != 0) {
								IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
								boolean useAbsoluteDeviation = preferenceStore.getBoolean(PreferenceSupplier.P_USE_ABSOLUTE_DEVIATION_RETENTION_TIME);
								double deviation;
								double deviationWarn;
								double deviationError;

								if(useAbsoluteDeviation) {
									deviation = Math.abs(retentionTime - retentionTimeTarget);
									deviationWarn = preferenceStore.getInt(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_ABS_OK);
									deviationError = preferenceStore.getInt(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_ABS_WARN);
								} else {
									deviation = (Math.abs(retentionTime - retentionTimeTarget) / retentionTimeTarget) * 100.0d;
									deviationWarn = preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_REL_OK);
									deviationError = preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_REL_WARN);
								}

								if(deviation < deviationWarn) {
									cell.setBackground(Colors.LIGHT_GREEN);
									cell.setForeground(Colors.BLACK);
								} else if(deviation < deviationError) {
									cell.setBackground(Colors.LIGHT_YELLOW);
									cell.setForeground(Colors.BLACK);
								} else {
									cell.setBackground(Colors.LIGHT_RED);
									cell.setForeground(Colors.BLACK);
								}
							}

							String text = TargetsLabelProvider.getRetentionTimeText(libraryInformation, retentionTime);
							cell.setText(text);
							super.update(cell);
						}
					}
				}
			});
		}
	}

	private void setColorProviderRetentionIndex() {

		TableViewerColumn tableViewerColumn = getTableViewerColumn(TargetsLabelProvider.RETENTION_INDEX);
		if(tableViewerColumn != null) {
			tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {

					if(cell != null) {
						Object element = cell.getElement();
						if(element instanceof IIdentificationTarget identificationTarget) {
							ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
							float retentionIndexTarget = libraryInformation.getRetentionIndex();

							if(retentionIndex != null && retentionIndexTarget != 0) {
								IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
								boolean useAbsoluteDeviation = preferenceStore.getBoolean(PreferenceSupplier.P_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX);
								double deviation;
								double deviationWarn;
								double deviationError;

								if(useAbsoluteDeviation) {
									deviation = Math.abs(retentionIndex - retentionIndexTarget);
									deviationWarn = preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_ABS_OK);
									deviationError = preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_ABS_WARN);
								} else {
									deviation = (Math.abs(retentionIndex - retentionIndexTarget) / retentionIndexTarget) * 100.0d;
									deviationWarn = preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_REL_OK);
									deviationError = preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_REL_WARN);
								}

								if(deviation < deviationWarn) {
									cell.setBackground(Colors.LIGHT_GREEN);
									cell.setForeground(Colors.BLACK);
								} else if(deviation < deviationError) {
									cell.setBackground(Colors.LIGHT_YELLOW);
									cell.setForeground(Colors.BLACK);
								} else {
									cell.setBackground(Colors.LIGHT_RED);
									cell.setForeground(Colors.BLACK);
								}
							}

							String text = TargetsLabelProvider.getRetentionIndexText(libraryInformation, retentionIndex);
							cell.setText(text);
							super.update(cell);
						}
					}
				}
			});
		}
	}

	private void createDragAndDropProvider() {

		DragSource dragSource = new DragSource(this.getTable(), DND.DROP_COPY);
		Transfer[] transfer = new Transfer[]{IdentificationTargetTransfer.getInstance()};
		dragSource.setTransfer(transfer);

		dragSource.addDragListener(new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {

				Object object = getStructuredSelection().getFirstElement();
				event.doit = (object instanceof IIdentificationTarget);
			}

			@Override
			public void dragSetData(DragSourceEvent event) {

				/*
				 * Identification Target Transfer
				 */
				if(IdentificationTargetTransfer.getInstance().isSupportedType(event.dataType)) {
					Object object = getStructuredSelection().getFirstElement();
					if(object instanceof IIdentificationTarget identificationTarget) {
						event.data = identificationTarget;
					}
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {

				/*
				 * No action required.
				 */
			}
		});
	}
}
