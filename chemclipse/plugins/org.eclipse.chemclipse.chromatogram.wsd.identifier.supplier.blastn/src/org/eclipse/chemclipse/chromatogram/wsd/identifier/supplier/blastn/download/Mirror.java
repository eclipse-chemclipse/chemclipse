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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download;

import org.eclipse.chemclipse.support.text.ILabel;

public enum Mirror implements ILabel {

	NCBI("National Center for Biotechnology Information", "https://ftp.ncbi.nlm.nih.gov/blast/db/"), //
	FUNET("Finnish Academic and Research Network", "https://ftp.funet.fi/pub/mirrors/ftp.ncbi.nlm.nih.gov/blast/db/"), //
	AWS("Amazon Web Services", "https://s3.amazonaws.com/ncbi-blast-databases/", true), //
	GCS("Google Cloud Storage", "https://storage.googleapis.com/blast-db/", true); //

	private String label = "";
	private String url = "";
	private boolean cloud = false;

	private Mirror(String label, String url) {

		this(label, url, false);
	}

	/**
	 * Cloud buckets keep the decompressed database files in a dated folder named by latest-dir.
	 */
	private Mirror(String label, String url, boolean cloud) {

		this.label = label;
		this.url = url;
		this.cloud = cloud;
	}

	@Override
	public String label() {

		return label;
	}

	public String url() {

		return url;
	}

	public boolean isCloud() {

		return cloud;
	}

	/**
	 * Maps a file of the metadata to this mirror. FTP mirrors list ftp://ftp.ncbi.nlm.nih.gov/blast/db/file,
	 * cloud buckets s3://bucket/latest-dir/file or gs://bucket/latest-dir/file.
	 */
	public String resolve(String file) {

		if(cloud) {
			String path = file.substring(file.indexOf("://") + 3);
			return url + path.substring(path.indexOf('/') + 1);
		}
		return url + file.substring(file.lastIndexOf('/') + 1);
	}
}
