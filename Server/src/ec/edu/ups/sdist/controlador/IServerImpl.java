package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import ec.edu.ups.sdist.common.IServer;
import ec.edu.ups.sdist.exceptions.UserConnected;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementacion del objeto remoto Iserver
 *
 * @author niel
 */
public class IServerImpl extends UnicastRemoteObject implements IServer {

    /**
     * Utilizamos una ConcurrentHashMap para sincronizar el acceso a este objeto
     * con el fin de mantener la integridad de los datos
     */
    private ConcurrentHashMap<String, IClient> clientList;
    private PingClients pingClients;

    /**
     *
     * @throws RemoteException
     */
    public IServerImpl() throws RemoteException {
        super();
        clientList = new ConcurrentHashMap<String, IClient>();
        pingClients = new PingClients(this, clientList);
        pingClients.start();
    }

    /**
     * Método remoto en el que un cliente se registra proporcionando su nombre
     * de usuario y su objeto remoto. Este método devuelve la lista de usuarios
     * conectados al cliente nuevo.
     *
     * @param name
     * @param callbackClientObject
     * @return
     * @throws RemoteException
     * @throws UserConnected
     */
    @Override
    public ConcurrentHashMap<String, IClient> registerClient(String name,
            IClient callbackClientObject) throws RemoteException, UserConnected {
        /**
         * Si el usuario no existe lo añadimos a la lista y lo comunicamos a
         * todos los usuarios conectados actualmente
         */
        synchronized (clientList) {
            if (!(clientList.containsKey(name))) {
                /**
                 * Si no hacemos un sychronized aqui, podría a ver un cambio de
                 * contexto y no garantizar los datos.
                 */
                doCallbacks(name, callbackClientObject);
                clientList.put(name, callbackClientObject);
                System.out.println("Nuevo miembro registrado, total de miembros: " + clientList.size());
            } else {
                throw new UserConnected();
            }
        }
        return clientList;
    }

    /**
     * Método remoto que devuelve la lista de todos los clientes conectados
     *
     * @return @throws RemoteException
     */
    @Override
    public ConcurrentHashMap<String, IClient> getClients() throws RemoteException {
        return clientList;
    }

    /**
     * Este método elimina el usuario de la lista de clientes cuando un cliente
     * se desconecta
     *
     * @param name
     * @throws RemoteException
     */
    @Override
    public void unregisterClient(String name) throws RemoteException {
        synchronized (clientList) {
            if (clientList.containsKey(name)) {
                clientList.remove(name);
                doCallBackUnregister(name);
                System.out.println("Cliente desconectado");
            }
        }
    }

    /**
     * Método que devuelve el objeto de un cliente en concreto en caso de no
     * existir devuelve null
     *
     * @param name
     * @return
     * @throws RemoteException
     */
    @Override
    public IClient searchClient(String name) throws RemoteException {
        return (IClient) clientList.get(name);
    }

    /**
     * Método privado no remoto. Se utiliza para comunicar a todos los usuarios
     * que se ha conectado un nuevo usuario y le enviamos al usuario el nombre y
     * el objeto del usuario nuevo. Hacemos el uso de threads así en caso de
     * fallo sólo se verá afectado ese hilo y los demás podrán seguir con su
     * tarea.
     */
    private void doCallbacks(String newClient, IClient objClient) throws RemoteException {
        for (Object actualClient : clientList.keySet()) {
            if (!actualClient.toString().equals(newClient)) {
                IClient client = (IClient) clientList.get(actualClient);
                CallBackConnect cb = new CallBackConnect(newClient, client, objClient);
                cb.start();
            }
        }
    }

    /**
     * Realiza la misma función que el método doCallbacks con la diferencia que
     * notifica desconexión de un cliente
     */
    private void doCallBackUnregister(String name) throws RemoteException {
        for (Object e : clientList.keySet()) {
            IClient client = (IClient) clientList.get(e);
            CallBackDisconnect cd = new CallBackDisconnect(name, client);
            cd.start();
        }
    }
}
