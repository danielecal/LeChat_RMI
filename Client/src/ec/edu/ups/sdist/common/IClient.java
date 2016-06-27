package ec.edu.ups.sdist.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz remota del cliente. Aquí están definidos los métodos remotos del
 * cliente
 *
 * @author niel
 */
public interface IClient extends Remote {

    /**
     *
     * @param name
     * @param objClient
     * @throws RemoteException
     */
    public void userConnected(String name, IClient objClient) throws RemoteException;

    /**
     *
     * @param name
     * @throws RemoteException
     */
    public void userDiconnected(String name) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    public void ping() throws RemoteException;

    /**
     *
     * @param name
     * @param msg
     * @throws RemoteException
     */
    public void writeInstantMsg(String name, String msg) throws RemoteException;

    /**
     *
     * @param idGroup
     * @param nameGroup
     * @param nameUser
     * @param msg
     * @throws RemoteException
     */
    public void writeGroupMsg(String idGroup, String nameGroup, String nameUser, String msg) throws RemoteException;

    /**
     *
     * @param idGroup
     * @param nameGroup
     * @param nameUser
     * @param objClients
     * @throws RemoteException
     */
    public void joinGroup(String idGroup, String nameGroup, String nameUser, HashMap<String, IClient> objClients) throws RemoteException;

    /**
     *
     * @param idGroup
     * @param nameUser
     * @throws RemoteException
     */
    public void leftGroup(String idGroup, String nameUser) throws RemoteException;

    /**
     *
     * @param idGroup
     * @param nameGroup
     * @param users
     * @throws RemoteException
     */
    public void newUserGroup(String idGroup, String nameGroup, ArrayList<String> users) throws RemoteException;
}
