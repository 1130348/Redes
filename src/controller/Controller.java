/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Egidio73
 */
public class Controller {

	private List<String> lServers;

	private boolean flag;

	private List<String> lServersActivos;

	private List<String> lServersTotal;

	private List<String> lServersStatus;

	private String myNick;

	private Client cl;

	public Controller() {

		lServers = new ArrayList<>();
		lServersActivos = new ArrayList<>();
		lServersTotal = new ArrayList<>();
		lServersStatus = new ArrayList<>();
		flag = false;
	}

	/**
	 * @return the lServers
	 */
	public List<String> getlServers() {
		return lServers;
	}

	/**
	 * @param lServers the lServers to set
	 */
	public void setlServers(List<String> lServers) {
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
	public List<String> getlServersActivos() {
		return lServersActivos;
	}

	/**
	 * @param lServersActivos the lServersActivos to set
	 */
	public void setlServersActivos(
		List<String> lServersActivos) {
		this.lServersActivos = lServersActivos;
	}

	public void connect() {
		this.flag = true;

	}

	public boolean registaNick(String nick) {
		return cl.registaNick(nick);

	}

	public void disconnect() {
		this.flag = false;
	}

	public boolean verify(int i) {

		if (lServersActivos.get(i) != null) {
			return true;
		}
		return false;
	}

	/**
	 * @return the lServersTotal
	 */
	public List<String> getlServersTotal() {
		return lServersTotal;
	}

	/**
	 * @param lServersTotal the lServersTotal to set
	 */
	public void setlServersTotal(List<String> lServersTotal) {
		this.lServersTotal = lServersTotal;
	}

}
