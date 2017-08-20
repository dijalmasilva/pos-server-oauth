package ifpb.ads.security.twitter;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 15/08/2017, 19:43:46
 */
public class TwitterApp {

    public static void main(String[] args) throws Exception {
//        String header = headerToRequestToken();
//        System.out.println("---");
//        System.out.println(header);
//        System.out.println("---");
        //oauth_token=xaektAAAAAAAFiEmAAABXegxvGk&oauth_token_secret=&oauth_callback_confirmed=true
        Scanner scanner = new Scanner(System.in);
//        String oauthToken = scanner.nextLine();
//        System.out.println("000");
//        redirectUser(oauthToken);
//        System.out.println("000");
        //http://localhost:8080/oauth/twitter?oauth_token=xaektAAAAAAAFiEmAAABXegxvGk&oauth_verifier=
//        String tokenTemp = scanner.nextLine();
//        String verifier = scanner.nextLine(); //
//        String token = headerToAcessToken(tokenTemp, verifier);
//        System.out.println("---");
//        System.out.println(token);
//        System.out.println("---");
        //oauth_token=90332417-&oauth_token_secret=&user_id=&screen_name=&x_auth_expires=0
//        String timelineHeader = hearderTimeline("67103730-2VSCvhXEoFolvUhFoSGrHTGHIFEhdaonudKQWV8A9",
//                "fkp8p5p9eaIRyEMMvvA8tdkRlKpvzHFZkXffmUASARWdL");
//        System.out.println("---");
//        System.out.println(timelineHeader);
//        System.out.println("---");
        String timelineHeader = hearderUpdate("67103730-2VSCvhXEoFolvUhFoSGrHTGHIFEhdaonudKQWV8A9",
                "fkp8p5p9eaIRyEMMvvA8tdkRlKpvzHFZkXffmUASARWdL");
        System.out.println("---");
        System.out.println(timelineHeader);
        System.out.println("---");
        

    }

    private static void redirectUser(String oauth_token) throws IOException {
        Desktop d = Desktop.getDesktop();
        d.browse(URI.create("https://api.twitter.com/oauth/authenticate?oauth_token=" + oauth_token));
    }

    private static String headerToRequestToken() {
        return new TwitterApp().headerAuthorization(
                "POST",
                "https://api.twitter.com/oauth/request_token",
                "", "");
        //oauth_token=5nJbYwAAAAAAFiEmAAABXegnVkQ&oauth_token_secret=&oauth_callback_confirmed=true
    }

    private static String headerToAcessToken(String token, String verifier) {
        return new TwitterApp().headerAuthorization(
                "POST",
                "https://api.twitter.com/oauth/access_token",
                token, verifier);
    }

    private static String hearderTimeline(String token, String verifier) {
        return new TwitterApp().headerAuthorization(
                "GET",
                "https://api.twitter.com/1.1/statuses/user_timeline.json",
                token, verifier);
    }

    private static String hearderUpdate(String token, String verifier) {
        return new TwitterApp().headerAuthorization(
                "POST",
                "https://api.twitter.com/1.1/statuses/update.json",
                token, verifier);
    }

    public String headerAuthorization(String method, String twitterEndpoint, String token, String verifier) {
        String oauthSignatureMethod = "HMAC-SHA1";
        String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
        String oauthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;
        String consumerKey = "2URxXsnyMBfn71XTtRs8A";
        String consumerSecret = "CM8WbuGDFPIQGhkLhFEVQMyCK6sZFq10uXM4IzHQQ";
        String oauthToken = ("".equals(token.trim())) ? "" : "&oauth_token=" + token;

        //ordem alfabetica
        String parameters = "oauth_consumer_key=" + consumerKey
                + "&oauth_nonce=" + oauthNonce
                + "&oauth_signature_method=" + oauthSignatureMethod
                + "&oauth_timestamp=" + oauthTimestamp
                + oauthToken
                + "&oauth_version=1.0"; 

        // METHOD & encode(endpoint) & encode(parametro)
        // O metodo URLEncoder.encode não funciona. RFC 3986 "+", "%20", "*","%2A"
        String signatureBase = method + "&" + encode(twitterEndpoint) + "&" + encode(parameters);
        String oauthVerifier = ("".equals(verifier.trim())) ? "" : encode(verifier);
        String composite = encode(consumerSecret) + "&" + oauthVerifier;
        String oauthSignature = "";
        try {
            oauthSignature = computeSignature(signatureBase, composite);
//            oauthSignature = computeSignature(signatureBase, consumerSecret + "&");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
//            throw new AuthenticatorException(e);
            e.printStackTrace();
        }
        //montando o cabecalho final
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
