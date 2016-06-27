package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import ec.edu.ups.sdist.common.IServer;
import ec.edu.ups.sdist.exceptions.UserConnected;
import ec.edu.ups.sdist.vista.MainFrame;
import ec.edu.ups.sdist.vista.PanelView;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;

/**
 * Intermediario entre los metodos remotos y la interfaz grafica
 *
 * @author niel
 */
public class Gestion {

    private ConcurrentHashMap<String, IClient> clientes;
    private ConcurrentHashMap<String, HashMap<String, IClient>> grupos;
    private IServer iserver;
    private String nombreUsuario;
    private String host;
    private String puerto;
    private MainFrame frameChat;
    private PanelView panelVista;
    private ControladorChat controladorChat;

    /**
     *
     * @param host
     * @param port
     */
    public Gestion(String host, String port) {
        this.host = host;
        this.puerto = port;
        grupos = new ConcurrentHashMap<>();
    }

    /**
     * Metodo se conecta al servidor y genera un objeto remoto del cliente para
     * su envio. Inicializa la interfaz grafica
     *
     * @param nombre
     * @throws UserConnected
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    public void connectToServer(String nombre) throws UserConnected, RemoteException,
            NotBoundException, MalformedURLException {
        this.nombreUsuario = nombre;

        iserver = (IServer) Naming.lookup("rmi://" + host + ":" + puerto + "/P2P");
        IClient callbackObj = new IClientImpl(this);

        iserver.registerClient(nombreUsuario, callbackObj);
        clientes = iserver.getClients();
        clientes.remove(nombreUsuario);

        controladorChat = new ControladorChat(this);
        panelVista = new PanelView(this, controladorChat);
        frameChat = new MainFrame(this, panelVista);
    }

    /**
     * Envia una notificacion de desconexion general.
     */
    public void disconnectToServer() {
        try {
            controladorChat.notificarDesconexionGrupos();
            iserver.unregisterClient(nombreUsuario);
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Solicitud del nombre de usuario
     *
     * @return Nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     *
     * @return
     */
    public ConcurrentHashMap getClients() {
        return clientes;
    }

    /**
     * Agrega un cliente nuevo
     *
     * @param name
     * @param objClient
     */
    public void addCliente(String name, IClient objClient) {
        clientes.put(name, objClient);
        panelVista.addClientToList(name);
    }

    /**
     * Agrega un nuevo grupo
     *
     * @param idGroup
     * @param nameGroup
     * @param objGroup
     */
    public void addGroup(String idGroup, String nameGroup, HashMap objGroup) {
        grupos.put(idGroup, objGroup);
    }

    /**
     * Elimina un cliente offline
     *
     * @param nombre Nombre del cliente a eliminar
     */
    public void eliminarCliente(String nombre) {
        /**
         * Sincroniza clientes para evitar suplantacion de nombres abrupta
         */
        synchronized (clientes) {
            clientes.remove(nombre);
            eliminarUsuarioAGrupos(nombre);
            panelVista.removeClientToList(nombre);
        }
    }

    /**
     * Elimina el usuario de los grupos realizando una busqueda por la llave
     * enviada en el parametro
     *
     * @param nombre Nombre del usuario a eliminar
     */
    public void eliminarUsuarioAGrupos(String nombre) {
        for (Object e : grupos.keySet()) {
            HashMap<String, IClient> hm = grupos.get(e.toString());
            if (hm.containsKey(nombre)) {
                hm.remove(nombre);
                //groups.replace((String) e, hm);
                controladorChat.actualizarChatUsuario(e.toString(), nombre);
            }
        }
    }

    /**
     * Recupera el objeto remoto del parametro enviado
     *
     * @param name El nombre del cliente a recuperar
     * @return Objeto remoto correspondiente al cliente
     */
    public IClient getObjetoCliente(String name) {
        return (IClient) clientes.get(name);
    }

    /**
     * Devuelve un HashMap con objetos remotos de todos los clientes con sus
     * respectivas identificaciones
     *
     * @param idGroup ID del grupo
     * @return HashMap de objetos remotos para cada cliente
     */
    public HashMap<String, IClient> getGrupoObjetos(String idGroup) {
        return grupos.get(idGroup);
    }

    /**
     * Imprime el parametro mensaje en la ventana de chat
     *
     * @param nombre Nombre del usuario que envia el mensaje
     * @param mensaje Mensaje en cuestion
     */
    public void mensajeRecibido(String nombre, String mensaje) {
        controladorChat.mensajeRecibido(nombre, mensaje);
    }

    /**
     * Solicitud remota de ingreso al grupo
     *
     * @param grupoID
     * @param grupoNombre
     * @param usuarioNombre
     * @param objetoCliente
     */
    public void entrarGrupo(String grupoID, String grupoNombre, String usuarioNombre, HashMap<String, IClient> objetoCliente) {
        JOptionPane.showMessageDialog(null, usuarioNombre + " te ha invitado al grupo: " + grupoNombre);
        objetoCliente.put(usuarioNombre, getObjetoCliente(usuarioNombre));
        objetoCliente.remove(getNombreUsuario());
        synchronized (grupos) {
            if (!grupos.containsKey(grupoID)) {
                grupos.put(grupoID, objetoCliente);
                panelVista.agregarGrupoALista(grupoID, grupoNombre);
            }
        }
    }

    /**
     * Recibir mensaje grupal
     *
     * @param idGroup
     * @param nameGroup
     * @param nameUser
     * @param msg
     */
    public void mensajeGrupoRecibido(String idGroup, String nameGroup, String nameUser, String msg) {
        controladorChat.mensajeRecibidoGrupo(idGroup, nameGroup, nameUser, msg);
    }

    /**
     *
     * @param name
     */
    public void eliminarGrupo(String name) {
        grupos.remove(name);
    }

    /**
     * Quita a un usuario de un grupo
     *
     * @param grupoID ID del grupo
     * @param nombreUsuario Nombre de usuario a eliminar
     */
    public void eliminarUsuarioEnGrupo(String grupoID, String nombreUsuario) {
        HashMap<String, IClient> hs = grupos.get(grupoID);
        hs.remove(nombreUsuario);
        //groups.replace(idGroup, hs);
        controladorChat.actualizarChatUsuario(grupoID, nombreUsuario);
    }

    /**
     * Envio de solicitudes a usuarios nuevos, se les notifica la lista actual
     * de usuarios
     *
     * @param grupoID ID del grupo
     * @param grupoNombre Nombre del grupo
     * @param usuarios Usuarios
     */
    public void agregarInvUsuarioAGrupo(String grupoID, String grupoNombre, ArrayList<String> usuarios) {

        HashMap<String, IClient> hs = grupos.get(grupoID);
        for (int i = 0; i < usuarios.size(); i++) {
            IClient newClient = getObjetoCliente(usuarios.get(i));

            DifusionGrupo bc = new DifusionGrupo(grupoID, grupoNombre, getNombreUsuario(), newClient, hs);
            bc.start();
            for (Object e : hs.keySet()) {
                IClient obj = getObjetoCliente(e.toString());

                DifusionGrupo bc2 = new DifusionGrupo(grupoID, grupoNombre, obj, usuarios);
                bc2.start();
            }
            hs.put(usuarios.get(i), newClient);
        }

        controladorChat.actualizarChatUsuario(grupoID, null);
    }

    /**
     * Consigue los usuarios mas recientes y los concatena a su HashMap
     * respectivo
     *
     * @param grupoID
     * @param nombreGrupo
     * @param usuarios
     */
    public void agregarNuevoUsuarioGrupo(String grupoID, String nombreGrupo, ArrayList<String> usuarios) {
        HashMap<String, IClient> hs = grupos.get(grupoID);
        for (int i = 0; i < usuarios.size(); i++) {
            IClient obj = getObjetoCliente(usuarios.get(i));
            if (usuarios.get(i) != null && obj != null) {
                hs.put(usuarios.get(i), obj);
            }
        }
        controladorChat.actualizarChatUsuario(grupoID, null);
    }
}
