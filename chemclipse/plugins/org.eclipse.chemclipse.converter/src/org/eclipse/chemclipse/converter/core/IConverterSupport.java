/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - simplify API, deprecate individual getters in favor to a new filter approach
 *******************************************************************************/
package org.eclipse.chemclipse.converter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.converter.methods.MethodConverter;
import org.eclipse.chemclipse.converter.support.FileExtensionCompiler;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.converter.ISupplier;

public interface IConverterSupport {

	public static final Predicate<ISupplier> EXPORT_SUPPLIER = supplier -> supplier.isExportable();
	public static final Predicate<ISupplier> IMPORT_SUPPLIER = supplier -> supplier.isImportable();
	public static final Predicate<ISupplier> ALL_SUPPLIER = _ -> true;

	/**
	 * Returns the filter extension which are actually registered at the
	 * chromatogram converter extension point.<br/>
	 * The filter extension are the specific chromatogram file extensions.
	 * Agilent has for example an filter extension (.D) which represents a
	 * chromatogram.
	 */
	default String[] getFilterExtensions(Predicate<? super ISupplier> filter) {

		List<String> extensions = new ArrayList<>();
		for(ISupplier supplier : getSupplier(filter)) {
			if(supplier.getDirectoryExtension().equals("")) {
				FileExtensionCompiler fileExtensionCompiler = new FileExtensionCompiler(supplier.getFileExtension(), true);
				extensions.add(fileExtensionCompiler.getCompiledFileExtension());
			} else {
				/*
				 * DirectoryExtension: Directory extension will return "*."
				 * otherwise directory could not be identified.
				 */
				extensions.add("*.");
			}
		}
		return extensions.toArray(new String[extensions.size()]);
	}

	/**
	 * Returns the filter names which are actually registered at the
	 * chromatogram converter extension point.<br/>
	 * The filter names are the specific chromatogram file names to be displayed
	 * for example in the SWT FileDialog. Agilent has for example an filter name
	 * "Agilent Chromatogram (.D)".
	 */
	default String[] getFilterNames(Predicate<? super ISupplier> filter) {

		ArrayList<String> filterNames = new ArrayList<>();
		for(ISupplier supplier : getSupplier(filter)) {
			filterNames.add(supplier.getFilterName());
		}
		return filterNames.toArray(new String[filterNames.size()]);
	}

	/**
	 * Returns the converter id "org.eclipse.chemclipse.msd.converter.supplier.agilent" available in the list defined by its name, e.g. "Agilent Chromatogram (*.D/DATA.MS)".
	 * If more converter with the given name "Agilent Chromatogram (*.D/DATA.MS)" are stored, the first match will be returned. If exportConverterOnly is true, only a converter
	 * that is able to export the file will be returned.
	 */
	default String getConverterId(String name, boolean exportConverterOnly) throws NoConverterAvailableException {

		Collection<ISupplier> supplier = getSupplier(s -> s.getFilterName().equals(name) && (!exportConverterOnly || s.isExportable()));
		if(supplier.isEmpty()) {
			throw new NoConverterAvailableException();
		} else {
			return supplier.iterator().next().getId();
		}
	}

	/**
	 * Returns an ArrayList with all available converter ids for the given file.<br/>
	 * If the file ends with "*.D" all converter ids which can convert
	 * directories ending with "*.D" will be returned.<br/>
	 * The same thing if the file is a file and not a directory.<br/>
	 * The header of {@link MethodConverter} lists some file format
	 * endings.
	 */
	default List<String> getAvailableConverterIds(File file) throws NoConverterAvailableException {

		List<ISupplier> suppliers = Converter.getSupplierForFile(file, getSupplier());
		if(suppliers.isEmpty()) {
			throw new NoConverterAvailableException();
		}

		ArrayList<String> list = new ArrayList<>();
		for(ISupplier supplier : suppliers) {
			list.add(supplier.getId());
		}

		return list;
	}

	/**
	 * Returns the list of all available suppliers
	 */
	default Collection<ISupplier> getSupplier(Predicate<? super ISupplier> filter) {

		List<ISupplier> list = new ArrayList<>();
		for(ISupplier supplier : getSupplier()) {
			if(filter.test(supplier)) {
				list.add(supplier);
			}
		}
		return list;
	}

	List<ISupplier> getSupplier();

	/**
	 * Returns the supplier with the given id.<br/>
	 * If no supplier with the given id is available, throw an exception.
	 */
	default ISupplier getSupplier(String id) throws NoConverterAvailableException {

		Collection<ISupplier> collection = getSupplier(supplier -> supplier.getId().equals(id));
		if(collection.isEmpty()) {
			throw new NoConverterAvailableException();
		} else {
			return collection.iterator().next();
		}
	}

	DataCategory getDataCategory();

	/**
	 *
	 * @return a name of this converter so a user can identify this among others
	 */
	String getName();

	/**
	 *
	 * @return an id that can be used to store a reference to this converter support
	 */
	default String getID() {

		return "ConverterSupport:" + getClass().getName();
	}
}