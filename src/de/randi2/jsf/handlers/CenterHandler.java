package de.randi2.jsf.handlers;

import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import de.randi2.dao.CenterDao;
import de.randi2.model.Center;
import de.randi2.model.Login;
import de.randi2.model.Person;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * <p>
 * This class cares about the center object or objects.
 * </p>
 * 
 * @author Lukasz Plotnicki <lplotni@users.sourceforge.net>
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
public class CenterHandler {

	@Autowired
	private CenterDao centerDao;
	
    /**
     * The current logged in user.
     */
    private Login currentUser = null;
    private Vector<Center> centers = null;
    private Center showedCenter = null;
    private boolean editable = false;
    private boolean creatingMode = false;
    
    
    private Vector<SelectItem> dummyCenters;
    private Vector<SelectItem> dummyMembers;

    public List<SelectItem> getDummyCenters() {
        if (dummyCenters == null) {
            dummyCenters = new Vector<SelectItem>(3);
            for (int i = 0; i < 2; i++) {
                Center temp = new Center();
                temp.setName("Testcenter " + i + 1);
                temp.setCity("Heidelberg");
                temp.setPostcode("69120");
                temp.setStreet("Im Neuenheimer Feld 1");
                dummyCenters.add(new SelectItem(i, temp.getName()));
            }
        }
        return dummyCenters;
    }

    public List<SelectItem> getDummyMembers() {
        if (dummyMembers == null) {
            dummyMembers = new Vector<SelectItem>(10);
            for (int i = 0; i < 9; i++) {
                dummyMembers.add(new SelectItem(i, "Memeber_" + i));
            }
        }
        return dummyMembers;
    }

    public Vector<Center> getCenters() {
        if (centers == null) {
            //Temp. solution
            centers = new Vector<Center>(25);
            Center tCenter;
            for (int i = 0; i < 20; i++) {
                tCenter = new Center();
                tCenter.setCity("Heidelberg");
                tCenter.setName("DKFZ" + i);
                tCenter.setPostcode("69120");
                tCenter.setStreet("Im Neuenheimer Feld 11");
                centers.add(tCenter);
            }
        }
        return centers;
    }

    public void setCenters(Vector<Center> centers) {
        this.centers = centers;
    }

    public Center getShowedCenter() {
        if (showedCenter == null) {
            showedCenter = this.getCurrentUser().getPerson().getCenter();
        }
        return showedCenter;
    }

    public void setShowedCenter(Center showedCenter) {
        if(showedCenter==null){
        	creatingMode = true;
        	this.showedCenter = new Center();
        	this.showedCenter.setContactPerson(new Person());
        }else{
        	creatingMode = false;
        	this.showedCenter = showedCenter;
        }
    }

    public Login getCurrentUser() {
        if (currentUser == null) {
            currentUser = ((LoginHandler) FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), "loginHandler")).getLogin();
        }
        return currentUser;
    }

    public void setCurrentUser(Login currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isEditable() {
        //TODO if the user has the right to edit the center properties this method should return true
        //Temporary I'll just look, if the current user is a member of this center - if it is so, then he can edit it
        //properties.
        if (this.getShowedCenter().equals(this.getCurrentUser().getPerson().getCenter())) {
            editable = true;
        } else {
            editable = false;
        }
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    public void saveCenter(){
    	centerDao.save(this.getShowedCenter());
    }

	public boolean isCreatingMode() {
		return creatingMode;
	}

	public void setCreatingMode(boolean creatingMode) {
		this.creatingMode = creatingMode;
	}
}