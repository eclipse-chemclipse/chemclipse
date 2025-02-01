/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - allow for data unloading
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.model.IDataUpdateListener;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;

import jakarta.annotation.PreDestroy;

public abstract class AbstractUpdater<T extends Composite> {

	private T control = null;
	private boolean initialUpdate = true;
	private String topic = "";
	//
	private DataUpdateSupport dataUpdateSupport;
	private IDataUpdateListener updateListener = new IDataUpdateListener() {

		@Override
		public void update(String topic, List<Object> objects) {

			updateSelection(objects, topic);
		}
	};

	/**
	 * Create the part with the given main topic.
	 * 
	 * @param parent
	 * @param topic
	 */
	protected AbstractUpdater(String topic, DataUpdateSupport dataUpdateSupport) {

		this(topic, null, dataUpdateSupport);
	}

	/**
	 * Create the part with the given main topic and the main control.
	 * 
	 * @param topic
	 * @param control
	 */
	protected AbstractUpdater(String topic, T control, DataUpdateSupport dataUpdateSupport) {

		this.topic = topic;
		this.dataUpdateSupport = dataUpdateSupport;
		dataUpdateSupport.add(updateListener);
		setControl(control);
		subscribeAdditionalTopics();
	}

	@Focus
	public void setFocus() {

		if(initialUpdate) {
			if(updateSelection(dataUpdateSupport.getUpdates(topic), topic)) {
				initialUpdate = false;
			}
		}
	}

	@PreDestroy
	protected void preDestroy() {

		dataUpdateSupport.remove(updateListener);
		UpdateNotifier.update(IChemClipseEvents.TOPIC_PART_CLOSED, getClass().getSimpleName());
	}

	protected void setControl(T control) {

		this.control = control;
	}

	/**
	 * Return the control of this part.
	 * This could be null if it has been not set yet.
	 * 
	 * @return T
	 */
	protected T getControl() {

		return control;
	}

	/**
	 * Overwrite, if additional topics shall be added.
	 */
	protected void subscribeAdditionalTopics() {

	}

	protected void subscribeAdditionalTopic(String topic, String property) {

		dataUpdateSupport.subscribe(topic, property);
	}

	protected void subscribeAdditionalTopic(String topic, String[] properties) {

		dataUpdateSupport.subscribe(topic, properties);
	}

	/**
	 * Implement to update the object of the given topic.
	 * If consuming the topic was successful, return true.
	 * 
	 * @param objects
	 * @param topic
	 * @return boolean
	 */
	protected abstract boolean updateData(List<Object> objects, String topic);

	/**
	 * Tidy up persisted data.
	 */
	protected void unloadData() {

		dataUpdateSupport.clearObjects();
	}

	/**
	 * Returns whether this topic shall be consumed or not.
	 * 
	 * @param topic
	 * @return boolean
	 */
	protected boolean isUpdateTopic(String topic) {

		return this.topic.equals(topic);
	}

	private boolean updateSelection(List<Object> objects, String topic) {

		/*
		 * Enforce an update in case of an editor close event.
		 */
		if(DataUpdateSupport.isVisible(control) || topic.matches(IChemClipseEvents.EDITOR_CLOSE_REGEX)) {
			if(isUpdateTopic(topic)) {
				return updateData(objects, topic);
			}
		}
		//
		return false;
	}
}
