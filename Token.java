import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Token implements Serializable {

		private int[] LN;
		private int nProc;
    private static boolean creado = false;

    /**
     * Constructor del Token
     */
    public Token(int nProcesos){
				nProc = nProcesos;

				LN = new int[nProc];
				int aux=0;
				for (int i=0; i<nProc; i++){
					aux = aux+LN[i];
				}
				System.out.println("suma = "+ aux);
				//return aux;

    }

    /**
     * Creación del token (solo 1 vez), a traves de variable "creado"
     * @return El token al ser creado, null si es que no lo ha sido.
     */
    public static Token crear(int nProc){
        if (!creado){
            creado = true;
            return new Token(nProc);
        }
        return null;
    }

    public int[] get_LN() {
        return LN;
    }
}
