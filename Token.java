/*
 * ObjetoRemoto.java
 *
 * Created on 27 de abril de 2004, 21:18
 */

//package chuidiang.ejemplos.rmi.suma;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;
import java.util.ArrayList;

/**
 * @author  Javier Abellan
 */
public class Token extends UnicastRemoteObject implements InterfazToken, Serializable
{
    /**
     * Construye una instancia de Token
     * @throws RemoteException
     */
		private int[] LN;
		private List<Integer> colaProcesos;
		private int n;
		private int procesoActual;
		private boolean Taken = false;
    public Token (int n) throws RemoteException
    {
        //super();
				LN = new int[n];
				this.n=n;
				for (int i=0;i<n;i++){
					LN[i]=0;
					System.out.println("LN["+i+"]="+LN[i]);
				}
				colaProcesos = new ArrayList<Integer>();

    }

    /**
     * Obtiene la suma de los sumandos que le pasan y la devuelve.
     */
    public int suma(int a, int b)
    {
	    System.out.println ("Sumando " + a + " + " + b +"...");
        return a+b;
    }

    public boolean soyDe(int a)
    {
				Taken = true;
				procesoActual = a;
				//return ("Token es de: "+procesoActual);
				System.out.println("Token es de: "+procesoActual);
				return true;
    }
		public void getToken(int p){

				colaProcesos.add(p);

				LN[p-1]=1;
		}

		public boolean available(int p){

				int aux;
				aux=colaProcesos.indexOf(p);
				if(Taken == false && aux == 0 ){
						System.out.println("token liberado y sera tomado por "+ p);
						return true;
				}
				else{
						return false;
				}
		}

		public boolean freeToken(int p){
				System.out.println("rmove en cola "+ p + " size = "+colaProcesos.size());
				colaProcesos.remove((Integer) p);
				System.out.println("remove en cola "+ p + " size = "+colaProcesos.size());
				kill();
				Taken = false;
				return true;
		}

		/* al pasar por todos los nodos se debe ejecuatar kill que mata el Servidor */
		public void kill(){
				if(colaProcesos.size()==0 && sumaProcesos() == n){
					// kill server
					System.out.println("Ahora hay que matar al server!!!");
				}
		}
		public int sumaProcesos(){
			int aux=0;
			for (int i=0; i<n;i++){
				aux=aux+LN[i];
			}
			System.out.println("suma = "+ aux);
			return aux;
		}

}
