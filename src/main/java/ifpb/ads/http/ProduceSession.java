package ifpb.ads.http;

import ifpb.ads.security.OAuth;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 14/08/2017, 11:34:48
 */
@ApplicationScoped
@Named
public class ProduceSession {

    @Inject
    private HttpServletRequest request;

    @Dependent
    @Produces
    public OAuth create() {
        return (OAuth) request.getSession().getAttribute("token");
    }
}
