/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Target extends AbstractVariable implements ITarget {

	private String target = ""; //$NON-NLS-1$

	public static List<Target> create(Set<String> targets) {

		List<Target> targetList = new ArrayList<>();
		for(String target : targets) {
			targetList.add(new Target(target));
		}
		return targetList;
	}

	public Target(String target) {

		super();
		setTarget(target);
		setValue(target);
		setType(ITarget.TYPE);
		setSelected(true);
	}

	public Target(String target, String description) {

		this(target);
		setDescription(description);
	}

	@Override
	public int compareTo(IVariable o) {

		if(o instanceof ITarget target) {
			return this.target.compareTo(target.getTarget());
		}
		return 0;
	}

	@Override
	public String getTarget() {

		return target;
	}

	@Override
	public void setTarget(String target) {

		this.target = target;
	}
}
