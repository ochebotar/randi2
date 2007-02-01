package de.randi2.model.fachklassen.beans;

import java.util.*;
import de.randi2.utility.*;
import de.randi2.model.fachklassen.Rolle;
import de.randi2.datenbank.Filter;

/**
 * Diese Klasse repraesentiert ein Benutzerkonto.
 * 
 * @author Daniel Haehn <dhaehn@stud.hs-heilbronn.de>
 * @author Lukasz Plotnicki <lplotni@stud.hs-heilbronn.de>
 * @author Thomas Willert <twillert@stud.hs-heilbronn.de>
 * @version $Id$
 */
public class BenutzerkontoBean extends Filter {
	 
	
	/*
	 * Change Log 29.01.2007 Thomas Willert
	 * 
	 * toString und equals sind noch nicht implementiert. Passwort Sonderzeichen
	 * muessen noch gecheckt werden. Pruefung der Rolle fraglich, da Verwendung
	 * der enum aus der Klasse Rolle nicht moeglich. Vielleicht sollte man die
	 * Konstanten dort public machen.
	 * 
	 */

	/**
	 * Benutzername
	 */
	private String benutzername = null;

	/**
	 * Passwort (md5)
	 */
	private String passwort = null;

	/**
	 * Rolle des Benutzerkontos
	 */
	private Rolle rolle = null;

	/**
	 * ID des Kontos TODO Datentyp checken, long korrekt?
	 */
	private long id = NullKonstanten.DUMMY_ID;

	/**
	 * Zugehoeriges PersonBean zu diesem Benutzerkonto.
	 */
	private PersonBean benutzer = null;

	/**
	 * Zugehoeriger Ansprechparter.
	 */
	private PersonBean ansprechpartner = null;

	/**
	 * Ein boolescher Wert, der dem Status gesperrt/entsperrt entspricht.
	 */
	private boolean gesperrt = false;

	/**
	 * Zeitpunkt des ersten Logins
	 */
	private GregorianCalendar ersterLogin = null;

	/**
	 * Zeitpunkt des letzten Logins
	 */
	private GregorianCalendar letzterLogin = null;

	/**
	 * Zentrum, zu dem dieses Benutzerkonto gehoert.
	 */
	private ZentrumBean zentrum = null;

	/**
	 * Der Standardkonstruktor
	 * 
	 */
	public BenutzerkontoBean() throws IllegalArgumentException {

	}

	/**
	 * Der Konstruktor mit allen Attributen.
	 * 
	 * @param benutzername
	 *            der Benutzername des Benutzers
	 * @param passwort
	 *            das Passwort des Benutzers
	 * @param rolle
	 *            die Rolle des Benutzerkontos
	 * @param benutzer
	 *            das PersonBean zu diesem Benutzer
	 * @param ansprechpartner
	 *            das PersonBean das dem Ansprechpartner des Benutzers
	 *            entspricht
	 * @param gesperrt
	 *            ob der Benutzer gesperrt ist
	 * @param zentrum
	 *            das ZentrumBean zu dem das Benutzerkonto gehoert
	 * @param ersterLogin
	 *            Zeitpunkt des ersten Logins als GregorianCalendar
	 * @param letzterLogin
	 *            Zeitpunkt des letzten Logins als GregorianCalendar
	 */
	public BenutzerkontoBean(String benutzername, String passwort, Rolle rolle,
			PersonBean benutzer, PersonBean ansprechpartner, boolean gesperrt,
			ZentrumBean zentrum, GregorianCalendar ersterLogin,
			GregorianCalendar letzterLogin) throws IllegalArgumentException {
		this.setBenutzername(benutzername);
		this.setPasswort(passwort);
		this.setRolle(rolle);
		this.setBenutzer(benutzer);
		this.setAnsprechpartner(ansprechpartner);
		this.setZentrum(zentrum);
		this.setGesperrt(gesperrt);
		this.setErsterLogin(ersterLogin);
		this.setLetzterLogin(letzterLogin);
	}

	/**
	 * Reduzierter Konstruktor, der die Attribute ersterLogin und letzterLogin
	 * automatisch setzt.
	 * 
	 * @param benutzername
	 *            der Benutzername des Benutzers
	 * @param passwort
	 *            das Passwort des Benutzers
	 * @param benutzer
	 *            das PersonBean zu diesem Benutzer
	 */
	public BenutzerkontoBean(String benutzername, String passwort,
			PersonBean benutzer) throws IllegalArgumentException {
		super();
		this.setBenutzername(benutzername);
		this.setPasswort(passwort);
		this.setBenutzer(benutzer);
	}

	/**
	 * @return the benutzer
	 */
	public PersonBean getBenutzer() {
		return benutzer;
	}

	/**
	 * @param benutzer
	 *            the benutzer to set
	 */
	public void setBenutzer(PersonBean benutzer) {
		// keine Pruefung, da bei der Erzeugung der PersonBean schon alles
		// geprueft wird
		this.benutzer = benutzer;
	}

	/**
	 * @return the benutzername
	 */
	public String getBenutzername() {
		return benutzername;
	}

