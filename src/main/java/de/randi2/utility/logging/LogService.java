package de.randi2.utility.logging;

import java.util.List;

import de.randi2.model.AbstractDomainObject;

public interface LogService {

	
	public void logChange(String action, String username, AbstractDomainObject value);
	
	public void logGet(String action, String username);
	
	public List<String> getLogEntries();
	
	public List<String> getLogEntries(String username);
	
	public List<String> getLogEntries(Class<? extends AbstractDomainObject> clazz, int id);
}
