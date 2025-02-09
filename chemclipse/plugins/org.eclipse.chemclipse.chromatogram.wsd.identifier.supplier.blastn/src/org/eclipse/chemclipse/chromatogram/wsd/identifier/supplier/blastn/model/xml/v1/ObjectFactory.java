/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {

	}

	public BlastOutput createBlastOutput() {

		return new BlastOutput();
	}

	public Iteration createIteration() {

		return new Iteration();
	}

	public Hit createHit() {

		return new Hit();
	}

	public Param createBlastOutputParam() {

		return new Param();
	}

	public Iterations createBlastOutputIterations() {

		return new Iterations();
	}

	public Mbstat createBlastOutputMbstat() {

		return new Mbstat();
	}

	public Hits createIterationHits() {

		return new Hits();
	}

	public Stat createIterationStat() {

		return new Stat();
	}

	public Parameters createParameters() {

		return new Parameters();
	}

	public Statistics createStatistics() {

		return new Statistics();
	}

	public Hsps createHitHsps() {

		return new Hsps();
	}

	public Hsp createHsp() {

		return new Hsp();
	}
}
