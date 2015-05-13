package dk.zbc.h4.kortspil.servlet;
import dk.zbc.h4.kortspil.KortspilMgr;
import dk.zbc.h4.kortspil.SorteperMgr;
import dk.zbc.h4.kortspil.Spiller;
import dk.zbc.h4.kortspil.xml.XmlMgr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Niklas on 06-05-2015.
 */
public class TagKortServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        SorteperMgr.getInstance().forberedSpil();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp);
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        int chosenCard = Integer.parseInt(req.getParameter("card"));

        KortspilMgr sortePer = SorteperMgr.getInstance();
        if(sortePer.spillereMedKort().size() > 1) {
            if (sortePer.isActivePlayer(req.getSession().getId())) {
                sortePer.findSpillerByID(req.getSession().getId()).getHaand().tagKort(chosenCard);
                resp.getWriter().print(XmlMgr.getInstance().transformResponse("success", "Kort er taget fra" + sortePer.findModstander().getNavn()));
            } else {
                resp.getWriter().print(XmlMgr.getInstance().transformResponse("error", "Det er ikke din tur" + sortePer.findModstander().getNavn()));
            }
        }else{
            sortePer.setGameEnded(true);
            resp.getWriter().print(XmlMgr.getInstance().transformResponse("error", "Spillet er slut"));
        }

    }
}
