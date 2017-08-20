package ifpb.ads.security.dropbox;

import ifpb.ads.security.OAuth;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 11/08/2017, 21:42:16
 */
public class Dropbox extends OAuth {

    private final String client_authorize = "https://www.dropbox.com/oauth2/authorize";
    private final String client_token = "https://api.dropboxapi.com/oauth2/token";
    private final String response_type;

    public Dropbox(String clientId, String clientSecret, 
            String redirectUri, String responseType) {
        super(clientId, clientSecret, redirectUri);
        this.response_type = responseType;
    }

    @Override
    public String locationAuthorized() {
        String collect = String.format("&response_type=%s", response_type);
        return super.locationAuthorized().concat(collect);
    }

    @Override
    public String toParams(String code) {
        String collect = String.format("&grant_type=%s", "authorization_code");
        return super.toParams(code).concat(collect);
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
