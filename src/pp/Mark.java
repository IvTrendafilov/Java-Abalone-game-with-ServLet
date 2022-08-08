package pp;

/*
 * COMMENTS FOR ENUM
 * 
 */

public enum Mark {
	E, R, G, Y, P;

    /**
     * this method return the opponents and partner of a player
     * @return
     */

    public Mark other() {
        if (this == R) {
            return G;
        } else if (this == G) {
            return R;
        } 
        else if(this == Y) {
        	return P;
        }
        else if(this == P) {
        	return Y;
        }
        else {
            return E;
        }
        
    }
}
