import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
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
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
			new serv_central();
    }

		public serv_central() throws UnknownHostException, InterruptedException {
			Scanner in = new Scanner(System.in);
			menu(in);
		}


		public void menu(Scanner in) throws UnknownHostException, InterruptedException{
			boolean menu = true;
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


			int choose = 0;
			while(menu){
				System.out.println("Elegir Opción:");
				System.out.println("1) Agregar Distrito");
        System.out.println("2) ver de Distritos");
				System.out.println("3) Enviar Mensajes a Clientes");
				System.out.println("4) Salir");
				choose = in.nextInt();

				if(choose == 1){
					agregarDistrito(in);
				}
				else if(choose == 2){
					mostrarDistritos(in);
				}
        else if(choose == 3){
					sendMessages();
				}
				else if(choose == 4){
					hiloA.stop();
					menu = false;
				}
				else if(choose == 5){

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

    public void mostrarDistritos(Scanner in){
      for (int i=1; (i-1)<contador_Distirtos ; i++){
        System.out.println(i+")"+Lista_Distritos[0][i-1]);
      }
      System.out.println("Enter para volver al Menu");
      in.nextLine();
      in.nextLine();
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

		public static void recibirMensajes()  throws UnknownHostException {
				String INET_ADDR = "224.0.0.3";
				Scanner in2 = new Scanner(System.in);
				int PORT = 8888; // escucha el puerto 8888
        			InetAddress address = InetAddress.getByName(INET_ADDR);

        			try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            				clientSocket.joinGroup(address);

            				while (true) {
                         byte[] buf = new byte[256];
                			   DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                			   clientSocket.receive(msgPacket);

                			   String msg= new String(msgPacket.getData(),msgPacket.getOffset(), msgPacket.getLength());
                			   msg = new String(buf, 0, buf.length);
                         // debo recibir del Mensajes la IP y el Distrito
                         // buscar Distrito X en lsita de dsitritos
                         String nombre = "";
                         //Thread.sleep(5000);
                         int numero_distrito = buscarDistrito(nombre);
                        // if(numero_distrito > 0){

                           System.out.println("Dar autorizacion a /IP: por Distrito X");
                           System.out.println("1.- SI");
                           System.out.println("2.- NO");
                           String res=in2.nextLine();
                           in2.nextLine();
													 System.out.println("res dice: "+res);
                      //   }

            				}
        			} catch (IOException ex) {
            		              ex.printStackTrace();
        	                }
	         }

      public static int buscarDistrito(String nombre){
          return 0;
      }
}
