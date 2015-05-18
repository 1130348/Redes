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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class Client {

	static InetAddress IPdestino;

	private static int TIMEOUT = 3;

	private String nick;

	static Socket sock;

	private tcp_chat_cli_con e;

	private boolean msgRecebida;

	private DataOutputStream sOut;

	private String frase;

	private byte[] data;

	private Thread serverConn;

	private List<Server> lServer;

	public Client() {

	}

	public void registaNick(String nick) {

		this.nick = nick;
		this.frase = "\\changeNick " + nick;
		enviaMsg(frase);

	}

	public boolean testaConexao(Server ser) throws SocketException, IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket sock = new DatagramSocket();
		sock.setSoTimeout(1000 * TIMEOUT); /* definir o tempo limite do socket */

		IPdestino = ser.getIp();
		System.out.println("IPdestino" + IPdestino.getHostAddress());
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

	public void connectTCP(Server ser) {

		IPdestino = ser.getIp();
		data = new byte[300];

		try {
			sock = new Socket(IPdestino, 27003);
		} catch (IOException ex) {
			String warn = IPdestino.getHostAddress();
			JOptionPane.
				showMessageDialog(null, "Server: " + warn + " deixou de estar disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);
			//tirar server da lista
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		sOut = null;
		try {
			sOut = new DataOutputStream(sock.getOutputStream());
		} catch (IOException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}

		e = new tcp_chat_cli_con(sock);
		serverConn = new Thread(e);
		serverConn.start();

		while (!msgRecebida) {

		}

	}

	public String getNick() {

		return this.nick;
	}

	public void enviaMsg(String text) {
		this.msgRecebida = true;
		this.frase = text;
		if (frase.compareTo("sair") == 0) {
			try {
				sOut.write(0);
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
		}

		data = frase.getBytes();

		for (Server f : lServer) {

			try {
				sock = new Socket(f.getIp(), 27003);
			} catch (IOException ex) {
				String warn = IPdestino.getHostAddress();
				JOptionPane.
					showMessageDialog(null, "Server: " + warn + " deixou de estar disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);
				//tirar server da lista
			}

			try {
				sOut = new DataOutputStream(sock.getOutputStream());
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}

			try {
				sOut.write((byte) frase.length());
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			try {
				sOut.write(data, 0, (byte) frase.length());
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
		}

		msgRecebida = false;

		try {
			serverConn.join();
		} catch (InterruptedException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			sock.close();
		} catch (IOException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setlServer(List<Server> lServersActivos) {
		this.lServer = lServersActivos;
	}

}

class tcp_chat_cli_con implements Runnable {

	private Socket s;
	private DataInputStream sIn;

	public tcp_chat_cli_con(Socket tcp_s) {
		s = tcp_s;
	}

	@Override
	public void run() {
		int nChars;
		byte[] data = new byte[300];
		String frase;
		try {
			sIn = new DataInputStream(s.getInputStream());
			while (true) {
				nChars = sIn.read();
				if (nChars == 0) {
					break;
				}
				sIn.read(data, 0, nChars);
				frase = new String(data, 0, nChars);

			}
		} catch (Exception ex) {
			System.out.println("Ligacao TCP terminada.");
		}
	}
}
