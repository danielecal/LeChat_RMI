package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import java.rmi.RemoteException;

/**
 * Hilo para notificar a un usuario que se ha conectado un nuevo usuario
 *
 * @author niel
 */
public class CallBackConnect extends Thread {

    private String newClient;
    private IClient obj;
    private IClient objToSend;

    /**
     *
     * @param newClient
     * @param obj
     * @param objToSend
     */
    public CallBackConnect(String newClient, IClient obj, IClient objToSend) {
        this.newClient = newClient;
        this.obj = obj;
        this.objToSend = objToSend;
    }

    @Override
    public void run() {
        try {
            obj.userConnected(newClient, objToSend);
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
