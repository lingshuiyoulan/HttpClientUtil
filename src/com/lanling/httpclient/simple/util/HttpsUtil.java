package com.lanling.httpclient.simple.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;


public class HttpsUtil {

    public static String doGet(String url) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        return doGet(url, null, StandardCharsets.UTF_8);
    }
    public static String doGet(String url, String charset) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        return doGet(url, null, Charset.forName(charset));
    }
    public static String doGet(String url, Map<String, String> params) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        return doGet(url, params, StandardCharsets.UTF_8);
    }
    public static String doGet(String url, Map<String, String> params, Charset charset) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        SSLContext sslcontext = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        // 创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        URIBuilder builder = new URIBuilder(url);
        builder.setCharset(charset);
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addParameter(key, params.get(key));
            }
        }
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(httpGet);
        return HttpUtil.response(response, charset);
    }


    public static String doPost(String url) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, null, StandardCharsets.UTF_8);
    }
    public static String doPost(String url, String charset) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, null,  Charset.forName(charset));
    }

    public static String doPost(String url, List<NameValuePair> params) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, params, StandardCharsets.UTF_8);
    }
    public static String doPost(String url, List<NameValuePair> params, String charset) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, params, Charset.forName(charset));
    }

    public static String doPostText(String url, String params) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, params, StandardCharsets.UTF_8);
    }
    public static String doPostText(String url, String params,String charset) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, params, Charset.forName(charset));
    }

    public static String doPostJson(String url, Object params) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, params, StandardCharsets.UTF_8);
    }
    public static String doPostJson(String url, Object params,String charset) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return doPost(url, params, Charset.forName(charset));
    }

    private static String doPost(String url, Object params, Charset charset) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        SSLContext sslcontext = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            if (params instanceof String) {
                httpPost.setHeader("Content-Type", "text/plain");
                httpPost.setEntity(new StringEntity((String) params, charset));
            } else if (params instanceof List) {
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity((List) params, charset));
            } else {
                httpPost.addHeader("Content-Type", "application/json");
                httpPost.setEntity(new StringEntity(params.toString(), charset));
            }
        }
        CloseableHttpResponse response = client.execute(httpPost);
        return HttpUtil.response(response, charset);
    }

    // 绕过验证
    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509ExtendedTrustManager trustManager = new X509ExtendedTrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
                    throws java.security.cert.CertificateException {

            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}