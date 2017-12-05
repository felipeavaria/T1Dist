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


		public void print(String aux) throws RemoteException;
    public boolean imFinish(int proc) throws RemoteException;
    public boolean soyUltimo()throws RemoteException;
		public void takeRequest(int proc) throws RemoteException;
		public boolean tokenHasQ() throws RemoteException;
		public int nextProcess() throws RemoteException;
		public Token getToken() throws RemoteException;
		public void asignToken(Token token_) throws RemoteException;
		public boolean hasToken() throws RemoteException;
}
