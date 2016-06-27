package ec.edu.ups.sdist.controlador;

import ec.edu.ups.sdist.common.IClient;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase llama a método remoto. Usamos threads para hacer los broadcast
 *
 * @author niel
 */
public class DifusionGrupo extends Thread {

    private String idGrupo;
    private String nombreGrupo;
    private String miNombre;
    private String mensaje;
    private IClient objeto;
    private HashMap<String, IClient> listaObjetos;
    private ArrayList<String> usuarios;
    private int opcion;

    /**
     * Envio de mensajes
     *
     * @param grupoID ID del grupo
     * @param nombreGrupo Nombre del grupo
     * @param nombreLocal Nombre de usuario local
     * @param mensaje Mensaje
     * @param objeto Objeto IClient
     */
    public DifusionGrupo(String grupoID, String nombreGrupo, String nombreLocal, String mensaje, IClient objeto) {
        this.idGrupo = grupoID;
        this.nombreGrupo = nombreGrupo;
        this.miNombre = nombreLocal;
        this.mensaje = mensaje;
        this.objeto = objeto;
        this.opcion = 1;
    }

    /**
     * Invitar un cliente a un grupo especifico
     *
     * @param grupoID ID del grupo
     * @param nombreGrupo Nombre del grupo
     * @param nombreLocal Nombre de usuario local
     * @param objeto Objeto IClient
     * @param listaObjeto HashMap de objetos IClient
     */
    public DifusionGrupo(String grupoID, String nombreGrupo, String nombreLocal, IClient objeto, HashMap<String, IClient> listaObjeto) {
        this.idGrupo = grupoID;
        this.nombreGrupo = nombreGrupo;
        this.miNombre = nombreLocal;
        this.objeto = objeto;
        this.listaObjetos = listaObjeto;
        this.opcion = 2;
    }

    /**
     * Abandonar grupo
     *
     * @param grupoID ID del grupo
     * @param nombreLocal Nombre de usuario local
     * @param objeto Objeto IClient
     */
    public DifusionGrupo(String grupoID, String nombreLocal, IClient objeto) {
        this.idGrupo = grupoID;
        this.miNombre = nombreLocal;
        this.objeto = objeto;
        this.opcion = 3;
    }

    /**
     * Enviar que clientes se agregan a un grupo
     *
     * @param grupoID ID del grupo
     * @param nombreGrupo Nombre del grupo
     * @param objeto Objeto IClient
     * @param usuarios Lista de usuarios
     */
    public DifusionGrupo(String grupoID, String nombreGrupo, IClient objeto, ArrayList<String> usuarios) {
        this.idGrupo = grupoID;
        this.nombreGrupo = nombreGrupo;
        this.objeto = objeto;
        this.usuarios = usuarios;
        this.opcion = 4;
    }

    @Override
    public void run() {
        switch (opcion) {
            case 1:
                try {
                    objeto.writeGroupMsg(idGrupo, nombreGrupo, miNombre, mensaje);
                } catch (RemoteException ex) {
                    Logger.getLogger(DifusionGrupo.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case 2:
                try {
                    objeto.joinGroup(idGrupo, nombreGrupo, miNombre, listaObjetos);
                    //objClient.joinGroup(idGroup, nameGroup, controlador.getUserName(), listObject);
                } catch (RemoteException ex) {
                    Logger.getLogger(DifusionGrupo.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case 3:
                try {
                    objeto.leftGroup(idGrupo, miNombre);
                } catch (RemoteException ex) {
                    Logger.getLogger(DifusionGrupo.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case 4:
                try {
                    objeto.newUserGroup(idGrupo, nombreGrupo, usuarios);
                } catch (RemoteException ex) {
                    Logger.getLogger(DifusionGrupo.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }
}
