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
package org.eclipse.chemclipse.msd.report.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.msd.report.exceptions.NoReportSupplierAvailableException;

public abstract class AbstractMassSpectraReportSupport implements IMassSpectraReportSupportSetter {

	private List<IMassSpectraReportSupplier> suppliers;

	protected AbstractMassSpectraReportSupport() {

		suppliers = new ArrayList<>();
	}

	@Override
	public void add(final IMassSpectraReportSupplier supplier) {

		suppliers.add(supplier);
	}

	@Override
	public String[] getReportExtensions() throws NoReportSupplierAvailableException {

		/*
		 * Test if the suppliers list is empty.
		 */
		areReportSuppliersStored();
		/*
		 * If the list is not empty, return the registered mass spectra
		 * report extensions.
		 */
		List<String> extensions = new ArrayList<>();
		for(IMassSpectraReportSupplier supplier : suppliers) {
			extensions.add(supplier.getFileExtension());
		}
		return extensions.toArray(new String[extensions.size()]);
	}

	@Override
	public String[] getFilterNames() throws NoReportSupplierAvailableException {

		/*
		 * Test if the suppliers list is empty.
		 */
		areReportSuppliersStored();
		/*
		 * If the list is not empty, return the registered mass spectra
		 * report names.
		 */
		List<String> filterNames = new ArrayList<>();
		for(IMassSpectraReportSupplier supplier : suppliers) {
			filterNames.add(supplier.getReportName());
		}
		return filterNames.toArray(new String[filterNames.size()]);
	}

	@Override
	public String getReportSupplierId(int index) throws NoReportSupplierAvailableException {

		/*
		 * Test if the suppliers list is empty.
		 */
		areReportSuppliersStored();
		/*
		 * Test if the index is out of range.
		 */
		if(index < 0 || index > suppliers.size() - 1) {
			throw new NoReportSupplierAvailableException("The index is out of range.");
		}
		IMassSpectraReportSupplier supplier = suppliers.get(index);
		return supplier.getId();
	}

	@Override
	public String getReportSupplierId(String name) throws NoReportSupplierAvailableException {

		/*
		 * Test if the suppliers list is empty.
		 */
		areReportSuppliersStored();
		/*
		 * Get the supplier by name.
		 */
		String id = "";
		for(IMassSpectraReportSupplier supplier : suppliers) {
			if(supplier.getReportName().equals(name)) {
				id = supplier.getId();
				break;
			}
		}
		/*
		 * If id is empty.
		 */
		if(id.isEmpty()) {
			throw new NoReportSupplierAvailableException("There is no mass spectra report generator available.");
		}
		return id;
	}

	@Override
	public List<IMassSpectraReportSupplier> getReportSupplier() {

		return suppliers;
	}

	@Override
	public IMassSpectraReportSupplier getReportSupplier(String id) throws NoReportSupplierAvailableException {

		IMassSpectraReportSupplier instance = null;
		for(IMassSpectraReportSupplier supplier : suppliers) {
			if(supplier.getId().equals(id)) {
				instance = supplier;
				break;
			}
		}
		/*
		 * Throw an exception if the requested generator is not available.
		 */
		if(instance == null) {
			throw new NoReportSupplierAvailableException("There is no mass spectra report generator available with the given id: " + id + ".");
		}
		return instance;
	}

	@Override
	public List<String> getAvailableProcessorIds() throws NoReportSupplierAvailableException {

		/*
		 * Test if the suppliers list is empty.
		 */
		areReportSuppliersStored();
		List<String> availableProcessors = new ArrayList<>();
		for(IMassSpectraReportSupplier supplier : suppliers) {
			availableProcessors.add(supplier.getId());
		}
		return availableProcessors;
	}

	/**
	 * Check if there are report generators stored in the list of
	 * {@link IMassSpectraReportSupplier}.
	 *
	 * @throws NoReportSupplierAvailableException
	 */
	private void areReportSuppliersStored() throws NoReportSupplierAvailableException {

		if(suppliers.isEmpty()) {
			throw new NoReportSupplierAvailableException();
		}
	}
}
