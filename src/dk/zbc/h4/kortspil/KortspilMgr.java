package dk.zbc.h4.kortspil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author runra
 */
public abstract class KortspilMgr {

    protected int activePlayerIndex = -1;
    protected boolean gameEnded = false;
    protected Deck deck = null;

    protected int maxPlayers;
    protected int minPlayers;

    public abstract void startSpil();
    public abstract void forberedSpil();
    public abstract String spilStatus();
    public abstract boolean erKlarTilAtSpille();
    public abstract void slutSpil();

    protected ArrayList<Spiller> spillerListe = null;

    public void tilfojSpiller(Spiller spiller) {
        spillerListe.add(spiller);
    }

    public boolean getGameEnded() {
        return this.gameEnded;
    }

    public void setGameEnded(boolean val) {
        this.gameEnded = val;
    }

    public void setActivePlayerIndex(int index) {
        this.activePlayerIndex = index;
    }

    public abstract void setNextPlayer(Spiller spiller);

    public boolean isActivePlayer(String uid) {
        if(activePlayerIndex == spillerListe.indexOf(this.findSpillerByID(uid))) {
            return true;
        }else{
            return false;
        }
    }

    public boolean spilStartet() {
        if(activePlayerIndex != -1) {
            return true;
        }else{
            return false;
        }
    }

    public Spiller findSpillerByID(String uid) {
        Spiller returSpiller = null;
        for(Spiller spiller : spillerListe) {
            if(spiller.getUserID() == uid) {
                returSpiller = spiller;
                break;
            }
        }
        return returSpiller;
    }

    public int findSpillerIndexByID(String uid) {

        int index = -1;
        for(Spiller spiller : spillerListe) {
            if(spiller.getUserID().equals(uid)) {
                index = spillerListe.indexOf(spiller);
                break;
            }
        }
        return index;
    }

    public void spillerKlar(String uid) {
        int spillerIndex = this.findSpillerIndexByID(uid);
        spillerListe.get(spillerIndex).setReady(true);

        this.startSpil();
    }

    public boolean erAlleSpillereKlar() {
        boolean spillereErKlar = true;
        for(Spiller spiller : spillerListe) {
            if(spiller.getReady() != true) {
                spillereErKlar = false;
            }
        }
        return spillereErKlar;
    }

    // TODO Flyt til
    protected void uddelKort() {

        // Bland kort
        deck = this.blandKort(this.deck);


        // DEBUG MSG //System.err.println(dk.size());

        //Uddeler kort
        int intCounter = 0;

        for (int i = 0; i< deck.size(); i++){

            for(Spiller s : spillerListe) {
                //System.err.println(dk.size());
                if (intCounter < deck.size()) {
                    Kort k = deck.get(intCounter);
                    s.getHaand().tilfoejKort(k);
                    intCounter++;
                }
            }
        }
        deck.clear();
    }

    public ArrayList<Spiller> spillereMedKort() {
        ArrayList<Spiller> tmpPlayerList = new ArrayList<Spiller>();
        for(Spiller spiller : spillerListe) {
            if(spiller.getHaand().size() > 0) {
                tmpPlayerList.add(spiller);
            }
        }
        return tmpPlayerList;
    }

    public abstract Spiller findNaesteSpiller();
    public abstract Spiller findModstander();



    public Deck blandKort(Deck deck) {
        // Shuffle cards
        if(deck != null) {
            Deck returBlandetKort = deck;
            long seed = System.nanoTime();
            Collections.shuffle(returBlandetKort, new Random(seed));
            return returBlandetKort;
        }else{
            return null;
        }
    }


    public abstract void lavDeck();

    public ArrayList<Spiller> listSpiller() {
        return this.spillerListe;
    }
}
