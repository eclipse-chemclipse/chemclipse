/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.converter.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.tsd.converter.Activator;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferenceSupplier implements IPreferenceSupplier {

	public static final int MIN_TRACE_MSD = 1;
	public static final int MAX_TRACE_MSD = 2000;
	public static final int MIN_TRACE_WSD = 200;
	public static final int MAX_TRACE_WSD = 800;
	//
	public static final String P_USE_ADAPTER_MSD = "useAdapterMSD";
	public static final boolean DEF_USE_ADAPTER_MSD = true;
	public static final String P_USE_ADAPTER_FIXED_RANGE_MSD = "useAdapterFixedRangeMSD";
	public static final boolean DEF_USE_ADAPTER_FIXED_RANGE_MSD = false;
	public static final String P_ADAPTER_MIN_TRACE_MSD = "adapterMinTraceMSD";
	public static final int DEF_ADAPTER_MIN_TRACE_MSD = 18;
	public static final String P_ADAPTER_MAX_TRACE_MSD = "adapterMaxTraceMSD";
	public static final int DEF_ADAPTER_MAX_TRACE_MSD = 600;
	//
	public static final String P_USE_ADAPTER_WSD = "useAdapterWSD";
	public static final boolean DEF_USE_ADAPTER_WSD = true;
	public static final String P_USE_ADAPTER_FIXED_RANGE_WSD = "useAdapterFixedRangeWSD";
	public static final boolean DEF_USE_ADAPTER_FIXED_RANGE_WSD = false;
	public static final String P_ADAPTER_MIN_TRACE_WSD = "adapterMinTraceWSD";
	public static final int DEF_ADAPTER_MIN_TRACE_WSD = 200;
	public static final String P_ADAPTER_MAX_TRACE_WSD = "adapterMaxTraceWSD";
	public static final int DEF_ADAPTER_MAX_TRACE_WSD = 600;
	//
	private static IPreferenceSupplier preferenceSupplier;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public IScopeContext getScopeContext() {

		return InstanceScope.INSTANCE;
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getContext().getBundle().getSymbolicName();
	}

	@Override
	public Map<String, String> getDefaultValues() {

		Map<String, String> defaultValues = new HashMap<String, String>();
		defaultValues.put(P_USE_ADAPTER_MSD, Boolean.toString(DEF_USE_ADAPTER_MSD));
		defaultValues.put(P_USE_ADAPTER_FIXED_RANGE_MSD, Boolean.toString(DEF_USE_ADAPTER_FIXED_RANGE_MSD));
		defaultValues.put(P_ADAPTER_MIN_TRACE_MSD, Integer.toString(DEF_ADAPTER_MIN_TRACE_MSD));
		defaultValues.put(P_ADAPTER_MAX_TRACE_MSD, Integer.toString(DEF_ADAPTER_MAX_TRACE_MSD));
		defaultValues.put(P_USE_ADAPTER_WSD, Boolean.toString(DEF_USE_ADAPTER_WSD));
		defaultValues.put(P_USE_ADAPTER_FIXED_RANGE_WSD, Boolean.toString(DEF_USE_ADAPTER_FIXED_RANGE_WSD));
		defaultValues.put(P_ADAPTER_MIN_TRACE_WSD, Integer.toString(DEF_ADAPTER_MIN_TRACE_WSD));
		defaultValues.put(P_ADAPTER_MAX_TRACE_WSD, Integer.toString(DEF_ADAPTER_MAX_TRACE_WSD));
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static boolean isUseAdapterMSD() {

		return INSTANCE().getBoolean(P_USE_ADAPTER_MSD, DEF_USE_ADAPTER_MSD);
	}

	public static boolean isUseAdapterFixedRangeMSD() {

		return INSTANCE().getBoolean(P_USE_ADAPTER_FIXED_RANGE_MSD, DEF_USE_ADAPTER_FIXED_RANGE_MSD);
	}

	public static int getAdapterMinTraceMSD() {

		return INSTANCE().getInteger(P_ADAPTER_MIN_TRACE_MSD, DEF_ADAPTER_MIN_TRACE_MSD);
	}

	public static int getAdapterMaxTraceMSD() {

		return INSTANCE().getInteger(P_ADAPTER_MAX_TRACE_MSD, DEF_ADAPTER_MAX_TRACE_MSD);
	}

	public static boolean isUseAdapterWSD() {

		return INSTANCE().getBoolean(P_USE_ADAPTER_WSD, DEF_USE_ADAPTER_WSD);
	}

	public static boolean isUseAdapterFixedRangeWSD() {

		return INSTANCE().getBoolean(P_USE_ADAPTER_FIXED_RANGE_WSD, DEF_USE_ADAPTER_FIXED_RANGE_WSD);
	}

	public static int getAdapterMinTraceWSD() {

		return INSTANCE().getInteger(P_ADAPTER_MIN_TRACE_WSD, DEF_ADAPTER_MIN_TRACE_WSD);
	}

	public static int getAdapterMaxTraceWSD() {

		return INSTANCE().getInteger(P_ADAPTER_MAX_TRACE_WSD, DEF_ADAPTER_MAX_TRACE_WSD);
	}
}