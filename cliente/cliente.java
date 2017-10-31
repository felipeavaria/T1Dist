import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.ArrayList;


public class cliente {


		//public String INET_ADDR = "224.0.0.3";
		public String INET_ADDR_SCENTRAL = "";
		public int PORT_SERVCENTRAL = 8886;
		public int PORT_SERVDISTRITO = 8887;

		public String DIST_NOMBRE = "";
		public String DIST_IPMULT = "";
		public int DIST_PMULT = 8887;
		public String DIST_IPPETIC = "";
		public int DIST_PPETIC = 8887;

		public Thread listenMulticast;
		public Future listMulticast;
		//public Thread listenMulticast;
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		boolean running = true;
		public ArrayList<Titan> capturados = new ArrayList<Titan>();
		public ArrayList<Titan> asesinados = new ArrayList<Titan>();
		public int id_dist= 0;


    public static void main(String[] args) throws UnknownHostException, Exception {
			new cliente();
    }

		public class Titan {
			int ID;
			String Name;
			int Tipo;
			String Estado;
			String Distrito;
			public Titan() {
			}
			public Titan(int ID_, String Name_, int Tipo_, String Estado_, String Distrito_) {
				this.ID = ID_;
				this.Name = Name_;
				this.Estado = Estado_;
				this.Tipo = Tipo_;
				this.Distrito = Distrito_;
			}
		}

		public void AskServCentral(Scanner in){
			System.out.println("Ingrese nombre de Distrito a Investigar");
			String distrito_selec = in.nextLine();

			String in_msg = "0-"+distrito_selec;
			System.out.println("Esperando respuesta del servidor central...");
			try{
				String data_distrito = sendUnicastMsg(in_msg, 0);
				if(!(data_distrito.equals(""))){
					String[] data = data_distrito.split("-");
					DIST_NOMBRE = data[0];  //nombre
					DIST_IPMULT = data[1];  //ip multicast
					DIST_PMULT = Integer.parseInt(data[2]);  //puerto multicast
					DIST_IPPETIC = data[3];  //ip peticiones (ip en la red del servidor de distrito)
					DIST_PPETIC = Integer.parseInt(data[4]);  // puerto peticiones
				}
				else{
					System.out.println("Conexión Erronea, no se encontro el distrito. Vuelva a iniciar programa.");
					System.exit(0);
				}

			} catch (Exception e) {}

		}


		public cliente() throws UnknownHostException, Exception {

			Scanner in = new Scanner(System.in);
			System.out.println("Ingresar IP del Servidor Central");
			INET_ADDR_SCENTRAL = in.nextLine();
			System.out.println("Ingrese Puerto de peticiones Servidor Central");
			String puerto_central = in.nextLine();
			if (puerto_central != "")
				PORT_SERVCENTRAL = Integer.parseInt(puerto_central);

			AskServCentral(in);
			listMulticast = executorService.submit(new Runnable() {
					public void run() {
						try{
							int this_id = id_dist;
							InetAddress address = InetAddress.getByName(DIST_IPMULT);
							try (MulticastSocket clientSocket = new MulticastSocket(DIST_PMULT)){
									clientSocket.joinGroup(address);
									while (true) {
											byte[] buf = new byte[256];
											DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
											clientSocket.receive(msgPacket);
											//String msg = new String(buf, 0, buf.length);
											String msg = new String(msgPacket.getData(), msgPacket.getOffset(), msgPacket.getLength());
											if(!(msg.equals("")) && (this_id == id_dist)){ System.out.println(msg);}
									}
							} catch (IOException ex) {
									ex.printStackTrace();
							}
						}
						catch(Exception e){
							 e.printStackTrace();
						}
					}
			});

			menu(in);
		}


		 public String sendUnicastMsg(String s, int target) throws Exception {
				DatagramSocket clientSocket = new DatagramSocket();
				String INET_ADDR = "";
				int PORT = 0;

				if(target == 0){
					INET_ADDR = INET_ADDR_SCENTRAL;
					PORT = PORT_SERVCENTRAL;
				}
				else {
					INET_ADDR = DIST_IPPETIC;
					PORT = DIST_PPETIC;
				}

				InetAddress IPAddress = InetAddress.getByName(INET_ADDR);
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];
				sendData = s.getBytes();

				// Aqui abrire la lógica, para enviar:
				// serv_central: pedir conexión a distrito, y obtener info
				// serv_distrito: acciones a un titan

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
				clientSocket.send(sendPacket);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
				System.out.println(modifiedSentence);
				clientSocket.close();

