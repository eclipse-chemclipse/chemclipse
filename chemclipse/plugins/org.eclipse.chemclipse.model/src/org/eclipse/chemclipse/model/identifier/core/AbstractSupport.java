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
 * Jan Holy - implementation
 * Christoph Läubrich - add getsuppliers
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;

public abstract class AbstractSupport<S extends ISupplier> implements ISupport {

	private final List<S> suppliers;

	public AbstractSupport() {

		suppliers = new ArrayList<>();
	}

	public void add(final S supplier) {

		suppliers.add(supplier);
	}

	// ---------------------------------------------private methods
	/**
	 * Check if there are converters stored in the ArrayList<ISupplier>.
	 *
	 * @throws NoIdentifierAvailableException
	 */
	private void areIdentifiersStored() throws NoIdentifierAvailableException {

		if(suppliers.isEmpty()) {
			throw new NoIdentifierAvailableException();
		}
	}
	// ---------------------------------------------private methods

	@Override
	public List<String> getAvailableIdentifierIds() throws NoIdentifierAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areIdentifiersStored();
		List<String> availableIdentifiers = new ArrayList<>();
		for(S supplier : suppliers) {
			availableIdentifiers.add(supplier.getId());
		}
		if(availableIdentifiers.isEmpty()) {
			throw new NoIdentifierAvailableException("There is no appropriate identifier available."); //$NON-NLS-1$
		}
		return availableIdentifiers;
	}

	@Override
	public String getIdentifierId(final int index) throws NoIdentifierAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areIdentifiersStored();
		/*
		 * Test if the index is out of range.
		 */
		if(index < 0 || index > suppliers.size() - 1) {
			throw new NoIdentifierAvailableException("There is no identifier stored."); //$NON-NLS-1$
		}
		S supplier = suppliers.get(index);
		return supplier.getId();
	}

	@Override
	public String[] getIdentifierNames() throws NoIdentifierAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areIdentifiersStored();
		/*
		 * If the ArrayList is not empty, return the registered identifier
		 * names.<br/>
		 */
		ArrayList<String> identifierNames = new ArrayList<>();
		for(S supplier : suppliers) {
			identifierNames.add(supplier.getIdentifierName());
		}
		return identifierNames.toArray(new String[identifierNames.size()]);
	}

	protected S getSpecificIdentifierSupplier(String identifierId) throws NoIdentifierAvailableException {

		S identifierSupplier = null;
		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areIdentifiersStored();
		if(identifierId == null || identifierId.equals("")) { //$NON-NLS-1$
			throw new NoIdentifierAvailableException("There is no identifier supplier available with the following id: " + identifierId + "."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		endsearch:
		for(S supplier : suppliers) {
			if(supplier.getId().equals(identifierId)) {
				identifierSupplier = supplier;
				break endsearch;
			}
		}
		if(identifierSupplier == null) {
			throw new NoIdentifierAvailableException("There is no identifier supplier available with the following id: " + identifierId + "."); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return identifierSupplier;
		}
	}

	public Collection<S> getSuppliers() {

		return Collections.unmodifiableList(suppliers);
	}
}
