package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.common.IClient;
import ec.edu.ups.sdist.controlador.DifusionGrupo;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author niel
 */
public class CuadroChatGrupo extends JFrame implements ActionListener {

    private String myName;
    private String nameGroup;
    private String idGroup;
    private HashMap<String, IClient> objectGroup;
    private JLabel membersofGroup = new JLabel();
    private JTextArea enteredText = new JTextArea(10, 32);
    private JTextField typedText = new JTextField(32);

    /**
     *
     * @param idGroup
     * @param myName
     * @param nameGroup
     * @param objectGroup
     */
    public CuadroChatGrupo(String idGroup, String myName, String nameGroup,
            HashMap<String, IClient> objectGroup) {

        this.myName = myName;
        this.nameGroup = nameGroup;
        this.idGroup = idGroup;
        this.objectGroup = objectGroup;

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
        showUsers(null);

        Container content = getContentPane();
        content.add(membersofGroup, BorderLayout.NORTH);
        content.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        content.add(typedText, BorderLayout.SOUTH);

        setTitle("LeChat: Grupo [" + nameGroup + "]");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        typedText.requestFocusInWindow();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = "[" + myName + "]: " + typedText.getText();
        enteredText.insert(s + "\n", enteredText.getText().length());
        enteredText.setCaretPosition(enteredText.getText().length());

        for (Object o : objectGroup.keySet()) {
            IClient ic = (IClient) objectGroup.get(o);
            DifusionGrupo bg = new DifusionGrupo(idGroup, nameGroup, myName, (String) typedText.getText(), ic);
            bg.start();
        }
        typedText.setText("");
        typedText.requestFocusInWindow();
    }

    /**
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

    /**
     * Mostramos los usuarios que están en el grupo y en caso de que un usuario
     * abandone el grupo lo notificamos.
     *
     * @param nameUser
     */
    public void showUsers(String nameUser) {
        String msgLabel = "Usuarios: ";
        for (Object o : objectGroup.keySet()) {
            msgLabel += " " + o.toString();
        }
        membersofGroup.setText(msgLabel);

        if (nameUser != null) {
            addMsg("Notificacion:", "El usuario " + nameUser + " ha abandonado el grupo.");
            objectGroup.remove(nameUser);
        }
    }
}
