/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egidio73
 */
public class Controller {

	private List<Server> lServers;

	private boolean flag;

	private List<Server> lServersActivos;

	private String myNick;

	private Client cl;

	private boolean b;

	public Controller() {

		lServers = new ArrayList<>();
		lServersActivos = new ArrayList<>();
		flag = false;
		try {
			cl = new Client();
		} catch (Exception ex) {
			Logger.getLogger(Controller.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @return the lServers
	 */
	public List<Server> getlServers() {
		return lServers;
	}

	/**
	 * @param lServers the lServers to set
	 */
	public void setlServers(List<Server> lServers) {
		this.lServers = lServers;
	}

	/**
	 * @return the myNick
	 */
	public String getMyNick() {
		return myNick;
	}

	/**
	 * @param myNick the myNick to set
	 */
	public void setMyNick(String myNick) {
		this.myNick = myNick;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @return the lServersActivos
	 */
	public List<Server> getlServersActivos() {
		return lServersActivos;
	}

	/**
	 * @param lServersActivos the lServersActivos to set
	 */
	public void setlServersActivos(
		List<Server> lServersActivos) {
		this.lServersActivos = lServersActivos;
	}

	public void connect() {

		this.flag = true;

	}

	public boolean registaNick(String nick) {
		return cl.registaNick(nick);

	}

	public void disconnect(Server ser) {

		this.flag = false;

	}

	public boolean verify(int i) {

		if (lServersActivos.get(i) != null) {
			return true;
		}
		return false;
	}

	public boolean testConnection(Server ser) {

		Thread tr = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					b = cl.testaConexao(ser);
				} catch (IOException ex) {
					Logger.getLogger(Controller.class.getName()).
						log(Level.SEVERE, null, ex);
				}
			}
		});
		tr.start();
		return b;
	}

}
