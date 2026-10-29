/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.process.ui.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.l10n.ProcessingMessages;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.chemclipse.xxd.process.ui.menu.IMenuIcon;
import org.eclipse.chemclipse.xxd.process.ui.toolbar.Processor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.graphics.Image;

public class ProcessorSupport {

	private static final Logger logger = Logger.getLogger(ProcessorSupport.class);

	public static final String PROCESSOR_IMAGE_DEFAULT = IApplicationImage.IMAGE_EXECUTE_EXTENSION;
	/*
	 * The processors must not contain the following persistence delimiters.
	 * : can't be used as it could be part of the id. Regex keywords can be also
	 * not used, like $.
	 */
	private static final String VALUE_DELIMITER = "§";
	private static final String PROCESSOR_DELIMITER = "%";

	private static ValueParserSupport valueParseSupport = new ValueParserSupport();

	public static List<Processor> getActiveProcessors(Set<IProcessSupplier<?>> processSuppliers, String settings) {

		List<Processor> processors = new ArrayList<>();
		for(IProcessSupplier<?> processSupplier : processSuppliers) {
			processors.add(new Processor(processSupplier));
		}
		/*
		 * Fetch the processor list.
		 */
		if(settings != null && !settings.isEmpty()) {
			for(String processorSettings : settings.split(PROCESSOR_DELIMITER)) {
				String[] values = processorSettings.split(VALUE_DELIMITER);
				if(values != null) {
					/*
					 * Parse the settings
					 */
					String id = getString(values, 0, "");
					String imageFileName = getString(values, 1, "");
					boolean active = getBoolean(values, 2, true);
					int index = getInteger(values, 3, Processor.INDEX_NONE);
					/*
					 * Cleanup. This needs to be literally null for overrides to work.
					 */
					if(imageFileName.equals("null")) {
						imageFileName = null;
					}
					/*
					 * Transfer
					 */
					if(!id.isEmpty()) {
						Processor processor = getProcessor(processors, id);
						if(processor != null) {
							processor.setImageFileName(imageFileName);
							processor.setActive(active);
							processor.setIndex(index);
						}
					}
				}
			}
		}
		/*
		 * Sort by index
		 */
		return sortProcessorsByIndex(processors);
	}

	public static String getActiveProcessors(List<Processor> processors) {

		StringBuilder builder = new StringBuilder();

		if(processors != null) {
			List<Processor> activeProcessors = processors.stream().filter(Processor::isActive).toList();
			Iterator<Processor> iterator = activeProcessors.iterator();
			while(iterator.hasNext()) {
				Processor processor = iterator.next();
				if(isValid(processor)) {
					IProcessSupplier<?> processSupplier = processor.getProcessSupplier();

					builder.append(processSupplier.getId());
					builder.append(VALUE_DELIMITER);
					builder.append(processor.getImageFileName());
					builder.append(VALUE_DELIMITER);
					builder.append(processor.isActive());
					builder.append(VALUE_DELIMITER);
					builder.append(processor.getIndex());

					if(iterator.hasNext()) {
						builder.append(PROCESSOR_DELIMITER);
					}
				} else {
					logger.warn("The processor is null or contains at least one unvalid char: " + processor);
				}
			}
		}

		return builder.toString();
	}

