package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementacion de metodos remotos IClientImpl
 *
 * @author niel
 */
public class IClientImpl extends UnicastRemoteObject implements IClient {

    private Gestion controlador;

    /**
     *
     * @param controlador
     * @throws RemoteException
     */
    public IClientImpl(Gestion controlador) throws RemoteException {
        super();
        this.controlador = controlador;
    }

    /**
     *
     * @param nombre
     * @param objClient
     * @throws RemoteException
     */
    @Override
    public void userConnected(String nombre, IClient objClient) throws RemoteException {
        System.out.println("Se ha conectado:" + nombre);
        controlador.addCliente(nombre, objClient);
    }

    /**
     *
     * @param nombre
     * @throws RemoteException
     */
    @Override
    public void userDiconnected(String nombre) throws RemoteException {
        System.out.println("Se ha desconectado:" + nombre);
        controlador.eliminarCliente(nombre);
    }

    /**
     *
     * @throws RemoteException
     */
    @Override
    public void ping() throws RemoteException {

    }

    /**
     *
     * @param nombre
     * @param msg
     * @throws RemoteException
     */
    @Override
    public void writeInstantMsg(String nombre, String msg) throws RemoteException {
        controlador.mensajeRecibido(nombre, msg);
    }

    /**
     *
     * @param grupoID
     * @param nameGroup
     * @param nameUser
     * @param msg
     */
    @Override
    public void writeGroupMsg(String grupoID, String nameGroup, String nameUser, String msg) {
        controlador.mensajeGrupoRecibido(grupoID, nameGroup, nameUser, msg);
    }

    /**
     *
     * @param grupoID
     * @param nameGroup
     * @param nameUser
     * @param objClients
     * @throws RemoteException
     */
    @Override
    public void joinGroup(String grupoID, String nameGroup, String nameUser, HashMap<String, IClient> objClients) throws RemoteException {
        controlador.entrarGrupo(grupoID, nameGroup, nameUser, objClients);
    }

    /**
     *
     * @param grupoID
     * @param nameUser
     * @throws RemoteException
     */
    @Override
    public void leftGroup(String grupoID, String nameUser) throws RemoteException {
        controlador.eliminarUsuarioEnGrupo(grupoID, nameUser);
    }

    /**
     *
     * @param idGroup
     * @param nameGroup
     * @param users
     * @throws RemoteException
     */
    @Override
    public void newUserGroup(String idGroup, String nameGroup, ArrayList<String> users) throws RemoteException {
        controlador.agregarNuevoUsuarioGrupo(idGroup, nameGroup, users);
    }
}
