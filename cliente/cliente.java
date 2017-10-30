import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;


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

		public Thread thread1;


    public static void main(String[] args) throws UnknownHostException, Exception {
			new cliente();
    }


		public cliente() throws UnknownHostException, Exception {

			Thread thread1 = new Thread() {
				@Override
				public void run() {
						try{
							InetAddress address = InetAddress.getByName(DIST_IPMULT);
							byte[] buf = new byte[256];
							
							try (MulticastSocket clientSocket = new MulticastSocket(DIST_PMULT)){
									clientSocket.joinGroup(address);

									while (!this.isInterrupted()) {
											DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
											clientSocket.receive(msgPacket);

											String msg = new String(buf, 0, buf.length);
											System.out.println(msg);
									}
									System.out.println("Se acabo el loop :)");
							} catch (IOException ex) {
									ex.printStackTrace();
							}
						}
						catch(Exception e){
							 e.printStackTrace();
						}
				}
			};

			Scanner in = new Scanner(System.in);
			System.out.println("Ingresar IP del Servidor Central");
			INET_ADDR_SCENTRAL = in.nextLine();
			System.out.println("Ingrese Puerto de peticiones Servidor Central");
			String puerto_central = in.nextLine();
			System.out.println("Ingrese nombre de Distrito a Investigar");
			String distrito_selec = in.nextLine();
			if (puerto_central != "")
				PORT_SERVCENTRAL = Integer.parseInt(puerto_central);

			String in_msg = "0-"+distrito_selec;
			System.out.println("Esperando respuesta del servidor central...");
			String data_distrito = sendUnicastMsg(in_msg, 0);
			if(data_distrito != null){
				System.out.println("antes del split");
				String[] data = data_distrito.split("-");
				System.out.println("despues del split");
				DIST_NOMBRE = data[0];  //nombre
				DIST_IPMULT = data[1];  //ip multicast
				DIST_PMULT = Integer.parseInt(data[2]);  //puerto multicast
				DIST_IPPETIC = data[3];  //ip peticiones (ip en la red del servidor de distrito)
				DIST_PPETIC = Integer.parseInt(data[4]);  // puerto peticiones
				thread1.start();
			}
			else{
				System.out.println("Conexión Erronea");
			}


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
			int choose = 0, titan_id;

			//

			while(menu){
				System.out.println("Consola");
				System.out.println("(1) Listar Titanes");
				System.out.println("(2) Cambiar Distrito");
				System.out.println("(3) Capturar Titan");
				System.out.println("(4) Asesinar Titan");
				System.out.println("(5) Listar Titanes Capturados");
				System.out.println("(6) Listar Titanes Asesinados");
				System.out.println("Elegir Opción:");
				System.out.println("7) Conectar con Servidor Central");
				System.out.println("8) Recibir Mensajes (no usada ahora)");
				System.out.println("9) Salir");
				choose = in.nextInt();

				if(choose == 1){
					String in_msg = "1";
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose == 2){
					String in_msg = "2";
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose == 3){
					System.out.println("Ingrese el ID del titan a capturar");
					titan_id = in.nextInt();
					in.nextLine();
					String in_msg = "3-"+titan_id;
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose == 4){
					System.out.println("Ingrese el ID del titan a asesinar");
					titan_id = in.nextInt();
					in.nextLine();
					String in_msg = "4-"+titan_id;
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose == 5){
					String in_msg = "5";
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose == 6){
					String in_msg = "6";
					sendUnicastMsg(in_msg, 1);
				}
				else if(choose == 7){
					// ConectarServCentral(in);
					String in_msg = "0-trost";
					sendUnicastMsg(in_msg, 0);

				}
				else if(choose == 8){
					//recibirMensajes();
				}
				else if(choose == 9){
					menu = false;
				}
				else{
					System.out.println("Opcion no reconocida. Elegir otra opción");
				}
			}

			System.out.println("Terminando App");
			thread1.interrupt();
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


		/*
		public void recibirMensajes()  throws UnknownHostException {
        InetAddress address = InetAddress.getByName(INET_ADDR);
				boolean while_ = true;
        byte[] buf = new byte[256];

        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            clientSocket.joinGroup(address);
            while (while_) {
								if (Thread.currentThread().isInterrupted()) {
									//System.out.println("Interrupting");
									while_ = false;
								}
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println(msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		}
		*/


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
