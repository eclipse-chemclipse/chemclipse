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
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.methods.IProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntry;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.methods.MethodExportConverter;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.methods.MethodImportConverter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.Test;

public class MethodReaderWriterTest {

	private static final String FIRST_STREAM_SUPPORT_FORMAT = "Format_v0.0.3.ocm";
	private static final String[] FORMAT_FILES = {"Format_v0.0.1.ocm", "Format_v0.0.2.ocm", FIRST_STREAM_SUPPORT_FORMAT};
	MethodImportConverter converter = new MethodImportConverter();
	MethodExportConverter exportConverter = new MethodExportConverter();

	@Test
	public void testRead() {

		boolean stream = false;
		for(String filename : FORMAT_FILES) {
			File file = new File("testData/files/methods/" + filename);
			if(FIRST_STREAM_SUPPORT_FORMAT.equals(filename)) {
				stream = true;
			}
			IProcessMethod result1 = checkRead(file, false);
			if(stream) {
				assertTrue(result1.contentEquals(checkRead(file, true), true), "result read from file differs from stream!");
			}
		}
	}

	private IProcessMethod checkRead(File file, boolean withStream) {

		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
			IProcessingInfo<IProcessMethod> result = withStream ? converter.readFrom(stream, file.getName(), new NullProgressMonitor()) : converter.convert(file, new NullProgressMonitor());
			checkResult(file.getAbsolutePath(), result, withStream ? "stream api" : "file api");
			return result.getProcessingResult();
		} catch(IOException e) {
			throw new AssertionError(e);
		}
	}

	private void checkResult(String filename, IProcessingInfo<IProcessMethod> convert, String context) {

		assertNotNull(convert, "[" + context + "] IProcessingInfo was null for " + filename);
		assertFalse(convert.hasErrorMessages(), "[" + context + "] has errors (" + convert.getMessages() + ")");
		assertNotNull(convert.getProcessingResult(), "[" + context + "] IProcessingInfo#getProcessingResult() for " + filename + " was null");
	}

	@Test
	public void testReadWriteLatest() throws IOException {

		File tempFile = File.createTempFile("test", ".ocm");
		tempFile.deleteOnExit();
		ProcessingInfo<Object> messages = new ProcessingInfo<>(new Object());
		ProcessMethod method = new ProcessMethod(ProcessMethod.CHROMATOGRAPHY);
		method.setSourceFile(tempFile);
		method.setOperator("Test-Operator");
		method.getMetaData().put("Meta", "Value");
		method.addProcessEntry(createEntry(method, "main1"));
		method.addProcessEntry(createEntryWithChilds(method, "main.withchilds"));
		method.addProcessEntry(createEntry(method, "main1"));
		exportConverter.convert(tempFile, method, messages, null);
		ProcessMethod read = (ProcessMethod)checkRead(tempFile, false);
		assertTrue(read.contentEquals(checkRead(tempFile, true), true), "result read from file differs from stream!");
		assertNotEquals(System.identityHashCode(method), System.identityHashCode(read), "not different objects!");
		assertEquals(method.getUUID(), read.getUUID());
		assertEquals(method.getName(), read.getSourceFile().getName());
		assertEquals(method.getOperator(), read.getOperator());
		assertEquals(method.getNumberOfEntries(), read.getNumberOfEntries(), "number of entries mismatched");
		ProcessEntry subEntry = (ProcessEntry)read.getEntries().get(1);
		assertEquals(subEntry.getEntries().size(), 2);
	}

	@Test
	public void testNestedMethodRead() throws IOException {

		File file = new File("testData/files/methods/" + "ChromIdentMethod.ocm");
		IProcessingInfo<IProcessMethod> convert = converter.convert(file, new NullProgressMonitor());
		checkResult("ChromIdentMethod.ocm", convert, "nested");
		IProcessMethod result = convert.getProcessingResult();
		assertEquals(3, result.getNumberOfEntries());
		IProcessEntryContainer next = result.iterator().next();
		assertEquals(1, next.getNumberOfEntries());
	}

	private ProcessEntry createEntryWithChilds(ProcessMethod method, String id) {

		ProcessEntry createEntry = createEntry(method, id);
		createEntry.addProcessEntry(createEntry(createEntry, "child1"));
		createEntry.addProcessEntry(createEntry(createEntry, "child2"));
		return createEntry;
	}

	private ProcessEntry createEntry(IProcessEntryContainer method, String id) {

		ProcessEntry entry = new ProcessEntry(method);
		entry.setProcessorId(id);
		return entry;
	}
}
