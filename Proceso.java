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
	private Token token = null;
	private int[] RN;
  public static int procesosInstanciado =0;
  public Proceso (int id_, int size) throws RemoteException
  {
      super();
			id = id_;
			RN = new int[size];

			System.out.print("[");
			for(int i=0; i<RN.length; i++){
				System.out.print(RN[i]+"-");
			}
			System.out.println("]");

      //procesosInstanciado++;
      //return procesosInstanciado;
  }

  public int getProceso () throws RemoteException
  {
    return procesosInstanciado;
  }

	public void print(String aux){
			System.out.println(aux);
	}

	public void takeRequest(int proc){
			RN[proc]++;
			if(token != null && proc != id){
					token.queve(proc);
			}
			
			System.out.print("[");
			for(int i=0; i<RN.length; i++){
				System.out.print(RN[i]+"-");
			}
			System.out.println("]");
	}

	public boolean tokenHasQ(){
			if(token != null){
					if(token.QLength() > 0) return true;	
			}
			return false;
	}

	public int nextProcess(){
			if(token != null){
					return token.getNextQ();
			}
			return 9999;
	}

	public Token getToken(){
			Token aux = token;
			token = null;
			return aux;
	}

	public boolean hasToken() {
			if(token != null){
					return true;
			}
			return false;
	}

	public void asignToken(Token token_){
			System.out.println("Asignando Token");
			token = token_;
	}

}
