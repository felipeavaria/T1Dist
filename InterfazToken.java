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
public interface InterfazToken extends Remote {
    public int suma (int a, int b) throws RemoteException; 
    public boolean soyDe (int a) throws RemoteException; 
    public boolean available () throws RemoteException; 
    public boolean freeToken () throws RemoteException; 
}
