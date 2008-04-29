package de.randi2.datenbank.junittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.GregorianCalendar;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import de.randi2.datenbank.DatenbankFactory;
import de.randi2.datenbank.exceptions.DatenbankExceptions;
import de.randi2.model.exceptions.BenutzerException;
import de.randi2.model.exceptions.BenutzerkontoException;
import de.randi2.model.exceptions.PersonException;
import de.randi2.model.exceptions.ZentrumException;
import de.randi2.model.fachklassen.Rolle;
import de.randi2.model.fachklassen.beans.AktivierungBean;
import de.randi2.model.fachklassen.beans.BenutzerkontoBean;
import de.randi2.model.fachklassen.beans.PersonBean;
import de.randi2.model.fachklassen.beans.ZentrumBean;
import de.randi2.utility.KryptoUtil;
import de.randi2.utility.Log4jInit;
import de.randi2.utility.NullKonstanten;
import de.randi2.utility.SystemException;

/**
 * ACHTUNG AUF GRUND DEr REFERENZIELLEN INTEGRITÄT TEST MINDESTENS 2mal DURCHFUEHREN
 * Testet die Datebankfunktionalität für die Beans der Benutzer und
 * Zentrenverwaltung.
 * <ul>
 * <li>Aktivierungsbean</li>
 * <li>Benutzerkontobean</li>
 * <li>Personbean</li>
 * <li>Zentrumbean</li>
 * </ul>
 * 
 * @author Andreas Freudling [afreudling@web.de]
 * @version $Id: BenutzerZentrenDBJunittest.java 2575 2007-05-09 19:09:10Z
 *          afreudli $
 * 
 */
public class BenutzerZentrenDBJunittest {

    /**
     * Initialisiert den Logger. Bitte log4j.lcf.pat in log4j.lcf umbenennen und es funktioniert.
     *
     */
    @BeforeClass
    public static void log(){
	Log4jInit.initDebug();
    }
    /**
         * Testet ob ein Aktivierungsbean erfolgreich in der Datenbank
         * gespeichert und gesucht werden kann.
         */
    @Test
    public void testAktivierungsbeanSpeichernSuchenAendern() throws BenutzerException,DatenbankExceptions{
    BenutzerkontoBean tmp = new BenutzerkontoBean();
    tmp.setFilter(true);
    tmp.setBenutzername("JUnitDB");
	AktivierungBean bean =new AktivierungBean(NullKonstanten.NULL_LONG, new GregorianCalendar(), 1, "23423424242");
	bean.setBenutzerkontoLogging(tmp);
	    bean=DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(bean);
	    
	    //Suchen ueber id:
	    AktivierungBean beanNachSuche=DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(bean.getId(), new AktivierungBean());
	   assertEquals(bean,beanNachSuche);	   
	    
	    //Suchen ueber Werte:
	    bean.setFilter(true);
	    Vector<AktivierungBean> beanVec = DatenbankFactory.getAktuelleDBInstanz().suchenObjekt(bean);
	    if(beanVec!=null) {
	    	 beanNachSuche=beanVec.firstElement();
	    }	   
	    assertEquals(bean,beanNachSuche); 
	    
	    //Bean aendern
	    AktivierungBean beanAendern=new AktivierungBean(bean.getId(),new GregorianCalendar(2000,1,1),1,"1232323232");
	    beanAendern.setBenutzerkontoLogging(tmp);
	    DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(beanAendern);
	    assertFalse(bean.equals(beanAendern));
	    
    }

