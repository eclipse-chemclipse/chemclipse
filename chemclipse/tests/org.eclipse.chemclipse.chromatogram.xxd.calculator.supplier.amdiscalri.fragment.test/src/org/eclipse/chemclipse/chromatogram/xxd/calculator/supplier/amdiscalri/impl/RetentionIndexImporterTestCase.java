/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.PathResolver;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.RetentionIndexFileOption;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.RetentionIndexImporterSettings;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.junit.Ignore;

@Ignore

public class RetentionIndexImporterTestCase {

	public static final String CHROMATOGRAM_1 = "data/retentionindex/importer/01/chromatogram1.cdf";

	public IChromatogram getChromatogram(String relativePath) {

		IChromatogram chromatogram = new MyChromatogram();
		chromatogram.setFile(new File(PathResolver.getAbsolutePath(relativePath)));
		Chromatogram chromatogramReference = new Chromatogram();
		chromatogram.addReferencedChromatogram(chromatogramReference);

		return chromatogram;
	}

	public RetentionIndexImporterSettings getRetentionIndexImporterSettings() {

		RetentionIndexImporterSettings settings = new RetentionIndexImporterSettings();
		settings.setRetentionIndexFileOption(RetentionIndexFileOption.CAL);
		settings.setFileNamePattern("");
		settings.setCaseSensitive(true);
		settings.setProcessReferenceChromatograms(true);

		return settings;
	}

	public void apply(IChromatogram chromatogram, RetentionIndexImporterSettings settings) {

		RetentionIndexImporter retentionIndexImporter = new RetentionIndexImporter();
		retentionIndexImporter.apply(chromatogram, settings);
	}

	private class MyChromatogram extends AbstractChromatogram {

		private static final long serialVersionUID = 688258405302662443L;

		@Override
		public String getName() {

			/*
			 * Strip .cdf extension.
			 */
			String name = getFile().getName();
			return name.substring(0, name.length() - 4);
		}

		@Override
		public void fireUpdate(IChromatogramSelection chromatogramSelection) {

		}

		@Override
		protected String getNoiseCalculatorId() {

			return null;
		}

		@Override
		protected INoiseCalculator createNoiseCalculator(String id) {

			return null;
		}
	}
}