	public static List<Processor> filterProcessors(List<Processor> processors, boolean active, boolean updateIndex) {

		/*
		 * Filter Inactive/Active
		 */
		List<Processor> processorsFiltered = processors.stream().filter(p -> (p.isActive() == active)).collect(Collectors.toList());
		if(active) {
			/*
			 * Update indices and sort on demand.
			 */
			if(updateIndex) {
				ProcessorSupport.updateProcessorIndices(processorsFiltered);
			}
			Collections.sort(processors, (p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		}

		return processorsFiltered;
	}

	/**
	 * Use the icon from the right-click menu that can be contributed by other plug-ins.
	 */
	public static Image getMenuIcon(IProcessSupplier<?> processSupplier) {

		// some process type suppliers add hard-coded prefixes
		return getMenuIcon(id -> processSupplier.getId().contains(id));
	}

	/**
	 * Use the icon contributed for exactly this supplier id, e.g. of a converter.
	 */
	public static Image getMenuIcon(String supplierId) {

		return getMenuIcon(supplierId::equals);
	}

	private static Image getMenuIcon(Predicate<String> matcher) {

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		for(IConfigurationElement element : registry.getConfigurationElementsFor(IMenuIcon.EXTENSION_POINT_ID)) {
			String id = element.getAttribute("id");
			if(id != null && matcher.test(id)) {
				return createMenuIcon(element);
			}
		}
		return null;
	}

	private static Image createMenuIcon(IConfigurationElement element) {

		String icon = element.getAttribute("icon");
		if(icon != null) {
			String bundleName = element.getContributor().getName();
			String key = bundleName + "/" + icon;
			ImageRegistry imageRegistry = JFaceResources.getImageRegistry();
			if(imageRegistry.getDescriptor(key) == null) {
				ResourceLocator.imageDescriptorFromBundle(bundleName, icon).ifPresent(descriptor -> imageRegistry.put(key, descriptor));
			}
			return imageRegistry.get(key);
		}

		try {
			if(element.createExecutableExtension("class") instanceof IMenuIcon menuIcon) {
				return menuIcon.getImage();
			}
		} catch(CoreException e) {
			logger.warn(e);
		}
		return null;
	}

	/**
	 * Provide fallback icons for the quick access toolbar.
	 */
	public static Image getDefaultIcon(IProcessSupplier<?> processSupplier) {

		String imageFileName = PROCESSOR_IMAGE_DEFAULT;

		if(processSupplier.getCategory().equals(ProcessingMessages.baselineDetector)) {
			imageFileName = IApplicationImage.IMAGE_BASELINE;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.chromatogramCalculator)) {
			imageFileName = IApplicationImage.IMAGE_CALCULATE;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.chromatogramClassifier)) {
			imageFileName = IApplicationImage.IMAGE_CLASSIFIER;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.chromatogramExport)) {
			imageFileName = IApplicationImage.IMAGE_SAVE;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.chromatogramFilter)) {
			imageFileName = IApplicationImage.IMAGE_CHROMATOGRAM;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.chromatogramIntegrator)) {
			imageFileName = IApplicationImage.IMAGE_CHROMATOGRAM_INTEGRATOR;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.chromatogramReports)) {
			imageFileName = IApplicationImage.IMAGE_CHROMATOGRAM_REPORT;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.combinedChromatogramPeakIntegrator)) {
			imageFileName = IApplicationImage.IMAGE_COMBINED_INTEGRATOR;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakDetector)) {
			imageFileName = IApplicationImage.IMAGE_PEAK_DETECTOR;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakExport)) {
			imageFileName = IApplicationImage.IMAGE_EXPORT;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakFilter)) {
			imageFileName = IApplicationImage.IMAGE_PEAKS;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakIdentifier)) {
			imageFileName = IApplicationImage.IMAGE_IDENTIFY_PEAKS;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakIntegrator)) {
			imageFileName = IApplicationImage.IMAGE_PEAK_INTEGRATOR;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakQuantifier)) {
			imageFileName = IApplicationImage.IMAGE_QUANTIFY_ALL_PEAKS;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.peakMassSpectrumFilter) || processSupplier.getCategory().equals(ProcessingMessages.scanMassSpectrumFilter)) {
			imageFileName = IApplicationImage.IMAGE_MASS_SPECTRUM;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.scanIdentifier)) {
			imageFileName = IApplicationImage.IMAGE_IDENTIFY_MASS_SPECTRUM;
		} else if(processSupplier.getCategory().equals(ProcessingMessages.system)) {
			imageFileName = IApplicationImage.IMAGE_PREFERENCES;
		}

		return ApplicationImageFactory.getInstance().getImage(imageFileName, IApplicationImageProvider.SIZE_16x16);
	}

	public static void switchProcessor(List<Processor> processors, Processor processorActive, boolean moveUp) {

		if(processors != null && processorActive != null) {
			/*
			 * Map active processor by index.
			 */
			TreeMap<Integer, Processor> processorMap = new TreeMap<>();
			int indexActive = -1;

			for(int i = 0; i < processors.size(); i++) {
				Processor processor = processors.get(i);
				if(processor.isActive()) {
					processorMap.put(i, processor);
					if(processor == processorActive) {
						indexActive = i;
					}
				}
			}

			if(indexActive >= 0) {
				Integer indexSwitch = moveUp ? processorMap.lowerKey(indexActive) : processorMap.higherKey(indexActive);
				if(indexSwitch != null) {
					Collections.swap(processors, indexActive, indexSwitch);
					updateProcessorIndices(processors);
				}
			}
		}
	}

	public static List<Processor> sortProcessorsByIndex(List<Processor> processors) {

		Collections.sort(processors, (p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		return processors;
	}

	private static Processor getProcessor(List<Processor> processors, String id) {

		for(Processor processor : processors) {
			if(processor.getProcessSupplier().getId().equals(id)) {
				return processor;
			}
		}

		return null;
	}

	/*
	 * Assign the processor indices.
	 */
	private static void updateProcessorIndices(List<Processor> processors) {

		for(int i = 0; i < processors.size(); i++) {
			Processor processor = processors.get(i);
			processor.setIndex(i);
		}
	}

	private static boolean isValid(Processor processor) {

		if(processor == null) {
			return false;
		}

		IProcessSupplier<?> processSupplier = processor.getProcessSupplier();
		String id = processSupplier.getId();
		if(id.contains(VALUE_DELIMITER) || id.contains(PROCESSOR_DELIMITER)) {
			return false;
		}

		return true;
	}

	private static String getString(String[] values, int index, String def) {

		return valueParseSupport.parseString(values, index, def);
	}

	private static boolean getBoolean(String[] values, int index, boolean def) {

		return valueParseSupport.parseBoolean(values, index, def);
	}

	private static int getInteger(String[] values, int index, int def) {

		return valueParseSupport.parseInteger(values, index, def);
	}
}
