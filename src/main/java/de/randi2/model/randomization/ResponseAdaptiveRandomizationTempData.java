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
package de.randi2.model.randomization;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Natalie Waskowzow
 *
 */

@Entity
@DiscriminatorValue("RESPONSE_ADAPTIVE")
@Data
@EqualsAndHashCode(callSuper=true)
public class ResponseAdaptiveRandomizationTempData extends
		AbstractRandomizationTempData {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 7079082707404044733L;
	
	@OneToMany
	@MapKeyColumn
	private Map<String, ResponseAdaptiveUrn> responseAdaptiveUrns = new HashMap<String, ResponseAdaptiveUrn>();
	 
	 
	public ResponseAdaptiveUrn getResponseAdaptiveUrn(String stratum) {
		return responseAdaptiveUrns.get(stratum);
	}

	public void setResponseAdaptiveUrn(String stratum, ResponseAdaptiveUrn currentUrn) {
		responseAdaptiveUrns.put(stratum, currentUrn);
	}

}
