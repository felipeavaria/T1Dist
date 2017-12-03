//import java.rmi.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Lista extends UnicastRemoteObject implements InterfazLista
{
  /**
   * Construye una instancia de Token
   * @throws RemoteException
   */
  private int id;
	private ArrayList<InterfazProceso> ProcessList;

  public Lista() throws RemoteException
  {
      //super();
			ProcessList = new ArrayList<InterfazProceso>();
  }

	public void addProceso(InterfazProceso proceso) throws RemoteException 
	{
			ProcessList.add(proceso);
			System.out.println("Largo de lista: "+ProcessList.size());
	}

	public void killProceso(InterfazProceso proc) throws RemoteException 
	{
			int pos = ProcessList.indexOf(proc);
			System.out.println("eliminando de posicion: "+pos);
			ProcessList.remove(pos);
			System.out.println("eliminado");
	}

  public ArrayList<InterfazProceso> getLista () throws RemoteException
  {
			return ProcessList;
  }
}
