import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class serv_distrito {
    
    //final static String INET_ADDR = "224.0.0.3";
    //final static int PORT = 8888;
		public static String nombre = "";

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
			new serv_distrito();
    }


		public serv_distrito() throws UnknownHostException, InterruptedException {
			Scanner in = new Scanner(System.in);
			System.out.println("Ingrese nombre del Distrito:");
			nombre = in.nextLine();

			menu(in);
		}


		public void menu(Scanner in) throws UnknownHostException, InterruptedException{
			boolean menu = true;
			int choose = 0; 


			while(menu){
				System.out.println("[Distrito "+nombre+"] Elegir Opción:");
				System.out.println("[Distrito "+nombre+"] 1) Agregar Distrito");
				System.out.println("[Distrito "+nombre+"] 2) Agregar Titan");
				System.out.println("[Distrito "+nombre+"] 3) Enviar Mensajes a Clientes");
				System.out.println("[Distrito "+nombre+"] 4) Salir");
				choose = in.nextInt();

				if(choose == 1){
					agregarDistrito(in);
				}
				else if(choose == 2){
					agregarTitan(in);
				}
				else if(choose == 3){
					sendMessages();
				}
				else if(choose == 4){
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
			String name_titan;

			in.nextLine();
			System.out.println("[Distrito "+nombre+"] Publicar Titan");
			System.out.println("[Distrito "+nombre+"] Introducir nombre");
			name_titan = in.nextLine();
			System.out.println("[Distrito "+nombre+"] Introducir tipo");
			System.out.println("1.- Normal");
			System.out.println("2.- Excentrico");
			System.out.println("3.- Cambiante");
			tipo_titan = in.nextInt();
			in.nextLine();
			//Agregar el Titan a la base de datos, con las variables anteriores para obtener el ID
			System.out.println("************");
			System.out.println("ID: ");
			System.out.println("Nombre: "+name_titan+"");
			System.out.println("Tipo: "+tipo_titan+"");
			System.out.println("************");
			/********************/
			// Avisar a los Cientes (a través de Multicast), que Titan nuevo ha aparecido, de la siguiente
			// forma:
			// [Cliente] Aparece nuevo Titan! Eren, tipo Cambiante, ID 1.
			/********************/
			String msg = "Aparece nuevo Titan! "+name_titan+", tipo "+tipo_titan+", ID: ?";
			sendMessage(msg);
		}


		public void sendMessage(String msg) throws UnknownHostException, InterruptedException{
			String INET_ADDR = "224.0.0.3";
			int PORT = 8888;

			InetAddress addr = InetAddress.getByName(INET_ADDR);
			try (DatagramSocket serverSocket = new DatagramSocket()) {
					DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
									msg.getBytes().length, addr, PORT);
					serverSocket.send(msgPacket);
					//Thread.sleep(500);
			} catch (IOException ex) {
					ex.printStackTrace();
			}
		}


		public void sendMessages() throws UnknownHostException, InterruptedException{
			String INET_ADDR = "224.0.0.3";
			int PORT = 8888;

			InetAddress addr = InetAddress.getByName(INET_ADDR);
			try (DatagramSocket serverSocket = new DatagramSocket()) {
					for (int i = 0; i < 5; i++) {
							String msg = "Sent message no " + i;

							DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
											msg.getBytes().length, addr, PORT);
							serverSocket.send(msgPacket);
	 
							System.out.println("Server sent packet with msg: " + msg);
							Thread.sleep(500);
					}
			} catch (IOException ex) {
					ex.printStackTrace();
			}
		}


}
