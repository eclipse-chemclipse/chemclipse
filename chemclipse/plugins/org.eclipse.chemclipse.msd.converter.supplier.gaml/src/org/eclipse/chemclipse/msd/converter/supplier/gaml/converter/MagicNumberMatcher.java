/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.gaml.converter;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;
import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.io.IConstants;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.GAML;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Technique;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Trace;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MagicNumberMatcher extends AbstractMagicNumberMatcher implements IMagicNumberMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean isValidFormat = false;
		if(!file.exists()) {
			return isValidFormat;
		}
		if(!checkFileExtension(file, ".gaml")) {
			return isValidFormat;
		}
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(IConstants.NODE_GAML);
			//
			JAXBContext jaxbContext = JAXBContext.newInstance(IConstants.CONTEXT_PATH_V_120);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			GAML gaml = (GAML)unmarshaller.unmarshal(nodeList.item(0));
			boolean chromatography = false;
			boolean msdetector = false;
			for(Trace trace : gaml.getExperiment().get(0).getTrace()) {
				if(trace.getTechnique() == Technique.CHROM) {
					chromatography = true;
				} else if(trace.getTechnique() == Technique.MS) {
					msdetector = true;
				}
			}
			if(chromatography && msdetector)
				isValidFormat = true;
		} catch(Exception e) {
			// fail silently
		}
		return isValidFormat;
	}
}
