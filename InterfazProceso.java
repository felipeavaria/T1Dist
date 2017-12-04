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
    public int getProceso() throws RemoteException;
		public void print(String aux) throws RemoteException;
		public TheToken getToken() throws RemoteException;
		public void asignToken(TheToken token_) throws RemoteException;
		public boolean hasToken() throws RemoteException;
}

