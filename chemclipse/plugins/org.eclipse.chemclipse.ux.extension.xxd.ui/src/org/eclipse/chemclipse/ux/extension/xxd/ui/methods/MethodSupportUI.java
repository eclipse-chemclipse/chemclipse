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
 * Christoph Läubrich - make helper method public static to read all configured methods, set processinginfo to the results view
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.converter.methods.MethodConverter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.methods.IProcessEntry;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ListProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.eclipse.chemclipse.rcp.app.ui.console.MessageConsoleAppender;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.settings.UserManagement;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.swt.ui.components.IMethodListener;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.preferences.PreferenceSupplierConverter;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.SupplierEditorSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.IExtendedPartUI;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class MethodSupportUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(MethodSupportUI.class);
	//
	private AtomicReference<ComboViewer> methodsControl = new AtomicReference<>();
	private AtomicReference<Button> addControl = new AtomicReference<>();
	private AtomicReference<Button> editControl = new AtomicReference<>();
	private AtomicReference<Button> copyControl = new AtomicReference<>();
	private AtomicReference<Button> recordControl = new AtomicReference<>();
	private AtomicReference<Button> deleteControl = new AtomicReference<>();
	private AtomicReference<Button> directoryControl = new AtomicReference<>();
	private AtomicReference<Button> executeControl = new AtomicReference<>();
	//
	private EventHandler eventHandler = null;
	private IEventBroker eventBroker = Activator.getDefault().getEventBroker();
	private IMethodListener methodListener = null;
	private SupplierEditorSupport supplierEditorSupport = new SupplierEditorSupport(DataType.MTH, () -> Activator.getDefault().getEclipseContext());
	//
	private ProcessMethod processMethodMacroRecorder = null;

	public MethodSupportUI(Composite parent, int style) {

		super(parent, style);
		createControl();
		//
		eventHandler = createEventHandler();
		eventBroker.subscribe(IChemClipseEvents.TOPIC_METHOD_UPDATE, null, eventHandler, true);
	}

	@Override
	public void dispose() {

		if(eventHandler != null) {
			eventBroker.unsubscribe(eventHandler);
		}
		super.dispose();
	}

	/**
	 * May return null. If null, then no recording has been activated.
	 * 
	 * @return {@link ProcessMethod}
	 */
	public ProcessMethod getProcessMethodMacroRecorder() {

		return processMethodMacroRecorder;
	}

	public void setMethodListener(IMethodListener methodListener) {

		this.methodListener = methodListener;
	}

	public void updateInput() {

		updateMethods();
	}

	private EventHandler createEventHandler() {

		return new EventHandler() {

			@Override
			public void handleEvent(Event event) {

				updateInput();
			}
		};
	}

	private void createControl() {

		setLayout(new FillLayout());
		//
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gridLayout = new GridLayout(8, false);
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		composite.setLayout(gridLayout);
		//
		createComboMethod(composite);
		createButtonAddMethod(composite);
		createButtonEditMethod(composite);
		createButtonCopyMethod(composite);
		createButtonRecordMethod(composite);
		createButtonDeleteMethod(composite);
		createButtonMethodDirectory(composite);
		createButtonExecuteMethod(composite);
		//
		initialize();
	}

	private void initialize() {

		updateInput();
	}

	private void createComboMethod(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(new ListContentProvider());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IProcessMethod processMethod) {
					return processMethod.getName();
				}
				return null;
			}
		});
		//
		combo.setToolTipText("Select a process method.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof IProcessMethod processMethod) {
					PreferenceSupplierConverter.setSelectedMethodName(processMethod.getName());
				}
				enableWidgets();
			}
		});
		//
		methodsControl.set(comboViewer);
	}

	private void createButtonAddMethod(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Creates and adds a new method.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_METHOD_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Shell shell = e.display.getActiveShell();
				File directory = MethodConverter.getUserMethodDirectory();
				if(directory.exists()) {
					createNewMethod(shell, true);
				} else if(selectMethodDirectory(shell)) {
					createNewMethod(shell, true);
				} else {
					MessageDialog.openError(shell, ExtensionMessages.methodEditor, ExtensionMessages.selectMethodsDirectory);
				}
			}
		});
		//
		addControl.set(button);
	}

	private void createButtonEditMethod(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ExtensionMessages.editSelectedMethod);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_METHOD_EDIT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				File file = getFile(getProcessMethod());
				if(file != null) {
					MessageConsoleAppender.printLine(ExtensionMessages.editMethod + ": " + file.getAbsolutePath());
					openProcessMethodEditor(file);
				}
			}
		});
		//
		editControl.set(button);
	}

	private void createButtonCopyMethod(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Copy the selected method.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_METHOD_COPY, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IProcessMethod processMethod = getProcessMethod();
				if(processMethod != null) {
					/*
					 * Container
					 */
					if(processMethod instanceof ListProcessEntryContainer processEntryContainer) {
						if(processEntryContainer.isReadOnly()) {
							MessageDialog.openInformation(e.display.getActiveShell(), ExtensionMessages.copyMethod, ExtensionMessages.cantCopyMethodContainer);
							return;
						}
					}
					/*
					 * Single Method
					 */
					File fileSource = getFile(processMethod);
					if(fileSource != null && fileSource.exists()) {
						if(MessageDialog.openQuestion(e.display.getActiveShell(), ExtensionMessages.copyMethod, MessageFormat.format(ExtensionMessages.shallCopyMethod, fileSource.getName()))) {
							/*
							 * Process Method (Source)
							 */
							try (InputStream inputStreamSource = new FileInputStream(fileSource)) {
								IProcessingInfo<IProcessMethod> processingInfoSource = MethodConverter.load(inputStreamSource, fileSource.getAbsolutePath(), null);
								IProcessMethod processMethodSource = processingInfoSource.getProcessingResult();
								/*
								 * Process Method (Sink)
								 */
								File fileSink = createNewMethod(e.display.getActiveShell(), false);
								if(fileSink != null && fileSink.exists()) {
									try (InputStream inputStreamSink = new FileInputStream(fileSink)) {
										IProcessingInfo<IProcessMethod> processingInfoSink = MethodConverter.load(inputStreamSink, fileSource.getAbsolutePath(), null);
										if(processingInfoSink.getProcessingResult() instanceof ProcessMethod processMethodSink) {
											/*
											 * Copy the entries.
											 */
											for(IProcessEntry processEntry : processMethodSource) {
												processMethodSink.addProcessEntry(processEntry);
											}
											/*
											 * Save the new method.
											 */
											IProcessingInfo<?> processingInfo = MethodConverter.convert(fileSink, processMethodSink, MethodConverter.DEFAULT_METHOD_CONVERTER_ID, new NullProgressMonitor());
											if(!processingInfo.hasErrorMessages()) {
												/*
												 * Open the editor
												 */
												updateInput();
												MessageConsoleAppender.printLine("Copied Method: " + fileSink.getAbsolutePath());
												openProcessMethodEditor(fileSink);
											}
										}
									} catch(IOException e1) {
										logger.warn(e1);
									}
								}
							} catch(IOException e1) {
								logger.warn(e1);
							}
						}
						return;
					}
					//
					MessageDialog.openInformation(e.display.getActiveShell(), ExtensionMessages.copyMethod, ExtensionMessages.cantCopyMethodFilesystem);
				}
			}
		});
		//
		copyControl.set(button);
	}

	private void createButtonRecordMethod(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		button.setToolTipText(ExtensionMessages.tooltipRecordMethod);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_METHOD_RECORD, IApplicationImageProvider.SIZE_16x16));
		button.setSelection(processMethodMacroRecorder != null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(processMethodMacroRecorder == null) {
					/*
					 * Start Recording
					 */
					processMethodMacroRecorder = new ProcessMethod(new HashSet<>(Arrays.asList(DataCategory.CSD, DataCategory.MSD, DataCategory.VSD, DataCategory.WSD)));
					MessageDialog.openInformation(e.display.getActiveShell(), ExtensionMessages.processMethod, ExtensionMessages.recordMethodMessage);
				} else {
					try {
						/*
						 * Stop Recording
						 */
						processMethodMacroRecorder.setDescription(ExtensionMessages.descriptionRecordMethod);
						processMethodMacroRecorder.setOperator(UserManagement.getCurrentUser());
						processMethodMacroRecorder.setCategory(ExtensionMessages.process);
						processMethodMacroRecorder.setSupportResume(PreferenceSupplier.isCreateMethodEnableResume());
						//
						if(MethodFileSupport.saveProccessMethod(e.display.getActiveShell(), processMethodMacroRecorder)) {
							File file = processMethodMacroRecorder.getSourceFile();
							if(file != null && file.exists()) {
								MethodConverter.setUserMethodFile(file);
								openProcessMethodEditor(file);
								updateInput();
							}
						}
					} catch(NoConverterAvailableException e1) {
						logger.warn(e1);
					}
					processMethodMacroRecorder = null;
				}
			}
		});
		//
		recordControl.set(button);
	}

	private void createButtonDeleteMethod(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ExtensionMessages.deleteSelectedMethod);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_METHOD_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IProcessMethod processMethod = getProcessMethod();
				if(processMethod != null) {
					/*
					 * Check constraints
					 */
					if(processMethod instanceof ListProcessEntryContainer listProcessEntryContainer) {
						if(listProcessEntryContainer.isReadOnly()) {
							MessageDialog.openInformation(e.display.getActiveShell(), ExtensionMessages.deleteMethod, ExtensionMessages.cantDeleteMethodReadonly);
							return;
						}
					}
					//
					File file = getFile(processMethod);
					if(file != null && file.exists()) {
						if(MessageDialog.openQuestion(e.display.getActiveShell(), ExtensionMessages.deleteMethod, MessageFormat.format(ExtensionMessages.shallDeleteMethod, file.getName()))) {
							file.delete();
							PreferenceSupplierConverter.setSelectedMethodName("");
							updateInput();
						}
						return;
					}
					//
					MessageDialog.openInformation(e.display.getActiveShell(), ExtensionMessages.deleteMethod, ExtensionMessages.cantDeleteMethodFilesystem);
				}
			}
		});
		//
		deleteControl.set(button);
	}

	private void createButtonExecuteMethod(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ExtensionMessages.applyMethodSelectedChromatogram);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IProcessMethod processMethod = getProcessMethod();
				if(processMethod != null) {
					MethodSupport.runMethod(methodListener, processMethod, e.display.getActiveShell());
					UpdateNotifierUI.update(e.display, IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, "The process method has been applied.");
				}
			}
		});
		//
		executeControl.set(button);
	}

	private void createButtonMethodDirectory(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ExtensionMessages.selectMethodDirectory);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_FOLDER_OPENED, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectMethodDirectory(e.display.getActiveShell());
				updateInput();
			}
		});
		//
		directoryControl.set(button);
	}

	private boolean selectMethodDirectory(Shell shell) {

		DirectoryDialog directoryDialog = new DirectoryDialog(shell);
		directoryDialog.setText(ExtensionMessages.methodDirectory);
		directoryDialog.setFilterPath(MethodConverter.getUserMethodDirectory().getAbsolutePath());
		//
		String directoryPath = directoryDialog.open();
		if(directoryPath != null && !directoryPath.equals("")) {
			MethodConverter.setUserMethodDirectory(new File(directoryPath));
			return true;
		} else {
			return false;
		}
	}

	private File createNewMethod(Shell shell, boolean openEditor) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(ExtensionMessages.processMethod);
		fileDialog.setFileName(MethodConverter.FILE_NAME);
		fileDialog.setFilterExtensions(new String[]{MethodConverter.FILTER_EXTENSION});
		fileDialog.setFilterNames(new String[]{MethodConverter.FILTER_NAME});
		fileDialog.setFilterPath(MethodConverter.getUserMethodDirectory().getAbsolutePath());
		//
		File file = null;
		String filePath = fileDialog.open();
		if(filePath != null && !filePath.equals("")) {
			/*
			 * Select a file where the process method shall be stored.
			 */
			file = new File(filePath);
			ProcessMethod processMethod = new ProcessMethod(ProcessMethod.CHROMATOGRAPHY);
			processMethod.setOperator(UserManagement.getCurrentUser());
			processMethod.setDescription(ExtensionMessages.processMethod);
			//
			IProcessingInfo<?> processingInfo = MethodConverter.convert(file, processMethod, MethodConverter.DEFAULT_METHOD_CONVERTER_ID, new NullProgressMonitor());
			if(!processingInfo.hasErrorMessages()) {
				PreferenceSupplierConverter.setSelectedMethodName(file.getName());
				if(openEditor) {
					updateInput();
					MessageConsoleAppender.printLine("New Method: " + file.getAbsolutePath());
					openProcessMethodEditor(file);
				}
			} else {
				file = null;
			}
		}
		//
		return file;
	}

	private void updateMethods() {

		List<IProcessMethod> methods = new ArrayList<>(MethodConverter.getUserMethods());
		if(!methods.isEmpty() && !methodsControl.get().getCombo().isDisposed()) {
			/*
			 * Sort methods
			 */
			Collections.sort(methods, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
			methodsControl.get().setInput(methods);
			if(methodsControl.get().getCombo().getItemCount() > 0) {
				updateProcessMethodSelection();
			}
		} else {
			methodsControl.get().setInput(null);
		}
		//
		enableWidgets();
	}

	private void updateProcessMethodSelection() {

		String selectedMethodName = PreferenceSupplierConverter.getSelectedMethodName();
		Combo combo = methodsControl.get().getCombo();
		//
		exitloop:
		for(int i = 0; i < combo.getItemCount(); i++) {
			if(combo.getItem(i).equals(selectedMethodName)) {
				combo.select(i);
				break exitloop;
			}
		}
		//
		if(combo.getSelectionIndex() == -1) {
			combo.select(0);
			PreferenceSupplierConverter.setSelectedMethodName(combo.getItem(0));
		}
	}

	private void enableWidgets() {

		addControl.get().setEnabled(true);
		editControl.get().setEnabled(false);
		copyControl.get().setEnabled(false);
		deleteControl.get().setEnabled(false);
		executeControl.get().setEnabled(false);
		/*
		 * Always true
		 */
		copyControl.get().setEnabled(true);
		directoryControl.get().setEnabled(true);
		//
		IProcessMethod processMethod = getProcessMethod();
		if(processMethod != null) {
			boolean editable = getFile(processMethod) != null;
			editControl.get().setEnabled(editable);
			copyControl.get().setEnabled(editable);
			deleteControl.get().setEnabled(editable);
			executeControl.get().setEnabled(true);
		}
	}

	private IProcessMethod getProcessMethod() {

		Object object = methodsControl.get().getStructuredSelection().getFirstElement();
		if(object instanceof IProcessMethod processMethod) {
			return processMethod;
		}
		//
		return null;
	}

	private File getFile(IProcessMethod processMethod) {

		if(processMethod != null) {
			return processMethod.getSourceFile();
		}
		//
		return null;
	}

	private void openProcessMethodEditor(File file) {

		supplierEditorSupport.openEditor(file, false);
	}
}