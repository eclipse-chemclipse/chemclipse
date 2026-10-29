/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - adjust the x label
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.charts;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;

import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.eclipse.chemclipse.converter.support.FileExtensionCompiler;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.pcr.converter.core.PlateConverterPCR;
import org.eclipse.chemclipse.pcr.converter.support.IPlateConverterSupport;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.ux.extension.pcr.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.xxd.process.ui.support.ProcessorSupport;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class ChartPCR extends LineChart {

	private static final Logger logger = Logger.getLogger(ChartPCR.class);

	private IPlate plate;

	public ChartPCR() {

		super();
		initialize();
	}

	public ChartPCR(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	public void updatePlate(IPlate plate) {

		this.plate = plate;
	}

	private void initialize() {

		modify();
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setTitleVisible(false);
	}

	private void modify() {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setCreateMenu(true);
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(false);
		chartSettings.getRangeRestriction().setZeroX(false);
		chartSettings.getRangeRestriction().setZeroY(false);
		chartSettings.setEnableTooltips(true);

		setPrimaryAxisSet(chartSettings);
		setExportMenu(chartSettings);
		applySettings(chartSettings);
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(ExtensionMessages.cycle);
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0"), new DecimalFormatSymbols(Locale.ENGLISH))); //$NON-NLS-1$
		primaryAxisSettingsX.setPosition(Position.Primary);
		primaryAxisSettingsX.setVisible(true);

		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(ExtensionMessages.fluorescence);
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH))); //$NON-NLS-1$
	}

	private void setExportMenu(IChartSettings settings) {

		IPlateConverterSupport converterSupport = PlateConverterPCR.getPlateConverterSupport();
		Collection<ISupplier> exportSupplier = converterSupport.getSupplier(IConverterSupport.EXPORT_SUPPLIER);
		for(ISupplier supplier : exportSupplier) {
			settings.addMenuEntry(new IChartMenuEntry() {

				@Override
				public String getName() {

					return supplier.getFilterName();
				}

				@Override
				public String getToolTipText() {

					return supplier.getDescription();
				}

				@Override
				public Image getIcon() {

					return ProcessorSupport.getMenuIcon(supplier.getId());
				}

				@Override
				public String getCategory() {

					return ICategories.EXPORT;
				}

				@Override
				public void execute(Shell shell, ScrollableChart scrollableChart) {

					if(plate == null) {
						return;
					}
					FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
					fileDialog.setText(ExtensionMessages.pcrExport);
					fileDialog.setFileName(plate.getName());
					FileExtensionCompiler fileExtensionCompiler = new FileExtensionCompiler(supplier.getFileExtension(), false);
					fileDialog.setFilterExtensions(fileExtensionCompiler.getCompiledFileExtension());
					fileDialog.setFilterNames(supplier.getFilterName());
					String pathname = fileDialog.open();
					if(pathname != null) {
						File file = new File(pathname);
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
						try {
							dialog.run(true, true, monitor -> {

								IProcessingInfo<File> convert = PlateConverterPCR.convert(file, plate, supplier.getId(), monitor);
								ProcessingInfoPartSupport.getInstance().update(convert);
							});
						} catch(InvocationTargetException e) {
							IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
							processingInfo.addErrorMessage(ExtensionMessages.pcrExport, ExtensionMessages.exportFailed);
							logger.error(e.getCause());
							ProcessingInfoPartSupport.getInstance().update(processingInfo);
						} catch(InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				}
			});
		}
	}
}
