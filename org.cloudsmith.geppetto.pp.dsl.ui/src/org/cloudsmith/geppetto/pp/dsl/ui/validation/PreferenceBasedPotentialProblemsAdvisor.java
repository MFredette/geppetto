/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package org.cloudsmith.geppetto.pp.dsl.ui.validation;

import org.cloudsmith.geppetto.pp.dsl.ui.preferences.PPPreferencesHelper;
import org.cloudsmith.geppetto.pp.dsl.validation.IPotentialProblemsAdvisor;
import org.cloudsmith.geppetto.pp.dsl.validation.ValidationPreference;

import com.google.inject.Inject;

/**
 * A potential problems advisor based on preference settings.
 * Note, that the preferences are read when this advisor is instantiated. Get a new instance when a preference has changed.
 * 
 * 
 */
public class PreferenceBasedPotentialProblemsAdvisor implements IPotentialProblemsAdvisor {

	private PPPreferencesHelper preferences;

	@Inject
	public PreferenceBasedPotentialProblemsAdvisor(PPPreferencesHelper preferences) {
		this.preferences = preferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.geppetto.pp.dsl.validation.IPotentialProblemsAdvisor#circularDependencyPreference()
	 */
	@Override
	public ValidationPreference circularDependencyPreference() {
		return preferences.getcircularDependencyPreference();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.geppetto.pp.dsl.validation.IPotentialProblemsAdvisor#interpolatedNonBraceEnclosedHyphens()
	 */
	@Override
	public ValidationPreference interpolatedNonBraceEnclosedHyphens() {
		return preferences.getInterpolatedNonBraceEnclosedHypens();
	}

}