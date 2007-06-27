package de.randi2.utility;

import java.util.HashMap;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import de.randi2.model.exceptions.HilfeException;
import de.randi2.model.exceptions.RandomisationsException;

/**
 * 
 * Die Klasse Hilfe liefert die Inhalte der einzelnen Hilfe-Unterseiten.
 * 
 * @author Daniel Haehn [dhaehn@stud.hs-heilbronn.de]
 * @version $Id$
 * 
 */
public class Hilfe {

	/**
	 * Die Hashmap fuer den schnellen Zugriff
	 */
	private HashMap<String, String> hilfeMap = new HashMap<String, String>();

	/**
	 * Die Unterseiten mit dem jeweiligen Hilfe-Text
	 */
	private static String[][] unterseiten = new String[][] {

	{
			JspTitel.STUDIE_AUSWAEHLEN,
			"Hier kann die Studie ausgew&auml;hlt werden!<br><br>Unter allen zur Verf&uuml;gung stehenden Studien kann gefiltert werden." },

	};

	/**
	 * Singleton Instanz
	 */
	private static Hilfe thisInstance = null;

	/**
	 * Liefert immer eine Instanz dieser Klasse.
	 * 
	 * @return eine Instanz dieser Klasse
	 */
	public static Hilfe getInstance() {
		if (thisInstance == null) {
			thisInstance = new Hilfe();
		}
		return thisInstance;
	}

	/**
	 * Der Konstruktor
	 * 
	 */
	private Hilfe() {

		for (int i = 0; i < unterseiten.length; i++) {

			hilfeMap.put(unterseiten[i][0], unterseiten[i][1]);

		}

	}

	/**
	 * Liefert den Hilfe-Text zu einer Unterseite
	 * 
	 * @param titel
	 *            der Titel der Jsp-Seite, muss ein Eintrag von JspTitel.class
	 *            sein
	 * @return der Hilfe-Text
	 */
	public String getHilfe(String titel) {

		String ausgabe = hilfeMap.get(titel);

		if (ausgabe != null) {

			return ausgabe;

		} else {

			return "Hier existiert noch keine Hilfe!";

		}

	}

}