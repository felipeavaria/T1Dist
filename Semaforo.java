/*
 * Cliente.java
 *
 * Ejemplo de muy simple de rmi
 */

import java.rmi.*;

/**
 * Ejemplo de cliente rmi nocivo, para aprovecharse de un servidor sin
 * SecurityManager.
 * @author  Javier Abellan
 */

public class Semaforo {

    /** Crea nueva instancia de Semaforo */
		private int id;
		InterfazToken token;
		// private int timeout;
    //public Semaforo(int id_)
    public Semaforo(int id_)//, int timeout)
    {
				id = id_;
        try
        {
		// Lugar en el que esta el objeto remoto.
		// Debe reemplazarse "localhost" por el nombre o ip donde
		// este corriendo "rmiregistry".
		// Naming.lookup() obtiene el objeto remoto
            //InterfazToken token =
            token = (InterfazToken)Naming.lookup ("//localhost/Token");

            // Se realiza la suma remota.
            ///System.out.print ("2 + 3 = ");
						// a traves
						// de objetoRemoto, el servidor realiza el metodo suma (no el cliente), y tengo la
						// respuesta a través de este
            ///System.out.println (Token.suma(2,3));
						waitToken();
						takeToken(token);
            System.out.println("En Zona Critica, el token es mio");
						//Thread.sleep(timeout);
						Thread.sleep(4000);
            System.out.println("Sali de Zona Critica");
						token.freeToken();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
				//System.out.println(args[0]);
        new Semaforo(Integer.parseInt(args[0]));
        //new Semaforo(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
    }

		public void request(int id, int seq){

		}

/*
    waitToken funcion que espera el token, inicialmente hace un peticion y esta queda en colada
*/
		public void waitToken(){
			boolean asd = true;
      try{
        token.getToken(1);
      }catch(Exception e){
        e.printStackTrace();
      }
			while(asd){
				try{
					asd = !token.available();
					Thread.sleep(100);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		public void takeToken(InterfazToken token){
			try{
				token.soyDe(id);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		public void kill(){

		}

}
