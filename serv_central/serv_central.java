import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class serv_central {
    
    //final static String INET_ADDR = "224.0.0.3";
    //final static int PORT = 8888;
		//Convertir String a int: int foo = Integer.parseInt("1234");
		public String INET_ADDR = "224.0.0.3";
		public int PORT = 8888;
		public String[][] data = new String[5][5];
		public int dist_count = 0;

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
			new serv_central();
    }


		public serv_central() throws UnknownHostException, InterruptedException {
			Scanner in = new Scanner(System.in);
			menu(in);
		}


		public void menu(Scanner in) throws UnknownHostException, InterruptedException{
			boolean menu = true;
			int choose = 0; 
			while(menu){
				System.out.println("Elegir Opción:");
				System.out.println("1) Agregar Distrito");
				System.out.println("2) Enviar Mensajes a Clientes");
				System.out.println("3) Salir");
				choose = in.nextInt();

				if(choose == 1){
					agregarDistrito(in);
				}
				else if(choose == 2){
					sendMessages();
				}
				else if(choose == 3){
					menu = false;
				}
				else{
					System.out.println("Opcion no reconocida. Elegir otra opción");
				}
			}
		}


		public void agregarDistrito(Scanner in){
			int a;
			String nombre, ip_multicast, ip_peticiones, p_multicast, p_peticiones;

			in.nextLine();
			System.out.println("Nombre Distrito:");
			nombre = in.nextLine();

			System.out.println("IP Multicast:");
			ip_multicast = in.nextLine();
			
			System.out.println("Puerto Multicast:");
			p_multicast = in.nextLine();

			System.out.println("IP Peticiones:");
			ip_peticiones = in.nextLine();

			System.out.println("Puerto Peticiones:");
			p_peticiones = in.nextLine();

			System.out.println("Varaibles:");
			System.out.println("p_multicast: "+p_multicast);
			System.out.println("p_peticiones: "+p_peticiones);
			System.out.println("nombre: "+nombre);
			System.out.println("ip_multicast: "+ip_multicast);
			System.out.println("ip_peticiones: "+ip_peticiones);
			in.nextLine();
		}


		public void sendMessages() throws UnknownHostException, InterruptedException{
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
