package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.common.IClient;
import ec.edu.ups.sdist.controlador.ControladorChat;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author niel
 */
public class FrameChat extends JFrame implements ActionListener {

    private String myName;
    private String hostName;
    private JTextArea enteredText = new JTextArea(10, 32);
    private JTextField typedText = new JTextField(32);
    private IClient objClient;
    private ControladorChat controlChat;

    /**
     *
     * @param myName
     * @param hostName
     * @param objClient
     * @param controlChat
     */
    public FrameChat(String myName, String hostName, IClient objClient,
            ControladorChat controlChat) {

        this.myName = myName;
        this.hostName = hostName;
        this.objClient = objClient;
        this.controlChat = controlChat;

        addWindowListener(
                new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //System.exit(0);
            }
        }
        );

        enteredText.setEditable(false);
        enteredText.setBackground(Color.LIGHT_GRAY);
        typedText.addActionListener(this);

        Container content = getContentPane();
        content.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        content.add(typedText, BorderLayout.SOUTH);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        setTitle("LeChat: [" + hostName + "]");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        typedText.requestFocusInWindow();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = "[" + myName + "]: " + typedText.getText();
        enteredText.insert(s + "\n", enteredText.getText().length());
        enteredText.setCaretPosition(enteredText.getText().length());

        try {
            objClient.writeInstantMsg(myName, (String) typedText.getText());
        } catch (RemoteException ex) {
            controlChat.reportarError(hostName);
            JOptionPane.showMessageDialog(null, "Problema de conextion con este cliente");
            this.dispose();
        }

        typedText.setText("");
        typedText.requestFocusInWindow();
    }

    /**
     * Anadimos un mensaje al textarea
     *
     * @param name
     * @param msg
     */
    public void addMsg(String name, String msg) {
        String s = "[" + name + "]: " + msg;
        enteredText.insert(s + "\n", enteredText.getText().length());
        enteredText.setCaretPosition(enteredText.getText().length());
        typedText.requestFocusInWindow();
    }
}
