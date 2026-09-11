/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - correct typos
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.segments;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class AnalysisSegmentColorScheme {

	public enum Type {
		SEGMENT_EVEN, //
		SEGMENT_ODD, //
		LINE, SELECTION; //
	}

	/**
	 * CHROMATOGRAM = Neutral Brown
	 * ANALYSIS = Green Scheme
	 * NOISE = Pink Scheme
	 */
	public static final AnalysisSegmentColorScheme CHROMATOGRAM = new AnalysisSegmentColorScheme(new RGB(200, 190, 183), new RGB(227, 222, 219), new RGB(172, 157, 147), new RGB(145, 124, 111));
	public static final AnalysisSegmentColorScheme ANALYSIS = new AnalysisSegmentColorScheme(new RGB(215, 244, 227), new RGB(175, 233, 198), new RGB(135, 222, 170), new RGB(95, 211, 141));
	public static final AnalysisSegmentColorScheme NOISE = new AnalysisSegmentColorScheme(new RGB(246, 213, 255), new RGB(238, 170, 255), new RGB(229, 128, 255), new RGB(170, 0, 212));

	private final RGB[] rgb;

	private AnalysisSegmentColorScheme(RGB... colours) {

		this.rgb = colours;
	}

	public AnalysisSegmentColors create() {

		return new AnalysisSegmentColors();
	}

	public final class AnalysisSegmentColors {

		private final Color[] colors;

		public AnalysisSegmentColors() {

			colors = new Color[rgb.length];
			for(int i = 0; i < rgb.length; i++) {
				colors[i] = new Color(rgb[i]);
			}
		}

		public Color get(Type type) {

			return colors[type.ordinal()];
		}
	}
}