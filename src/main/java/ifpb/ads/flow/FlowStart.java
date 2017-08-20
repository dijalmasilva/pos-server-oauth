package ifpb.ads.flow;

import ifpb.ads.security.OAuth;
import ifpb.ads.security.twitter.Twitter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(name = "FlowStart", urlPatterns = {"/authorize"})
public class FlowStart extends HttpServlet {

    private OAuth auth;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String client_id = request.getParameter("client_id");
        String client_secret = request.getParameter("client_secret");
        String redirect_uri = request.getParameter("redirect_uri");
//        auth = new Github(client_id, client_secret, redirect_uri);
//        String response_type = request.getParameter("response_type");
//        auth = new Dropbox(client_id, client_secret, redirect_uri, response_type);
    
        auth = new Twitter(client_id, client_secret, redirect_uri);
        
        request.getSession().setAttribute("token", auth);
        response.sendRedirect(auth.locationAuthorized());

    }
}
