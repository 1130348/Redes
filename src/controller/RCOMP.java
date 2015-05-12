/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import ui.UI;

/**
 *
 * @author Egidio73
 */
public class RCOMP {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		// TODO code application logic here

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.
				setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(RCOMP.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(RCOMP.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(RCOMP.class.getName()).log(Level.SEVERE, null, ex);
		}

		/*try {
		 Client s = new Client();
		 s.getAvailableServers();
		 } catch (Exception ex) {
		 Logger.getLogger(RCOMP.class.getName()).log(Level.SEVERE, null, ex);
		 }*/
		UI ui = new UI();
		//Notification n = new Notification();
	}

}
