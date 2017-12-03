/*
 * InterfaceRemota.java
 *
 * Created on 27 de abril de 2004, 21:17
 */

import java.rmi.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Interface remota con los metodos que se podran llamar en remoto
 * @author  Javier Abellan
 */
public interface InterfazLista extends Remote {
		public void addProceso(InterfazProceso proceso) throws RemoteException;
		public void killProceso(InterfazProceso proc) throws RemoteException;
    public ArrayList<InterfazProceso> getLista() throws RemoteException;
}
