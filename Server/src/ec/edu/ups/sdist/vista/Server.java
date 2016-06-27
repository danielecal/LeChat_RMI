package ec.edu.ups.sdist.vista;

import ec.edu.ups.sdist.controlador.IServerImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Clase principal de servidor, aqui se instancia la conexcion, nombres y puerto
 *
 * @author niel
 */
public class Server {

    /**
     *
     * @param args
     */
    public static void main(String args[]) {

        String portNum, registryURL;
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        /**
         * Obtenemos el puerto e iniciamos el servidor RMI en el host:
         * niel.akasha
         */
        try {
            System.out.println("Ingrese numero de puerto para registrar RMI:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);

            System.setProperty("java.rmi.server.hostname", "niel.akasha");

            /**
             * Creamos el objeto remoto y lo vinculamos
             */
            IServerImpl exportedObj = new IServerImpl();
            registryURL = "rmi://niel.akasha:" + portNum + "/P2P";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Servidor iniciado...");

        } catch (NumberFormatException ex) {
            System.out.println("Ingrese solamente numeros en el puerto");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Funcion que inicia al Registro del servidor
     *
     * @param RMIPortNum
     */
    private static void startRegistry(int RMIPortNum) {
        try {
            LocateRegistry.createRegistry(RMIPortNum);
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
