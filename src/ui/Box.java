/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.Controller;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Egidio73
 */
public class Box extends javax.swing.JFrame {

	/**
	 * Creates new form Box
	 */
	private UI ui;

	private Controller controller;

	private List<String> lNicks;

	private Color c;

	private int pos;

	private TrayIcon trayIcone;

	private List<Color> lc;

	private boolean flag;

	private boolean sound;

	private String n;

	private String nameFicheiro;

	public Box(UI ui, Controller controller, boolean sou) {
		nameFicheiro = "";
		initComponents();
		setLocationRelativeTo(null);
		setVisible(true);
		setIconImage(new ImageIcon("LIB\\logo.png").getImage());

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowClosing(WindowEvent e
			) {
				sair();
			}

			@Override
			public void windowOpened(WindowEvent e
			) {

			}

			@Override
			public void windowClosed(WindowEvent e
			) {

			}

			@Override
			public void windowIconified(WindowEvent e
			) {
				minimizar();

			}

			@Override
			public void windowDeiconified(WindowEvent e
			) {

			}

			@Override
			public void windowActivated(WindowEvent e
			) {

			}

			@Override
			public void windowDeactivated(WindowEvent e
			) {

			}
		});

		this.c = Color.BLUE;
		this.controller = controller;
		this.ui = ui;
		this.flag = false;
		this.sound = sou;
		this.lNicks = new ArrayList<>();
		this.ui.setEnabled(
			false);

		iniciaBox();
		listaDeCores();
		janelaServidores();

	}

	private void listaDeCores() {

		lc = new ArrayList<>();

		lc.add(Color.GREEN);
		lc.add(Color.CYAN);
		lc.add(Color.MAGENTA);
		lc.add(Color.ORANGE);
		lc.add(Color.PINK);
		lc.add(Color.RED);
		lc.add(Color.YELLOW);
	}

	private void janelaServidores() {

		jMenuItem9.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JanelaServidores js = new JanelaServidores(controller);

			}
		});
	}

	private void changeFont(String name) {
		Font font = new Font(name, Font.PLAIN, 12);
		jTextPane1.setFont(font);
		jTextField1.setFont(font);
		jTextPane1.updateUI();
		jTextField1.updateUI();

	}

	private void setCaretPosicao(int pos) {

		this.pos = pos;
	}

	private int getCaretPosicao() {

		return pos;
	}

	private void iniciaBox() {

		jMenuItem7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				SelectCorUI cui = new SelectCorUI(Box.this);

			}
		});

		jButton7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				jTextPane1.setText("Chat:");
				jTextPane1.updateUI();

			}
		});

		jMenuItem8.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String oldNick;
				oldNick = controller.getMyNick();

				do {
					n = JOptionPane.
						showInputDialog(rootPane, "Insira novo Nickname (15 caracteres):", "Mudar Nickname", JOptionPane.INFORMATION_MESSAGE);
					if (n == null) {
						n = "erroerroerroerro";

					}
				} while (n.length() > 15 || n.length() == 0);

				String sd[];
				Thread changeNick = new Thread(new Runnable() {

					@Override
					public void run() {
						controller.registaNick(n);
					}
				});
				changeNick.start();

				sd = jTextPane1.getText().split("\n");
				jTextPane1.setText("");
				for (int i = 0; i < sd.length; i++) {
					String sd2[] = sd[i].split(":");
					for (int j = 0; j < sd2.length; j++) {
						if (sd2[j].equals(oldNick)) {

							StyledDocument doc = jTextPane1.
								getStyledDocument();

							Style style = jTextPane1.
								addStyle("I'm a Style", null);

							StyleConstants.setForeground(style, c);

							try {
								doc.
									insertString(doc.getLength(), controller.
												 getMyNick() + ":", style);
							} catch (BadLocationException ei) {
							}

							jTextPane1.setDocument(doc);
							jTextPane1.updateUI();

						} else {
							if (sd2[j].equals("Chat")) {
								StyledDocument doc = jTextPane1.
									getStyledDocument();

								Style style = jTextPane1.
									addStyle("I'm a Style", null);
								StyleConstants.
									setForeground(style, Color.BLACK);

								try {
									doc.
										insertString(doc.getLength(), "Chat:\n", style);
								} catch (BadLocationException ei) {
								}

								jTextPane1.setDocument(doc);
								jTextPane1.updateUI();
							} else {

								StyledDocument doc = jTextPane1.
									getStyledDocument();

								Style style = jTextPane1.
									addStyle("I'm a Style", null);

								StyleConstants.
									setForeground(style, Color.BLACK);

								if (lNicks.contains(sd2[j])) {
									try {
										for (int k = 0; k < lNicks.size(); k++) {
											if (sd2[j].equals(lNicks.get(k))) {
												if (k > lc.size()) {
													StyleConstants.
														setForeground(style, lc.
																	  get(k - lc.
																		  size()));
												} else {
													StyleConstants.
														setForeground(style, lc.
																	  get(k));
												}
											}
										}
										doc.
											insertString(doc.getLength(), sd2[j] + ":", style);
									} catch (BadLocationException ei) {
									}
								} else {
									if (i == sd.length - 1) {
										try {
											doc.
												insertString(doc.getLength(), sd2[j], style);
										} catch (BadLocationException ei) {
										}

										jTextPane1.setDocument(doc);
										jTextPane1.updateUI();
									} else {
										try {
											doc.
												insertString(doc.getLength(), sd2[j] + "\n", style);
										} catch (BadLocationException ei) {
										}

										jTextPane1.setDocument(doc);
										jTextPane1.updateUI();
									}
								}
							}
						}
					}
				}

				jTextPane1.updateUI();
			}
		});

		jMenuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});

		jMenuItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				changeFont("Verdana");

			}
		});

		jMenuItem4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				changeFont("Arial");

			}
		});

		jMenuItem5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				changeFont("Times New Roman");

			}
		});

		jMenuItem6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				changeFont("Comic Sans MS");

			}
		});

		jMenuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				sair();

			}
		});

		jButton3.setVisible(false);
		jButton3.setIcon(new ImageIcon("LIB\\e1.png"));
		jButton3.setText("");
		jButton3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.enviaMsg("\\sendImage img1");

			}
		});

		jButton4.setVisible(false);
		jButton4.setIcon(new ImageIcon("LIB\\e2.png"));
		jButton4.setText("");
		jButton4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.enviaMsg("\\sendImage img2");
			}
		});

		jButton5.setVisible(false);
		jButton5.setIcon(new ImageIcon("LIB\\e3.png"));
		jButton5.setText("");
		jButton5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.enviaMsg("\\sendImage img3");
			}
		});

		jButton6.setVisible(false);
		jButton6.setIcon(new ImageIcon("LIB\\e4.png"));
		jButton6.setText("");
		jButton6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.enviaMsg("\\sendImage img4");
			}
		});

		jMenuItem3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (jMenuItem3.getText().contains("Activar")) {
					jButton3.setVisible(true);
					jButton4.setVisible(true);
					jButton5.setVisible(true);
					jButton6.setVisible(true);
					jMenuItem3.setText("Desactivar Emojis");
					jMenuItem3.updateUI();
				} else {
					jButton3.setVisible(false);
					jButton4.setVisible(false);
					jButton5.setVisible(false);
					jButton6.setVisible(false);
					jMenuItem3.setText("Activar Emojis");
					jMenuItem3.updateUI();
				}

			}
		});

		jTextPane1.setEditable(false);
		jTextPane1.setText("Chat:");

		InputMap im = jTextField1.
			getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = jTextField1.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "entered");
		am.put("entered", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonActionEnter();
			}

		});

		jButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buttonActionEnter();
			}
		});

		jButton2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				sair();
			}
		});

		jTextField1.setDocument(new JTextFieldLimit(40));
		jTextField1.revalidate();

		jTextField1.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldListener();

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldListener();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldListener();
			}
		});

	}

	private void minimizar() {
		Box.this.setState(Frame.ICONIFIED);
		ui.setState(Frame.ICONIFIED);
		ui.setVisible(false);
		Box.this.setVisible(false);

		if (SystemTray.isSupported()) {
			// get the SystemTray instance

			boolean flagFile = true;
			int cont = 1;
			String nameFile = "LIB\\systemTray";
			File f = null;
			while (flagFile) {
				if (new File(nameFile).exists()) {
					nameFile += cont;
				} else {
					f = new File(nameFile);
					System.out.println("Criou systemTray");
					try {
						f.createNewFile();
					} catch (IOException ex) {
						Logger.getLogger(Box.class.getName()).
							log(Level.SEVERE, null, ex);
					}
					flagFile = false;
					setNameFile(nameFile);
				}
				cont++;
			}

			SystemTray tray = SystemTray.getSystemTray();
			// load an image
			Image image = Toolkit.getDefaultToolkit().
				getImage("LIB\\logo.png");
			// create a action listener to listen for default action executed on the tray icon
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tray.remove(trayIcone);
					File f = new File(getNameFile());
					f.delete();
					sairFromTray();
					java.awt.Window win[] = java.awt.Window.getWindows();
					for (int i = 0; i < win.length; i++) {
						win[i].dispose();
					}
					System.exit(0);

				}

			};

			// create a popup menu
			PopupMenu popup = new PopupMenu();
			// create menu item for the default action
			MenuItem exitProgram = new MenuItem("Sair");
			PopupMenu soundOpt = new PopupMenu("Som");
			exitProgram.addActionListener(listener);
			MenuItem onOff = new MenuItem();
			if (sound) {
				onOff.setLabel("Off");
			} else {
				onOff.setLabel("On");
			}

			onOff.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (onOff.getLabel().equals("Off")) {
						sound = false;
						onOff.setLabel("On");
					} else {
						sound = true;
						onOff.setLabel("Off");
					}

				}
			});

			soundOpt.add(onOff);
			popup.add(soundOpt);
			popup.add(exitProgram);

			/// ... add other items
			// construct a TrayIcon
			trayIcone = new TrayIcon(image, "MULTICHAT", popup);
			// set the TrayIcon properties
			trayIcone.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Box.this.setState(Frame.NORMAL);
					ui.setState(Frame.NORMAL);
					ui.setVisible(true);
					ui.toFront();
					Box.this.setVisible(true);
					Box.this.toFront();
					tray.remove(trayIcone);
					File f = new File(getNameFile());
					f.delete();

				}
			});

			try {
				tray.add(trayIcone);
			} catch (AWTException e) {
				System.err.println(e);
			}
		}

