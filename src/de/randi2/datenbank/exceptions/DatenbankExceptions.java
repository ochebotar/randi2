package de.randi2.datenbank.exceptions;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.randi2.datenbank.StatistikDB;
import de.randi2.utility.SystemException;

/**
 * Diese Exception zeigt einen generellen Fehler beim Datenbankzugriff auf.
 * 
 * @version $Id$
 * @author Daniel Haehn [dhaehn@stud.hs-heilbronn.de]
 */
@SuppressWarnings("serial")
public class DatenbankExceptions extends SystemException {

	/**
	 * Fehlermeldung, wenn eine negative oder =0 Id an die setId() Methode
	 * uebergeben wurde.
	 */
	public static final String ID_FALSCH = "Es wurde keine positive Id uebergeben!";

	/**
	 * Konstante falls Argument null.
	 */
	public static final String ARGUMENT_IST_NULL = "Das uebergebene Argument ist null";

	/**
	 * Konstante falls der Filter beim Objekt nicht gesetzt.
	 */
	public static final String SUCHOBJEKT_IST_KEIN_FILTER = "Beim Uebergebenen Objekt wurde der Filter nicht gesetzt";

	/**
	 * Allgemeine Fehlerkonstante wenn beim suchen eines Objektes ein Fehler
	 * auftritt
	 */
	public static final String SUCHEN_ERR = "Beim Suchen trat ein Fehler auf.";

	/**
	 * Allgemeine Fehlerkonstante wenn beim schreiben eines Objektes ein Fehler
	 * auftritt
	 */
	public static final String SCHREIBEN_ERR = "Beim Schreiben trat ein Fehler auf.";
	
	/**
	 * Fliegt falls versucht wurde einen Tester mit einem bereits vorhandenen Loginname anzulegen.
	 */
	public static final String TESTER_EXISTIERT_ERR ="Dieser Loginname ist bereits vergeben. Bitte w&auml;hlen sie einen Anderen.";

	/**
	 * Allgemeine Fehlerkonstante fuer Fehler die beim Loeschen auftretten.
	 */
	public static final String LOESCHEN_ERR = "Beim Loeschen trat ein Fehler auf.";

	/**
	 * Konstante falls beim Suchen nach einer spezifischen ID diese nicht in der
	 * Datenbank vorhanden ist.
	 */
	public static final String ID_NICHT_VORHANDEN = "Es existiert kein Objekt mit dieser ID.";

	/**
	 * Allgemeine Konstante falls ein Datensatz unkorrekte Daten enthaelt.
	 */
	public static final String UNGUELTIGE_DATEN = "Geladener Datensatz ist nicht valide.";

	/**
	 * Konstante, falls beim Verbinden oder Trennen ein Fehler auftritt.
	 */
	public static final String CONNECTION_ERR = "Es sind Probleme mit der Verbindung zur Datenbank aufgetreten.";

	/**
	 * Konstante, falls bei der Proxool Konfiguration ein Fehler auftritt
	 */
	public static final String PROXOOL_CONF_ERR = "Fehler bei der Konfiguration des Connectionspool aufgetreten.";

	/**
	 * Wenn der Vector bei einer 1:1 Relation mehr als ein ergebnis liefert,
	 * muss eine Exception geworfen werden.
	 */
	public static final String VECTOR_RELATION_FEHLER = "Suche ergab mehr als ein Ergebnis, trotz 1:1 Beziehung.";

	/**
	 * Fehler beim Datenbanklogging.
	 */
	public static final String LOG_FEHLER = "Es trat ein Fehler beim Logging für die Datenbank auf.";
	
	/**
	 * Fehler tritt auf wenn versucht wird zu schreiben oder zu loeschen und bei Filter (von dem alle Beans erben) das aktuelle Benutzerkontobean 
	 * nicht gesetzt wurde. Dies wird fuer das Logging benoetigt.
	 */
	public static final String KEIN_BK_EINGETRAGEN="Es wurde versucht eine schreibende Aktion auf der Datenbank auszufuehren ohne das das aktuelle Benutzerkonto, beim Filter, gesetzt wurden.";
	
	/**
	 * Allgemeine Fehler Konstante fuer die Klasse {@link StatistikDB}
	 */
	public static final String STATISTIK_ERR="Fehler bei Erstellung der Statistik aufgetreten";
	
	/**
	 * Fehlerkonstante wenn bei {@link StatistikDB#getVertPatMW(long)} auftrat
	 */
	public static final String STATISTIK_VIEW1="Statistik f&uuml;r Verteilung der m&auml;nnlichen und weiblichen Patienten in den Studienarmen konnte nicht erstellt werden.";

	/**
	 * Konstruktor.
	 * 
	 * @param msg
	 *            Eine Nachricht aus der Liste der Konstanten.
	 */
	public DatenbankExceptions(String msg) {
		super(msg);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param e
	 *            Die SQLException.
	 * @param msg
	 *            Eine Nachricht aus der Liste der Konstanten.
	 */
	public DatenbankExceptions(SQLException e, String sql, String msg) {
		Logger log = Logger.getLogger(this.getClass());
		super.initCause(e);
		log.error(
				"SQL-Exception. Folgendes SQL-Statement wurde ausgeführt: \n "
						+ sql, this);
	}

}
