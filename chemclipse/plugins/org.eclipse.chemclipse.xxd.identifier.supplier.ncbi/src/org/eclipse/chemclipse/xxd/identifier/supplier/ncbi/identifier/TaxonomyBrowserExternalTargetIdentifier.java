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
package org.eclipse.chemclipse.xxd.identifier.supplier.ncbi.identifier;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.identifier.targets.ITargetIdentifierSupplier;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

public class TaxonomyBrowserExternalTargetIdentifier implements ITargetIdentifierSupplier {

	private static final Logger logger = Logger.getLogger(TaxonomyBrowserExternalTargetIdentifier.class);
	private static final String PREFIX = "https://www.ncbi.nlm.nih.gov/datasets/taxonomy/browser/?taxon=";

	@Override
	public String getId() {

		return "org.eclipse.chemclipse.xxd.identifier.supplier.ncbi.taxonomy.identifier";
	}

	@Override
	public String getDescription() {

		return "Click to open the corresponding NCBI Taxonomy entry in a web browser.";
	}

	@Override
	public String getIdentifierName() {

		return "NCBI Taxonomy Browser";
	}

	@Override
	public Class<? extends IIdentifierSettings> getSettingsClass() {

		return null;
	}

	@Override
	public URL getURL(ILibraryInformation libraryInformation) {

		URL url = null;
		try {
			url = new URI(PREFIX + libraryInformation.getTaxonomyIdentifierNCBI()).toURL();
		} catch(MalformedURLException e) {
			logger.warn(e);
		} catch(URISyntaxException e) {
			logger.warn(e);
		}
		return url;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return null;
	}
}
