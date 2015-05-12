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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Client {

	static InetAddress IPdestino;

	public Client() throws Exception {
		/*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 DatagramSocket sock = new DatagramSocket();
		 sock.setBroadcast(true);
		 IPdestino = InetAddress.getByName("255.255.255.255");
		 byte[] data = new byte[300];
		 String frase;
		 while (true) {
		 System.out.print("Frase a enviar (\"sair\" para terminar): ");
		 frase = in.readLine();
		 if (frase.compareTo("sair") == 0) {
		 break;
		 }
		 data = frase.getBytes();

		 //Envia
		 DatagramPacket request = new DatagramPacket(data, frase.length(), IPdestino, 9006);
		 sock.send(request);

		 //recebe resposta
		 DatagramPacket reply = new DatagramPacket(data, data.length);
		 sock.receive(reply);

		 //Obtem IP
		 IPdestino = reply.getAddress();
		 frase = new String(reply.getData(), 0, reply.getLength());
		 System.out.println("Resposta: " + frase);
		 }
		 sock.close();*/
	}

	public void getAvailableServers() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket sock = null;
		try {
			sock = new DatagramSocket();
		} catch (SocketException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			sock.setBroadcast(
				true);
		} catch (SocketException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			IPdestino = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
		byte[] data = new byte[300];
		String frase = null;

		while (true) {
			System.out.print("Frase a enviar (\"sair\" para terminar): ");

			try {
				frase = in.readLine();
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			if (frase.compareTo("sair") == 0) {
				break;
			}
			data = frase.getBytes();

			//pedido
			DatagramPacket request = new DatagramPacket(data, frase.length(), IPdestino, 9999);
			try {
				sock.send(request);
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}

			//resposta
			DatagramPacket reply = new DatagramPacket(data, data.length);
			try {
				sock.receive(reply);
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			IPdestino = reply.getAddress();
			frase = new String(reply.getData(), 0, reply.getLength());
			System.out.println("Resposta: " + frase);
		}

		sock.close();
	}

	public boolean registaNick(String nick) {
		//send nick e faz regista no server
		return true;
	}
}
