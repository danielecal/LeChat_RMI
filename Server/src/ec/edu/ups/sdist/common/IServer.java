package ec.edu.ups.sdist.common;

import ec.edu.ups.sdist.exceptions.UserConnected;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interfaz remota. Aquí están definidos los métodos que son remotos y se pueden
 * acceder a ellos
 *
 * @author niel
 */
public interface IServer extends Remote {

    /**
     *
     * @param name
     * @param callbackClientObject
     * @return
     * @throws RemoteException
     * @throws UserConnected
     */
    public ConcurrentHashMap<String, IClient> registerClient(String name, IClient callbackClientObject
    ) throws RemoteException, UserConnected;

    /**
     *
     * @return @throws RemoteException
     */
    public ConcurrentHashMap<String, IClient> getClients() throws RemoteException;

    /**
     *
     * @param name
     * @throws RemoteException
     */
    public void unregisterClient(String name) throws RemoteException;

    /**
     *
     * @param name
     * @return
     * @throws RemoteException
     */
    public IClient searchClient(String name) throws RemoteException;
}
