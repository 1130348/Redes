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
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class Client {

	static Controller controller;

	static InetAddress IPdestino;

	private static final int TIMEOUT = 1;

	private String nick;

	static Socket sock;

	private boolean msgEnviada;

	private DataOutputStream sOut;

	private String frase;

	private byte[] data;

	private List<Thread> lserverConn;

	public static List<Server> lServerConnected;

	public static Semaphore sem = new Semaphore(1);

	public Client(Controller c) {
		msgEnviada = false;
		controller = c;
	}

	public void registaNick(String nick) {

		this.nick = nick;
		this.frase = "\\changeNick " + nick;
		enviaMsg(frase);

	}

	public boolean testaConexao(Server ser) throws SocketException, IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket sock2 = new DatagramSocket();
		sock2.setSoTimeout(1000 * TIMEOUT); /* definir o tempo limite do socket */

		IPdestino = ser.getIp();
		System.out.println("IPdestino" + IPdestino.getHostAddress());
		byte[] data2 = new byte[300];
		String frase2 = "ConnectTest";

		data2 = frase2.getBytes();
		DatagramPacket request = new DatagramPacket(data2, frase2.length(), IPdestino, 27004);
		sock2.send(request);
		DatagramPacket reply = new DatagramPacket(data2, data2.length);
		try {
			sock2.receive(reply);
			frase2 = new String(reply.getData(), 0, reply.getLength());
			for (int i = 0; i < controller.getlServersActivos().size(); i++) {
				if (controller.getlServersActivos().get(i).getNome().
					equalsIgnoreCase(frase2)) {
					for (int j = 0; j < controller.getlServersActivos().size(); j++) {
						if (controller.getlServersActivos().get(j).getIp() == IPdestino) {
							if (!controller.getlServersActivos().get(j).
								isNomeVerificado()) {
								JOptionPane.
									showMessageDialog(null, "Já existe um servidor com o nome " + frase2 + " logo o servidor não será adicionado.", "Erro", JOptionPane.INFORMATION_MESSAGE);
								return false;
							}
						}
					}

				}
			}

			for (int i = 0; i < controller.getlServersActivos().size(); i++) {
				if (controller.getlServersActivos().get(i).getIp() == IPdestino) {
					controller.getlServersActivos().get(i).setNome(frase2);
					controller.getlServersActivos().get(i).
						setNomeVerificado(true);
				}
			}
			System.out.println("Conectado com Sucesso");
		} catch (SocketTimeoutException ex) {
			System.out.println("O servidor não respondeu");
			return false;
		}

		sock2.close();
		return true;
	}

	public void connectTCP() {
		System.out.println(lServerConnected.toString());
		for (Server ser : lServerConnected) {

			ser.setEnviar(true);
			ser.setReceber(true);
			ser.setEstado(true);

			IPdestino = ser.getIp();
			data = new byte[300];

			try {
				sock = new Socket(IPdestino, 27004);
				ser.add(sock);

			} catch (IOException ex) {
				String warn = IPdestino.getHostAddress();
				JOptionPane.
					showMessageDialog(null, "Server: " + warn + " deixou de estar disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);
				//tirar server da lista
			}
		}

		for (Server rec : lServerConnected) {
			Thread serverConn = new Thread(new tcp_chat_cli_con(rec.getSocket()));
			serverConn.start();
		}

		synchronized (this) {
			while (true) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					System.out.println("wait");
				}
				if (lServerConnected.isEmpty()) {
					controller.disconnect();
					break;
				}
				if (msgEnviada) {

					if (frase.compareTo("sair") == 0) {
						try {
							sOut.write(0);

						} catch (IOException ex) {
							Logger.getLogger(Client.class
								.getName()).
								log(Level.SEVERE, null, ex);
						}
						break;
					}
					data = frase.getBytes();

					for (Server serv : lServerConnected) {
						if (controller.getdisconnectServer() != null) {
							if (controller.getdisconnectServer().equals(serv)) {
								frase = "\\Sair";
								data = frase.getBytes();
								sOut = null;
								try {
									sOut = new DataOutputStream(serv.getSocket().
										getOutputStream());

								} catch (IOException ex) {
									Logger.getLogger(Client.class
										.getName()).
										log(Level.SEVERE, null, ex);
								}
								try {
									sOut.write((byte) frase.length());

								} catch (IOException ex) {
									Logger.getLogger(Client.class
										.getName()).
										log(Level.SEVERE, null, ex);
								}
								try {
									sOut.write(data, 0, (byte) frase.length());

								} catch (IOException ex) {
									Logger.getLogger(Client.class
										.getName()).
										log(Level.SEVERE, null, ex);
								}
								frase = "\\";
								lServerConnected.remove(serv);
								controller.disconnectServer(null);
								break;
							}
						}

						if (serv.isEnviar()) {
							sOut = null;
							try {
								sOut = new DataOutputStream(serv.getSocket().
									getOutputStream());

							} catch (IOException ex) {
								Logger.getLogger(Client.class
									.getName()).
									log(Level.SEVERE, null, ex);
							}
							try {
								sOut.write((byte) frase.length());

							} catch (IOException ex) {
								Logger.getLogger(Client.class
									.getName()).
									log(Level.SEVERE, null, ex);
							}
							try {
								sOut.write(data, 0, (byte) frase.length());

							} catch (IOException ex) {
								Logger.getLogger(Client.class
									.getName()).
									log(Level.SEVERE, null, ex);
							}
						}
					}
					msgEnviada = false;
				}
			}

			for (Server se : lServerConnected) {

				try {
					se.getSocket().close();

				} catch (IOException ex) {
					Logger.getLogger(Client.class
						.getName()).
						log(Level.SEVERE, null, ex);
				}
			}
		}

	}

	public String getNick() {

		return this.nick;
	}

	public void enviaMsg(String text) {
		synchronized (this) {
			frase = text;
			msgEnviada = true;
			this.notifyAll();
		}

	}

	public void setlServerConnected(List<Server> lServersConectados) {
		this.lServerConnected = lServersConectados;

	}

}

