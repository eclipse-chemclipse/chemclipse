/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.locations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.util.UserLocationListUtil;

public class UserLocations {

	private static final Logger logger = Logger.getLogger(UserLocations.class);
	//
	public static final String DESCRIPTION = "User Locations";
	public static final String FILE_EXTENSION = ".ulo";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";
	//
	private final Map<String, UserLocation> userLocationMap = new HashMap<>();

	public UserLocations() {

	}

	public UserLocations(String userLocations) {

		load(userLocations);
	}

	public void addAll(Collection<UserLocation> userLocations) {

		for(UserLocation userLocation : userLocations) {
			add(userLocation);
		}
	}

	public void add(UserLocation userLocation) {

		userLocationMap.put(userLocation.getName(), userLocation);
	}

	public void remove(String name) {

		userLocationMap.remove(name);
	}

	public void remove(List<UserLocation> userLocations) {

		for(UserLocation userLocation : userLocations) {
			remove(userLocation);
		}
	}

	public void remove(UserLocation userLocation) {

		if(userLocation != null) {
			userLocationMap.remove(userLocation.getName());
		}
	}

	public UserLocation get(String name) {

		return userLocationMap.get(name);
	}

	public Set<String> keySet() {

		return userLocationMap.keySet();
	}

	public Collection<UserLocation> values() {

		return userLocationMap.values();
	}

	public void clear() {

		userLocationMap.clear();
	}

	public String extractUserLocation(UserLocation userLocation) {

		StringBuilder builder = new StringBuilder();
		extractUserLocation(userLocation, builder);
		return builder.toString();
	}

	public UserLocation extractUserLocation(String item) {

		UserLocation userLocation = null;
		//
		if(!"".equals(item)) {
			String[] values = item.split("\\" + UserLocationListUtil.SEPARATOR_ENTRY);
			String name = ((values.length > 0) ? values[0].trim() : "");
			String path = ((values.length > 1) ? values[1].trim() : "");
			userLocation = new UserLocation(name, path);
		}
		//
		return userLocation;
	}

	public void load(String userLocations) {

		loadSettings(userLocations);
	}

	public void loadDefault(String userLocations) {

		loadSettings(userLocations);
	}

	public String save() {

		StringBuilder builder = new StringBuilder();
		Iterator<UserLocation> iterator = values().iterator();
		while(iterator.hasNext()) {
			UserLocation userLocation = iterator.next();
			extractUserLocation(userLocation, builder);
			if(iterator.hasNext()) {
				builder.append(UserLocationListUtil.SEPARATOR_TOKEN);
			}
		}
		//
		return builder.toString().trim();
	}

	public void importItems(File file) {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = bufferedReader.readLine()) != null) {
				UserLocation userLocation = extractUserLocation(line);
				if(userLocation != null) {
					add(userLocation);
				}
			}
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	public boolean exportItems(File file) {

		try (PrintWriter printWriter = new PrintWriter(file)) {
			/*
			 * Sort the items.
			 */
			List<UserLocation> userLocations = new ArrayList<>(values());
			Collections.sort(userLocations, (r1, r2) -> r1.getName().compareTo(r2.getName()));
			//
			Iterator<UserLocation> iterator = userLocations.iterator();
			while(iterator.hasNext()) {
				StringBuilder builder = new StringBuilder();
				UserLocation userLocation = iterator.next();
				extractUserLocation(userLocation, builder);
				printWriter.println(builder.toString());
			}
			printWriter.flush();
			return true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
			return false;
		}
	}

	private void loadSettings(String timeRanges) {

		if(!"".equals(timeRanges)) {
			UserLocationListUtil userLocationListUtil = new UserLocationListUtil();
			String[] items = userLocationListUtil.parseString(timeRanges);
			if(items.length > 0) {
				for(String item : items) {
					UserLocation userLocation = extractUserLocation(item);
					if(userLocation != null) {
						add(userLocation);
					}
				}
			}
		}
	}

	private void extractUserLocation(UserLocation namedTrace, StringBuilder builder) {

		builder.append(namedTrace.getName());
		builder.append(" ");
		builder.append(UserLocationListUtil.SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(namedTrace.getPath());
	}
}