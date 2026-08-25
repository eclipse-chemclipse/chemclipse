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

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.BlastOutput2;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Hit;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.HitDescr;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Hsp;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;

public abstract class AbstractNucleotideBLAST {

	public static void transferTargets(IChromatogramWSD chromatogram, BlastOutput2 blastOutput) {

		if(blastOutput == null) {
			return;
		}

		Report report = blastOutput.getReport().getReport();
		for(Hit hit : report.getResults().getResults().getSearch().getSearch().getHits().getHit()) {
			ILibraryInformation libraryInformation = new LibraryInformation();
			HitDescr description = hit.getDescription().getHitDescr().getFirst();
			libraryInformation.setName(description.getTitle());
			libraryInformation.setDatabase(report.getSearchTarget().getTarget().getDb());
			libraryInformation.setGenBankAccesion(description.getAccession());
			libraryInformation.setReferenceIdentifier(description.getId());
			libraryInformation.setTaxonomyIdentifierNCBI(description.getTaxid().intValue());
			for(Hsp hsp : hit.getHsps().getHsp()) {
				ComparisonResult comparisionResult = new ComparisonResult((float)hsp.getBitScore(), (float)hsp.getScore(), (float)hsp.getEvalue(), hsp.getIdentity().floatValue()); // TODO: wrong model
				IdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisionResult);
				identificationTarget.setIdentifier(report.getVersion());
				chromatogram.getTargets().add(identificationTarget);
			}
		}
	}
}