class tcp_chat_cli_con implements Runnable {

	private Socket s;
	private DataInputStream sIn;
	private String frase;
	private boolean msgRecebida;
	private boolean sendFlag;

	public tcp_chat_cli_con(Socket tcp_s) {
		s = tcp_s;
		msgRecebida = true;
		sendFlag = false;
	}

	@Override
	public void run() {
		int nChars;
		byte[] data = new byte[300];

		try {
			sIn = new DataInputStream(s.getInputStream());
			while (true) {

				if (msgRecebida) {
					msgRecebida = true;
					nChars = sIn.read();
					if (nChars == 0) {
						break;
					}

					sIn.read(data, 0, nChars);
					frase = new String(data, 0, nChars);
					System.out.println(frase);
					try {
						Client.sem.acquire();
					} catch (InterruptedException ex) {
						Logger.getLogger(tcp_chat_cli_con.class.getName()).
							log(Level.SEVERE, null, ex);
					}
					for (Server e : Client.lServerConnected) {
						if (e.getSocket().equals(s)) {

							if (e.isReceber()) {
								sendFlag = true;

							} else {
								sendFlag = false;
							}
						}
					}
					if (sendFlag) {
						if (frase.contains("\\nickChanged")) {
							Client.controller.setFlag(false);
						} else if (frase.contains("\\nickNotAllowed")) {
							Client.controller.setNickRegisted(false);
							Client.controller.setFlag(true);
						} else {
							Client.controller.recebeMsg(frase, s.
														getInetAddress().
														getHostAddress());
						}
					}

					Client.sem.release();

					msgRecebida = true;
				}
			}
		} catch (IOException ex) {
			System.out.println("Ligacao TCP terminada.");
			String warn = IPdestino.getHostAddress();
			JOptionPane.
				showMessageDialog(null, "Server: " + warn + " deixou de estar disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);
			//tirar da lista
		}
	}
}
