package ifpb.ads.security.github;

import ifpb.ads.security.OAuth;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 11/08/2017, 21:42:16
 */
public class Github extends OAuth {

    private final String client_authorize = "http://github.com/login/oauth/authorize";
    private final String client_token = "https://github.com/login/oauth/access_token";

    public Github(String client_id, String client_secret, String redirect_uri) {
        super(client_id, client_secret, redirect_uri);
    }

    @Override
    protected String clientAuthorized() {
        return this.client_authorize;
    }

    @Override
    protected String clientToken() {
        return this.client_token;
    }
}
