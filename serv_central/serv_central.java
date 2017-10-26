import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.ArrayList;
import java.net.MulticastSocket;

/*
Servidor Central:
  coordinador de comunicacion entre clientes y distritos
  debe contener:
    lista de distitos (nombre, Direccion IP grupo multicas, Direccion IP de reciproco , puerto)
funcionalidad Agragar un Distrito
  pregunta nombre, ip Multicast , puerto Multicast, ip peticiones , peruto peticiones
funcionalidad escuchar clientes
respuesta a clientes
  Dar autorizacion a ip a Distrito X
*/

public class serv_central {

    //final static String INET_ADDR = "224.0.0.3";
    //final static int PORT = 8888;
		//Convertir String a int: int foo = Integer.parseInt("1234");
		public String INET_ADDR = "224.0.0.3";
		public int Puerto_escucha = 8888; // puerto Multicast que escucha
		public String[][] Lista_Distritos = new String[5][500]; // maximo de dsitritos 500
    public int contador_Distirtos = 0;
    // Lista_Distritos: [0] nombre, [1] ip Multicast, [2] puerto Multicast,[3] ip reciproco, [4] puerto
		public int dist_count = 0;
		public String user_input = "";
		public boolean client_requesting = false;
		public ArrayList<clienteEntrante> clientes = new ArrayList<clienteEntrante>();
		public Object lock = new Object();
		public MulticastSocket clientSocket;

