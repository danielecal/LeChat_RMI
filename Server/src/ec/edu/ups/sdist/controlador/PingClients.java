package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creamos un hilo que, cada 10 segundos realiza ping a los clientes. Util
 * cuando un cliente haya terminado su conexion de manera abrupta, sin
 * comunicarselo a servidor.
 *
 * @author niel
 */
public class PingClients extends Thread {

    private ConcurrentHashMap<String, IClient> listClients;
    private IServerImpl iserver;
    private boolean activar;

    /**
     * Se encarga de realizar peticiones ping ICMP a cada cliente para verificar
     * que se encuentren activos
     *
     * @param isever
     * @param listClients
     */
    public PingClients(IServerImpl isever, ConcurrentHashMap<String, IClient> listClients) {
        this.iserver = isever;
        this.listClients = listClients;
        activar = true;
    }

    @Override
    public void run() {
        while (activar) {
            for (Object e : listClients.keySet()) {
                IClient obj = (IClient) listClients.get(e);
                try {
                    obj.ping();
                } catch (RemoteException ex) {
                    try {
                        iserver.unregisterClient(e.toString());
                    } catch (RemoteException ex1) {
                        System.out.println(ex1.getMessage());
                        activar = false;
                    }
                }
            }
            try {
                sleep(10000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                activar = false;
            }
        }
    }

    /**
     * Cambiamos el valor de activar para cortar el hilo
     */
    public void stopPings() {
        activar = false;
    }
}
