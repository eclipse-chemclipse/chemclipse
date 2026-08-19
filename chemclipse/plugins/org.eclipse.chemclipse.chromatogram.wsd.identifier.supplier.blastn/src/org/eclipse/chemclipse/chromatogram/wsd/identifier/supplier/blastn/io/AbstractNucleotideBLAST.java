/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.BlastOutput;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hit;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hsp;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Iteration;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;

public abstract class AbstractNucleotideBLAST {

	public static void transferTargets(IChromatogramWSD chromatogram, BlastOutput blastOutput) {

		if(blastOutput == null) {
			return;
		}
		for(Iteration iteration : blastOutput.getIterations().getIteration()) {
			for(Hit hit : iteration.getHits().getHit()) {
				ILibraryInformation libraryInformation = new LibraryInformation();
				libraryInformation.setName(hit.getDef());
				libraryInformation.setDatabase(blastOutput.getDb());
				libraryInformation.setMiscellaneous(hit.getId());
				libraryInformation.setComments(hit.getAccession());
				for(Hsp hsp : hit.getHsps().getHsp()) {
					ComparisonResult comparisionResult = new ComparisonResult((float)hsp.getBitScore(), (float)hsp.getScore(), (float)hsp.getEvalue(), hsp.getIdentity().floatValue()); // TODO: wrong model
					IdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisionResult);
					identificationTarget.setIdentifier(blastOutput.getVersion());
					chromatogram.getTargets().add(identificationTarget);
				}
			}
		}
	}
}
