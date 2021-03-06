/*
 * Cliente.java
 *
 * Ejemplo de muy simple de rmi
 */

import java.rmi.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Ejemplo de cliente rmi nocivo, para aprovecharse de un servidor sin
 * SecurityManager.
 * @author  Javier Abellan
 */
 // process <id> <n> <initialDelay> <bearer>

public class Semaforo {

    /** Crea nueva instancia de Semaforo */
		private static int id;
    private static int n;
    private static int initialDelay;
    private static boolean bearer;
		InterfazToken token;
		InterfazLista lista;
		InterfazProceso proceso;

    private String resetColor = "\u001B[0m";
    private String Verdeblanco ="\u001B[42m"+"\u001B[37m";
    private String RojoBlanco ="\u001B[41m"+"\u001B[37m";
    private String AmarilloNegro="\u001B[43m"+"\u001B[30m";
		// private int timeout;
    //public Semaforo(int id_)
    public Semaforo(int id_, int n_ , int initialDelay_ ,boolean bearer_)
    {
				id = id_;
        n = n_;
        initialDelay =initialDelay_;
        bearer =bearer_;
        if(bearer){
          //crear el token
          Thread a = new Thread(){
            public void run(){
              try{
                token = new Token(n);

                Naming.rebind("//localhost/Token",token);
                InterfazLista lista = new Lista(n);
                Naming.rebind ("//localhost/Lista", lista);
                System.out.println("Lista RMI Creada");
                LocateRegistry.createRegistry(1199);
                System.out.println("LocateRegistry ready");
                System.out.println("creando token");
                  System.out.println("hola");
                //Thread.sleep(100000);
                System.out.println("chao");
              }catch ( Exception a) {
                a.printStackTrace();
              }
            }
          };
          a.start();
          try{
            Thread.sleep(1000);
          }catch (Exception e) {
            e.printStackTrace();
          }

        }else{

        }
        // hay que espera que todos esten listos para empezar



        boolean ver=true;
        try
        {
		// Lugar en el que esta el objeto remoto.
		// Debe reemplazarse "localhost" por el nombre o ip donde
		// este corriendo "rmiregistry".
		// Naming.lookup() obtiene el objeto remoto
            //InterfazToken token =
            try{
              lista = (InterfazLista)Naming.lookup ("//localhost/Lista");
              token = (InterfazToken)Naming.lookup ("//localhost/Token");
            }
            catch(Exception a){

              if(ver){
                System.out.println("Esperado que se inicie el proceso con bearer True ...");
                ver= false;
                try {
                  Thread.sleep(2000);
                }catch (Exception b) {
                  b.printStackTrace();
                }
              }
            }

						proceso = new Proceso(id);
						System.out.println ("Añaidendo proceso...");
						addToList(proceso);
						System.out.println ("Listaylor");
            waitStart();

            // Se realiza la suma remota.
            ///System.out.print ("2 + 3 = ");
						// a traves
						// de objetoRemoto, el servidor realiza el metodo suma (no el cliente), y tengo la
						// respuesta a través de este
            ///System.out.println (Token.suma(2,3));
            System.out.println (AmarilloNegro+"   Esperando el Token   "+resetColor);

            waitToken();
						takeToken(token);
            System.out.println(RojoBlanco+"   En Zona Critica      "+ resetColor);

						//Thread.sleep(timeout);
						Thread.sleep(initialDelay);
            System.out.println(Verdeblanco+"   Estoy ocioso         "+resetColor);
						boolean sali=token.freeToken(id);
            if(sali){
              System.out.println("algo !!");
              Thread.sleep(100000);
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }

				//kill();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
				//System.out.println(args[0]);
        id = Integer.parseInt(args[0]);
        n= Integer.parseInt(args[1]);
        initialDelay = Integer.parseInt(args[2]);
        if(args[3].equals("true")){
          bearer =true;
        }else{
          bearer = false;
        }


        new Semaforo(id,n,initialDelay,bearer);

				System.out.println("En codigo main");
				System.exit(1);
        //new Semaforo(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
    }

		public void addToList(InterfazProceso proceso){
			try{
        lista.addProceso(proceso);
			}
			catch (Exception e)
			{
        System.out.println("Aun no se crea la lista esperado que se inicie el proceso con bearer True ...");
					//e.printStackTrace();
			}
		}

		public void request(int id, int seq){

		}
    /*
        waitStart espera que todos los procesos esten iniciados para empezar
    */
    public void waitStart(){
      boolean start = false;
      while(!start){
        try{
          start = lista.iniciar();// no puedo hacer que ejecute el start...siempre cae en el catch...
          Thread.sleep(1000);
        }catch (Exception e) {
          try {
            Thread.sleep(1000);
          }catch (Exception a) {
            a.printStackTrace();
          }

        }
      }
    }





/*
    waitToken funcion que espera el token, inicialmente hace un peticion y esta queda en colada
*/
		public void waitToken(){
			boolean asd = true;
      try{
        token.getToken(id);
      }catch(Exception e){
        e.printStackTrace();
      }
			while(asd){
				try{
					asd = !token.available(id);
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
				try{
						//int pos = lista_.indexOf(proceso);
						//System.out.println("elimiando proceso en posicion: "+proceso);
						lista.killProceso(proceso);
						System.out.println("eliminacion lista");
				}
				catch (Exception e){
						e.printStackTrace();
				}
				System.out.println("x2");
		}

}
