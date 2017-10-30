import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.ArrayList;


public class serv_distrito {

		public String INET_ADDR = "224.0.0.3";
		public int PORT = 8888;
		public int PORT_UCAST = 8887;
		public Thread thread1;
		public ArrayList<Titan> titanes = new ArrayList<Titan>();
		public int curr_id = 0;

		public String NOMBRE = "";
		public String IP_MULTICAST = "";
		public int P_MULTICAST = 0;
		public String IP_SERVER = "";
		public int P_SERVER = 0;

		public String IP_SCENTRAL = "";
		public int P_SCENTRAL = 0;


    public static void main(String[] args) throws UnknownHostException, InterruptedException {
			new serv_distrito();
    }


		public class Titan {
			int ID;
			String Name;
			int Estado, Tipo;
			public Titan() {
			}
			public Titan(int ID_, String Name_, int Tipo_, int Estado_) {
				this.ID = ID_;
				this.Name = Name_;
				this.Estado = Estado_;
				this.Tipo = Tipo_;
			}
		}


		public serv_distrito() throws UnknownHostException, InterruptedException {
			Scanner in = new Scanner(System.in);
			String aux = "";
			System.out.println("\033[34mIngrese nombre del Distrito:");
			NOMBRE = in.nextLine();

			System.out.println("Ingresar IP Multicast");
			IP_MULTICAST = in.nextLine();

			System.out.println("Ingrese puerto Multicast:");
			aux = in.nextLine();
			P_MULTICAST =  Integer.parseInt(aux);

			System.out.println("IP de Peiticones:");
			IP_SERVER = in.nextLine();

			System.out.println("Puerto de peticiones:");
			aux = in.nextLine();
			P_SERVER = Integer.parseInt(aux);

			System.out.println("IP de Servidor Central:");
			IP_SCENTRAL = in.nextLine();

			System.out.println("Puerto de peticiones Servidor Central:");
			aux = in.nextLine();
			P_SCENTRAL = Integer.parseInt(aux);

			thread1 = new Thread() {
				public void run() {
						try {
							  DatagramSocket serverSocket = new DatagramSocket(P_SERVER);
								byte[] receiveData = new byte[256];
								byte[] sendData = new byte[1024];
								String stringResponse = "";
								while(true) {
											DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
											serverSocket.receive(receivePacket);
											String[] s = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength()).split("-");
											if(s[0].equals("1")){
												stringResponse+="Lista de Titanes:";
												stringResponse = imprimirTitanesCliente(0);
											}
											else if(s[0].equals("2")){
												stringResponse+="Ingrese los datos del distrito a cambiar";
											}
											else if(s[0].equals("3")){
												stringResponse = manipularTitan(Integer.parseInt(s[1]), 1);
												stringResponse+="Titan Capturado! (ficticiamente)";
											}
											else if(s[0].equals("4")){
												stringResponse = manipularTitan(Integer.parseInt(s[1]), 2);
												stringResponse+="Titan asesinado! eres malvado";
											}
											else if(s[0].equals("5")){
												stringResponse+="Listado de Titantes Capturados :D";
												stringResponse = imprimirTitanesCliente(1);
											}
											else if(s[0].equals("6")){
												stringResponse+="Listado de Titanes Asesinados :( ";
												stringResponse = imprimirTitanesCliente(2);
											}

											InetAddress IPAddress = receivePacket.getAddress();
											System.out.println("IP: " + IPAddress);
											int port = receivePacket.getPort();
											sendData = stringResponse.getBytes();
											DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
											serverSocket.send(sendPacket);
									 }
						}
						catch(Exception e){
							 e.printStackTrace();
						}
				}
			};
			thread1.start();
			menu(in);
		}


		public void menu(Scanner in) throws UnknownHostException, InterruptedException{
			boolean menu = true;

			int choose = 0;
			//Opciones de Inicialicación de Servidor
			/*
			System.out.println("Ingrese el Puerto del Servidor");
			String puerto = in.nextLine();
			if (puerto != "")
				PORT = Integer.parseInt(puerto);
				*/

			while(menu){
				System.out.println("[Distrito "+NOMBRE+"] Elegir Opción:");
				System.out.println("[Distrito "+NOMBRE+"] 1) Agregar Distrito");
				System.out.println("[Distrito "+NOMBRE+"] 2) Agregar Titan");
				System.out.println("[Distrito "+NOMBRE+"] 3) Enviar Mensajes a Clientes (no se usa)");
				System.out.println("[Distrito "+NOMBRE+"] 4) Imprimir Titanes");
				System.out.println("[Distrito "+NOMBRE+"] 5) Salir");
				choose = in.nextInt();

				if(choose == 1){
					agregarDistrito(in);
				}
				else if(choose == 2){
					agregarTitan(in);
				}
				else if(choose == 3){
				}
				else if(choose == 4){
					imprimirTitanes();
				}
				else if(choose == 5){
					menu = false;
				}
				else{
					System.out.println("Opcion no reconocida. Elegir otra opción");
				}
			}
		}


		public void agregarDistrito(Scanner in){
			int a;
			float p_multicast, p_peticiones;
			String input_consola = "", ip_multicast, ip_peticiones;

			in.nextLine();
			System.out.println("[Distrito "+input_consola+"] Nombre Distrito:");
			input_consola = in.nextLine();

			System.out.println("[Distrito "+input_consola+"] IP Multicast:");
			ip_multicast = in.nextLine();

			System.out.println("[Distrito "+input_consola+"] Puerto Multicast:");
			p_multicast = in.nextInt();
			in.nextLine();

			System.out.println("[Distrito "+input_consola+"] IP Peticiones:");
			ip_peticiones = in.nextLine();

			System.out.println("[Distrito "+input_consola+"] Puerto Peticiones:");
			p_peticiones = in.nextInt();

			System.out.println("[Distrito "+input_consola+"] Varaibles:");
			System.out.println("p_multicast: "+p_multicast);
			System.out.println("p_peticiones: "+p_peticiones);
			System.out.println("input_consola: "+input_consola);
			System.out.println("ip_multicast: "+ip_multicast);
			System.out.println("ip_peticiones: "+ip_peticiones);
			in.nextLine();
		}


		public void agregarTitan(Scanner in) throws UnknownHostException, InterruptedException{
			int tipo_titan;
			int id_titan = getTitanID();
			String name_titan;

			in.nextLine();
			System.out.println("[Distrito "+NOMBRE+"] Publicar Titan");
			System.out.println("[Distrito "+NOMBRE+"] Introducir nombre");
			name_titan = in.nextLine();
			System.out.println("[Distrito "+NOMBRE+"] Introducir tipo");
			System.out.println("1.- Normal");
			System.out.println("2.- Excentrico");
			System.out.println("3.- Cambiante");
			tipo_titan = in.nextInt();
			in.nextLine();
			System.out.println("************");
			System.out.println("ID: "+id_titan);
			System.out.println("Nombre: "+name_titan+"");
			System.out.println("Tipo: "+tipo_titan+"");
			System.out.println("************");

			Titan titan = new Titan(id_titan, name_titan, tipo_titan, 0);
			titanes.add(titan);
			curr_id++;
			String msg = "Aparece nuevo Titan! "+name_titan+", tipo "+tipo_titan+", ID: "+id_titan+"";
			sendMessage(msg);
		}


		public void imprimirTitanes() {
			Titan aux;
			System.out.println("Lista de titanes:");
			if(titanes.size() == 0){
					System.out.println("No se han registrado Titanes en Distrito");
			}
			else {
				for(int i = 0; i<titanes.size(); i++){
					aux = titanes.get(i);
					System.out.println("************");
					System.out.println("ID: "+aux.ID);
					System.out.println("Nombre: "+aux.Name+"");
					System.out.println("Tipo: "+aux.Tipo+"");
					System.out.println("Estado: "+aux.Estado+"");
					System.out.println("************");
				}
			}
		}


		public String imprimirTitanesCliente(int opcion) {
				Titan aux;
				String response = "";
				System.out.println("Lista de titanes:");
				if(opcion == 0){
					if(titanes.size() == 0){
							response = "No se han registrado Titanes en Distrito";
					}
					else {
						for(int i = 0; i<titanes.size(); i++){
							aux = titanes.get(i);
							response+="************\n";
							response+="ID: "+aux.ID+"\n";
							response+="Nombre: "+aux.Name+"\n";
							response+="Tipo: "+aux.Tipo+"\n";
							response+="Estado: "+aux.Estado+"\n";
							response+="************\n";
						}
					}
					return response;
				}
				else {
					ArrayList<Titan> resp = new ArrayList<Titan>();
					for(int i=0; i<titanes.size(); i++){
						aux = titanes.get(i);
						if (aux.Estado == opcion){
							resp.add(aux);
						}
					}
					if(resp.size() == 0){
							response = "No se han registrado Titanes en Distrito";
					}
					else {
						for(int i = 0; i<resp.size(); i++){
							aux = resp.get(i);
							response+="************\n";
							response+="ID: "+aux.ID+"\n";
							response+="Nombre: "+aux.Name+"\n";
							response+="Tipo: "+aux.Tipo+"\n";
							response+="Estado: "+aux.Estado+"\n";
							response+="************\n";
						}
					}
					return response;
				}
		}


		public String manipularTitan(int id_titan, int accion) {
				Titan aux;
				for(int i=0; i<titanes.size(); i++){
					aux = titanes.get(i);
					if (aux.ID == id_titan && aux.Estado == 0){
						aux.Estado = accion;
						return "Titan "+aux.Name+" manipulado con "+accion+", con id="+aux.ID+"; ";
					}
				}
				System.out.println("Titan no disponible (ya capturado, asesinado, o no pertenece a distrito)");
				return "Titan no disponible (ya capturado, asesinado, o no pertenece a distrito)";
		}


		public String enviarEstado(int Estado){
			if (Estado == 0){
				return "Libre";
			}
			else if (Estado == 1) {
				return "Capturado";
			}
			else{
				return "Asesinado";
			}
		}


		public void sendMessage(String msg) throws UnknownHostException, InterruptedException{
			InetAddress addr = InetAddress.getByName(IP_MULTICAST);
			try (DatagramSocket serverSocket = new DatagramSocket()) {
					DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
									msg.getBytes().length, addr, P_MULTICAST);
					serverSocket.send(msgPacket);
			} catch (IOException ex) {
					ex.printStackTrace();
			}
		}


		public int getTitanID(){
			String modifiedSentence;
			try{
				DatagramSocket clientSocket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName(IP_SCENTRAL);
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];
				String s = "1";
				sendData = s.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, P_SCENTRAL);
				clientSocket.send(sendPacket);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("Esperando ID del servidor central...");
				clientSocket.receive(receivePacket);
				modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
				System.out.println(modifiedSentence);
				clientSocket.close();
			} catch (Exception e) { return 9999; }
				return Integer.parseInt(modifiedSentence);
		}

}
