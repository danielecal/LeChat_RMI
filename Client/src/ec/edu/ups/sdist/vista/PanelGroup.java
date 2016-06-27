package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.common.IClient;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author niel
 */
public class PanelGroup extends JPanel implements ActionListener {

    private JList<String> listGroups;
    private DefaultListModel<String> listModel;
    private PanelView panelView;
    private ConcurrentHashMap<String, IClient> listUsers;
    private ArrayList<String> listidGroup;

    /**
     *
     * @param panelView
     * @param listUsers
     */
    public PanelGroup(PanelView panelView, ConcurrentHashMap<String, IClient> listUsers) {
        this.panelView = panelView;
        this.listUsers = listUsers;
        this.listidGroup = new ArrayList<>();
        initialize();
    }


    private void initialize() {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel();
        listGroups = new JList(listModel);
        listGroups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listGroups.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if (evt.getClickCount() == 2 && listGroups.getSelectedIndex() > - 1) {
                    
                    panelView.openGroupChat(listidGroup.get(listGroups.getSelectedIndex()), (String) listModel.get(listGroups.getSelectedIndex()));
                    System.out.println(listModel.getElementAt(listGroups.getSelectedIndex()) + " ff " + listGroups.getSelectedIndex());
                }
            }
        });
        add(listGroups);
    }

    /**
     *
     * @param idGroup
     * @param name
     */
    public synchronized void addGroupToList(String idGroup, String name) {
        listModel.addElement(name);
        listidGroup.add(idGroup);
    }

    /**
     *
     * @param idGroup
     */
    public synchronized void removeGroupToList(String idGroup) {
        int indexID = listidGroup.indexOf(idGroup);
        listModel.remove(indexID);
        listidGroup.remove(indexID);
        panelView.leftGroup(idGroup);
    }

    /**
     *
     * @param idGroup
     * @param nameGroup
     * @param users
     */
    public void setSelectedUsers(String idGroup, String nameGroup, ArrayList<String> users) {
        panelView.createGroup(idGroup, nameGroup, users);
    }

    /**
     *
     * @param idGroup
     * @param nameGroup
     * @param users
     */
    public void addMoreUserToGroup(String idGroup, String nameGroup, ArrayList<String> users) {
        panelView.addMoreUserToGroup(idGroup, nameGroup, users);
    }


    private String generateID() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (listUsers.size() < 1) {
            JOptionPane.showMessageDialog(null, "No hay usuarios para invitar");
        } else if (e.getActionCommand().equals("Crear grupo")) {
            String nameGroup = JOptionPane.showInputDialog(null, "Nombre del grupo");
            if (nameGroup != null) {
                String idGroup = generateID();
                new DialogUsers(idGroup, nameGroup, listUsers, this);
                addGroupToList(idGroup, nameGroup);
            }

        } else if (e.getActionCommand().equals("Anadir usuario")) {
            System.out.println("añadir usuario");
            if (listGroups.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(null, "Debe primero seleccionar un grupo");
            } else {
                String idGroup = listidGroup.get(listGroups.getSelectedIndex());
                String nameGroup = (String) listModel.get(listGroups.getSelectedIndex());
                HashMap<String, IClient> usrObj = panelView.getUserOfGroup(idGroup);
                new DialogUsers(idGroup, nameGroup, listUsers, usrObj, this);
            }

        } else if (e.getActionCommand().equals("Dejar grupo")) {
            if (listGroups.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un grupo");
            } else {
                removeGroupToList(listidGroup.get(listGroups.getSelectedIndex()));
            }
        }
    }
}
