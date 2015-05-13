package dk.zbc.h4.kortspil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 *
 * @author runra
 */
public class SorteperMgr extends KortspilMgr {
    
    private static SorteperMgr instance = null;
    
    private SorteperMgr() {
        this.spillerListe = new ArrayList<Spiller>();
        this.maxPlayers = 4;
        this.minPlayers = 2;
    }
    
    public static SorteperMgr getInstance() {
        if(instance == null) {
            instance = new SorteperMgr();
        }
        return instance;
    }

    public void forberedSpil() {
        // Forbereder spillet;
    }
    public String spilStatus() {
        String msg = null;
        if(gameEnded != true) {
            if(spillerListe.size() >= minPlayers && spillerListe.size() <= maxPlayers) {
                if(this.erAlleSpillereKlar()) {
                    Spiller spiller = spillerListe.get(activePlayerIndex);
                    msg = "Venter på at " + spiller.getNavn() + " laver et træk.";
                }else{
                    msg = "Venter på at alle spillere er klar.";
                }
            }else{
                msg = "Venter på flere spillere.";
            }
        }else{
            msg = "Spillet er slut, spiller: " + spillerListe.get(activePlayerIndex).getNavn() + " har tabt.";
        }
        return msg;
    }
    public void startSpil() {
        if(erKlarTilAtSpille()) {
            this.lavDeck();
            this.deck = this.blandKort(this.deck);
            this.uddelKort();
            this.activePlayerIndex = 0;
        }
    }



    @Override
    public boolean erKlarTilAtSpille() {
        if(spillerListe.size() >= minPlayers && spillerListe.size() <= maxPlayers) {
            if(this.erAlleSpillereKlar()) {
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    public void slutSpil() {
        gameEnded = true;
    }

    public Spiller findNaesteSpiller() {
        Spiller naesteSpiller = null;
        ArrayList<Spiller> spillereMedKort = this.spillereMedKort();
        Collections.reverse(spillereMedKort);
        for(Spiller spiller : spillereMedKort) {
            if(spillerListe.indexOf(spiller) != activePlayerIndex) {
                naesteSpiller = spiller;
                break;
            }
        }
        return naesteSpiller;
    }
    public void setNextPlayer(Spiller spiller) {
        // Hvis spilleren har mere end 0 kort på hånden
        int index = 0;
        if(spiller.getHaand().size() > 0) {
            index = this.spillerListe.indexOf(spiller);
        }else{
            index = this.spillerListe.indexOf(this.findModstander());
        }

        this.setActivePlayerIndex(index);
    }

    public Spiller findModstander() {
        Spiller modstander = null;

        // Hvis den aktive spiller er lig med 0 - altså først i arrayet
        if(activePlayerIndex == 0) {
            // Hvis den sidste sidste spiller i spiller arrayet har kort, sæt person som aktiv spiller
            if(spillerListe.get(spillerListe.size()-1).getHaand().size() > 0) {
                modstander = spillerListe.get((spillerListe.size()-1));
            }else{ // Hvis ikke, find den næste spiller med kort.
                modstander = this.findNaesteSpiller();
            }
        }else{ // Hvis ikke den aktive spiller er lige med nul
            if(spillerListe.get((activePlayerIndex -1)).getHaand().size() > 0) {
                modstander = spillerListe.get((activePlayerIndex-1));
            }else{
                modstander = findNaesteSpiller();
            }
        }

        if (modstander == null) {
            slutSpil();
        }
        return modstander;
    }

    @Override
    public void lavDeck() {
        // LAVER KORT DECK
        Deck dk = new Deck();
        for (int i = 1; i <= 13; i++) {
            for(int kuloer = 1; kuloer <= 4; kuloer++) {


                if(kuloer == 1) {
                    dk.tilfoejKort(new Kort(i, Kort.Kuloer.HJERTER));
                }else if(kuloer == 2) {
                    dk.tilfoejKort(new Kort(i, Kort.Kuloer.KLOER));
                }else if(kuloer == 3) {
                    dk.tilfoejKort(new Kort(i, Kort.Kuloer.RUDER));
                }else if(kuloer == 4) {
                    dk.tilfoejKort(new Kort(i, Kort.Kuloer.SPAR));
                }
            }

        }

        // Tilføj joker / sortper
        dk.tilfoejKort(new Kort(15, Kort.Kuloer.JOKER));

        // Bland kort
        dk = this.blandKort(dk);

        // set kort variable
        this.deck = dk;
    }

    public static void main(String[] args) {
        SorteperMgr.getInstance().startSpil();
    }
}

