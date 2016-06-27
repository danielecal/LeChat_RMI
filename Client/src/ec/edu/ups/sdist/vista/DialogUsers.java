package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.common.IClient;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Esta clase muestra los usuarios que puede elegir el cliente para invitarlos
 * al grupo que vaya a crear
 *
 * @author niel
 */
public class DialogUsers extends JDialog implements ActionListener {

    private String name;
    private JButton accept;
    private JButton cancel;
    private JCheckBox[] checkBox;
    private ConcurrentHashMap<String, IClient> list;
    private ArrayList<String> userSelected;
    private JPanel panelButton;
    private JPanel panelchecks;
    private PanelGroup panelGroup;
    private String idGroup;
    private HashMap<String, IClient> usrObj;

    /**
     * Contructor, se invoca cuando se crea el grupo. Aparecen todos los
     * usuarios de la lista
     *
     * @param idGroup
     * @param nameGroup
     * @param list
     * @param panelGroup
     */
    public DialogUsers(String idGroup, String nameGroup, ConcurrentHashMap<String, IClient> list,
            PanelGroup panelGroup) {
        this.idGroup = idGroup;
        this.name = nameGroup;
        this.list = list;
        this.panelGroup = panelGroup;
        userSelected = new ArrayList<>();
        initialize();
    }

    /**
     * Constructor. Para cuando se quiere añadir usuarios a un grupo ya creado.
     */
    DialogUsers(String idGroup, String nameGroup, ConcurrentHashMap<String, IClient> list,
            HashMap<String, IClient> usrObj,
            PanelGroup panelGroup) {
        this.idGroup = idGroup;
        this.name = nameGroup;
        this.list = list;
        this.usrObj = usrObj;
        this.panelGroup = panelGroup;
        userSelected = new ArrayList<>();
        initialize();
    }

    /* Inicializamos componentes*/
    private void initialize() {

        panelButton = new JPanel(new FlowLayout());
        panelchecks = new JPanel();
        panelchecks.setLayout(new BoxLayout(panelchecks, BoxLayout.Y_AXIS));

        accept = new JButton("Aceptar");
        accept.addActionListener(this);
        cancel = new JButton("Cancelar");
        cancel.addActionListener(this);

        panelButton.add(accept);
        panelButton.add(cancel);

        checkBox = new JCheckBox[list.size()];

        int i = 0;
        for (Object e : list.keySet()) {
            /* Miramos si usrObj es nulo para saber si el grupo ya sta creado o no*/
 /* Evitamos poner usuarios que ya estan dentro del grupo*/
            if (usrObj != null && usrObj.containsKey(e)) {
                checkBox[i] = new JCheckBox(e.toString());
            } else {
                checkBox[i] = new JCheckBox(e.toString());
                panelchecks.add(checkBox[i]);
            }
            i++;
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelchecks, BorderLayout.NORTH);
        getContentPane().add(panelButton, BorderLayout.SOUTH);
        pack();
        setTitle("Lista de usuarios");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(accept.getText())) {
            for (int i = 0; i < list.size(); i++) {
                JCheckBox c = checkBox[i];
                if (c.isSelected()) {
                    userSelected.add(c.getText());
                }
            }
            if (usrObj == null) {
                panelGroup.setSelectedUsers(idGroup, name, userSelected);
            } else {
                panelGroup.addMoreUserToGroup(idGroup, name, userSelected);
            }
            this.dispose();
        } else if (e.getActionCommand().equals(cancel.getText())) {
            this.dispose();
        }
    }
}
