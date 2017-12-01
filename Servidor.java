/*
 * Javier Abellan. Servidor.java
 *
 * Created on 2 de abril de 2004, 19:15
 */

//package chuidiang.ejemplos.rmi.suma;

//import java.rmi.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Servidor para el ejemplo de RMI.
 * Exporte un metodo para sumar dos enteros y devuelve el resultado.
 */
public class Servidor 
{
    
    /** Crea nueva instancia de Servidor rmi */
    public Servidor() {
        try 
        {
		// Se indica a rmiregistry donde estan las clases.
		// Cambiar el path al sitio en el que este. Es importante
		// mantener la barra al final.
		/*
		System.setProperty(
			"java.rmi.server.codebase",
			"file:$HOME/myclasses");*/
		
            // Se publica el objeto remoto
            InterfazToken token = new Token(6);
            Naming.rebind ("//localhost/Token", token);
            //LocateRegistry.createRegistry(1099);
						System.out.println("LocateRegistry ready");
						//Thread.sleep(50000);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Servidor();
    }
}




/**
 * Main class to initiate RMI registry and start servers
 */
/*
 *
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
public class DA_Suzuki_Kasami_Main {
    
    public static void main(String[] args){

        / / RMI registry initialization
        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        //Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        new ProcessManager().startServer();
    }

}*/
