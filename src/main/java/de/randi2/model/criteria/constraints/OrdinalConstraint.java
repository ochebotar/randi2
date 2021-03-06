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
package de.randi2.model.criteria.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import de.randi2.unsorted.ConstraintViolatedException;

@Entity
public class OrdinalConstraint extends AbstractConstraint<String> {

	private static final long serialVersionUID = 3642808577019112783L;

	
	protected OrdinalConstraint(){}
	
	public OrdinalConstraint(List<String> args)
			throws ConstraintViolatedException {
		super(args);
	}

	@ElementCollection
	@Getter @Setter
	public Set<String> expectedValues;


	@Override
	public void isValueCorrect(String _value) throws ConstraintViolatedException {
		if(!expectedValues.contains(_value)){
			throw new ConstraintViolatedException();
		}
		
	}

	@Override
	protected void configure(List<String> args)
			throws ConstraintViolatedException {
		if(args == null || args.size() <1 || args.get(0) == null){
			throw new ConstraintViolatedException();
		}
		this.expectedValues = new HashSet<String>(args);
		
	}
	
	@Override
	public String getUIName() {
		StringBuilder result = new StringBuilder();
		ArrayList<String> listOfExpectedValues = new ArrayList<String>(expectedValues);
		Collections.sort(listOfExpectedValues);
		for(String s: listOfExpectedValues){
			result.append(s + "|");
		}
		return result.toString();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1; 
		result = prime * result
				+ ((expectedValues == null) ? 0 : expectedValues.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdinalConstraint other = (OrdinalConstraint) obj;
		if (expectedValues == null) {
			if (other.expectedValues != null)
				return false;
		} else if (!expectedValues.equals(other.expectedValues))
			return false;
		return true;
	}
	
}
