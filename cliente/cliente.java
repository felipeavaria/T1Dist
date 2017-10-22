import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class cliente{

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		new cliente();
	}


	public cliente() throws UnknownHostException, InterruptedException {
		Scanner in = new Scanner(System.in);
		menu(in);
	}


	public void menu(Scanner in) throws UnknownHostException, InterruptedException {
		boolean menu = true;
		int choose = 0; 
		Thread hiloA;
		hiloA = new Thread(){
				public void run(){
					try{
						recibirMensajes();
					}catch (IOException ex){
						ex.printStackTrace();
					}
				}
			};
			hiloA.start();

			while(menu){
				System.out.println("Elegir Opción:");
				System.out.println("1) Conectar con Servidor Central");
				System.out.println("2) Recibir Mensajes");
				System.out.println("3) Salir");

				choose = in.nextInt();
		
			if(choose == 1){
				ConectarServCentral(in);
			}
			else if(choose == 2){


			}
			else if(choose == 3){
				hiloA.stop();
				menu = false;

			}
			else{
				System.out.println("Opcion no reconocida. Elegir otra opción");

			}
		}
	}



	public static void recibirMensajes() throws UnknownHostException {
		String INET_ADDR = "224.0.0.3";
		int PORT = 8889;
		InetAddress address = InetAddress.getByName(INET_ADDR);
		byte[] buf = new byte[256];
		try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
			clientSocket.joinGroup(address);
			while (true) {
				DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				clientSocket.receive(msgPacket);	
				String msg = new String(buf, 0, buf.length);
				System.out.println("Socket 1 received msg: " + msg);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
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
			DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),msg.getBytes().length, addr, 8887);
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
