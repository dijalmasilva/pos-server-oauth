package ifpb.ads.security.twitter;

import ifpb.ads.http.HttpRequest;
import ifpb.ads.security.OAuth;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 11/08/2017, 21:42:16
 */
public class Twitter extends OAuth {

    private final String client_authorize = "https://api.twitter.com/oauth/authenticate";
    private final String client_request = "https://api.twitter.com/oauth/request_token";
    private final String client_token = "https://api.twitter.com/oauth/access_token";

    public Twitter(String client_id, String client_secret,
            String redirect_uri) {
        super(client_id, client_secret, redirect_uri);
    }

    @Override
    public String locationAuthorized() {
        try {
            String header = postToRequestToken();
            HttpRequest req = new HttpRequest(client_request);
            return this.client_authorize + "?" + req.readWithAuthorization(header, "");
        } catch (IOException ex) {
            Logger.getLogger(Twitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.client_authorize;
    }

    @Override
    protected String clientAuthorized() {
        return "";
    }

    @Override
    protected String clientToken() {
        return this.client_token;
    }

    private String postToRequestToken() {
        return headerAuthorization(
                "POST",
                "https://api.twitter.com/oauth/request_token",
                "", "");
        //oauth_token=5nJbYwAAAAAAFiEmAAABXegnVkQ&oauth_token_secret=VmHp48zuuucS9uq0pyO6BhQg8ydbFuIt&oauth_callback_confirmed=true
    }

    public String headerToAcessToken(String token, String verifier) {
        return headerAuthorization(
                "POST",
                "https://api.twitter.com/oauth/access_token",
                token, verifier);
    }

//    private String hearderTimeline(String token, String verifier) {
//        return new Twiiter().headerAuthorization(
//                "GET",
//                "https://api.twitter.com/1.1/statuses/user_timeline.json",
//                token, verifier);
//    }
    public String headerAuthorization(String method, String twitterEndpoint, String token, String verifier) {
        String oauthSignatureMethod = "HMAC-SHA1";
        String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
        String oauthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;
        String consumerKey = "2URxXsnyMBfn71XTtRs8A";
        String consumerSecret = "CM8WbuGDFPIQGhkLhFEVQMyCK6sZFq10uXM4IzHQQ";
        String oauthToken = ("".equals(token.trim())) ? "" : "&oauth_token=" + token;

        String parameters = "oauth_consumer_key=" + consumerKey
                + "&oauth_nonce=" + oauthNonce
                + "&oauth_signature_method=" + oauthSignatureMethod
                + "&oauth_timestamp=" + oauthTimestamp
                + oauthToken
                + "&oauth_version=1.0";

        String signatureBase = method + "&" + encode(twitterEndpoint) + "&" + encode(parameters);
        String oauthVerifier = ("".equals(verifier.trim())) ? "" : encode(verifier);
        String composite = encode(consumerSecret) + "&" + oauthVerifier;
        String oauthSignature = "";
        try {
            oauthSignature = computeSignature(signatureBase, composite);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        oauthToken = ("".equals(token.trim())) ? "" : "\", oauth_token=\"" + token;
        String authorizationHeader = "OAuth "
                + "oauth_consumer_key=\"" + consumerKey
                + "\", oauth_nonce=\"" + oauthNonce
                + "\", oauth_signature=\"" + encode(oauthSignature)
                + "\", oauth_signature_method=\"HMAC-SHA1"
                + "\", oauth_timestamp=\"" + oauthTimestamp
                + oauthToken
                + "\", oauth_version=\"1.0\"";
        return authorizationHeader;
    }

    protected String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = keyString.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = baseString.getBytes();
        return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
    }

    public String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
//            throw new AuthenticatorException(ignore);
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%'
                    && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
}