//		if (trayIcon != null) {
//			Image image = Toolkit.getDefaultToolkit().
//				getImage("LIB\\logo.png");
//			trayIcon.setImage(image);
//		}
	}

	public void setColor(Color c) {
		this.c = c;

	}

	public Color getColor() {
		return c;

	}

	public void selectColor(Color c) {

		for (String lNick : lNicks) {
			System.out.println(lNick);
		}

		setColor(c);
		String sd[];
		sd = jTextPane1.getText().split("\n");
		jTextPane1.setText("");
		for (int i = 0; i < sd.length; i++) {
			String sd2[] = sd[i].split(":");
			for (int j = 0; j < sd2.length; j++) {
				if (sd2[j].equals(controller.getMyNick())) {

					StyledDocument doc = jTextPane1.
						getStyledDocument();

					Style style = jTextPane1.
						addStyle("I'm a Style", null);

					StyleConstants.setForeground(style, c);

					try {
						doc.
							insertString(doc.getLength(), controller.getMyNick() + ":", style);
					} catch (BadLocationException ei) {
					}

					jTextPane1.setDocument(doc);
					jTextPane1.updateUI();

				} else {
					if (sd2[j].equals("Chat")) {
						StyledDocument doc = jTextPane1.
							getStyledDocument();

						Style style = jTextPane1.
							addStyle("I'm a Style", null);
						StyleConstants.
							setForeground(style, Color.BLACK);

						try {
							doc.
								insertString(doc.getLength(), "Chat:\n", style);
						} catch (BadLocationException ei) {
						}

						jTextPane1.setDocument(doc);
						jTextPane1.updateUI();
					} else {

						StyledDocument doc = jTextPane1.
							getStyledDocument();

						Style style = jTextPane1.
							addStyle("I'm a Style", null);

						StyleConstants.
							setForeground(style, Color.BLACK);

						if (lNicks.contains(sd2[j])) {
							try {
								for (int k = 0; k < lNicks.size(); k++) {
									if (sd2[j].equals(lNicks.get(k))) {
										if (k > lc.size()) {
											StyleConstants.
												setForeground(style, lc.
															  get(k - lc.
																  size()));
										} else {
											StyleConstants.
												setForeground(style, lc.get(k));
										}
									}
								}
								doc.
									insertString(doc.getLength(), sd2[j] + ":", style);
							} catch (BadLocationException ei) {
							}
						} else {
							if (i == sd.length - 1) {
								try {
									doc.
										insertString(doc.getLength(), sd2[j], style);
								} catch (BadLocationException ei) {
								}

								jTextPane1.setDocument(doc);
								jTextPane1.updateUI();
							} else {
								try {
									doc.
										insertString(doc.getLength(), sd2[j] + "\n", style);
								} catch (BadLocationException ei) {
								}

								jTextPane1.setDocument(doc);
								jTextPane1.updateUI();
							}
						}
					}
				}
			}
		}

		jTextPane1.updateUI();

	}

	private void textFieldListener() {
		jLabel1.
			setText(40 - jTextField1.getText().length() + " Caracteres");
		jLabel1.updateUI();

	}

	private void buttonActionEnter() {

		if (!jTextField1.getText().equals("")) {
			Thread novaThread = new Thread(new Runnable() {

				@Override
				public void run() {
					controller.enviaMsg(jTextField1.getText());
				}
			});
			novaThread.start();

		}
	}

	public void recebeMensagem(String s, String namesrv, String ip) {

		if (!s.contains("\\nick")) {
			String ls[] = s.split("break");
			if (s.contains("\\sendImage")) {
				String ls2[] = ls[1].split(" ");

				StyledDocument doc = jTextPane1.getStyledDocument();
				Style style = jTextPane1.addStyle("I'm a Style", null);
				Style style1 = jTextPane1.addStyle("I'm a Style1", null);

				boolean fl = false;
				for (int d = 0; d < lNicks.size(); d++) {
					if (lNicks.get(d).equals(ls[0])) {
						if (d > lc.size()) {
							StyleConstants.setForeground(style, lc.
														 get(d - lc.size()));
						} else {
							StyleConstants.setForeground(style, lc.get(d));
						}
						fl = true;
					}
				}

				if (!fl) {

					int ni = lNicks.size();
					StyleConstants.setForeground(style, lc.get(ni));
					lNicks.add(ls[0]);

				}

				if (controller.getMyNick().equals(ls[0])) {
					StyleConstants.setForeground(style, getColor());
				}

				try {
					if (jCheckBox1.isSelected()) {
						StyleConstants.setForeground(style1, Color.BLACK);
						doc.
							insertString(doc.getLength(), "\n" + "<" + namesrv + "> " + "("+ip+") ", style1);

						doc.
							insertString(doc.getLength(), ls[0] + ": ", style);
					} else {
						doc.
							insertString(doc.getLength(), "\n" + "<" + namesrv + "> ", style1);
                                                doc.
							insertString(doc.getLength(), ls[0] + ": ", style);
					}
				} catch (Exception e) {
					System.out.println("Erro insert UI");
				}

				ImageIcon im = null;
				if (ls2[1].equals("img1")) {
					im = new ImageIcon("LIB\\e1.png");
					im.setDescription("e1");
				} else if (ls2[1].equals("img2")) {
					im = new ImageIcon("LIB\\e2.png");
					im.setDescription("e2");
				} else if (ls2[1].equals("img3")) {
					im = new ImageIcon("LIB\\e3.png");
					im.setDescription("e3");
				} else {
					im = new ImageIcon("LIB\\e4.png");
					im.setDescription("e4");
				}
				setCaretPosicao(doc.getLength());
				jTextPane1.setCaretPosition(getCaretPosicao());
				jTextPane1.insertIcon(im);
				//jTextPane1.updateUI();
			} else {

				StyledDocument doc = jTextPane1.getStyledDocument();
				Style style = jTextPane1.addStyle("I'm a Style", null);
				Style style1 = jTextPane1.addStyle("I'm a Style1", null);
				boolean fl = false;
				for (int d = 0; d < lNicks.size(); d++) {
					if (lNicks.get(d).equals(ls[0])) {
						if (d > lc.size()) {
							StyleConstants.setForeground(style, lc.
														 get(d - lc.size()));
						} else {
							StyleConstants.setForeground(style, lc.get(d));
						}
						fl = true;
					}
				}

				if (!fl) {

					int ni = lNicks.size();
					StyleConstants.setForeground(style, lc.get(ni));
					lNicks.add(ls[0]);
					System.out.println("Tamanho = " + ni);

				}

				if (controller.getMyNick().equals(ls[0])) {
					StyleConstants.setForeground(style, getColor());
				}

				try {

					if (jCheckBox1.isSelected()) {
						StyleConstants.setForeground(style1, Color.BLACK);
						doc.
							insertString(doc.getLength(), "\n" + "<" + namesrv + "> " + "("+ip+") ", style1);

						doc.
							insertString(doc.getLength(), ls[0] + ": ", style);
					} else {
						doc.
							insertString(doc.getLength(), "\n" + "<" + namesrv + "> ", style1);
                                                doc.
							insertString(doc.getLength(), ls[0] + ": ", style);
					}
				} catch (Exception e) {
					System.out.println("Erro insert UI");
				}

				StyleConstants.setForeground(style, Color.BLACK);

				try {
					doc.
						insertString(doc.getLength(), ls[1], style);
				} catch (Exception e) {
					System.out.println("Erro insert UI");
				}

				jTextPane1.setDocument(doc);
				//jTextPane1.updateUI();

				Thread showNot = new Thread(new Runnable() {

					@Override
					public void run() {

						if (new File(getNameFile()).exists()) {
							Notification n2;
							if (!flag) {
								n2 = new Notification(Box.this, ls[0], ls[1]);
								playNotification();
								flag = true;
							} else {
								n2 = new Notification(Box.this, ls[0], ls[1]);
								playNotification();
								flag = true;
							}
						}

					}
				});

				showNot.start();

				jTextField1.setText("");
			}
		}

	}

	private void playNotification() {

		if (sound) {
			File f = new File("LIB\\bip.mp3");
			try {
				FileInputStream fis = new FileInputStream(f);
				BufferedInputStream bis = new BufferedInputStream(fis);

				Player p = new Player(bis);
				p.play();

			} catch (FileNotFoundException | JavaLayerException ex) {
				Logger.getLogger(ListaServidores.class
					.getName()).
					log(Level.SEVERE, null, ex);
			}
		}
	}

	public void fechaNotification() {

		this.flag = false;

	}

	public void menuToFront() {

		ui.toFront();

	}

	public void insertInfo() {

		StyledDocument doc = jTextPane1.getStyledDocument();
		Style style = jTextPane1.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, Color.BLACK);

		try {
			doc.
				insertString(doc.getLength(), "\nErro: Não está conectado a nenhum servidor! A sair...", style);
		} catch (Exception e) {
			System.out.println("Erro insert UI");
		}

		jTextPane1.setDocument(doc);

		Thread df = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000 * 2);
					dispose();
					ui.toFront();
					ui.setEnabled(true);
				} catch (InterruptedException ex) {
					Logger.getLogger(Box.class.getName()).
						log(Level.SEVERE, null, ex);
				}

			}
		});
		df.start();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton7 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MultiChat - RCOMP");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButton1.setText("Enviar");

        jButton2.setText("Sair");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("40 Caracteres");

        jButton3.setText("jButton3");

        jButton4.setText("jButton4");

        jButton5.setText("jButton5");

        jButton6.setText("jButton6");

        jScrollPane2.setViewportView(jTextPane1);

        jButton7.setText("Limpar Ecrã");

        jCheckBox1.setText("Show IP");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox1)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(13, 13, 13))
        );

        jMenu1.setText("Chat");

        jMenuItem8.setText("Mudar Nickname");
        jMenu1.add(jMenuItem8);

        jMenuItem9.setText("Servidores");
        jMenu1.add(jMenuItem9);

        jMenuItem1.setText("Disconnect");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Opções");

        jMenu3.setText("Propriedades");

        jMenuItem3.setText("Activar Emojis");
        jMenu3.add(jMenuItem3);

        jMenuItem7.setText("Cores");
        jMenu3.add(jMenuItem7);

        jMenu4.setText("Fontes");

        jMenuItem2.setText("Verdana");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem4.setText("Arial");
        jMenu4.add(jMenuItem4);

        jMenuItem5.setText("TimesNewRoman");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("ComicSans");
        jMenu4.add(jMenuItem6);

        jMenu3.add(jMenu4);

        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void sair() {
		Thread novaThread = new Thread(new Runnable() {

			@Override
			public void run() {
				controller.enviaMsg("\\Sair");
			}
		});
		novaThread.start();
		dispose();
		controller.disconnect();
		this.ui.setEnabled(true);
		this.ui.toFront();

	}

	private void sairFromTray() {
		Thread novaThread = new Thread(new Runnable() {

			@Override
			public void run() {
				controller.enviaMsg("\\Sair");
			}
		});
		novaThread.start();

	}

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

	private String getNameFile() {
		return this.nameFicheiro;
	}

	private void setNameFile(String nameFile) {
		this.nameFicheiro = nameFile;
	}
}
