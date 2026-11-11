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
package org.eclipse.chemclipse.xxd.identifier.supplier.openchemlib.ui.support;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;

import com.actelion.research.chem.MolfileParser;
import com.actelion.research.chem.SmilesParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.coords.CoordinateInventor;

public class MoleculeParser {

	private static final Logger logger = Logger.getLogger(MoleculeParser.class);

	private MoleculeParser() {

	}

	/**
	 * Returns a molecule with 2D coordinates from the molfile or else the SMILES
	 * of the library information. May return null.
	 */
	public static StereoMolecule parse(ILibraryInformation libraryInformation) {

		if(libraryInformation == null) {
			return null;
		}

		StereoMolecule molecule = parseMolfile(libraryInformation.getMoleculeStructure());
		if(molecule == null) {
			molecule = parseSmiles(libraryInformation.getSmiles());
		}

		return molecule;
	}

	private static StereoMolecule parseMolfile(String molfile) {

		if(molfile == null || molfile.isBlank()) {
			return null;
		}

		try {
			StereoMolecule molecule = new StereoMolecule();
			if(new MolfileParser().parse(molecule, molfile) && molecule.getAllAtoms() > 0) {
				if(!hasPlanarCoordinates(molecule)) {
					new CoordinateInventor().invent(molecule);
				}
				return molecule;
			}
		} catch(RuntimeException e) {
			logger.warn(e);
		}

		return null;
	}

	private static StereoMolecule parseSmiles(String smiles) {

		if(smiles == null || smiles.isBlank()) {
			return null;
		}

		try {
			StereoMolecule molecule = new StereoMolecule();
			new SmilesParser().parse(molecule, smiles.trim());
			if(molecule.getAllAtoms() > 0) {
				return molecule;
			}
		} catch(Exception e) {
			logger.warn(e);
		}

		return null;
	}

	/*
	 * Molfiles may come without coordinates (all zero) or with 3D coordinates.
	 */
	private static boolean hasPlanarCoordinates(StereoMolecule molecule) {

		boolean hasCoordinates = false;
		for(int atom = 0; atom < molecule.getAllAtoms(); atom++) {
			if(molecule.getAtomZ(atom) != 0) {
				return false;
			}
			if(molecule.getAtomX(atom) != 0 || molecule.getAtomY(atom) != 0) {
				hasCoordinates = true;
			}
		}

		return hasCoordinates;
	}
}
