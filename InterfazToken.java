/*
 * InterfaceRemota.java
 *
 * Created on 27 de abril de 2004, 21:17
 */

import java.rmi.*;
import java.io.Serializable;
//import TheToken;

/**
 * Interface remota con los metodos que se podran llamar en remoto
 * @author  Javier Abellan
 */
public interface InterfazToken extends Remote {
    public int suma (int a, int b) throws RemoteException;
    public boolean soyDe (int a) throws RemoteException;
    public void getToken(int p)throws RemoteException;
    public boolean available (int p) throws RemoteException;
    public boolean freeToken (int p) throws RemoteException;

  //  public TheToken recibirToken()throws RemoteException;
  //  public boolean sendtoken(TheToken token) throws RemoteException;


}
