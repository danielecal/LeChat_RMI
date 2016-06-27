package ec.edu.ups.sdist.exceptions;

/**
 * Excepción propia. En caso que el usuario ya exista, se lanzará esta excepción
 *
 * @author niel
 */
public class UserConnected extends Exception {

    /**
     *Excepcion propia en caso de que el usuario ya exista
     */
    public UserConnected() {
        super("Exception! User alredy exists.");
    }
}
