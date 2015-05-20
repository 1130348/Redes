
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

class tcp_chat_srv {

	public static int MAXCLI = 100;
	public static Socket[] cliSock = new Socket[MAXCLI];
	public static DataOutputStream[] sOut = new DataOutputStream[MAXCLI];
	public static Boolean[] inUse = new Boolean[MAXCLI];
	public static Semaphore changeLock = new Semaphore(1);
	public static Map<String, String> mapaNickIps = new HashMap<>();

	static ServerSocket sock;

	public static void main(String args[]) throws Exception {
		int i;
		Thread serverConnTest = new Thread(new startUDP());
		serverConnTest.start();

		try {
			sock = new ServerSocket(27003);
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
}

class tcp_chat_srv_conn implements Runnable {

	int myNum;
	private DataInputStream sIn;

	public tcp_chat_srv_conn(int cli_n) {
		myNum = cli_n;
	}

	@Override
	public void run() {
		int i, nChars;
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

				String str = "";
				String nstr = "";
				int nickLeng = 0;
				try {
					str = new String(data, "UTF-8");
					if (str.contains("\\")) {
						str = executaComando(str);

					} else {
						if (tcp_chat_srv.mapaNickIps.containsKey(getIp())) {
							/*nickLeng = tcp_chat_srv.mapaNickIps.get(getIp()).
							 length();*/
							String oldStr = str;
							str += tcp_chat_srv.mapaNickIps.
								get(getIp());

							/*str = nstr.concat(tcp_chat_srv.mapaNickIps.
							 get(getIp()) + ": " + oldStr + '\0');*/
						}
					}

					//System.out.println(tcp_chat_srv.mapaNickIps.values());
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}

				data = str.getBytes();
				nChars = str.length();

				tcp_chat_srv.changeLock.acquire();
				for (i = 0; i < tcp_chat_srv.MAXCLI; i++) // retransmitir a linha
				{
					if (tcp_chat_srv.inUse[i]) {
						System.out.println(i);
						tcp_chat_srv.sOut[i].write(nChars);
						tcp_chat_srv.sOut[i].write(data, 0, nChars);
					}
				}
				tcp_chat_srv.changeLock.release();
			}
			// o cliente quer sair
			tcp_chat_srv.changeLock.acquire();
			tcp_chat_srv.sOut[myNum].write(nChars);
			tcp_chat_srv.inUse[myNum] = false;
			tcp_chat_srv.cliSock[myNum].close();
			tcp_chat_srv.changeLock.release();
		} catch (IOException ex) {
			System.out.println("IOException");
			System.out.println(ex.toString());
		} catch (InterruptedException ex) {
			System.out.println("Interrupted");
			System.out.println(ex.toString());
		}
	}

	private int getTamanho(String s) {
		System.out.println(s);
		return s.length();
	}

	private String getIp() {

		String ip = tcp_chat_srv.cliSock[myNum].toString();
		String[] ls = ip.split("addr=/");
		ls = ls[1].split(",");
		return ls[0];
	}

	private String executaComando(String comando) {
		String rep = "";
		System.out.println(comando);
		String ip = getIp();
		if (comando.contains("\\changeNick")) {
			String[] ls2 = comando.split(" ");
			boolean flag;
			String[] n = (String[]) tcp_chat_srv.mapaNickIps.values().toArray();
			for (String f : n) {
				if (f.equals(ls2[1])) {
					rep = "\\nickNotAllowed";
				} else {
					rep = "\\nickAllowed";
					if (tcp_chat_srv.mapaNickIps.containsKey(ip)) {
						tcp_chat_srv.mapaNickIps.put(ip, ls2[1]);
					} else {
						tcp_chat_srv.mapaNickIps.put(ip, ls2[1]);
					}
				}
			}

			System.out.println(ip);

		}
		return rep;
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
			sock = new DatagramSocket(27003);
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
				System.out.println("Conexao fallhada");
			}
			clientIP = pedido.getAddress();
			clientPort = pedido.getPort();
			res = pedido.getLength();
			for (i = 0; i < res; i++) {
				data1[res - 1 - i] = data[i];
			}
			DatagramPacket resposta = new DatagramPacket(data1, res, clientIP, clientPort);
			try {
				sock.send(resposta);
			} catch (IOException ex) {
				System.out.println("Conexao fallhada");
			}
		}
	}
}
