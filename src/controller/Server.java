/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Egidio73
 */
public class Server {

    private String nome;

    private InetAddress ip;

    private boolean estado;

    private boolean enviar;

    private boolean receber;

    private boolean nomeVerificado;

    private Socket sock;

    public Server(String name, InetAddress IP) {

        this.nome = name;
        this.ip = IP;
        this.nomeVerificado = false;
    }

    public Server() {
        this.nome = "SemNome";
        this.nomeVerificado = false;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the ip
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    /**
     * @return the estado
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * @return the enviar
     */
    public boolean isEnviar() {
        return enviar;
    }

    /**
     * @param enviar the enviar to set
     */
    public void setEnviar(boolean enviar) {
        this.enviar = enviar;
    }

    /**
     * @return the receber
     */
    public boolean isReceber() {
        return receber;
    }

    /**
     * @param receber the receber to set
     */
    public void setReceber(boolean receber) {
        this.receber = receber;
    }

    public void connect() {

        this.enviar = true;
        this.receber = true;
        this.estado = true;

    }

    public void disconnect() {

        this.enviar = false;
        this.receber = false;
        this.estado = false;

    }

    @Override
    public String toString() {
        if (nome == null) {
            return "(s/Dados) " + this.ip.getHostAddress();
        } else {
            return "(" + this.nome + ") " + this.ip.getHostAddress();
        }
    }

    public boolean equals(Server ser) {

        if (this.nome.equals(ser.nome) && this.ip.equals(ser.ip)) {
            return true;
        }

        return false;
    }

    public void add(Socket sock) {
        this.sock = sock;
    }

    public Socket getSocket() {
        return this.sock;
    }

    /**
     * @return the nomeVerificado
     */
    public boolean isNomeVerificado() {
        return nomeVerificado;
    }

    /**
     * @param nomeVerificado the nomeVerificado to set
     */
    public void setNomeVerificado(boolean nomeVerificado) {
        this.nomeVerificado = nomeVerificado;
    }
    
}
