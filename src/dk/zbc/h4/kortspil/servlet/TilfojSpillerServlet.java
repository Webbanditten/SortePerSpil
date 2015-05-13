package dk.zbc.h4.kortspil.servlet;
import dk.zbc.h4.kortspil.SorteperMgr;
import dk.zbc.h4.kortspil.xml.XmlMgr;
import dk.zbc.h4.kortspil.Spiller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Niklas on 06-05-2015.
 */
public class TilfojSpillerServlet extends HttpServlet {
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


        String navn = req.getParameter("navn");
        if(!SorteperMgr.getInstance().spilStartet()) {
            if (navn != null) {
                // TODO Tilføj bruger til spillerArray
                Spiller spiller = new Spiller(navn, req.getSession().getId());
                SorteperMgr.getInstance().tilfojSpiller(spiller);
                resp.getWriter().print(XmlMgr.getInstance().transformResponse("success", "Spiller tilføjet"));
            } else {
                resp.getWriter().print(XmlMgr.getInstance().transformResponse("error", "Missing name parameter"));
            }
        }else{
            resp.getWriter().print(XmlMgr.getInstance().transformResponse("error", "Cant add player, game started."));
        }
        


    }
}
