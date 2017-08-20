package ifpb.ads.security.twitter;

import ifpb.ads.http.HttpRequest;
import ifpb.ads.security.OAuth;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(name = "FlowTwitter", urlPatterns = {"/twitter"})
public class FlowTwitter extends HttpServlet {

    @Inject
    private OAuth auth;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String client_id = request.getParameter("oauth_token");
        String client_secret = request.getParameter("oauth_verifier");

//        System.out.println(client_id);
//        System.out.println(client_secret);
        Twitter twitter = (Twitter) auth;
        String url = twitter.locationToken();
        String header = twitter.headerAuthorization("POST", url, client_id, client_secret);
        HttpRequest req = new HttpRequest(url);
        String retorno = req.readWithAuthorization(header, "oauth_verifier=" + client_secret);
        response.setContentType("application/json");
        response.getWriter().println(retorno);
    }
}