    /**
         * Testet ob ein Benutzerkontobean erfolgreich in der Datenbank
         * gespeichert und gesucht werden kann.
         */
    @Test
    public void testBenutzerkontobeanSpeichernSuchenAendern() throws BenutzerException,SystemException{
    	BenutzerkontoBean tmp = new BenutzerkontoBean();
        tmp.setFilter(true);
        tmp.setBenutzername("JUnitDB");
	BenutzerkontoBean benutzerbean=new BenutzerkontoBean(NullKonstanten.NULL_LONG,"benutzername",
		KryptoUtil.getInstance().hashPasswort("Passwort").toString(),1,Rolle.getAdmin(),1,false,new GregorianCalendar(),new GregorianCalendar());
	benutzerbean.setBenutzerkontoLogging(tmp);
	benutzerbean=DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(benutzerbean);
	//Suchen ueber id:
	benutzerbean.setFilter(true);
	BenutzerkontoBean beanNachsuche=DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(benutzerbean.getBenutzerId(), new BenutzerkontoBean());
	assertEquals(benutzerbean,beanNachsuche);
	
	//Suchen ueber Werte
	beanNachsuche=DatenbankFactory.getAktuelleDBInstanz().suchenObjekt(benutzerbean).firstElement();
	assertEquals(benutzerbean,beanNachsuche);
	
	
	//Bean Aendern
	BenutzerkontoBean benutzerAendern=new BenutzerkontoBean(benutzerbean.getBenutzerId(),"benutzername12",
		KryptoUtil.getInstance().hashPasswort("Passwort12").toString(),1,Rolle.getStudienarzt(),1,true,new GregorianCalendar(),new GregorianCalendar());
	benutzerAendern.setBenutzerkontoLogging(tmp);
	    DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(benutzerAendern);
	    assertFalse(benutzerbean.equals(benutzerAendern));
    }
    /**
         * Testet ob ein Personbean erfolgreich in der Datenbank gespeichert und
         * gesucht werden kann. Testet ob ein vorhandenes Personbean geändert
         * werden kann.
         * 
         * @throws PersonException
         *                 Fehler im Test
         * @throws DatenbankExceptions
         *                 Fehler im Test
     * @throws BenutzerkontoException 
         */
    @Test
    public void testPersonbeanSpeichernSuchenAendern() throws PersonException, DatenbankExceptions, BenutzerkontoException {
    	BenutzerkontoBean tmp = new BenutzerkontoBean();
        tmp.setFilter(true);
        tmp.setBenutzername("JUnitDB");

	PersonBean pBeanSchreiben = null;
	pBeanSchreiben = new PersonBean(NullKonstanten.NULL_LONG, NullKonstanten.NULL_LONG, "Nachname", "Vorname", PersonBean.Titel.PROF_DR, 'w', "andreasd@web.de", "09878979", "097987987987",
		"0980809809809");
	pBeanSchreiben.setBenutzerkontoLogging(tmp);
	pBeanSchreiben = DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(pBeanSchreiben);
	// Suchen Über id
	PersonBean pBeanSuchen = DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(pBeanSchreiben.getId(), new PersonBean());
	assertEquals(pBeanSchreiben, pBeanSuchen);

	// Suchen über Objekt
	pBeanSchreiben.setFilter(true);
	pBeanSuchen = DatenbankFactory.getAktuelleDBInstanz().suchenObjekt(pBeanSchreiben).lastElement();
	assertEquals(pBeanSchreiben, pBeanSuchen);

	// Ändern
	PersonBean pBeanAendern = DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(pBeanSchreiben.getId(), new PersonBean());
	// Stellvertreter is man selber
	pBeanAendern = new PersonBean(pBeanAendern.getId(), pBeanAendern.getId(), "Nachname1", "Vorname1", PersonBean.Titel.DR, 'm', "wurst@wweb.de", "009878979", "0097987987987", "00980809809809");
	pBeanAendern.setBenutzerkontoLogging(tmp);
	DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(pBeanAendern);
	PersonBean pBeanNachAenderung = DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(pBeanAendern.getId(), new PersonBean());
	assertEquals(pBeanNachAenderung, pBeanAendern);
    }

    /**
         * Test funktioniert nur nach erfolgreichem Personentest! Testet ob ein
         * Zentrumbean erfolgreich in der Datenbank gespeichert und gesucht
         * werden kann. Testet ob ein vorhandenes Zentrumbean geändert werden
         * kann.
         * 
         * @throws ZentrumException
         * @throws DatenbankExceptions
         * @throws PersonException
     * @throws BenutzerkontoException 
         */
    @Test
    public void testZentrumbeanSpeichernSuchenAendern() throws ZentrumException, DatenbankExceptions, PersonException, BenutzerkontoException {
    	BenutzerkontoBean tmp = new BenutzerkontoBean();
        tmp.setFilter(true);
        tmp.setBenutzername("JUnitDB");
        
	ZentrumBean zBeanSchreiben = new ZentrumBean(NullKonstanten.NULL_LONG, "institution", "abteilung", "ort", "01234", "strasse", "2", 1, KryptoUtil.getInstance().hashPasswort(
		"passwort23423&$&§&§&§"), false);
	zBeanSchreiben.setBenutzerkontoLogging(tmp);
	zBeanSchreiben = DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(zBeanSchreiben);
	// Suchen über ID
	zBeanSchreiben.setFilter(true);
	ZentrumBean zBeanSuchen = DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(zBeanSchreiben.getId(), new ZentrumBean());
	assertEquals(zBeanSuchen, zBeanSchreiben);

	// Suchen über Objekt
	//greife auf Last Element zu, da es mehrere Zentren mit den identischen Daten gibt und sich nur
	//in der ID unterscheiden. Resultiert aus dem wiederholten einfuegen des gleichen Zentrums.
	zBeanSuchen = DatenbankFactory.getAktuelleDBInstanz().suchenObjekt(zBeanSchreiben).lastElement();
	assertEquals(zBeanSuchen, zBeanSchreiben);

	// Ändern
	ZentrumBean zBeanAendern = DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(zBeanSchreiben.getId(), new ZentrumBean());
	zBeanAendern = new ZentrumBean(zBeanAendern.getId(), "institution1", "abteilung1", "ort1", "12345", "strasse1", "1", 1, KryptoUtil.getInstance().hashPasswort(
		"passwort1"), true);
	zBeanAendern.setBenutzerkontoLogging(tmp);
	DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(zBeanAendern);
	ZentrumBean zBeanNachAenderung = DatenbankFactory.getAktuelleDBInstanz().suchenObjektId(zBeanAendern.getId(), new ZentrumBean());
    }
}