	/**
	 * @param benutzername
	 *            the benutzername to set
	 */
	public void setBenutzername(String benutzername)
			throws IllegalArgumentException {
		boolean filter = super.isFilter();

		if (!filter && benutzername == null) {
			throw new IllegalArgumentException(
					"Bitte geben Sie einen Benutzernamen ein.");
		}
		benutzername = benutzername.trim();
		if (!filter && benutzername.length() == 0) {
			throw new IllegalArgumentException(
					"Bitte geben Sie einen Benutzernamen ein.");
		}
		if (!(benutzername.matches("(\\w|\\d|[._-]|\\@){0,50}"))) {
			// FIXME Min Laenge auf Anweisung der PL auf 2 heruntergesetzt.
			// FIXME 14 Zeichen sind IMO zu wenig, alleine
			// "@med.uni-heidelberg.de" sind ja schon 23 Zeichen! BTheel
			if (!filter)
				throw new IllegalArgumentException(
						"Nur 4-14 Zeichen. Bitte geben Sie den Benutzernamen erneut ein.");
		}
		this.benutzername = benutzername;
	}

	/**
	 * @return the ersterLogin
	 */
	public GregorianCalendar getErsterLogin() {
		return ersterLogin;
	}

	/**
	 * @param ersterLogin
	 *            the ersterLogin to set
	 */
	public void setErsterLogin(GregorianCalendar ersterLogin)
			throws IllegalArgumentException {
		// Testen, ob sich das Datum in der Zukunft befindet
		if ((new GregorianCalendar(Locale.GERMANY)).before(ersterLogin)) {
			throw new IllegalArgumentException(
					"Zeit des ersten Logins ist fehlerhaft.");
		}
		this.ersterLogin = ersterLogin;
	}

	/**
	 * @return the gesperrt
	 */
	public boolean isGesperrt() {
		return gesperrt;
	}

	/**
	 * @param gesperrt
	 *            the gesperrt to set
	 */
	public void setGesperrt(boolean gesperrt) {
		this.gesperrt = gesperrt;
	}

	/**
	 * @return the letzterLogin
	 */
	public GregorianCalendar getLetzterLogin() {
		return letzterLogin;
	}

	/**
	 * @param letzterLogin
	 *            the letzterLogin to set
	 */
	public void setLetzterLogin(GregorianCalendar letzterLogin)
			throws IllegalArgumentException {
		// Testen, ob sich das Datum in der Zukunft befindet
		if ((new GregorianCalendar(Locale.GERMANY)).before(letzterLogin)) {
			throw new IllegalArgumentException(
					"Zeit des letzten Logins ist fehlerhaft.");
		}
		this.letzterLogin = letzterLogin;
	}

	/**
	 * @return the passwort
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * @param passwort
	 *            the passwort to set
	 */
	public void setPasswort(String passwort) throws IllegalArgumentException {
		if (passwort == null) {
			throw new IllegalArgumentException(
					"Bitte geben Sie ein Passwort ein.");
		}
		passwort = passwort.trim();
		if (passwort.length() == 0) {
			throw new IllegalArgumentException(
					"Bitte geben Sie ein Passwort ein.");
		}
		if (passwort.length() < 2) {
			// XXX Auf Wunsch der Pl Laenge von 6 auf 2 heruntergesetzt.

			throw new IllegalArgumentException(
					"Mindestens 2 Zeichen. Bitte geben Sei ein anderes Passwort ein.");
		}
		if (!(passwort.matches(".*[A-Za-z].*") || passwort.matches(".*[0-9].*") /*
																				 * ||
																				 * passwort.matches(".*[\^,.-#+;:_'*!\"�$%&/()=?|<>].*")
																				 */)) {
			throw new IllegalArgumentException(
					"Beachten Sie die Vorschriften! Bitte geben Sie ein anderes Passwort ein.");
		}
		this.passwort = passwort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return "Benutzerkontoname: " + this.benutzername + "(Last LogIn: "
				+ this.letzterLogin + ")";

	}

	/**
	 * Diese Methode prueft, ob zwei Kontos identisch sind. Zwei Kontos sind
	 * identisch, wenn Benutzernamen identisch sind.
	 * 
	 * @param zuvergleichendesObjekt
	 *            das zu vergleichende Objekt vom selben Typ
	 * @return <code>true</code>, wenn beide Kontos gleich sind, ansonstenm
	 *         <code>false</code>
	 */
	public boolean equals(BenutzerkontoBean zuvergleichendesObjekt) {
		if (benutzername.equals(zuvergleichendesObjekt.getBenutzername()))
			return true;

		return false;

	}

	/**
	 * @return the ansprechpartner
	 */
	public PersonBean getAnsprechpartner() {
		return ansprechpartner;
	}

	/**
	 * @param ansprechpartner
	 *            the ansprechpartner to set
	 */
	public void setAnsprechpartner(PersonBean ansprechpartner) {
		// keine Pruefung, da bei der Erzeugung der PersonBean schon alles
		// geprueft wird
		this.ansprechpartner = ansprechpartner;
	}

	public void setRolle(Rolle rolle) throws IllegalArgumentException {
		if (rolle == null) {
			throw new IllegalArgumentException("Keine Rolle!");
		}
		if (!(rolle == Rolle.getAdmin() || rolle == Rolle.getStatistiker()
				|| rolle == Rolle.getStudienleiter()
				|| rolle == Rolle.getStudienarzt() || rolle == Rolle.getSysop())) {
			throw new IllegalArgumentException("Falsche Rolle!");
		}
		this.rolle = rolle;
	}

	public Rolle getRolle() {
		return this.rolle;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public ZentrumBean getZentrum() {
		return zentrum;
	}

	public void setZentrum(ZentrumBean zentrum) {
		this.zentrum = zentrum;
	}
}
