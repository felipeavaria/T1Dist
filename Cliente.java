/*
 * Cliente.java
 *
 * Ejemplo de muy simple de rmi
 */

import java.rmi.*;

/**
 * Ejemplo de cliente rmi nocivo, para aprovecharse de un servidor sin
 * SecurityManager.
 * @author  Javier Abellan
 */
public class Cliente {
    
    /** Crea nueva instancia de Cliente */
    public Cliente() 
    {
        try
        {
		// Lugar en el que esta el objeto remoto.
		// Debe reemplazarse "localhost" por el nombre o ip donde
		// este corriendo "rmiregistry".
		// Naming.lookup() obtiene el objeto remoto
            IRemota objetoRemoto = 
                (IRemota)Naming.lookup ("//localhost/ObjetoRemoto");
            
            // Se realiza la suma remota.
            System.out.print ("2 + 3 = ");
						// a traves
						// de objetoRemoto, el servidor realiza el metodo suma (no el cliente), y tengo la
						// respuesta a través de este
            System.out.println (objetoRemoto.suma(2,3));
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
        new Cliente();
    }
    
}
