package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.common.IClient;
import ec.edu.ups.sdist.controlador.Gestion;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JFrame;

/**
 * Extension de JFrame, llama toda la interfaz grafica de la aplicacion
 *
 * @author niel
 */
public class MainFrame extends JFrame {

    private PanelView panel;
    private Gestion controlador;

    /**
     *
     * @param controlador
     * @param panel
     */
    public MainFrame(Gestion controlador, PanelView panel) {

        this.controlador = controlador;
        this.panel = panel;
        initialize();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        addClients();
    }

    private void initialize() {
        panel.setUserName(controlador.getNombreUsuario());
        setLayout(new java.awt.BorderLayout());
        add(panel, java.awt.BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.disconnectToServer();
                System.exit(0);
            }
        });
        setTitle("Chat de " + controlador.getNombreUsuario());
        setMinimumSize(new Dimension(250, 400));
        setVisible(true);
        pack();
    }

    /**
     * Al ser la primera vez que se inicia el programa obtenemos todos los
     * clientes y los añadimos a la lista.
     */
    private void addClients() {
        ConcurrentHashMap<String, IClient> clients = controlador.getClients();
        for (Object actualClient : clients.keySet()) {
            panel.addClientToList(actualClient.toString());
        }
    }
}
