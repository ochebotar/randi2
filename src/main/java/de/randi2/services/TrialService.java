/* 
 * (c) 2008- RANDI2 Core Development Team
 * 
 * This file is part of RANDI2.
 * 
 * RANDI2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * RANDI2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * RANDI2. If not, see <http://www.gnu.org/licenses/>.
 */
package de.randi2.services;

import de.randi2.model.Trial;
import de.randi2.model.TrialSubject;

/**
 * All Trial-relevant services.
 * 
 * @author Lukasz Plotnicki <lp@randi2.de>
 */
public interface TrialService extends AbstractService<Trial> {

	/**
	 * Stores a new trial object in the system.
	 * 
	 * @param newTrial
	 */
	public void create(Trial newTrial);

	/**
	 * Updates the trial object and returns the its newest version.
	 * 
	 * @param trial
	 * @return
	 */
	public Trial update(Trial trial);

	/**
	 * Add a new trial subject to the given trial and randomize it. The
	 * refreshed Trial object will be returned.
	 * 
	 * @param subject
	 * @return
	 */
	public Trial randomize(Trial trial, TrialSubject subject);

}
