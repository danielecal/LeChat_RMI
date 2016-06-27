package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.controlador.Gestion;
import ec.edu.ups.sdist.exceptions.UserConnected;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author niel
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        boolean accept = false;
        String name = null;
        Gestion controlador;

        if (args.length != 2) {
            System.out.println("Parametros: <host> <port>");
        } else {
            try {
                Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                System.out.println("Puerto no válido debe contener números");
                System.exit(0);
            }

            controlador = new Gestion(args[0], args[1]);

            do {
                name = setName();
                try {
                    controlador.connectToServer(name);
                    accept = true;
                } catch (UserConnected ex) {
                    JOptionPane.showMessageDialog(null, "El nombre ya se encuentra registrado");
                } catch (ConnectException ex1) {
                    JOptionPane.showMessageDialog(null, "Problema con la conexion");
                    System.exit(0);
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Problema con la conexion");
                    System.exit(0);
                } catch (NotBoundException ex) {
                    JOptionPane.showMessageDialog(null, "Se intento buscar o desenlazar el registro de un nombre no asociado.");
                    System.exit(0);
                } catch (MalformedURLException ex) {
                    JOptionPane.showMessageDialog(null, "URL invalida!");
                    System.exit(0);
                }
            } while (!accept);
        }
    }

    /**
     * Ingreso de nombre en un cuadro de dialogo
     *
     * @return Nombre
     */
    public static String setName() {
        String name;
        do {
            name = JOptionPane.showInputDialog(null, "Escriba su nombre:");
            if (name == null) {
                System.exit(0);
            }
        } while (name.length() == 0);
        return name;
    }
}
