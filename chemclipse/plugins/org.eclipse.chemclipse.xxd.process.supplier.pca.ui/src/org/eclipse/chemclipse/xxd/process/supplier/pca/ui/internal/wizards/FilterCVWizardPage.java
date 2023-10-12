/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - reduce compiler warnings
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards;

import org.eclipse.chemclipse.xxd.process.supplier.pca.core.filters.AbstractFilter;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.filters.CVFilter;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.filters.IFilter.DataTypeProcessing;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FilterCVWizardPage extends WizardPage implements IFilterWizardPage {

	private final DataBindingContext dataBindingContext = new DataBindingContext();
	//
	private IObservableValue<Double> observeAlfa;
	private IObservableValue<DataTypeProcessing> dataTypeFiltration;

	protected FilterCVWizardPage(CVFilter cvFilter) {

		super("CV filter");
		setTitle("Coefficient of Variation Filter for Noise Reduction");
		setDescription("CV filter works just with selected samples, which are in group (contains group name)");
		observeAlfa = PojoProperties.value(CVFilter.class, "alpha", Double.class).observe(cvFilter);
		dataTypeFiltration = PojoProperties.value(AbstractFilter.class, "dataTypeProcessing", DataTypeProcessing.class).observe(cvFilter);
	}

	@Override
	public void createControl(Composite parent) {

		WizardPageSupport.create(this, dataBindingContext);
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(1, true));
		WizardPageSupport.create(this, dataBindingContext);
		//
		Label label = new Label(composite, SWT.None);
		label.setText("Select data type filtration");
		SelectObservableValue<DataTypeProcessing> selectedRadioButtonObservableProcessData = new SelectObservableValue<>();
		Button button = new Button(composite, SWT.RADIO);
		button.setText("Use on raw data");
		selectedRadioButtonObservableProcessData.addOption(DataTypeProcessing.RAW_DATA, WidgetProperties.buttonSelection().observe(button));
		button = new Button(composite, SWT.RADIO);
		button.setText("Use on modified data");
		selectedRadioButtonObservableProcessData.addOption(DataTypeProcessing.MODIFIED_DATA, WidgetProperties.buttonSelection().observe(button));
		dataBindingContext.bindValue(selectedRadioButtonObservableProcessData, dataTypeFiltration, new UpdateValueStrategy<>(UpdateValueStrategy.POLICY_CONVERT), null);
		//
		label = new Label(composite, SWT.None);
		label.setText("Select a row in a data table whose noise for each group is less than the value (in %)");
		GridDataFactory.fillDefaults().grab(true, false).applyTo(label);
		Text text = new Text(composite, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
		ISWTObservableValue<String> targetObservableValue = WidgetProperties.text(SWT.Modify).observe(text);
		UpdateValueStrategy<String, Double> targetToModel = new UpdateValueStrategy<>(UpdateValueStrategy.POLICY_CONVERT);
		targetToModel.setConverter(IConverter.create(String.class, Double.class, o1 -> {
			try {
				return Double.parseDouble((String)o1) / 100.0;
			} catch(NumberFormatException e) {
			}
			return null;
		}));
		targetToModel.setAfterConvertValidator(o1 -> {
			if(o1 instanceof Double) {
				Double d = (Double)o1;
				if(d <= 1 && d >= 0) {
					return ValidationStatus.ok();
				}
			}
			return ValidationStatus.error("Warning The value must be between 0 and 100");
		});
		UpdateValueStrategy<Double, String> modelToTarget = UpdateValueStrategy.create(IConverter.create(Double.class, String.class, o1 -> Double.toString(((Double)o1) * 100.0)));
		dataBindingContext.bindValue(targetObservableValue, observeAlfa, targetToModel, modelToTarget);
		setControl(composite);
	}

	@Override
	public void update() {

		dataBindingContext.updateModels();
	}
}