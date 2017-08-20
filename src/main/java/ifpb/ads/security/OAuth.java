package ifpb.ads.security;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 14/08/2017, 11:12:39
 */
//https://tools.ietf.org/html/rfc6749#section-1.3
public abstract class OAuth {

    protected final String client_id;
    protected final String client_secret;
    protected final String redirect_uri;

    public OAuth(String client_id, String client_secret, String redirect_uri) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
    }
    public String locationAuthorized() {
        String collect = String.format("client_id=%s&redirect_uri=%s", client_id, redirect_uri);
        return clientAuthorized().concat("?").concat(collect);
    }

    public String toParams(String code) {
//        String code = request.getParameter("code");
        return String.format("client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
                client_id, client_secret, redirect_uri, code);
    }

    public String locationToken() {
        return clientToken();
    }

    protected abstract String clientAuthorized();

    protected abstract String clientToken();
}
