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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {

	}

	public BlastOutput2 createBlastOutput2() {

		return new BlastOutput2();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report createReport() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results createResults() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results();
	}

	public Iteration createIteration() {

		return new Iteration();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search createSearch() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search();
	}

	public Hit createHit() {

		return new Hit();
	}

	public BlastOutput2.Report createBlastOutput2Report() {

		return new BlastOutput2.Report();
	}

	public BlastOutput2.Error createBlastOutput2Error() {

		return new BlastOutput2.Error();
	}

	public BlastXML2 createBlastXML2() {

		return new BlastXML2();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report.SearchTarget createReportSearchTarget() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report.SearchTarget();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report.Params createReportParams() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report.Params();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report.Results createReportResults() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report.Results();
	}

	public Err createErr() {

		return new Err();
	}

	public Target createTarget() {

		return new Target();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results.Iterations createResultsIterations() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results.Iterations();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results.Search createResultsSearch() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results.Search();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results.Bl2Seq createResultsBl2Seq() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results.Bl2Seq();
	}

	public Iteration.Search createIterationSearch() {

		return new Iteration.Search();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.QueryMasking createSearchQueryMasking() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.QueryMasking();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.Hits createSearchHits() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.Hits();
	}

	public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.Stat createSearchStat() {

		return new org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.Stat();
	}

	public Parameters createParameters() {

		return new Parameters();
	}

	public Range createRange() {

		return new Range();
	}

	public Statistics createStatistics() {

		return new Statistics();
	}

	public HitDescr createHitDescr() {

		return new HitDescr();
	}

	public Hit.Description createHitDescription() {

		return new Hit.Description();
	}

	public Hit.Hsps createHitHsps() {

		return new Hit.Hsps();
	}

	public Hsp createHsp() {

		return new Hsp();
	}
}
