/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Egidio73
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

class Client {

	static InetAddress IPdestino;

	private static int TIMEOUT = 3;

	public Client() {

	}

	public boolean registaNick(String nick) {
		return true;
	}

	public boolean testaConexao(Server ser) throws SocketException, IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket sock = new DatagramSocket();
		sock.setSoTimeout(1000 * TIMEOUT); /* definir o tempo limite do socket */

		IPdestino = ser.getIp();

		byte[] data = new byte[300];
		String frase = "ConnectTest";

		data = frase.getBytes();
		DatagramPacket request = new DatagramPacket(data, frase.length(), IPdestino, 27003);
		sock.send(request);
		DatagramPacket reply = new DatagramPacket(data, data.length);
		try {
			sock.receive(reply);
			frase = new String(reply.getData(), 0, reply.getLength());
			System.out.println("Conectado com Sucesso");
		} catch (SocketTimeoutException ex) {
			System.out.println("O servidor não respondeu");
			return false;
		}

		sock.close();
		return true;
	}
}
