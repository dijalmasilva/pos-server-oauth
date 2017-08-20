package ifpb.ads.flow;

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
@WebServlet(name = "FlowToken", urlPatterns = {"/token"})
public class FlowToken extends HttpServlet {

    @Inject
    private OAuth auth;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String url = auth.locationToken();
        HttpRequest req = new HttpRequest(url);
        String retorno = req.read(auth.toParams(code));
        response.setContentType("application/json");
        response.getWriter().println(retorno);

    }
}
