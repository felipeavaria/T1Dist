/*
 * InterfaceRemota.java
 *
 * Created on 27 de abril de 2004, 21:17
 */

import java.rmi.*;
import java.io.Serializable;

/**
 * Interface remota con los metodos que se podran llamar en remoto
 * @author  Javier Abellan
 */
public interface InterfazProceso extends Remote {
    public int Proceso() throws RemoteException;
    public int getProceso() throws RemoteException;
}
