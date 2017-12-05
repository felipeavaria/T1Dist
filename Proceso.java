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
	private int sec;
  public Proceso (int id_, int size) throws RemoteException
  {
      super();
			id = id_;
			RN = new int[size];
			sec = 0;

			System.out.print("[");
			for(int i=0; i<RN.length; i++){
				System.out.print(RN[i]+"-");
			}
			System.out.println("]");
  }

  public int getProceso () throws RemoteException
  {
    return procesosInstanciado;
  }

	public void print(String aux){
			System.out.println(aux);
	}

/**
 * El metodo, incrementa el valor en su arreglo RN[i], para el proceso que pide
 * estar en la seccion critica
 * */
	public boolean takeRequest(int proc, int sec_){
			//RN[proc]++;
			boolean accept = false;
			if(RN[proc] < sec_ || proc == id) accept = true;
			else return false;

			RN[proc] = sec_;
			if(token != null && proc != id){
					token.queve(proc);
			}
			
			System.out.print("[");
			for(int i=0; i<RN.length; i++){
				System.out.print(RN[i]+"-");
			}
			System.out.println("]");
			return accept;
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

	public int secuencia() {
			sec++;
			return sec;
	}

	public void actualizarLN() {
			if(token != null){
					token.set_LN(id, RN[id]);
			}
	}

}
