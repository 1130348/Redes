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

	private boolean flagActiveServers;

	private List<Server> lServersActivos;

	private String myNick;

	private Client cl;

	private boolean b;

	final Charset ENCODING = StandardCharsets.UTF_8;

	private Box box;

	private boolean nickRegisted;

	private Server disconnectServ;

	public Controller() {

		lServers = new ArrayList<>();
		lServersActivos = new ArrayList<>();
		flag = false;
		nickRegisted = true;
		disconnectServ = null;
		flagActiveServers = false;
		try {
			cl = new Client(this);
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
		cl.setlServerConnected(lServersActivos);
	}

	public void connect() {
		this.flagActiveServers = true;
		Thread tt = new Thread(new Runnable() {

			@Override
			public void run() {
				cl.connectTCP();
			}
		});
		tt.start();

	}

	public boolean registaNick(String nick) {
		cl.registaNick(nick);

		return true;
	}

	public void disconnect() {
		//cl.disconnectTCP();
		this.flag = false;
		this.flagActiveServers = false;

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
		if (isFlagActiveServers()) {
			cl.enviaMsg(text);

		} else {
			box.insertInfo();
		}

	}

	public void recebeMsg(String text, String namesrv, String ip) {

		box.recebeMensagem(text, namesrv, ip);

	}

	public void setBox(Box b) {
		this.box = b;
	}

	public Box getBox() {
		return this.box;
	}

	/**
	 * @return the nickRegisted
	 */
	public boolean isNickRegisted() {
		return nickRegisted;
	}

	/**
	 * @param nickRegisted the nickRegisted to set
	 */
	public void setNickRegisted(boolean nickRegisted) {
		this.nickRegisted = nickRegisted;
	}

	public void setFlag(boolean b) {
		this.flag = b;
	}

	public void disconnectServer(Server s) {
		this.disconnectServ = s;
	}

	public Server getdisconnectServer() {
		return this.disconnectServ;
	}

	/**
	 * @return the flagActiveServers
	 */
	public boolean isFlagActiveServers() {
		return flagActiveServers;
	}

	/**
	 * @param flagActiveServers the flagActiveServers to set
	 */
	public void setFlagActiveServers(boolean flagActiveServers) {
		this.flagActiveServers = flagActiveServers;
	}

}
