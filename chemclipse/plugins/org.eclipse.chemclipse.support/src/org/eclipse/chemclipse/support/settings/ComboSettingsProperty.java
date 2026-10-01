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
 * Jan Holy - initial API and implementation
 * Philip Wenig - editable combo support
 * Matthias Mailänder - required combo support
 *******************************************************************************/
package org.eclipse.chemclipse.support.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface ComboSettingsProperty {

	Class<? extends ComboSupplier<?>> value();

	boolean edit() default false;

	/**
	 * If set, an empty selection is reported as an error, hence the dialog can't be finished.
	 */
	boolean required() default false;

	public static interface ComboSupplier<T> {

		Collection<T> items();

		T fromString(String string);

		String asString(T item);
	}
}