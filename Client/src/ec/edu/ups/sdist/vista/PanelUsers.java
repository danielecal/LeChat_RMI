package ec.edu.ups.sdist.vista;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author niel
 */
public class PanelUsers extends JPanel {
    
    private PanelView panel;
    private JList<String> listClients;
    private DefaultListModel<String> listModel;
    
    /**
     *
     * @param panel
     */
    public PanelUsers(PanelView panel){
        this.panel = panel;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        
        listModel = new DefaultListModel<>();
        listClients = new JList<>(listModel);
        listClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        listClients.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if(evt.getClickCount() == 2 && listClients.getSelectedIndex() > -1){
                    panel.openChat((String) listModel.get(listClients.getSelectedIndex()));
                    System.out.println(listModel.getElementAt(listClients.getSelectedIndex()));
                }
            }
        });
        add(listClients);
    }

    /**
     *
     * @param name
     */
    public void addClientToList(String name) {
        listModel.addElement(name);
    }
    
    /**
     *
     * @param name
     */
    public void removeClientToList(String name) {
        listModel.removeElement(name);
    }
}
