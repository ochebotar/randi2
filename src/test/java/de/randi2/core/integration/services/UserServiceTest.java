package de.randi2.core.integration.services;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import de.randi2.dao.LoginDao;
import de.randi2.dao.TrialSiteDao;
import de.randi2.model.Login;
import de.randi2.model.Person;
import de.randi2.model.Role;
import de.randi2.model.TrialSite;
import de.randi2.services.UserService;
import de.randi2.testUtility.utility.DomainObjectFactory;
import de.randi2.testUtility.utility.TestStringUtil;
@Transactional
public class UserServiceTest extends AbstractServiceTest{


	@Autowired private UserService userService;
	@Autowired private DomainObjectFactory factory;
	@Autowired private TestStringUtil stringUtil;
	@Autowired private ApplicationContext context;
	@Autowired private LoginDao loginDao;
	@Autowired private TrialSiteDao trialSiteDao;

	
	@Test
	public void testInit(){
		assertNotNull(userService);
	}
	

	//FIXME Create a test for the getAllRoles 
	
	@Test
	public void testAddRole(){
		authenticatAsAdmin();
		Login login = factory.getLogin();
		entityManager.persist(login);
		assertTrue(login.getId()>0);
		Role role = factory.getRole();
		entityManager.persist(role);
		assertTrue(role.getId()>0);
		entityManager.flush();
		userService.addRole(login, role);
		assertTrue(login.getRoles().contains(role));
		Login login2 = entityManager.find(Login.class, login.getId());
		assertNotNull(login2);
		assertTrue(login2.getRoles().contains(role));
		try{
			userService.addRole(null, role);
			fail("should throw exception (login = null)");
		}catch(Exception e){}
		try{
			userService.addRole(login, null);
			fail("should throw exception (role = null)");
		}catch(Exception e){}
		try{
			userService.addRole(login, factory.getRole());
			fail("should throw exception (role not saved)");
		}catch(Exception e){}
	}
	
	@Test
	public void testRemoveRole(){
		authenticatAsAdmin();
		Role role = factory.getRole();
		entityManager.persist(role);
		assertTrue(role.getId()>0);
		userService.addRole(user, role);
		assertTrue(user.getRoles().contains(role));
		Login login2 = entityManager.find(Login.class, user.getId());
		assertNotNull(login2);
		assertTrue(login2.getRoles().contains(role));
		try{
			userService.removeRole(null, role);
			fail("should throw exception (login = null)");
		}catch(Exception e){}
		try{
			userService.removeRole(user, null);
			fail("should throw exception (role = null)");
		}catch(Exception e){}
		try{
			userService.removeRole(factory.getLogin(), role);
			fail("should throw exception (login not saved)");
		}catch(Exception e){}
		try{
			userService.removeRole(user, factory.getRole());
			fail("should throw exception (role not saved)");
		}catch(Exception e){}
		userService.removeRole(login2, role);
		assertFalse(login2.getRoles().contains(role));
	}
	
	@Test
	public void testCreateRole(){
		authenticatAsAdmin();
		Role role = factory.getRole();
		userService.createRole(role);
		assertTrue(role.getId()>0);
	}
	
	@Test
	public void testDeleteRole(){
		assertTrue(true);
	}
	
	@Test
	public void testPrepareInvestigator(){
		userService.prepareInvestigator();
		assertNotNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		assertTrue(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Login);
		Login login = (Login)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		assertTrue(login.getRoles().contains(Role.ROLE_ANONYMOUS));
	}
	
	
	@Test
	public void testRegister(){
		SecurityContextHolder.getContext().setAuthentication(null);
		Login l = userService.prepareInvestigator();
		l.setUsername(stringUtil.getWithLength(Login.MIN_USERNAME_LENGTH)+"@xyz.com");
		l.setPassword(stringUtil.getWithLength(Login.MIN_PASSWORD_LENGTH)+".ada6");
		l.setPerson(factory.getPerson());
		l.getPerson().setLogin(l);
		l.setLastLoggedIn(new GregorianCalendar());
		TrialSite site = factory.getTrialSite();
		entityManager.persist(site);
		entityManager.flush();
		userService.register(l, site);
		assertTrue(l.getId()>0);
	}
	
	
	@Test
	public void testCreate(){
		authenticatAsAdmin();
		TrialSite site = trialSiteDao.get(user.getPerson());
		Login login = factory.getLogin();
		
		userService.create(login, site);
		assertTrue(login.getId()>0);
	}
	
	
	@Test
	public void testUpdate(){
		authenticatAsAdmin();
		
		TrialSite site = trialSiteDao.get(user.getPerson());
		
		Login login = factory.getLogin();
		
		userService.create(login, site);
		assertTrue(login.getId()>0);
		
		String oldName = login.getUsername();
		login.setUsername(factory.getPerson().getEmail());
		userService.update(login);
		Login login2 = entityManager.find(Login.class, login.getId());
		assertEquals(login.getUsername(), login2.getUsername());
		assertFalse(login2.getUsername().equals(oldName));
	}

	
	@Test
	public void testUpdateRole(){
		authenticatAsAdmin();
		Role role = factory.getRole();
		entityManager.persist(role);
		assertTrue(role.getId()>0);
		String oldName = role.getName();
		role.setName(stringUtil.getWithLength(30));
		userService.updateRole(role);
		Role role2 = entityManager.find(Role.class,role.getId());
		assertEquals(role.getName(), role2.getName());
		assertFalse(role2.getName().equals(oldName));
	}
	
	
	@Test
	public void testGetAll(){
		authenticatAsAdmin();
		for(int i =0; i<10; i++){
			Login login = factory.getLogin();
			loginDao.create(login);
		}
		List<Login> list = userService.getAll();
		assertTrue(list.size()>=10);
	}
	
	
	@Test
	public void testGetObject(){
		authenticatAsAdmin();
		TrialSite site = trialSiteDao.get(user.getPerson());
		((AffirmativeBased)context.getBean("methodAccessDecisionManager")).setAllowIfAllAbstainDecisions(true);
		Login login = factory.getLogin();
		userService.create(login, site);
		Login login2 = findLogin("admin@trialsite1.de");
		rolesAndRights.grantRights(login, site);
		Login login3 = userService.getObject(login.getId());
		assertTrue(login3 != null);
	}
	
	@Test
	public void testSaveLoginWithPerson() {
		TrialSite site = factory.getTrialSite();
		entityManager.persist(site);
		entityManager.flush();
		authenticatAsAdmin();
		Person validPerson = factory.getPerson();
		Login login = factory.getLogin();
		userService.create(login, site);
		assertTrue(login.getId()>0);
		
		
		login = factory.getLogin();

		login.setPerson(validPerson);
		login.setUsername("");
		try {
			userService.create(login, site);
			fail("should throw exception");
		} catch (ConstraintViolationException e) {
		}

	
	}
	
}
