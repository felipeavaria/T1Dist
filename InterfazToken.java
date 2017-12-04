/*
 * InterfaceRemota.java
 *
 * Created on 27 de abril de 2004, 21:17
 */

import java.rmi.*;
import java.io.Serializable;

/**
 * Interface remota con los metodos que se podran llamar en remoto
 * @author  Javier Abellan
 */
public interface InterfazToken extends Remote {
    public int suma (int a, int b) throws RemoteException;
    public boolean soyDe (int a) throws RemoteException;
    public void getToken(int p)throws RemoteException;
    public boolean available (int p) throws RemoteException;
    public boolean freeToken (int p) throws RemoteException;
}


/*
public interface InterfazToken2 extends Serializable
{
    public int getSumando1();
    public int getSumando2();
}


public class Token implements InterfazToken2
{
    private int a=0;
    private int b=0;
    public Sumando (int a, int b)    {
        this.a=a;
        this.b=b;
    }
    public int getSumando1()    {
        return a;
    }
    public int getSumando2()    {
        return b;
    }
}
*/
