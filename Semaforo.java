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
import java.util.Scanner;

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
    public static int delay2;
		InterfazLista lista;
		InterfazProceso proceso;
    private Scanner sc;
    private String resetColor = "\u001B[0m";
    private String Verdeblanco ="\u001B[42m"+"\u001B[37m";
    private String RojoBlanco ="\u001B[41m"+"\u001B[37m";
    private String AmarilloNegro="\u001B[43m"+"\u001B[30m";

    public Semaforo(int id_, int n_ , int initialDelay_ ,boolean bearer_,int delay2_)
    {
				id = id_;
        n = n_;
        initialDelay =initialDelay_;
        delay2=delay2_;
        bearer =bearer_;
        if(bearer){
          Thread a = new Thread(){
							public void run(){
									try{
										InterfazLista lista = new Lista();
										Naming.rebind ("//localhost/Lista", lista);
										System.out.println("Lista RMI Creada");

										//LocateRegistry.createRegistry(1099);
										System.out.println("LocateRegistry ready");
										System.out.println("creando token");

										Thread.sleep(100000);

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
        }
        boolean espero=true;
        boolean gate =true;
        while (espero) {
          try
          {
              if (gate) {
                System.out.println ("Esperando al resto de los procesos...");
              }

              lista = (InterfazLista)Naming.lookup ("//localhost/Lista");
              lista.setSize(n_);
              proceso = new Proceso(id, n_);
              if(bearer){
                  Token tokenmaestro = new Token(n_);
                  //System.out.println("creando token maestro");
                  proceso.asignToken(tokenmaestro);
                  bearer=false;

              }

              Naming.rebind ("//localhost/Proceso"+id_, proceso);

              System.out.println ("Añaidendo proceso...");
              addToList(proceso);

              waitStart();


              System.out.println(Verdeblanco+"   Estoy ocioso         "+resetColor);
              Thread.sleep(initialDelay);

              System.out.println (AmarilloNegro+"   Esperando el Token   "+resetColor);
              request(id, 1); //Solicitud a Otros Procesos, de entrar a la Zona Critica
              if(!proceso.hasToken()){
                  waitToken();
              }
              System.out.println("Tengo el Token");
              System.out.println(RojoBlanco+"   En Zona Critica      "+ resetColor);
              Thread.sleep(1000); //Zona Critica

              System.out.println(Verdeblanco+"   Estoy ocioso         "+resetColor);
              passToken();

              espero= false;
              if(delay2>0){
                initialDelay=delay2;
                delay2=0;

                espero=true;
              }else{
                boolean todosTerminaron=false;
                while(!todosTerminaron){
                  try{
                    todosTerminaron = proceso.imFinish(id);
                    for(int i=0; i<n; i++){
              				if(i != id){
              					String url = "//localhost/Proceso"+i;
              					try{
              						InterfazProceso aux = (InterfazProceso)Naming.lookup (url);
              						//aux.print("Si.... proceso "+id+" me esta webeando");
              						aux.imFinish(id);
              					}
              					catch (Exception e){
              						e.printStackTrace();
              					}
              				}
              			}

                    Thread.sleep(1000);
                  }catch (Exception e) {

                  }
                }
                System.out.println("");

              }


          }
          catch (Exception e)
          {
            try{
              Thread.sleep(2000);
              gate =false;
            }
            catch (Exception a)
            {
                a.printStackTrace();
            }
          }

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
        String[] parts = args[2].split(",");
        try{
          delay2=Integer.parseInt(parts[1]);
        }catch (Exception e) {
          delay2=0;
        }

        initialDelay = Integer.parseInt(parts[0]);
        if(args[3].equals("true")){
          bearer =true;
        }else{
          bearer = false;
        }
        new Semaforo(id,n,initialDelay,bearer,delay2);
				System.exit(1);
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

	/**
	 * Envia mensaje "request" al resto de los procesos, para aplicar el
	 * algoritmo de Susuki-Kazami
	 */
		public void request(int id, int seq){
			try{
					proceso.takeRequest(id);
			}
			catch (Exception e){
					e.printStackTrace();
			}
			for(int i=0; i<n; i++){
				if(i != id){
					String url = "//localhost/Proceso"+i;
					try{
						InterfazProceso aux = (InterfazProceso)Naming.lookup (url);
						aux.takeRequest(id);
					}
					catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}

/**
 * Función que espera, al resto de los procesos para iniciar algoritmo
 */
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


/**
 * Método que espera, que el proceso realcionado con este Semaforo,
 * obtenga el Token para entrar a la Zona Critica
*/
		public void waitToken(){
				boolean asd = true;
				//System.out.println("en waittoken");
				while(asd){
						try{
								asd = !proceso.hasToken();
								Thread.sleep(100);
						}
						catch(Exception e){
								e.printStackTrace();
						}
				}
		}

		/* ESTE METODO TIENE QUE ESTAR EN EL SEMAFORO!!!!*/
/**
 * Método de Semaforo/Proceso, el cual toma el Token, una vez que este es
 * liberado por otro proceso.
 */
		/*
		public void takeToken(InterfazToken token){
			try{
				token.soyDe(id);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		*/

/**
 * Método que termina la ejecución del proceso; necesario para terminar
 * con la ejecución del algoritmo Susuki-Kazami.
 */
		public void kill(){
				try{
						lista.killProceso(proceso);
						System.out.println("eliminacion lista");
				}
				catch (Exception e){
						e.printStackTrace();
				}
				System.out.println("x2");
		}

/**
 * *** Este metodo, tiene que de alguna manera, utilizarse con "TakeToken", para
 * obtener el token ***
 * */
		public void passToken(){
				boolean asdf = true;
				try{
						while(asdf){
								Thread.sleep(100);
								if(proceso.tokenHasQ()){
										asdf = false;
										int id_proc = proceso.nextProcess();
										if(id_proc < n){
												Token aux = proceso.getToken();
												InterfazProceso proc =
													(InterfazProceso)Naming.lookup ("//localhost/Proceso"+id_proc);
												proc.asignToken(aux);
												System.out.println("pasando token a "+id_proc);

										}
								}
                if(proceso.soyUltimo()){
                  asdf =false;
                }
						}
				}
				catch (Exception e){
						e.printStackTrace();
				}
		}
}
