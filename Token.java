import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Token implements Serializable {

		private int[] LN;
		private ArrayList<Integer> Q;
		private int nProc;
    private static boolean creado = false;

    /**
     * Constructor del Token
     */
    public Token(int nProcesos){
				nProc = nProcesos;

				LN = new int[nProc];
				Q = new ArrayList<Integer>();
				int aux=0;
				for (int i=0; i<nProc; i++){
					aux = aux+LN[i];
				}
				//System.out.println("suma = "+ aux);
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

		public void queve(int proc){
				Q.add(proc);
				System.out.print("["+Q.get(0));
				for(int i=1; i<Q.size(); i++){
					System.out.print("-"+Q.get(i));
				}
				System.out.println("]");
		}

		public int QLength(){
				return Q.size();
		}

		public int getNextQ(){
				int aux = Q.get(0);
				Q.remove(0);
				return aux;
		}


    public int[] get_LN() {
        return LN;
    }
}
