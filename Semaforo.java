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
                InterfazLista lista = new Lista();
                Naming.rebind ("//localhost/Lista", lista);
                System.out.println("Lista RMI Creada");


                //LocateRegistry.createRegistry(1099);
                System.out.println("LocateRegistry ready");
                System.out.println("creando token");
                  System.out.println("hola");
                Thread.sleep(100000);
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
        try
        {
		// Lugar en el que esta el objeto remoto.
		// Debe reemplazarse "localhost" por el nombre o ip donde
		// este corriendo "rmiregistry".
		// Naming.lookup() obtiene el objeto remoto

						/*
						TheToken thetoken = new TheToken(n_);
						IntObjeto AToken = new OToken("//localhost/OToken",n_,thetoken);
						Naming.rebind("//localhost/OToken", AToken);
						*/
						//No puedo realizar lo de arriba... por que me tira un error de que no
						//es algo remoto.
						//Creo que tendre que asociar a una "interfaz Remota", para poder
						//registrar ese, y utilizar el Serializable.

						System.out.println("TheToken Created");
            token = (InterfazToken)Naming.lookup ("//localhost/Token");
						lista = (InterfazLista)Naming.lookup ("//localhost/Lista");
						lista.setSize(n_);
						proceso = new Proceso(id);
						if(bearer){
								TheToken tokenmaestro = new TheToken(n_);
								proceso.asignToken(tokenmaestro);
						}
						Naming.rebind ("//localhost/Proceso"+id_, proceso);


						System.out.println ("Añaidendo proceso...");
						addToList(proceso);
						System.out.println ("Esperando al resto de los procesos...");
						waitStart();
						System.out.println ("Listaylor");
						request(id, 1);
            
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
            e.printStackTrace();
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
					e.printStackTrace();
			}
		}

		public void request(int id, int seq){
			for(int i=0; i<n; i++){
				if(i != id){
					String url = "//localhost/Proceso"+i;
					try{
						InterfazProceso aux = (InterfazProceso)Naming.lookup (url);
						aux.print("Si.... proceso "+id+" me esta webeando");
					}
					catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}

/*
    waitStart funcion que espera, al resto de los procesos para iniciar algoritmo*/
		public void waitStart(){
			boolean start = false;
			while(!start){
				try{
					start = lista.start();
					Thread.sleep(100);
				}
				catch(Exception e){
					e.printStackTrace();
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

		public void passToken(int id_proc){
				try{
						TheToken aux = proceso.getToken();
						InterfazProceso proc = 
							(InterfazProceso)Naming.lookup ("//localhost/Proceso"+id_proc);
						proc.asignToken(aux);
				}
				catch (Exception e){
						e.printStackTrace();
				}
		}

}
