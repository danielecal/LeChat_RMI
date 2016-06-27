package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import java.rmi.RemoteException;

/**
 * Hilo para notificar a un usuario que se ha deconectado un usuario
 *
 * @author niel
 */
public class CallBackDisconnect extends Thread {

    private String name;
    private IClient obj;

    /**
     *
     * @param name
     * @param obj
     */
    public CallBackDisconnect(String name, IClient obj) {
        this.name = name;
        this.obj = obj;
    }

    @Override
    public void run() {
        try {
            obj.userDiconnected(name);
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
