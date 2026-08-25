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
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.VendorIon;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

public class ReaderProxy implements IReaderProxy {

	private static final Logger logger = Logger.getLogger(ReaderProxy.class);

	private Path spectraPeaksParquet;
	private Path spectraDataParquet;

	public ReaderProxy(Path spectraPeaksParquet, Path spectraDataParquet) {

		this.spectraPeaksParquet = spectraPeaksParquet;
		this.spectraDataParquet = spectraDataParquet;
	}

	@Override
	public void readMassSpectrum(IVendorScanProxy scanProxy, IProgressMonitor monitor) throws IOException {

		monitor.beginTask(ConverterMessages.importScan, IProgressMonitor.UNKNOWN);

		try {
			Class.forName("org.duckdb.DuckDBDriver");
		} catch(ClassNotFoundException e) {
			logger.error(e);
			return;
		}

		String sql = """
				    SELECT
				        struct_extract(point, 'spectrum_index') AS spectrum_index,
				        struct_extract(point, 'mz') AS mz,
				        struct_extract(point, 'intensity') AS intensity
				    FROM read_parquet(?)
				    WHERE struct_extract(point, 'spectrum_index') = ?
				""";

		try (Connection connection = DriverManager.getConnection("jdbc:duckdb:");
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(2, scanProxy.getScanNumber() - 1);

			if(PreferenceSupplier.isImportCentroidedSpectra()) {
				preparedStatement.setString(1, spectraPeaksParquet.toString());
			} else if(PreferenceSupplier.isImportProfileSpectra()) {
				preparedStatement.setString(1, spectraDataParquet.toString());
			}

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while(resultSet.next()) {
					double mz = resultSet.getDouble("mz");
					float intensity = resultSet.getFloat("intensity");
					if(mz > 0) {
						IVendorIon ion = new VendorIon(mz, intensity);
						scanProxy.addIon(ion);
					}
					monitor.worked(1);
				}
			}
		} catch(SQLException e) {
			logger.error(e);
		}

		monitor.done();
	}
}