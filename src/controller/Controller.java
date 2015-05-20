/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.Box;

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

	final Charset ENCODING = StandardCharsets.UTF_8;

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
		return cl.getNick();
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
		cl.setlServer(lServersActivos);
	}

	public void connect(Box b) {
		cl.connectTCP();
		this.flag = true;

	}

	public boolean registaNick(String nick) {
		cl.registaNick(nick);

		return true;
	}

	public void disconnect() {
		//cl.disconnectTCP();
		this.flag = false;

	}

	public boolean verify(int i) {

		if (lServersActivos.get(i) != null) {
			return true;
		}
		return false;
	}

	public boolean testConnection(Server ser) {

		try {
			b = cl.testaConexao(ser);
		} catch (IOException ex) {
			Logger.getLogger(Controller.class.getName()).
				log(Level.SEVERE, null, ex);
		}

		return b;
	}

	public List<String> importFile(String aFileName) {
		Path path = Paths.get(aFileName);
		try {
			return Files.readAllLines(path, ENCODING);
		} catch (IOException ex) {
			Logger.getLogger(Controller.class.getName()).
				log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public void enviaMsg(String text) {
		cl.enviaMsg(text);
	}

}
