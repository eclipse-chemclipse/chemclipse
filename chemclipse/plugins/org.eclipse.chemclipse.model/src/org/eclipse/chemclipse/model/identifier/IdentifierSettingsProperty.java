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
package org.eclipse.chemclipse.model.identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface IdentifierSettingsProperty {

	/*
	 * @IdentifierSettingsProperty(value = "Number of Targets", defaultValue = "15", minValue = "1", maxValue = "30", identifierIds = {"net.a", "net.b", "net.c"}, identifierNames = {"A", "B", "C"})
	 */
	public final static String USE_DEFAULT_NAME = ""; //$NON-NLS-1$
	public final static int INDEX_UNKNOWN = -1;

	String value() default USE_DEFAULT_NAME;

	boolean required() default false;

	int index() default INDEX_UNKNOWN;

	String defaultValue() default "";

	Access access() default Access.AUTO;

	String minValue() default "";

	String maxValue() default "";

	String[] identifierIds() default {};

	String[] identifierNames() default {};

	public enum Access {
		AUTO, READ_ONLY, WRITE_ONLY, READ_WRITE;
	}
}
