package dk.zbc.h4.kortspil.xml;

import dk.zbc.h4.kortspil.Deck;
import dk.zbc.h4.kortspil.Kort;
import dk.zbc.h4.kortspil.Spiller;

import java.util.ArrayList;

/**
 * Created by Niklas on 06-05-2015.
 */
public class XmlMgr {
    private static XmlMgr instance = null;

    private XmlMgr() {

    }

    public static XmlMgr getInstance() {
        if(instance == null) {
            instance = new XmlMgr();
        }
        return instance;
    }

    /*
   //
   // KONVERTERE EN LISTE AF KORT TIL XML
   //
    */
    public String transformKort(Deck dk) {
        StringBuffer sb = new StringBuffer();


        sb.append(getHeader());
        sb.append("<kortListe>");
        for(Kort k : dk) {
            sb.append("<kort>\n" +
                    "        <kuloer>" + k.getKuloer() + "</kuloer>\n" +
                    "        <vaerdi>" + k.getVaerdi() + "</vaerdi>\n" +
                    "                </kort>");
        }
        sb.append("</kortListe>");
        return sb.toString();
    }


    /*
    //
    // KONVERETERE EN LISTE AF SPILLERE TIL XML
    //
     */
    public String transformSpillere(ArrayList<Spiller> spillere) {
        StringBuffer sb = new StringBuffer();

        sb.append(getHeader());
        sb.append("<spillere>");
        for(Spiller spiller : spillere) {
            sb.append("<spiller>\n" +
                        "<navn>" + spiller.getNavn() + "</navn>\n" +
                        "<klar>" + spiller.getReady() + "</klar>\n" +
                        "<antalKort>" + (spiller.getHaand().size()) + "</antalKort>\n" +
                    "</spiller>");
        }
        sb.append("</spillere>");
        return sb.toString();
    }



    public String transformStatus(String status) {
        StringBuffer sb = new StringBuffer();

        sb.append(getHeader());

        sb.append("<status>"+status+"</status>");

        return sb.toString();
    }

    public String transformModstanderKort(Spiller modstander) {
        StringBuffer sb = new StringBuffer();

        sb.append(getHeader());

        int modstanderAntalKort = 0;
        if(modstander.getHaand().size() != 0) {
            modstanderAntalKort = modstander.getHaand().size();
        }

        sb.append("<antalKort>"+modstanderAntalKort+"</antalKort>");

        return sb.toString();
    }

    public String transformResponse(String type, String status) {
        StringBuffer sb = new StringBuffer();

        sb.append(getHeader());


        sb.append("<response type='"+type+"'>"+status+"</response>");

        return sb.toString();
    }

    private String getHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }
}
