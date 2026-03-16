/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.classification.ui.provider;

import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.xxd.classification.model.ClassificationRule;
import org.eclipse.jface.viewers.Viewer;

public class ClassificationRuleTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof ClassificationRule rule1 && e2 instanceof ClassificationRule rule2) {

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = rule2.getSearchExpression().compareTo(rule1.getSearchExpression());
					break;
				case 1:
					sortOrder = rule2.getClassification().compareTo(rule1.getClassification());
					break;
				case 2:
					sortOrder = rule2.getReference().compareTo(rule1.getReference());
					break;
			}

			if(getDirection() == ASCENDING) {
				sortOrder = -sortOrder;
			}
		}

		return sortOrder;
	}
}
