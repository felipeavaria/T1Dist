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
				System.out.println("suma = "+ aux);
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

    /**
     * Método del Token, para poder encolar valores en Q.
     */
		public void queve(int proc){
				Q.add(proc);
				System.out.print("[");
				for(int i=0; i<Q.size(); i++){
					System.out.print(Q.get(i)+"-");
				}
				System.out.println("]");
		}

    /**
     * Retorna el largo de la lista Q, es utilizado para calculos en procesos.
     * @return Largo de la lista Q.
     */
		public int QLength(){
				return Q.size();
		}

    /**
     * Devuelve elemento de la lista que haya ingresado antes, de la forma FIFO.
     * @return N° de proceso, al cual le toca estar en la Seccion Critica
     */
		public int getNextQ(){
				int aux = Q.get(0);
				Q.remove(0);
				return aux;
		}

    /**
     * Funcion, que retorna la lista LN del Token; Utilizado para calculos en 
		 * los procesos.
     * @return Arreglo LN() del Token
     */
    public int[] get_LN() {
        return LN;
    }

    /**
     *  Edita valores de LN(), esto sirve cuando se tiene numeros de secuencia.
     */
		public void set_LN(int proc, int value) {
				LN[proc] = value;
		}
}
