/*
 * ObjetoRemoto.java
 *
 * Created on 27 de abril de 2004, 21:18
 */

//package chuidiang.ejemplos.rmi.suma;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author  Javier Abellan
 */
public class Token extends UnicastRemoteObject implements InterfazToken
{
    /**
     * Construye una instancia de Token
     * @throws RemoteException
     */
		private int[] LN;
		private int procesoActual;
		private boolean Taken = false;
    public Token (int n) throws RemoteException
    {
        //super();
				LN = new int[n];
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

		public boolean available(){
				if(Taken == false){
						return true;
				}
				else{
						return false;
				}
		}

		public boolean freeToken(){
				Taken = false;
				return true;
		}
    
}