				return modifiedSentence;
		 }


		public void menu(Scanner in) throws UnknownHostException, Exception {
			boolean menu = true;
			String choose = "0";
		 	int titan_id;

			while(menu){
				System.out.println("Consola");
				System.out.println("(1) Listar Titanes");
				System.out.println("(2) Cambiar Distrito");
				System.out.println("(3) Capturar Titan");
				System.out.println("(4) Asesinar Titan");
				System.out.println("(5) Listar Titanes Capturados");
				System.out.println("(6) Listar Titanes Asesinados");

				choose = in.nextLine();

				if(choose.equals("1")){
					String in_msg = "1";
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose.equals("2")){
					String in_msg = "2";
					listMulticast.cancel(true); 
					AskServCentral(in);
					id_dist++;

					listMulticast = executorService.submit(new Runnable() {
							public void run() {
								try{
									int this_id = id_dist;
									InetAddress address = InetAddress.getByName(DIST_IPMULT);
									try (MulticastSocket clientSocket = new MulticastSocket(DIST_PMULT)){
											clientSocket.joinGroup(address);
											while (running) {
													byte[] buf = new byte[256];
													DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
													clientSocket.receive(msgPacket);
													String msg = new String(msgPacket.getData(), msgPacket.getOffset(), msgPacket.getLength());
													if(!(msg.equals("")) && (this_id == id_dist)){ System.out.println(msg);}
											}
									} catch (IOException ex) {
											ex.printStackTrace();
									}
								}
								catch(Exception e){
									 e.printStackTrace();
								}
							}
					});
				}
				else if(choose.equals("3")){
					System.out.println("Ingrese el ID del titan a capturar");
					titan_id = in.nextInt();
					in.nextLine();
					String in_msg = "3-"+titan_id;
					String resp = sendUnicastMsg(in_msg, 1);
					manipularTitan(resp, 1);
				}
				else if(choose.equals("4")){
					System.out.println("Ingrese el ID del titan a asesinar");
					titan_id = in.nextInt();
					in.nextLine();
					String in_msg = "4-"+titan_id;
					String resp = sendUnicastMsg(in_msg, 1);
					manipularTitan(resp, 2);
				}
				else if(choose.equals("5")){
					String in_msg = "5";
					imprimirTitanes(capturados);
					//sendUnicastMsg(in_msg, 1);
				}
				else if(choose.equals("6")){
					String in_msg = "6";
					imprimirTitanes(asesinados);
					//sendUnicastMsg(in_msg, 1);
				}

				else{
					System.out.println("Opcion no reconocida. Elegir otra opción");
				}
			}

			System.out.println("Terminando App");
			listenMulticast.interrupt();
		}


		public void manipularTitan(String resp, int accion){
				if(!resp.equals("0")){
					String [] val= resp.split("-");
					Titan aux = new Titan(Integer.parseInt(val[0]), val[1], 
							Integer.parseInt(val[2]), val[3], val[4]);
					if (accion == 1) {
						capturados.add(aux);
					}
					else {
						asesinados.add(aux);
					}
				}
				else{
					System.out.println("Titan no manipulable");
				}
		}


		public void imprimirTitanes(ArrayList<Titan> titanes){
			String response = "";
			if(titanes.size() > 0){
				for (int i = 0; i < titanes.size(); i++){
					Titan aux = titanes.get(i);
					response+="************\n";
					response+="ID: "+aux.ID+"\n";
					response+="Nombre: "+aux.Name+"\n";
					response+="Tipo: "+aux.Tipo+"\n";
					response+="Estado: "+aux.Estado+"\n";
					response+="Distrito: "+aux.Distrito+"\n";
					response+="************\n";
					System.out.println(response);
				}
			}
			else {
				System.out.println("No se encuentran titanes en este estado para el cliente");
			}
		}


		public static void conectarDistrito(Scanner in) {
			int a;
			float p_multicast, p_peticiones;
			String nombre, ip_multicast, ip_peticiones;

			in.nextLine();
			System.out.println("Nombre Distrito:");
			nombre = in.nextLine();

			System.out.println("IP Multicast:");
			ip_multicast = in.nextLine();

			System.out.println("Puerto Multicast:");
			p_multicast = in.nextInt();
			in.nextLine();

			System.out.println("IP Peticiones:");
			ip_peticiones = in.nextLine();

			System.out.println("Puerto Peticiones:");
			p_peticiones = in.nextInt();

			System.out.println("Varaibles:");
			System.out.println("p_multicast: "+p_multicast);
			System.out.println("p_peticiones: "+p_peticiones);
			System.out.println("nombre: "+nombre);
			System.out.println("ip_multicast: "+ip_multicast);
			System.out.println("ip_peticiones: "+ip_peticiones);
			in.nextLine();
		}


	public static void ConectarServCentral(Scanner in) throws UnknownHostException, InterruptedException{
		InetAddress addr = InetAddress.getByName("224.0.0.3");
		try (DatagramSocket serverSocket = new DatagramSocket()) {
			System.out.println("ip distrito:");
			in.nextLine();
			String ip_distrito = in.nextLine();
			System.out.println("puerto distrito:");
			int puerto_distrito = in.nextInt();
			String msg;
			msg = ip_distrito +"/"+puerto_distrito; // ver luego el formato de envio
			DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),msg.getBytes().length, addr, 8888);
			serverSocket.send(msgPacket);// envio del mensaje
			System.out.println("Espere mensaje de respuesta del dsitrito" + msg);
			String res = respuestaServiroCentral();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

// esperar respuesta del servidor central
	public static String respuestaServiroCentral() throws UnknownHostException {
		String INET_ADDR = "224.0.0.3";
		String respuesta = new String();
		int PORT = 8889;
		InetAddress address = InetAddress.getByName(INET_ADDR);
		byte[] buf = new byte[256];
		try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
			clientSocket.joinGroup(address);
			while (true) {

				DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				clientSocket.receive(msgPacket);
				respuesta = new String(buf, 0, buf.length);
				System.out.println("Respuesta del Servidor Central: " + respuesta);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return respuesta;
	}
}
