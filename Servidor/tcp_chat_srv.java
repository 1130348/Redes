
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

class tcp_chat_srv {

	public final static int MAXCLI = 100;
	public static Socket[] cliSock = new Socket[MAXCLI];
	public static DataOutputStream[] sOut = new DataOutputStream[MAXCLI];
	public static Boolean[] inUse = new Boolean[MAXCLI];
	public static Semaphore changeLock = new Semaphore(1);
	public static Map<Socket, String> mapaNickIps = new HashMap<>();
	public static String name;

	static ServerSocket sock;

	public static void main(String args[]) throws Exception {
		if (args.length > 0) {
			name = args[0];
		} else {
			name = getNameHashCode();
		}

		System.out.println("NOME: " + name);

		int i;
		Thread serverConnTest = new Thread(new startUDP());
		serverConnTest.start();

		try {
			sock = new ServerSocket(27004);
		} catch (IOException ex) {
			System.out.println("O porto local esta ocupado.");
			System.exit(1);
		}

		for (i = 0; i < MAXCLI; i++) {
			inUse[i] = false;
		}
		while (true) {
			changeLock.acquire();
			for (i = 0; i < MAXCLI; i++) {
				if (!inUse[i]) {
					break; // procurar um socket livre
				}
			}
			changeLock.release();
			cliSock[i] = sock.accept(); // esperar por lig. de novo cliente
			changeLock.acquire();
			inUse[i] = true;
			sOut[i] = new DataOutputStream(cliSock[i].getOutputStream());
			changeLock.release();
			new Thread(new tcp_chat_srv_conn(i)).start();
		}
	}

	public static String getNameHashCode() {
		int hash = 1;
		try {
			hash = hash * 2 + InetAddress.getLocalHost().getHostAddress().
				hashCode();
		} catch (UnknownHostException ex) {
			Logger.getLogger(startUDP.class.getName()).
				log(Level.SEVERE, null, ex);
		}
		return String.valueOf(hash);
	}
}

class tcp_chat_srv_conn implements Runnable {

	private int myNum;
	private DataInputStream sIn;
	private String frase;
	private boolean state;

	public tcp_chat_srv_conn(int cli_n) {
		myNum = cli_n;
		state = false;
	}

	@Override
	public void run() {
		int i, nChars;
		String rep = "";
		byte[] data = new byte[300];
		try {

			sIn = new DataInputStream(tcp_chat_srv.cliSock[myNum].
				getInputStream());

			while (true) {

				nChars = sIn.read();
				if (nChars == 0) {
					break; // linha vazia
				}
				sIn.read(data, 0, nChars);

				try {

					frase = new String(data, 0, nChars);
					System.out.println("Msg Recebida: " + frase);
					if (frase.contains("\\")) {
						if (frase.contains("Sair")) {
							break;
						}
						executaComando(frase);
					} else {
						if (tcp_chat_srv.mapaNickIps.
							containsKey(tcp_chat_srv.cliSock[myNum])) {

							rep = tcp_chat_srv.mapaNickIps.get(getSocket());
							rep += "break";
							rep += frase;
							frase = rep;

						}
					}

					System.out.
						println("Utilizadores:" + tcp_chat_srv.mapaNickIps.
							values());
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}

				System.out.println("Resposta: " + frase);
				data = frase.getBytes();
				nChars = frase.length();

				tcp_chat_srv.changeLock.acquire();
				for (i = 0; i < tcp_chat_srv.MAXCLI; i++) // retransmitir a linha
				{
					if (tcp_chat_srv.inUse[i]) {
						tcp_chat_srv.sOut[i].write(nChars);
						tcp_chat_srv.sOut[i].write(data, 0, nChars);
					}
				}
				tcp_chat_srv.changeLock.release();
				frase = null;
				rep = null;
				data = new byte[300];

			}
			// o cliente quer sair
			nChars = 0;
			tcp_chat_srv.changeLock.acquire();
			tcp_chat_srv.sOut[myNum].write(nChars);
			tcp_chat_srv.inUse[myNum] = false;
			tcp_chat_srv.cliSock[myNum].close();
			tcp_chat_srv.mapaNickIps.remove(tcp_chat_srv.cliSock[myNum]);
			tcp_chat_srv.changeLock.release();
		} catch (IOException ex) {
			System.out.println("IOException");
			System.out.println(ex.toString());
		} catch (InterruptedException ex) {
			System.out.println("Interrupted");
			System.out.println(ex.toString());
		}
	}

	private Socket getSocket() {

		return tcp_chat_srv.cliSock[this.myNum];

	}

	private void executaComando(String comando) {
		String rep = "teste";
		String[] ls2 = comando.split(" ");
		if (ls2[0].equals("\\changeNick")) {
			boolean flag;
			if (tcp_chat_srv.mapaNickIps.isEmpty()) {
				tcp_chat_srv.mapaNickIps.put(getSocket(), ls2[1]);
				frase = "\\nickChanged";
			} else {
				String[] n = tcp_chat_srv.mapaNickIps.values().
					toArray(new String[0]);
				for (String f : n) {
					if ((f).equals(ls2[1])) {
						frase = "\\nickNotAllowed";
					} else {
						if (tcp_chat_srv.mapaNickIps.containsKey(getSocket())) {
							tcp_chat_srv.mapaNickIps.put(getSocket(), ls2[1]);
						} else {
							tcp_chat_srv.mapaNickIps.put(getSocket(), ls2[1]);
						}
						frase = "\\nickChanged";
					}
				}
			}
		} else {
			rep = tcp_chat_srv.mapaNickIps.get(getSocket());
			rep += "break";
			rep += frase;
			frase = rep;
		}

	}
}

class startUDP implements Runnable {

	int myNum;
	private DataInputStream sIn;
	static DatagramSocket sock;

	public startUDP() {

	}

	@Override
	public void run() {
		byte[] data = new byte[300];
		byte[] data1 = new byte[300];
		String frase;
		InetAddress clientIP;
		int res, i, clientPort;

		try {
			sock = new DatagramSocket(27004);
		} catch (BindException ex) {
			System.out.println("O porto esta ocupado");
			System.exit(1);
		} catch (SocketException ex) {
			System.out.println("Conexao fallhada");
		}

		System.out.println("A escutar pedidos. Use CTRL+C para terminar");
		while (true) {
			DatagramPacket pedido = new DatagramPacket(data, data.length);
			try {

				sock.receive(pedido);

			} catch (IOException ex) {
				System.out.println("Conexao falhada");
			}
			clientIP = pedido.getAddress();
			clientPort = pedido.getPort();

			res = tcp_chat_srv.name.getBytes().length;
			data1 = tcp_chat_srv.name.getBytes();
			DatagramPacket resposta = new DatagramPacket(data1, res, clientIP, clientPort);
			try {
				sock.send(resposta);
			} catch (IOException ex) {
				System.out.println("Conexao fallhada");
			}
		}
	}

}
