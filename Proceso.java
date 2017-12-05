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
	private int sec;
  private int[] finish;
  private int size;
  public Proceso (int id_, int size_) throws RemoteException
  {
      super();
			id = id_;
      size=size_;
			RN = new int[size];
      finish = new int[size];
			sec = 0;
			System.out.print("RN : ["+RN[0]);
			for(int i=1; i<RN.length; i++){
				System.out.print("-"+RN[i]);
			}
			System.out.println("]");


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

			System.out.print("RN : ["+RN[0]);
			for(int i=1; i<RN.length; i++){
				System.out.print("-"+RN[i]);
			}
			System.out.println("]");
			return accept;
	}

 /* funcion que permite indicar a los demas procesos que termine y a la vez revisa si los demas estan aun en ejecucion */
  public boolean imFinish(int proc){
    finish[proc]=1;
    int aux=0;
    for(int i=0;i<size;i++){
      aux=aux+finish[i];
    }
    if(size == aux){
      return true;
    }
    return false;
  }

  public int sumaArray(int[] array){
      int aux=0;
      for(int i=0;i<size;i++){
        aux=aux+array[i];
      }
      return aux;
  }


  /* funcion que permite que un procesos sepa que es el ultimo por lo que esta en condiciones de terminar la ejecucion */
  public boolean soyUltimo(){

    int aux=0;
    for(int i=0;i<size;i++){
      aux=aux+finish[i];
    }
    if((size-1) == aux){

      return true;
    }
    return false;
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