		public class clienteEntrante{
			InetAddress IP;
			String msg;
			DatagramPacket request;
			DatagramSocket socket;
			public clienteEntrante() {
			}
			public clienteEntrante(InetAddress ID_, String msj, DatagramPacket a, DatagramSocket b) {
				this.IP = ID_;
				this.msg = msj;
				this.request = a;
				this.socket = b;
			}
		}

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
			new serv_central();
    }

		public serv_central() throws UnknownHostException, InterruptedException {
			Scanner in = new Scanner(System.in);
			//Scanner in2 = new Scanner(System.in);

			Thread hiloA = new Thread(){
						public void run(){
							try{
								recibirMensajes();
							}catch (Exception ex){
								ex.printStackTrace();
							}
						}
					};


				Thread hiloB = new Thread(){
						public void run(){
							try{
								menu();
							}catch (Exception ex){
								ex.printStackTrace();
							}
						}
					};


			Thread hiloC = new Thread(){
						public void run(){
							try{
								while(true){
									user_input = in.nextLine();
								}
							}catch (Exception ex){
								ex.printStackTrace();
							}
						}
					};

			hiloA.start();
			hiloB.start();
			hiloC.start();

		}

	
		public void menu() throws UnknownHostException, InterruptedException{
			boolean menu = true;

			/*
			EscucharMensajes escucha = new EscucharMensajes();
			escucha.start();
			*/

			while(menu){
				System.out.println("Elegir Opción:");
				System.out.println("1) Agregar Distrito");
        System.out.println("2) ver de Distritos");
				System.out.println("3) Enviar Mensajes a Clientes");
				System.out.println("4) Permitir conexión de cliente");
				System.out.println("5) Salir");

        //synchronized (lock) {

					String user_input = waitInput();
					System.out.println("String recieved!");


					if(user_input.equals("1")){
						System.out.println("opcion 1");
						agregarDistrito();

					}
					else if(user_input.equals("2")){
						mostrarDistritos();
					}
					else if(user_input.equals("3")){
						sendMessages();
					}
					else if(user_input.equals("4")){
						//hiloA.stop();
						/*
						lock.notify();
						System.out.println("Permitiendo conexion de usuarios");
						lock.wait();
						*/
						aceptarClientes();
					}
					else if(user_input.equals("5")){
						//hiloA.stop();
						menu = false;
					}
					else if(user_input == "5"){
					}
					else if(user_input == "6"){
						System.out.println("Rechazando solicitud conexion");
					}
					else{
						System.out.println("Opcion no reconocida. Elegir otra opción");
					}

				//}
			}
		}


		public void agregarDistrito() throws InterruptedException{
			int a;
			String nombre, ip_multicast, ip_peticiones, p_multicast, p_peticiones;

			//in.nextLine();
			System.out.println("Nombre Distrito:");
			//nombre = in.nextLine();
			nombre = waitInput();

			System.out.println("IP Multicast:");
			//ip_multicast = in.nextLine();
			ip_multicast = waitInput();

			System.out.println("Puerto Multicast:");
			//p_multicast = in.nextLine();
			p_multicast = waitInput();

			System.out.println("IP Peticiones:");
			//ip_peticiones = in.nextLine();
			ip_peticiones = waitInput();

			System.out.println("Puerto Peticiones:");
			//p_peticiones = in.nextLine();
			p_peticiones = waitInput();

			System.out.println("Se agregara el Distrito:");
      System.out.println("Nombre: "+nombre);
      System.out.println("IP Multicast: "+ip_multicast);
      System.out.println("Puerto Multicast: "+p_multicast);
      System.out.println("IP Peticiones: "+ip_peticiones);
      System.out.println("Puerto Peticiones: "+p_peticiones);
	    //intsertar Distrito
      Lista_Distritos[0][contador_Distirtos]= nombre;
      Lista_Distritos[1][contador_Distirtos]= ip_multicast;
      Lista_Distritos[2][contador_Distirtos]= p_multicast;
      Lista_Distritos[3][contador_Distirtos]= ip_peticiones;
      Lista_Distritos[4][contador_Distirtos]= p_peticiones;
      contador_Distirtos++;
			System.out.println("\n");
		}

    public void mostrarDistritos(){
      for (int i=1; (i-1)<contador_Distirtos ; i++){
        System.out.println(i+")"+Lista_Distritos[0][i-1]);
      }
      System.out.println("Enter para volver al Menu");
      //in.nextLine();
    }


		public void sendMessages() throws UnknownHostException, InterruptedException{
			InetAddress addr = InetAddress.getByName(INET_ADDR);
			try (DatagramSocket serverSocket = new DatagramSocket()) {
					for (int i = 0; i < 5; i++) {
							String msg = "Sent message no " + i;

							DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
											msg.getBytes().length, addr, 8889);
							serverSocket.send(msgPacket);

							System.out.println("Server sent packet with msg: " + msg);
							Thread.sleep(500);
					}
			} catch (IOException ex) {
					ex.printStackTrace();
			}
		}

		public void aceptarClientes() {
			if (clientes.size() > 0){

				while(clientes.size() > 0){
					try{
					clienteEntrante aux = clientes.remove(0);
					DatagramPacket peticion = aux.request;


					 System.out.println("Dar autorizacion a /IP: "+aux.IP+" por Distrito X");
					 System.out.println("6.- SI");
					 System.out.println("7.- NO");
					 
					DatagramPacket respuesta =
						new DatagramPacket(peticion.getData(), peticion.getLength(),
															 peticion.getAddress(), peticion.getPort());

					 //String res = in2.nextLine();
					 //user_input = in2.nextLine();
					 //String res = user_input;
					 System.out.println("Waiting option");
					String option = waitInput();

					 System.out.println("option recived!");
					 //user_input = "";
					 System.out.println("res dice: "+option);
					 client_requesting = false;
					 lock.notify();
					 System.out.println("Coloque opción nueva del menú");
					 //aux.socket.send(respuesta);
					 clientSocket.send(respuesta);
					} catch (Exception e){}
				}
			
			}

			else{
				System.out.println("No hay clientes solicitando conexión");
			}
		}

		public void recibirMensajes()  throws UnknownHostException, InterruptedException {
				String INET_ADDR = "224.0.0.3";
				int PORT = 8888; // escucha el puerto 8888
        			InetAddress address = InetAddress.getByName(INET_ADDR);

        			//try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //				clientSocket.joinGroup(address);
								try{
									//DatagramSocket clientSocket = new DatagramSocket(PORT);
									clientSocket = new MulticastSocket(PORT);
            			clientSocket.joinGroup(address);
									 byte[] buf = new byte[256];
            				while (true) {
                			   DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                			   clientSocket.receive(msgPacket);
												System.out.println("Conexión de cliente entrante de Ip "+msgPacket.getAddress());
												 //client_requesting = true;
												//synchronized (lock) {

													 String msg= new String(msgPacket.getData(),msgPacket.getOffset(), msgPacket.getLength());
													 msg = new String(buf, 0, buf.length);
													 // debo recibir del Mensajes la IP y el Distrito
													 // buscar Distrito X en lsita de dsitritos
													 String nombre = "";
													 //Thread.sleep(5000);

														 System.out.println("Esperando lock");
													//lock.wait();
													 int numero_distrito = buscarDistrito(nombre);
													// if(numero_distrito > 0){
														clienteEntrante aux = new clienteEntrante(msgPacket.getAddress(), 
																msg, msgPacket, clientSocket);
														clientes.add(aux);

														 //System.out.println("Dar autorizacion a /IP: por Distrito X");
														 //System.out.println("6.- SI");
														 //System.out.println("7.- NO");

														 //String res = in2.nextLine();
														 //user_input = in2.nextLine();
														 //String res = user_input;
														 System.out.println("Waiting option");
														String option = waitInput();

														 System.out.println("option recived!");
														 //user_input = "";
														 System.out.println("res dice: "+option);
														 client_requesting = false;
														 //lock.notify();
														 System.out.println("Coloque opción nueva del menú");
														 
												//	}

                      //   }

            				}
        			} catch (IOException ex) {
            		              ex.printStackTrace();
        	                }
	         }

		public String waitInput() throws InterruptedException{
			while(true){
				if (!user_input.equals("")){
					String response = user_input;
					user_input = "";
					return response;
				}
				Thread.sleep(500);
			}
		}

      public static int buscarDistrito(String nombre){
          return 0;
      }
}
