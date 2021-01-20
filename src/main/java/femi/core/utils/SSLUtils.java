package femi.core.utils;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLUtils {
    private static HostnameVerifier _hostnameVerifier;
    private static TrustManager[] _trustManagers;

    public static void trustAllHostNames() {
        if(_hostnameVerifier == null) {
            _hostnameVerifier = new FakeHostnameVerifier();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(_hostnameVerifier);
    }

    public static void trustAllHttpsCertificates() {
        SSLContext context;
        final SSLSocketFactory sslSocketFactory;
        // Create a trust manager that does not validate certificate chains
        if(_trustManagers == null) {
            _trustManagers = new TrustManager[] {
                    new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
        }
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, _trustManagers, new SecureRandom());
        } catch(GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static class FakeX509TrustManager implements X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return(_AcceptedIssuers);
        }
    }

    public static class FakeHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return (true);
        }
    }
}
