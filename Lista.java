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
	private int list_size;
	private boolean size_activated;
	private ArrayList<InterfazProceso> ProcessList;

  public Lista() throws RemoteException
  {
      //super();
			ProcessList = new ArrayList<InterfazProceso>();
			size_activated = false;
  }

	public void setSize(int size) throws RemoteException{
			if(!size_activated){
					list_size = size;
					size_activated = true;
		}	
		System.out.println("size of list: "+list_size);
	}

	public boolean start() throws RemoteException 
	{
			if(ProcessList.size() >= list_size){
					return true;
			}
			return false;	
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
