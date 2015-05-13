package dk.zbc.h4.kortspil;
import java.util.ArrayList;

/**
 * Created by Niklas on 04-05-2015.
 */
public class Deck extends ArrayList<Kort> {
    
    public void tagKort(int kortIndex) {
        Spiller modstander = SorteperMgr.getInstance().findModstander();

        if (modstander.getHaand().size() > 0) {
            Kort kort = modstander.getHaand().get(kortIndex);

            Spiller player1 = SorteperMgr.getInstance().listSpiller().get(SorteperMgr.getInstance().activePlayerIndex);
            player1.getHaand().add(kort);
            modstander.getHaand().remove(kort);

            SorteperMgr.getInstance().setNextPlayer(modstander);
        }
    }

    public void tilfoejKort(Kort kort) {
        super.add(kort);
    }
}
