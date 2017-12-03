//import java.rmi.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Proceso extends UnicastRemoteObject implements InterfazProceso
{
  /**
   * Construye una instancia de Token
   * @throws RemoteException
   */
  private int id;
  public static int procesosInstanciado =0;
  public Proceso (int id_) throws RemoteException
  {
      super();
			id = id_;
      //procesosInstanciado++;
      //return procesosInstanciado;
  }

  public int getProceso () throws RemoteException
  {
    return procesosInstanciado;
  }
}
