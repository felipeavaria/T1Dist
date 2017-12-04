public class OToken extends IntObjeto {

    private TheToken thetoken;

    public OToken(String srcUrl, int srcId, TheToken thetoken){
        super(srcUrl, srcId);
        this.thetoken = thetoken;
    }

    public TheToken getToken() {
        return thetoken;
    }
}
