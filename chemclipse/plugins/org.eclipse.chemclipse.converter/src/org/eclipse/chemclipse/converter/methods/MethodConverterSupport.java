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
 *******************************************************************************/
package org.eclipse.chemclipse.converter.methods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.support.util.FileUtil;

public class MethodConverterSupport implements IMethodConverterSupport {

	private List<ISupplier> suppliers;

	public MethodConverterSupport() {

		suppliers = new ArrayList<>();
	}

	@Override
	public void add(final ISupplier supplier) {

		suppliers.add(supplier);
	}

	@Override
	public String getConverterId(final int index) throws NoConverterAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areConvertersStored();
		/*
		 * Test if the index is out of range.
		 */
		if(index < 0 || index > suppliers.size() - 1) {
			throw new NoConverterAvailableException("There is no converter available.");
		}
		ISupplier supplier = suppliers.get(index);
		return supplier.getId();
	}

	@Override
	public String getConverterId(final String name) throws NoConverterAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areConvertersStored();
		/*
		 * Get the supplier by name.
		 */
		String id = "";
		breakloop:
		for(ISupplier supplier : suppliers) {
			if(supplier.getFilterName().equals(name)) {
				id = supplier.getId();
				break breakloop;
			}
		}
		/*
		 * If id is empty.
		 */
		if(id.equals("")) {
			throw new NoConverterAvailableException("There is no converter available.");
		}
		return id;
	}

	@Override
	public String[] getFilterExtensions() throws NoConverterAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areConvertersStored();
		ArrayList<String> extensions = new ArrayList<>();
		for(ISupplier supplier : suppliers) {
			extensions.add(supplier.getFileExtension());
		}
		return extensions.toArray(new String[extensions.size()]);
	}

	@Override
	public String[] getFilterNames() throws NoConverterAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areConvertersStored();
		/*
		 * If the ArrayList is not empty, return the registered chromatogram
		 * converter filter names.<br/>
		 */
		ArrayList<String> filterNames = new ArrayList<>();
		for(ISupplier supplier : suppliers) {
			filterNames.add(supplier.getFilterName());
		}
		return filterNames.toArray(new String[filterNames.size()]);
	}

	@Override
	public List<String> getAvailableConverterIds(final File file) throws NoConverterAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areConvertersStored();
		List<String> availableConverters = new ArrayList<>();
		String fileName = file.getName();
		for(ISupplier supplier : suppliers) {
			if(file.isDirectory()) {
				/*
				 * The method is stored in a directory. Enable to read directories
				 * whether they end with lower or upper case letters.
				 */
				String directoryExtension = supplier.getDirectoryExtension();
				if(directoryExtension == null || directoryExtension.equals("")) {
					continue;
				} else {
					if(fileName.endsWith(directoryExtension) || fileName.endsWith(directoryExtension.toLowerCase()) || fileName.endsWith(directoryExtension.toUpperCase())) {
						availableConverters.add(supplier.getId());
					}
				}
			} else if(FileUtil.fileHasExtension(file)) {
				/*
				 * Enable to read files whether they end with lower or upper
				 * case letters. Take care, files like *.cdf would cause no
				 * problem, as they could be *.cdf (lower case) or *.CDF (upper
				 * case).<br/> There are problems using files, e.g. *.ionXML. The
				 * name must exactly fit the file extension or must be all in
				 * lower case (*.ionxml) or in upper case (*.IonXML) notation.
				 */
				String fileExtension = supplier.getFileExtension();
				if(fileExtension == null || fileExtension.equals("")) {
					continue;
				} else {
					if(fileName.endsWith(fileExtension) || fileName.endsWith(fileExtension.toLowerCase()) || fileName.endsWith(fileExtension.toUpperCase())) {
						/*
						 * Normal handling
						 */
						availableConverters.add(supplier.getId());
					}
				}
			}
		}
		if(availableConverters.isEmpty()) {
			throw new NoConverterAvailableException("There is no converter available to process the file: " + file.toString());
		}
		return availableConverters;
	}

	@Override
	public List<ISupplier> getSupplier() {

		return suppliers;
	}

	@Override
	public ISupplier getSupplier(String id) throws NoConverterAvailableException {

		ISupplier instance = null;
		exitloop:
		for(ISupplier supplier : suppliers) {
			if(supplier.getId().equals(id)) {
				instance = supplier;
				break exitloop;
			}
		}
		/*
		 * Throw an exception if the requested converter is not available.
		 */
		if(instance == null) {
			throw new NoConverterAvailableException("There is no converter available with the given id: " + id + ".");
		}
		return instance;
	}

	/**
	 * Check if there are converters stored in the
	 * ArrayList<IChromatogramSupplier>.
	 * 
	 * @throws NoConverterAvailableException
	 */
	private void areConvertersStored() throws NoConverterAvailableException {

		if(suppliers.isEmpty()) {
			throw new NoConverterAvailableException();
		}
	}
}
