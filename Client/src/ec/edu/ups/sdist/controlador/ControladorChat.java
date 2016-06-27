package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import ec.edu.ups.sdist.vista.FrameChat;
import ec.edu.ups.sdist.vista.CuadroChatGrupo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Administra condiciones de las sesiones de chat
 *
 * @author niel
 */
public class ControladorChat {

    private ConcurrentHashMap<String, FrameChat> listChat;
    private ConcurrentHashMap<String, CuadroChatGrupo> listGroup;
    private Gestion controlador;
    private FrameChat frameChat;

    /**
     *
     * @param controlador
     */
    public ControladorChat(Gestion controlador) {
        listChat = new ConcurrentHashMap<>();
        listGroup = new ConcurrentHashMap<>();
        this.controlador = controlador;
    }

    private void agregarNuevoChat(String nombre) {
        frameChat = new FrameChat(controlador.getNombreUsuario(),
                nombre, controlador.getObjetoCliente(nombre), this);
        listChat.put(nombre, frameChat);
    }

    private void agregarChatGrupal(String grupoID, String grupoNombre) {
        CuadroChatGrupo fcg = new CuadroChatGrupo(grupoID, controlador.getNombreUsuario(),
                grupoNombre, (HashMap<String, IClient>) controlador.getGrupoObjetos(grupoID));
        listGroup.put(grupoID, fcg);
    }

    /**
     *
     * @param nombre
     */
    public void eliminarChat(String nombre) {
        listChat.remove(nombre);
    }

    /**
     *
     * @param nombre
     */
    public void abrirChat(String nombre) {
        if (listChat.containsKey(nombre)) {
            FrameChat frameChat = (FrameChat) listChat.get(nombre);
            frameChat.setVisible(true);
        } else {
            agregarNuevoChat(nombre);
        }
    }

    /**
     *
     * @param grupoID
     * @param nombreGrupo
     */
    public void abrirConversacionGrupal(String grupoID, String nombreGrupo) {
        if (listGroup.containsKey(grupoID)) {
            CuadroChatGrupo fcg = (CuadroChatGrupo) listGroup.get(grupoID);
            fcg.setVisible(true);
        } else {
            agregarChatGrupal(grupoID, nombreGrupo);
        }
    }

    /**
     *
     * @param nombre
     * @param mensaje
     */
    public void mensajeRecibido(String nombre, String mensaje) {
        if (!listChat.containsKey(nombre)) {
            FrameChat fc = new FrameChat(controlador.getNombreUsuario(), nombre,
                    controlador.getObjetoCliente(nombre), this);
            listChat.put(nombre, fc);
            fc.addMsg(nombre, mensaje);
        } else {
            FrameChat fc = (FrameChat) listChat.get(nombre);
            fc.addMsg(nombre, mensaje);
            fc.setVisible(true);
        }
    }

    /**
     *
     * @param grupoID
     * @param nombreGrupo
     * @param nombreUsuario
     * @param mensaje
     */
    public void mensajeRecibidoGrupo(String grupoID, String nombreGrupo, String nombreUsuario, String mensaje) {
        if (!listGroup.containsKey(grupoID)) {
            CuadroChatGrupo fcg = new CuadroChatGrupo(grupoID, controlador.getNombreUsuario(),
                    nombreGrupo, controlador.getGrupoObjetos(grupoID));
            listGroup.put(grupoID, fcg);
            fcg.addMsg(nombreUsuario, mensaje);
        } else {
            CuadroChatGrupo chg = (CuadroChatGrupo) listGroup.get(grupoID);
            chg.addMsg(nombreUsuario, mensaje);
            chg.setVisible(true);
        }
    }

    /**
     *
     * @param name
     */
    public void reportarError(String name) {
        listChat.remove(name);
        controlador.eliminarCliente(name);
    }

    /**
     *
     * @param idGrupo
     * @param nombreGrupo
     * @param usuarios
     */
    public void crearGrupo(String idGrupo, String nombreGrupo, ArrayList<String> usuarios) {
        if (!listGroup.containsKey(idGrupo)) {
            HashMap<String, IClient> listObject = new HashMap<>();
            for (int i = 0; i < usuarios.size(); i++) {
                IClient objClient = controlador.getObjetoCliente(usuarios.get(i));
                listObject.put(usuarios.get(i), objClient);
            }

            for (Object e : listObject.keySet()) {
                IClient objClient = controlador.getObjetoCliente(e.toString());
                DifusionGrupo bg = new DifusionGrupo(idGrupo, nombreGrupo, controlador.getNombreUsuario(), objClient, listObject);
                bg.start();
            }
            controlador.addGroup(idGrupo, nombreGrupo, listObject);
        }
    }

    /**
     *
     * @param grupoID
     */
    public void abandonarChatGrupal(String grupoID) {
        String nombreUsuario = controlador.getNombreUsuario();
        if (listGroup.containsKey(grupoID)) {
            CuadroChatGrupo fcg = (CuadroChatGrupo) listGroup.get(grupoID);
            fcg.dispose();
            listGroup.remove(grupoID);
        }
        HashMap<String, IClient> obj = controlador.getGrupoObjetos(grupoID);
        for (Object e : obj.keySet()) {
            IClient objClient = (IClient) obj.get(e.toString());
            DifusionGrupo bc = new DifusionGrupo(grupoID, nombreUsuario, objClient);
            bc.start();
            controlador.eliminarGrupo(grupoID);
        }
    }

    /**
     *
     * @param grupoID
     * @param nombreUsuario
     */
    public void actualizarChatUsuario(String grupoID, String nombreUsuario) {
        if (listGroup.containsKey(grupoID)) {
            CuadroChatGrupo fcg = (CuadroChatGrupo) listGroup.get(grupoID);
            fcg.showUsers(nombreUsuario);
        }
    }

    /**
     * Recorre los grupos notificando las desconexiones de cada usuario
     */
    public void notificarDesconexionGrupos() {
        for (Object e : listGroup.keySet()) {
            abandonarChatGrupal(e.toString());
        }
    }
}
