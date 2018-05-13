/*Benjamin Meiﬂner <lordmason@gmail.com*/
package de.jost_net.JVerein.Queries;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl.DIFFERENZ;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;

/**
 * Initiates a query for getting all Mail recipients for a given mail from Database
 * @author mason
 *
 */
public class MailQuery {
	
	private ArrayList<Mitglied> empfArrayList;
	private TreeSet<Mitglied> empfTreeSet;
	
	public MailQuery(Object[] params) throws RemoteException {
		this.empfArrayList = new ArrayList<Mitglied>();
		this.empfTreeSet = new TreeSet<Mitglied>();
		query(params);
	}

	private void query(Object[] params) throws RemoteException {
	    final DBService service = Einstellungen.getDBService();
	    ArrayList ergebnis = new ArrayList<Mitgliedskonto>();
	    String sql = "SELECT MAILEMPFAENGER.versand, MITGLIED.* from MAILEMPFAENGER LEFT JOIN MITGLIED ON MAILEMPFAENGER.MITGLIED = MITGLIED.ID WHERE MAIL = ?;";
	    
	    ResultSetExtractor rs = new ResultSetExtractor() {	    	
	      @Override
	      public Object extract(ResultSet rs) throws RemoteException, SQLException {
	        ArrayList<Mitglied> list = new ArrayList<Mitglied>();
	        while (rs.next()) {
	        	list.add((Mitglied) service.createObject(Mitglied.class, rs.getString(2)));
	        }
        	return list;
	      }
	    };
	    this.empfArrayList = (ArrayList<Mitglied>) service.execute(sql, params, rs);
	    convertToTreeSet();
	  }
	
	private void convertToTreeSet() {
		/*for(Mitglied me : this.empfArrayList) {			
			this.empfTreeSet.add(me);
		}*/
	}
	
	/**
	 * Gets ArrayList of Mailempfaenger
	 * @return List of all Mail Recipients
	 */
	public ArrayList<Mitglied> getEmpfArrayList() {
		return this.empfArrayList;
	}
	
	
	/**
	 * Gets TreeSet of Mailempfaenger
	 * @return List of all Mail Recipients
	 */
	public TreeSet<Mitglied> getEmpfTreeSet() {
		return this.empfTreeSet;
	}
}
