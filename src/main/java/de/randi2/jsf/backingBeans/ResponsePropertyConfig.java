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
package de.randi2.jsf.backingBeans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import lombok.Getter;
import lombok.Setter;

import de.randi2.jsf.wrappers.CriterionWrapper;
import de.randi2.model.criteria.AbstractCriterion;
import de.randi2.model.criteria.DichotomousCriterion;
import de.randi2.model.criteria.constraints.AbstractConstraint;

/**
 * @author Natalie Waskowzow
 *
 */

@ManagedBean(name = "responsePropertyConfig")
@SessionScoped
public class ResponsePropertyConfig extends AbstractSubjectProperty {
	
	@Getter
	@Setter
	private DichotomousCriterion responseCriterion = new DichotomousCriterion();
	
	@Override
	public void addCriterion(ActionEvent event) {
		try {
			if (criteria == null)
				getCriteria();
			criteria.add(new CriterionWrapper<String>(
					(AbstractCriterion<String, ?>) responseCriterion.getClass()
							.newInstance(), loginHandler.getChosenLocale()));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<CriterionWrapper<? extends Serializable>> getCriteria() {
		if (criteria == null)
			criteria = new ArrayList<CriterionWrapper<? extends Serializable>>();
		if (criteria.isEmpty() && trialHandler.isEditing()) {
			for (AbstractCriterion<? extends Serializable, ? extends AbstractConstraint<?>> c : trialHandler
					.getCurrentObject().getCriteria()) {
				criteria.add(new CriterionWrapper<Serializable>(
						(AbstractCriterion<Serializable, ?>) c, loginHandler
								.getChosenLocale()));
			}
		}
		return criteria;
	}
	
	@Override
	public boolean isCriteriaEmpty() {
		return this.getCriteria().isEmpty();
	}

}