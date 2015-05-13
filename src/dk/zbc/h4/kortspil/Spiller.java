package dk.zbc.h4.kortspil;

/**
 * Created by Niklas on 04-05-2015.
 */
public class Spiller {
    
    private String navn;
    private Haand haand;
    private String userID;
    private boolean ready;
    
    public Spiller(String navn, String userID) {
        this.navn = navn;
        this.haand = new Haand();
        this.userID = userID;
        this.ready = false;
    }

    public void setReady(boolean ready) { this.ready = ready; }

    public boolean getReady() { return ready; }

    public String getNavn() {
        return navn;
    }

    public Haand setHaand(Haand haand) { this.haand = haand; return this.haand; }

    public String getUserID() {
        return userID;
    }

    public Haand getHaand() { return haand; }


    public void fjernPar() {
        for (int index1 = this.getHaand().size()-1; index1 > 0; index1--) {

            for (int index = 0; index < index1; index++) {

                if (this.getHaand().get(index1).getVaerdi() == this.getHaand().get(index).getVaerdi())
                {
                    this.getHaand().remove(index1);
                    this.getHaand().remove(index);

                    --index1;
                    break;
                }
            }
        }
    }
}
