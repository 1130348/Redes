/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.Controller;
import controller.Server;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Egidio73
 */
public class ListaServidores extends javax.swing.JFrame {

	private UI ui;

	private Controller controller;

	private List ls;

	private List ls2;

	private JCheckBox checkSound;

	private JTextField te;

	private JLabel f;

	/**
	 * Creates new form ListaServidores
	 *
	 * @param ui
	 * @param controller
	 */
	public ListaServidores(UI ui, Controller controller) {

		List<Server> ls = new ArrayList<>();

		List<String> lIP = controller.importFile("teste.txt");
		for (String lIP1 : lIP) {
			Server s1 = new Server();
			try {
				s1.setIp(InetAddress.getByName(lIP1));
			} catch (UnknownHostException ex) {
				Logger.getLogger(ListaServidores.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			ls.add(s1);
		}

		controller.setlServers(ls);

		initComponents();
		this.ui = ui;
		this.controller = controller;
		setLocationRelativeTo(null);

		jButton1.setEnabled(false);
		jButton2.setEnabled(false);

		setIconImage(new ImageIcon("LIB\\server.png").getImage());
		iniciaListas();
		iniciaProgressBar();

	}

	private void iniciaListas() {
		jPanel2.setLayout(new BorderLayout());
		f = new JLabel(new ImageIcon("LIB\\loading.gif"));
		jPanel2.add(f);
		f.setVisible(false);
		jPanel2.updateUI();
		jPanel2.setVisible(true);

		boolean fl = true;
		ls = new ArrayList();
		ls2 = new ArrayList();
		if (controller.isFlag()) {
			for (int i = 0; i < controller.getlServers().size(); i++) {
				for (int j = 0; j < controller.getlServersActivos().size(); j++) {
					if (controller.getlServers().get(i).
						equals(controller.
							getlServersActivos().get(j))) {
						fl = false;
					}
				}
				if (fl) {
					ls.add(controller.getlServers().get(i));
				}
				fl = true;
			}
		} else {
			ls = controller.getlServers();
		}

		if (ls.isEmpty()) {
			ls.add("Vazio");
			jList1.setEnabled(false);
		}

		ListSelectionModel listSelectionModel = jList1.getSelectionModel();
		listSelectionModel.
			addListSelectionListener(new ListSelectionListener() {

				int cont = 0;

				@Override
				public void valueChanged(ListSelectionEvent e) {

					jProgressBar1.setVisible(false);
					jProgressBar1.setValue(0);

					if (cont == 0) {
						cont++;
						int firstIndex = e.getFirstIndex();
						int lastIndex = e.getLastIndex();
						boolean isAdjusting = e.getValueIsAdjusting();

						if (jList1.isSelectionEmpty()) {

						} else {
							// Find out which indexes are selected.
							int minIndex = jList1.getMinSelectionIndex();
							int maxIndex = jList1.getMaxSelectionIndex();
							for (int i = minIndex; i <= maxIndex; i++) {
								if (jList1.isSelectedIndex(i)) {

									int c = jList1.getSelectedIndex();
									if (ls2.contains("Vazio")) {
										ls2.remove("Vazio");
									}
									ls2.add(ls.get(c));
									ls.remove(c);

									jList1.removeAll();
									jList2.removeAll();

									jList1.setListData(ls.toArray());
									jList2.setListData(ls2.toArray());

									jList1.updateUI();
									jList2.updateUI();

								}
							}
						}
					} else {
						cont = 0;

					}

					if (ls.isEmpty()) {
						ls.add("Vazio");
						jList1.setListData(ls.toArray());
						jList1.setEnabled(false);
						jList1.updateUI();
					}

					jButton2.setEnabled(false);
					jButton2.updateUI();

					jList2.setEnabled(true);
					jButton1.setEnabled(true);
					jButton1.updateUI();

				}
			});

		jList1.setListData(ls.toArray());
		jList1.updateUI();

		ListSelectionModel listSelectionModel2 = jList2.getSelectionModel();
		listSelectionModel2.
			addListSelectionListener(new ListSelectionListener() {

				int cont = 0;

				@Override
				public void valueChanged(ListSelectionEvent e) {

					jProgressBar1.setVisible(false);
					jProgressBar1.setValue(0);

					if (cont == 0) {
						cont++;
						int firstIndex = e.getFirstIndex();
						int lastIndex = e.getLastIndex();
						boolean isAdjusting = e.getValueIsAdjusting();

						if (jList2.isSelectionEmpty()) {

						} else {
							// Find out which indexes are selected.
							int minIndex = jList2.getMinSelectionIndex();
							int maxIndex = jList2.getMaxSelectionIndex();
							for (int i = minIndex; i <= maxIndex; i++) {
								if (jList2.isSelectedIndex(i)) {

									int c = jList2.getSelectedIndex();
									if (ls.contains("Vazio")) {
										ls.remove("Vazio");
									}
									ls.add(ls2.get(c));
									ls2.remove(c);

									jList1.removeAll();
									jList2.removeAll();

									jList1.setListData(ls.toArray());
									jList2.setListData(ls2.toArray());

									jList1.updateUI();
									jList2.updateUI();

								}
							}
						}
					} else {
						cont = 0;

					}

					if (ls2.isEmpty()) {
						ls2.add("Vazio");
						jList2.setListData(ls2.toArray());
						jList2.setEnabled(false);
						jList2.updateUI();
						jButton1.setEnabled(false);
						jButton1.updateUI();
						jButton2.setEnabled(false);
						jButton2.updateUI();
					}

					jList1.setEnabled(true);

				}
			}
			);

		if (controller.isFlag()) {
			if (controller.getlServersActivos() != null) {
				ls2 = controller.getlServersActivos();
			}
			jList2.setListData(ls2.toArray());
			jButton1.setEnabled(true);
			jList2.updateUI();

		} else {

			ls2.add("Vazio");
			jList2.setEnabled(false);
			jList2.setListData(ls2.toArray());
			jList2.updateUI();

		}

	}

	private void iniciaProgressBar() {

		jProgressBar1.setStringPainted(true);
		jProgressBar1.setIndeterminate(true);
		jProgressBar1.setVisible(false);

	}

	private boolean valida(String nick) {

		if (nick.contains(".") || nick.contains("?") || nick.contains("!") || nick.
			contains("@") || nick.length() > 15 || nick.length() == 0) {
			return false;
		}

		return true;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Servidores");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jList1ComponentMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Validar Servidores");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Continue");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jLabel1.setText("Servidores Disponíveis");

        jLabel2.setText("Servidores Adicionados");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(126, 126, 126)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(35, 35, 35)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

		buttonValidar();

    }//GEN-LAST:event_jButton1ActionPerformed

	private void buttonValidar() {

		if (!ls2.isEmpty() && ls2.get(0) != "Vazio") {
			controller.setlServersActivos(ls2);

			f.setVisible(true);
			jPanel2.updateUI();

			jProgressBar1.setVisible(true);
			jProgressBar1.setMaximum(100);
			jProgressBar1.setMinimum(0);
			List<Server> lt = controller.getlServersActivos();
			for (int j = 1; j <= lt.size(); j++) {
				if (controller.testConnection(lt.get(j - 1))) {
					jProgressBar1.setValue(j * ((100 / lt.size()) + (100 % lt.
						size())));

				} else {
					jProgressBar1.setValue(0);
					jProgressBar1.setVisible(false);
					jButton2.setEnabled(false);

					Server tes = lt.get(j - 1);
					for (int k = 0; k < ls2.size(); k++) {
						if (ls2.get(k).equals(tes)) {
							if (ls.contains("Vazio")) {
								ls.remove("Vazio");
								ls.add(ls2.get(k));
								ls2.remove(k);
							} else {
								ls.add(ls2.get(k));
								ls2.remove(k);
							}

							jProgressBar1.updateUI();
							jList1.removeAll();
							jList1.setListData(ls.toArray());
							jList2.removeAll();
							if (ls2.isEmpty()) {
								ls2.add("Vazio");
							}
							jList2.setListData(ls2.toArray());
							if (ls2.contains("Vazio")) {
								jList2.setEnabled(false);
							}

							jList1.setEnabled(true);
							jList1.updateUI();
							jList2.updateUI();
							jPanel1.updateUI();

							String warn = tes.toString();
							JOptionPane.
								showMessageDialog(rootPane, "Server: " + warn + " não esta disponível! ", "Erro", JOptionPane.INFORMATION_MESSAGE);

						}
					}

					controller.setlServersActivos(null);
				}

				if (j == lt.size()) {
					jButton2.setEnabled(true);
				}
			}
			f.setVisible(false);
			jPanel2.updateUI();

		} else {

			JOptionPane.
				showMessageDialog(rootPane, "Lista Vazia!", "Erro", JOptionPane.INFORMATION_MESSAGE);

		}

	}

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

		this.dispose();

		JLabel nn = new JLabel("NickName :");

		te = new JTextField(30);
		JTextFieldLimit j = new JTextFieldLimit(15);
		te.setDocument(j);

		JButton button = new JButton("Connect");

		InputMap im = te.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = te.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "entered");
		am.put("entered", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAccao();
			}

		});

		checkSound = new JCheckBox("Som");
		checkSound.setSelected(true);

		checkSound.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkSound.isSelected()) {
					Thread play = new Thread(new Runnable() {

						@Override
						public void run() {

							File f = new File("LIB\\bip.mp3");
							try {
								FileInputStream fis = new FileInputStream(f);
								BufferedInputStream bis = new BufferedInputStream(fis);

								Player p = new Player(bis);
								p.play();

							} catch (FileNotFoundException ex) {
								Logger.
									getLogger(ListaServidores.class.getName()).
									log(Level.SEVERE, null, ex);
							} catch (JavaLayerException ex) {
								Logger.
									getLogger(ListaServidores.class.getName()).
									log(Level.SEVERE, null, ex);

							}
						}
					});
					play.start();
				}

			}
		}
		);

		button.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e
				) {

					buttonAccao();

				}
			}
		);

		ui.back.removeAll();
		ui.back.add(nn);
		ui.back.add(te);
		ui.back.add(button);
		ui.back.add(checkSound);
		ui.back.updateUI();

		dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

	private void buttonAccao() {

		if (valida(te.getText()) == true) {
			Box b = new Box(ui, controller, checkSound.isSelected());
			controller.setBox(b);
			Thread tconnect = new Thread(new Runnable() {

				@Override
				public void run() {
					controller.connect(b);
				}
			});
			tconnect.start();

			Thread nThre = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 1);
					} catch (InterruptedException ex) {
						Logger.getLogger(ListaServidores.class.getName()).
							log(Level.SEVERE, null, ex);
					}
					controller.registaNick(te.getText());
				}
			});
			nThre.start();

		} else {

			JOptionPane.
				showMessageDialog(rootPane, "O seu Nickname contem caracteres nao permitidos ou tem demasiados caracteres (maximo 15)!", "Erro", JOptionPane.INFORMATION_MESSAGE);
			te.setText("");

		}

	}

    private void jList1ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jList1ComponentMoved

    }//GEN-LAST:event_jList1ComponentMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
