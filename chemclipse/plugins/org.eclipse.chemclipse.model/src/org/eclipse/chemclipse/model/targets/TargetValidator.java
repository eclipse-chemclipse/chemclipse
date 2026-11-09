/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.targets;

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;

public class TargetValidator implements IValidator<Object> {

	public static final String IDENTIFIER = "Manual"; //$NON-NLS-1$

	private static final String ERROR_TARGET = NLS.bind(ModelMessages.enterTargetExample, TargetListUtil.EXAMPLE);
	private static final String ERROR_TOKEN = NLS.bind(ModelMessages.targetMustNotContain, TargetListUtil.SEPARATOR_TOKEN);

	private String name = ""; //$NON-NLS-1$
	private String casNumber = ""; //$NON-NLS-1$
	private String comments = ""; //$NON-NLS-1$
	private String contributor = ""; //$NON-NLS-1$
	private String referenceId = ""; //$NON-NLS-1$

	@Override
	public IStatus validate(Object value) {

		/*
		 * Reset
		 */
		name = ""; //$NON-NLS-1$
		casNumber = ""; //$NON-NLS-1$
		comments = ""; //$NON-NLS-1$
		contributor = ""; //$NON-NLS-1$
		referenceId = ""; //$NON-NLS-1$

		String message = null;
		if(value == null) {
			message = ERROR_TARGET;
		} else {
			if(value instanceof String text) {
				text = text.trim();
				if(text.contains(TargetListUtil.SEPARATOR_TOKEN)) {
					message = ERROR_TOKEN;
				} else if("".equals(text.trim())) { //$NON-NLS-1$
					message = ERROR_TARGET;
				} else {
					/*
					 * Extract name, casNumber, ...
					 */
					String[] values = text.trim().split("\\" + TargetListUtil.SEPARATOR_ENTRY); // The pipe needs to be escaped. //$NON-NLS-1$
					if(values.length > 0) {
						name = values[0].trim();

						if(values.length > 1) {
							casNumber = values[1].trim();
						}

						if(values.length > 2) {
							comments = values[2].trim();
						}

						if(values.length > 3) {
							contributor = values[3].trim();
						}

						if(values.length > 4) {
							referenceId = values[4].trim();
						}
					} else {
						message = ERROR_TARGET;
					}
				}
			} else {
				message = ERROR_TARGET;
			}
		}

		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	public String getName() {

		return name;
	}

	public String getCasNumber() {

		return casNumber;
	}

	public String getComments() {

		return comments;
	}

	public String getContributor() {

		return contributor;
	}

	public String getReferenceId() {

		return referenceId;
	}

	public IIdentificationTarget getIdentificationTarget() {

		ILibraryInformation libraryInformation = new LibraryInformation();
		libraryInformation.setName(name);
		libraryInformation.setCasNumber(casNumber);
		libraryInformation.setComments(comments);
		libraryInformation.setContributor(contributor);
		libraryInformation.setReferenceIdentifier(referenceId);
		IComparisonResult comparisonResult = ComparisonResult.COMPARISON_RESULT_BEST_MATCH;
		IIdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisonResult);
		identificationTarget.setIdentifier(IDENTIFIER);

		return identificationTarget;
	}
}