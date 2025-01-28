/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - enable profiles
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.methods;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.chemclipse.processing.methods.IProcessEntry;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntry;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;

public class MethodReaderWriter_1003 extends ObjectStreamMethodFormat {

	public MethodReaderWriter_1003() {

		super(Format.METHOD_VERSION_0003);
	}

	@Override
	protected void writeObjectToStream(ObjectOutputStream stream, IProcessMethod processMethod, IMessageConsumer consumer, IProgressMonitor monitor) throws IOException {

		writeIterable(processMethod.getDataCategories(), stream, ObjectStreamMethodFormat::writeEnum);
		writeString(processMethod.getUUID(), stream);
		writeString(processMethod.getName(), stream);
		writeString(processMethod.getDescription(), stream);
		writeString(processMethod.getCategory(), stream);
		writeString(processMethod.getOperator(), stream);
		writeMap(processMethod.getMetaData(), stream, ObjectStreamMethodFormat::writeString, ObjectStreamMethodFormat::writeString);
		writeIterable(processMethod, stream, this::writeProcessEntry);
		stream.writeBoolean(processMethod.isFinal());
	}

	@Override
	protected IProcessMethod readObjectFromStream(ObjectInputStream stream, IMessageConsumer consumer, IProgressMonitor monitor) throws IOException, ClassNotFoundException {

		Set<DataCategory> categories = new LinkedHashSet<>();
		readItems(stream, deserializeDataType(DataCategory.AUTO_DETECT), categories::add);
		ProcessMethod processMethod = new ProcessMethod(categories);
		processMethod.setUUID(readString(stream));
		processMethod.setName(readString(stream));
		processMethod.setDescription(readString(stream));
		processMethod.setCategory(readString(stream));
		processMethod.setOperator(readString(stream));
		readMap(stream, ObjectStreamMethodFormat::readString, ObjectStreamMethodFormat::readString, processMethod.getMetaData()::put);
		readItems(stream, processEntryDeserialization(processMethod), processMethod.getEntries()::add);
		processMethod.setReadOnly(stream.readBoolean());
		return processMethod;
	}

	private void writeProcessEntry(IProcessEntry entry, ObjectOutputStream stream) throws IOException {

		writeString(entry.getProcessorId(), stream);
		writeString(entry.getName(), stream);
		writeString(entry.getDescription(), stream);
		writeString(entry.getSettings(), stream);
		writeIterable(entry.getDataCategories(), stream, ObjectStreamMethodFormat::writeEnum);
		// write child entries if there are any...
		ProcessEntryContainer container;
		if(entry instanceof ProcessEntryContainer) {
			container = entry;
		} else {
			container = null;
		}
		writeIterable(container, stream, this::writeProcessEntry);
		stream.writeBoolean(entry.isReadOnly());
	}

	private ObjectInputStreamDeserializer<IProcessEntry> processEntryDeserialization(ProcessEntryContainer parent) {

		return stream -> {
			ProcessEntry processEntry = new ProcessEntry(parent);
			processEntry.setProcessorId(readString(stream));
			processEntry.setName(readString(stream));
			processEntry.setDescription(readString(stream));
			processEntry.setSettings(readString(stream));
			readItems(stream, deserializeDataType(DataCategory.AUTO_DETECT), processEntry::addDataCategory);
			readItems(stream, processEntryDeserialization(processEntry), processEntry.getEntries()::add);
			processEntry.setReadOnly(stream.readBoolean());
			return processEntry;
		};
	}
}
