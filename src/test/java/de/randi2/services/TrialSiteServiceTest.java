package de.randi2.services;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.providers.dao.salt.SystemWideSaltSource;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.vote.AffirmativeBased;

import de.randi2.model.Login;
import de.randi2.model.Person;
import de.randi2.model.Role;
import de.randi2.model.TrialSite;
import de.randi2.test.utility.DomainObjectFactory;
import de.randi2.utility.security.RolesAndRights;

public class TrialSiteServiceTest extends AbstractServiceTest {

	
	@Autowired private TrialSiteService service;
	@Autowired private DomainObjectFactory factory;
	@Autowired private SessionFactory sessionFactory;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private SystemWideSaltSource saltSource;
	@Autowired private RolesAndRights rolesAndRights;
	@Autowired private ApplicationContext context;
	

	
	@Test
	public void testInit(){
		assertNotNull(service);
	}
	
	@Test
	public void testCreate(){
		authenticatAsAdmin();
		TrialSite site = factory.getTrialSite();
		service.create(site);
		assertTrue(site.getId()>0);
		
	}
	
	@Test
	public void testAuthorize(){
		authenticatAsAnonymous();
		TrialSite site = factory.getTrialSite();
		site.setPassword(passwordEncoder.encodePassword("1$heidelberg", saltSource.getSystemWideSalt()));
		assertTrue(service.authorize(site, "1$heidelberg"));
		assertFalse(service.authorize(site, "234§heidelberg"));
		
	}
	
	
	@Test
	public void testUpdate(){
		authenticatAsAdmin();
		TrialSite site = factory.getTrialSite();
		service.create(site);
		assertTrue(site.getId()>0);
//		aclService.createAclwithPermissions(site, "admin@test.de",new PermissionHibernate[]{PermissionHibernate.ADMINISTRATION},null);
//		aclService.readAclById(new ObjectIdentityHibernate(TrialSite.class,site.getId()), new Sid[]{new PrincipalSid("admin@test.de")});
		site.setName("Name23");
		service.update(site);
		TrialSite site2 = (TrialSite) sessionFactory.getCurrentSession().get(TrialSite.class, site.getId());
		assertEquals(site.getName(), site2.getName());
	}
	
	@Test
	public void testgetAll(){
		authenticatAsAdmin();
		Login login = findLogin("admin@test.de"); //@Daniel - why do we need this line //TODO
		for(int i=0;i<10;i++){
			TrialSite site = factory.getTrialSite();
			service.create(site);
			rolesAndRights.grantRigths(site,site);
			assertTrue(site.getId()>0);
		}
		
		assertTrue(service.getAll().size()>=10);

	}
	
	
	@Test
	public void testGetObject(){
		authenticatAsAdmin();
		((AffirmativeBased)context.getBean("methodAccessDecisionManager")).setAllowIfAllAbstainDecisions(true);
		TrialSite site = factory.getTrialSite();
		
		
		service.create(site);
		assertTrue(site.getId()>0);
		rolesAndRights.grantRigths(site,site);
		TrialSite site2 = service.getObject(site.getId());
		assertEquals(site.getName(), site2.getName());
		assertEquals(site.getId(), site2.getId());
		assertEquals(site.getCity(), site2.getCity());
	}
	

	private void authenticatAsAnonymous(){
		Login newUser = new Login();
		newUser.setPerson(new Person());
		newUser.addRole(Role.ROLE_ANONYMOUS);
		AnonymousAuthenticationToken authToken = new AnonymousAuthenticationToken(
				"anonymousUser", newUser, newUser.getAuthorities());
		// Perform authentication
		SecurityContextHolder.getContext().setAuthentication(authToken);
		SecurityContextHolder.getContext().getAuthentication()
				.setAuthenticated(true);
	}
	
	
}
