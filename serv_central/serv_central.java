import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.ArrayList;
import java.net.MulticastSocket;
import java.util.ArrayList;

/*
Servidor Central:
  coordinador de comunicacion entre clientes y distritos
  debe contener:
    lista de distritos (nombre, Direccion IP grupo multicas, Direccion IP de reciproco , puerto)
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
		public int Puerto_escucha; // puerto Multicast que escucha
		public ArrayList<Distrito> distritos = new ArrayList<Distrito>(); // lista de distritos
		public int ID_titan = 0;
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
				this.request = a;
				this.IP = ID_;
				this.socket = b;
				this.msg = msj;
			}
		}

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

			new serv_central();

    }

		public serv_central() throws UnknownHostException, InterruptedException {
			Scanner in = new Scanner(System.in);
			System.out.println("ingrese el puerto de que escucha el servidor central");
			Puerto_escucha = in.nextInt();

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
		public class Distrito{
			String Nombre;
			String ip_multicast;
			String p_multicast;
			String ip_peticiones;
			String p_peticiones;

			public Distrito(){
			}
			public Distrito(String Nombre_,String ip_multicast_,String p_multicast_,String ip_peticiones_,String p_peticiones_){
				this.Nombre = Nombre_;
				this.ip_multicast=ip_multicast_;
				this.p_multicast = p_multicast_;
				this.ip_peticiones = ip_peticiones_;
				this.p_peticiones = p_peticiones_;
			}
		}



		public void menu() throws UnknownHostException, InterruptedException{
			boolean menu = true;
			//Scanner in= new Scanner(System.in);
			/*
			EscucharMensajes escucha = new EscucharMensajes();
			*/
			//escucha.start();

			while(menu){
				System.out.println("Elegir Opción:");
				System.out.println("1) Agregar Distrito");
        System.out.println("2) Ver lista de Distritos");
				System.out.println("3) Permitir conexión a cleintes pendientes ("+clientes.size()+")");



					String user_input = waitInput();



					if(user_input.equals("1")){
						//System.out.println("opcion 1");
						agregarDistrito();

					}
					else if(user_input.equals("2")){
						mostrarDistritos();
					}
					else if(user_input.equals("3")){
						//hiloA.stop();
						/*
						lock.notify();
						System.out.println("Permitiendo conexion de usuarios");
						lock.wait();
						*/
						aceptarClientes();
					}
			}
		}



		public void agregarDistrito() throws InterruptedException{
			int a;
			String nombre, ip_multicast, ip_peticiones;
			String p_multicast, p_peticiones;
			System.out.println("AGREGAR DISTRITO");
			//in.nextLine();
			System.out.println("Nombre Distrito:");
			//nombre = in.nextLine();
			nombre = waitInput();

			System.out.println("IP Multicast:");
			//ip_multicast = in.nextLine();
			ip_multicast = waitInput();

			System.out.println("Puerto Multicast:");
			p_multicast = waitInput();
			//p_multicast = in.nextLine();

			System.out.println("IP Peticiones:");
			ip_peticiones = waitInput();
			//ip_peticiones = in.nextLine();

			System.out.println("Puerto Peticiones:");
			p_peticiones = waitInput();
			//p_peticiones = in.nextLine();

			Distrito distrito = new Distrito(nombre,ip_multicast, p_multicast,ip_peticiones,p_peticiones);
			distritos.add(distrito);
			System.out.println("**************");
			System.out.println("Distrito agregado:");
      System.out.println("Nombre: "+ nombre);
      System.out.println("IP Multicast: "+ ip_multicast);
      System.out.println("Puerto Multicast: "+ p_multicast);
      System.out.println("IP Peticiones: "+ ip_peticiones);
      System.out.println("Puerto Peticiones: "+ p_peticiones);
			System.out.println("**************");
	    //intsertar Distrito


			System.out.println("\n");
		}

    public void mostrarDistritos() throws InterruptedException{
			Distrito aux;
			System.out.println("***********************************************");
			System.out.println("Lista de dsitritos:");
			if(distritos.size() == 0){
					System.out.println("No se han registrado Distritos");
			}else {
				for(int i = 0; i<distritos.size(); i++){
					aux = distritos.get(i);
					System.out.println("************");
					System.out.println("Nombre: "+aux.Nombre);
					System.out.println("IP Multicast: "+aux.ip_multicast+"");
					System.out.println("IP Peticiones: "+aux.ip_peticiones+"");
					System.out.println("Puerto Multicast: "+aux.p_multicast+"");
					System.out.println("Puerto Peticiones: "+aux.p_peticiones+"");
					System.out.println("************");
				}
			}

      System.out.println("***********************************************");
			Thread.sleep(1000);
    }


		public void sendMessages(String cuerpo,int puerto) throws UnknownHostException, InterruptedException{
			InetAddress addr = InetAddress.getByName(INET_ADDR);
			try (DatagramSocket serverSocket = new DatagramSocket()) {
				DatagramPacket msgPacket = new DatagramPacket(cuerpo.getBytes(),cuerpo.getBytes().length,addr,puerto);
				serverSocket.send(msgPacket);
				System.out.println("cuerpo del mensaje enviado : " + cuerpo);
			} catch (IOException ex) {
					ex.printStackTrace();
			}
		}

		public void aceptarClientes() throws InterruptedException {
			if (clientes.size() > 0){

				while(clientes.size() > 0){
					try{
					clienteEntrante aux = clientes.remove(0);
					DatagramPacket peticion = aux.request;
					// estructura del mensaje
					 System.out.println("Dar autorizacion a /IP: "+aux.IP+" para Distrito "+aux.msg);
					 System.out.println("1.- SI");
					 System.out.println("2.- NO");
					 String ans = waitInput();
					 String response = "";
						if (ans.equals("1")){
							Distrito datos = buscarDatosDistrito(aux.msg);
							response = datos.Nombre+"-"+datos.ip_multicast+"-"+datos.p_multicast+"-"+datos.ip_peticiones+"-"+datos.p_peticiones;

							//nombre?ip_multicast?

							//Aqui falta poner los datos del distrito, que estan en el listado de distritos,
							//para poder enviar la respuesta de servidor al cliente
						}
						else if(ans.equals("2")){
							response = null;
							System.out.println("Conexión rechazada al servidor");
						}
						InetAddress IPAddress = peticion.getAddress();
						int port = peticion.getPort();
						byte[] sendData = new byte[1024];
						sendData = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(
								sendData,
							 	sendData.length,
							 	IPAddress,
								port);
						aux.socket.send(sendPacket);
					} catch (Exception e){
					}
				}
			}
			else{
				System.out.println("No hay clientes solicitando conexión \n");
				Thread.sleep(1000);
			}
		}

		public void imprimirMenu()throws InterruptedException {
			Thread.sleep(1000);
			System.out.println("Elegir Opción:");
			System.out.println("1) Agregar Distrito");
			System.out.println("2) Ver lista de Distritos");
			System.out.println("3) Permitir conexión a cleintes pendientes ("+clientes.size()+")");
		}


		public void recibirMensajes()  throws UnknownHostException, InterruptedException {
				//String INET_ADDR = "224.0.0.3"; //multicast
				int PORT = Puerto_escucha; // escucha el puerto 8888
        			//InetAddress address = InetAddress.getByName(INET_ADDR);
								try{
											DatagramSocket serverSocket = new DatagramSocket(PORT);
											byte[] receiveData = new byte[1024];
											while(true)
												 {
														DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
														serverSocket.receive(receivePacket);
														String[] sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength()).split("-");
														if(sentence[0].equals("0")){
															//Cliente
																System.out.println("Cliente conectandose!");
																if(buscarDistrito(sentence[1])){
																	clienteEntrante aux = new clienteEntrante(receivePacket.getAddress(),
																			sentence[1], receivePacket, serverSocket);
																	clientes.add(aux);
																	System.out.println(">> Cliente añadido a la lista de pendientes");
																}else{
																	System.out.println(">> Cliente NO añadido a la lista de pendientes, distrito consultado no existe");
																	String response = "";;
																	InetAddress IPAddress = receivePacket.getAddress();
																	int port = receivePacket.getPort();
																	byte[] sendData = new byte[1024];
																	sendData = response.getBytes();
																	DatagramPacket sendPacket = new DatagramPacket(
																			sendData,
																		 	sendData.length,
																		 	IPAddress,
																			port);
																	serverSocket.send(sendPacket);

																}

																imprimirMenu();

														}
														else {
															//Servidor distrito
															byte[] sendData = new byte[1024];
															System.out.println(">> ID titan asignado : "+ID_titan);
															imprimirMenu();
															InetAddress IPAddress = receivePacket.getAddress();
															int port = receivePacket.getPort();
															sendData = String.valueOf(ID_titan).getBytes();
															DatagramPacket sendPacket =
																new DatagramPacket(sendData, sendData.length, IPAddress, port);
															serverSocket.send(sendPacket);
															ID_titan++;
														}
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


      public boolean buscarDistrito(String nombre){
					Distrito aux;
					boolean salida=false;
					for(int i = 0; i<distritos.size(); i++){
							aux = distritos.get(i);
							if ( nombre.equals(aux.Nombre) ){
								salida = true;
								break;
							}
					}
					return salida;
      }

			public Distrito buscarDatosDistrito(String nombre){
					Distrito aux;
					for(int i = 0; i<distritos.size(); i++){
							aux = distritos.get(i);
							if ( nombre.equals(aux.Nombre) ){
								return aux;
							}
					}

					return null;
			}

}
