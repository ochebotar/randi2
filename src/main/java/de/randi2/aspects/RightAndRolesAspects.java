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
package de.randi2.aspects;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.randi2.dao.TrialSiteDao;
import de.randi2.model.AbstractDomainObject;
import de.randi2.model.Login;
import de.randi2.utility.security.RolesAndRights;

/**
 * Aspect to grant automatically the rights for new AbstracDomainObject and
 * User.
 * 
 * @author Daniel Schrimpf <dschrimpf@users.sourceforge.net>
 * 
 */
@Aspect
public class RightAndRolesAspects {

	private Logger logger = Logger.getLogger(RightAndRolesAspects.class);
	@Autowired
	private RolesAndRights roleAndRights;

	@Autowired
	private TrialSiteDao trialSiteDao;

	/**
	 * This around advice grant the rights for an new domain object and register
	 * a new user with his special rights. It matches all executions of save
	 * methods in the de.randi2.dao package.
	 * 
	 * @param pjp
	 *            the proceeding join point
	 * @throws Throwable
	 */
	@Around("execution(public void de.randi2.dao.*.create*(de.randi2.model.AbstractDomainObject))")
	@Transactional(propagation = Propagation.REQUIRED)
	public void afterSaveNewDomainObject(ProceedingJoinPoint pjp)
			throws Throwable {
		pjp.proceed();
		for (Object o : pjp.getArgs()) {
			// special case for self registration
			if (SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal().equals("anonymousUser")
					&& o instanceof Login) {
				SecurityContextHolder.getContext().setAuthentication(
						new AnonymousAuthenticationToken("anonymousUser", o,
								new ArrayList<GrantedAuthority>(((Login) o)
										.getAuthorities())));
			}
			Login login = (Login) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			//extra method for login objects is necessary, because of site - login relationship
			if (!(o instanceof Login)) {
				logger.debug("Register Object (" + o.getClass().getSimpleName()
						+ " id=" + ((AbstractDomainObject) o).getId() + ")");
				roleAndRights.grantRights(((AbstractDomainObject) o),
						trialSiteDao.get(login.getPerson()));
			}
		}

	}

	@Around("execution(public void de.randi2.services.*.register*(..))")
	@Transactional(propagation = Propagation.REQUIRED)
	public void afterRegisterNewUserObject(ProceedingJoinPoint pjp)
			throws Throwable {
		pjp.proceed();
		for (Object o : pjp.getArgs()) {
			if (o instanceof Login) {
				// special case for self registration
				if (SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal().equals("anonymousUser")
						&& o instanceof Login) {
					SecurityContextHolder.getContext().setAuthentication(
							new AnonymousAuthenticationToken("anonymousUser",
									o, new ArrayList<GrantedAuthority>(
											((Login) o).getAuthorities())));
				}
				Login login = (Login) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				logger.debug("Register Object (" + o.getClass().getSimpleName()
						+ " id=" + ((AbstractDomainObject) o).getId() + ")");
				roleAndRights.grantRights(((AbstractDomainObject) o),
						trialSiteDao.get(login.getPerson()));
				roleAndRights.registerPerson(((Login) o));
			}
		}

	}

	@Around("execution(public void de.randi2.services.UserService*.create*(..))")
	@Transactional(propagation = Propagation.REQUIRED)
	public void afterCreateNewUserObject(ProceedingJoinPoint pjp)
			throws Throwable {
		pjp.proceed();
		for (Object o : pjp.getArgs()) {
			if (o instanceof Login) {
				Login login = (Login) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				logger.debug("Register Object (" + o.getClass().getSimpleName()
						+ " id=" + ((AbstractDomainObject) o).getId() + ")");
				roleAndRights.grantRights(((AbstractDomainObject) o),
						trialSiteDao.get(login.getPerson()));
				roleAndRights.registerPerson(((Login) o));
			}
		}

	}

}
