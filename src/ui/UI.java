package ui;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * Classe para apresentar janela com introdução ao objetivo do programa e a
 * opção de importação de dados de um ficheiro binário
 *
 */
public class UI extends JFrame {

	//Tamanho da área de texto
	private Dimension TXT_TAMANHO = new Dimension(585, 400);

	private Controller controller;

	public JLabel back;

	/**
	 * Cria a Janela com o menu.
	 *
	 * @throws IOException
	 */
	public UI() throws IOException {
		super("MULTICAST");
		this.controller = new Controller();

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(criarMenu());
		menuBar.add(criarAcerca());

		setIconImage(new ImageIcon("LIB\\logo.png").getImage());
		back = new JLabel(new ImageIcon("LIB\\main.png"));

		back.setLayout(new FlowLayout());

		setLayout(new BorderLayout());
		add(back, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fechar();
			}
		});

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(750, 400);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * Cria o menu.
	 *
	 * @return Menu.
	 * @throws IOException
	 */
	private JMenu criarMenu() throws IOException {
		final JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menu.add(connectSever());
		menu.addSeparator();
		menu.add(criarItemSair());
		return menu;
	}

	/**
	 * Criar o menu Acerca.
	 *
	 * @return menu
	 */
	private JMenu criarAcerca() {
		JMenu menu = new JMenu("Acerca");
		menu.setMnemonic(KeyEvent.VK_A);

		menu.add("António Pinheiro - 1130339").setEnabled(false);
		menu.add("Cristina Lopes - 1130371").setEnabled(false);
		menu.add("Egídio Santos - 1130348").setEnabled(false);

		return menu;
	}

	/**
	 * Criar item Sair
	 *
	 * @return item
	 */
	private JMenuItem criarItemSair() {
		JMenuItem item = new JMenuItem("Sair");
		item.setAccelerator(KeyStroke.
			getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});

		return item;
	}

	/**
	 *
	 * Criar item para importação de um ficheiro
	 *
	 * @return
	 */
	private JMenuItem connectSever() {
		JMenuItem item = new JMenuItem("Connect", KeyEvent.VK_F);
		item.setAccelerator(KeyStroke.
			getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListaServidores l = new ListaServidores(UI.this, controller);
				l.setVisible(true);
			}
		});

		return item;
	}

	/**
	 * Opção para perguntar se deseja fechar a aplicação.
	 */
	private void fechar() {

		back.setEnabled(false);
		back.updateUI();

		String[] opSimNao = {"Sim", "Não"};
		int resposta = JOptionPane.showOptionDialog(this,
													"Fechar ?",
													"RCOMP",
													0,
													JOptionPane.QUESTION_MESSAGE,
													null,
													opSimNao,
													opSimNao[1]);

		final int SIM = 0;
		if (resposta == SIM) {
			dispose();
			SystemTray tray = SystemTray.getSystemTray();
			TrayIcon li[] = tray.getTrayIcons();
			for (int i = 0; i < li.length; i++) {
				tray.remove(li[i]);
			}
			File f = new File("LIB\\systemTray");
			if (f.exists()) {
				f.delete();
			}
			System.exit(0);
		} else {
			back.setEnabled(true);
			back.updateUI();
		}
	}

}
