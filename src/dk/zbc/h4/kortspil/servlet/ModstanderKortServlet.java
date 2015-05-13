package dk.zbc.h4.kortspil.servlet;
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
public class ModstanderKortServlet extends HttpServlet {
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

        String content = null;
        if(SorteperMgr.getInstance().spilStartet()) {
            if(SorteperMgr.getInstance().isActivePlayer(req.getSession().getId())) {
                if(!SorteperMgr.getInstance().getGameEnded()) {
                    Spiller modstander = SorteperMgr.getInstance().findModstander();
                    content = XmlMgr.getInstance().transformModstanderKort(modstander);
                }else{
                    content = XmlMgr.getInstance().transformResponse("error", "Spillet er slut");
                }
            }else{
                content = XmlMgr.getInstance().transformResponse("error", "Det er ikke din tur");
            }
        }else{
            content = XmlMgr.getInstance().transformResponse("error", "Spillet er ikke startet");
        }

        resp.getWriter().print(content);
    }
}
