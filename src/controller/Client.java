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
import static controller.Client.IPdestino;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class Client {

	static Controller controller;

	static InetAddress IPdestino;

	private static int TIMEOUT = 3;

	private String nick;

	static Socket sock;

	private boolean msgRecebida;

	private DataOutputStream sOut;

	private String frase;

	private byte[] data;

	private Thread serverConn;

	private List<Socket> lsocks;

	private List<Server> lServer;

	public Client(Controller c) {
		lsocks = new ArrayList<>();
		msgRecebida = false;
		controller = c;
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

	public void connectTCP() {
		System.out.println(lServer.toString());
		for (Server ser : lServer) {

			IPdestino = ser.getIp();
			data = new byte[300];

			try {
				sock = new Socket(IPdestino, 27003);
				ser.add(sock);
			} catch (IOException ex) {
				String warn = IPdestino.getHostAddress();
				JOptionPane.
					showMessageDialog(null, "Server: " + warn + " deixou de estar disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);
				//tirar server da lista
			}
			Thread serverConn = new Thread(new tcp_chat_cli_con(sock));
			serverConn.start();
		}

		synchronized (this) {
			while (true) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					System.out.println("wait");
				}

				if (msgRecebida) {

					if (frase.compareTo("sair") == 0) {
						try {
							sOut.write(0);
						} catch (IOException ex) {
							Logger.getLogger(Client.class.getName()).
								log(Level.SEVERE, null, ex);
						}
						break;
					}
					data = frase.getBytes();

					for (Server serv : lServer) {

						sOut = null;
						try {
							sOut = new DataOutputStream(serv.getSocket().
								getOutputStream());
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
				}
			}
			try {
				serverConn.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			try {
				sock.close();
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).
					log(Level.SEVERE, null, ex);
			}
		}

	}

	public String getNick() {

		return this.nick;
	}

	public void enviaMsg(String text) {
		synchronized (this) {
			frase = text;
			msgRecebida = true;
			this.notifyAll();
		}

	}

	public void setlServer(List<Server> lServersActivos) {
		this.lServer = lServersActivos;

	}

}

class tcp_chat_cli_con implements Runnable {

	private Socket s;
	private DataInputStream sIn;
	private String frase;

	public tcp_chat_cli_con(Socket tcp_s) {
		s = tcp_s;
	}

	@Override
	public void run() {
		int nChars;
		byte[] data = new byte[300];

		try {
			sIn = new DataInputStream(s.getInputStream());

			while (true) {
				nChars = sIn.read();
				if (nChars == 0) {
					break;
				}

				sIn.read(data, 0, nChars);
				System.out.println(nChars);
				frase = new String(data, 0, nChars);
				System.out.println(frase);

				Client.controller.recebeMsg(frase);

			}

		} catch (Exception ex) {
			System.out.println("Ligacao TCP terminada.");
			String warn = IPdestino.getHostAddress();
			JOptionPane.
				showMessageDialog(null, "Server: " + warn + " deixou de estar disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);
			//tirar da lista
		}
	}
}
