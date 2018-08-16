package com.lanling.httpclient.simple.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * The Class
 *
 * @author Lanling
 *         on 2018/8/16
 */
public class HttpUtil {

    private final static int DEFAULT_CONNECT_TIMEOUT = 5000;
    private final static int DEFAULT_SOCKET_TIMEOUT = 5000;

    /**
     * 私有构造
     */
    private HttpUtil() {
    }

    public static String get(String url) throws URISyntaxException, IOException {
        return get(url, null, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String get(String url, String charset) throws URISyntaxException, IOException {
        return get(url, null, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String get(String url, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return get(url, null, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static String get(String url, String charset, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return get(url, null, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    public static String get(String url, Map<String, String> params) throws URISyntaxException, IOException {
        return get(url, params, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String get(String url, Map<String, String> params, String charset) throws URISyntaxException, IOException {
        return get(url, params, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String get(String url, Map<String, String> params, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return get(url, params, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static String get(String url, Map<String, String> params, String charset, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return get(url, params, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    private static String get(String url, Map<String, String> params, Charset charset, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(url);
        builder.setCharset(charset);
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addParameter(key, params.get(key));
            }
        }
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout).setConnectTimeout(socketTimeout).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);
        return response(response, charset);
    }


    public static InputStream getInputStream(String url) throws URISyntaxException, IOException {
        return getInputStream(url, null, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static InputStream getInputStream(String url, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return getInputStream(url, null, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static InputStream getInputStream(String url, String charset, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return getInputStream(url, null, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    public static InputStream getInputStream(String url, String charset) throws URISyntaxException, IOException {
        return getInputStream(url, null, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static InputStream getInputStream(String url, Map<String, String> params) throws URISyntaxException, IOException {
        return getInputStream(url, params, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static InputStream getInputStream(String url, Map<String, String> params, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return getInputStream(url, params, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static InputStream getInputStream(String url, Map<String, String> params, String charset, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        return getInputStream(url, params, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    public static InputStream getInputStream(String url, Map<String, String> params, String charset) throws URISyntaxException, IOException {
        return getInputStream(url, params, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    private static InputStream getInputStream(String url, Map<String, String> params, Charset charset, int connectTimeout, int socketTimeout) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(url);
        builder.setCharset(charset);
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addParameter(key, params.get(key));
            }
        }
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout).setConnectTimeout(socketTimeout).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);
        InputStream in = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            in = response.getEntity().getContent();
        } else {
            System.out.println("HTTP Status:" + response.getStatusLine().getStatusCode());
        }
        response.close();
        return in;
    }


    public static String post(String url) throws IOException {
        return post(url, null, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String post(String url, String charset) throws IOException {
        return post(url, null, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String post(String url, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, null, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static String post(String url, String charset, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, null, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    public static String post(String url, List<NameValuePair> params) throws IOException {
        return post(url, params, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String post(String url, List<NameValuePair> params, String charset) throws IOException {
        return post(url, params, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String post(String url, List<NameValuePair> params, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, params, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static String post(String url, List<NameValuePair> params, String charset, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, params, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    public static String postText(String url, String params) throws IOException {
        return post(url, params, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String postText(String url, String params, String charset) throws IOException {
        return post(url, params, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String postText(String url, String params, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, params, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static String postText(String url, String params, String charset, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, params, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    public static String postJson(String url, Object params) throws IOException {
        return post(url, params, StandardCharsets.UTF_8, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String postJson(String url, Object params, String charset) throws IOException {
        return post(url, params, Charset.forName(charset), DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String postJson(String url, Object params, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, params, StandardCharsets.UTF_8, connectTimeout, socketTimeout);
    }

    public static String postJson(String url, Object params, String charset, int connectTimeout, int socketTimeout) throws IOException {
        return post(url, params, Charset.forName(charset), connectTimeout, socketTimeout);
    }

    private static String post(String url, Object params, Charset charset, int connectTimeout, int socketTimeout) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout).setConnectTimeout(socketTimeout).build();
        httpPost.setConfig(requestConfig);
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

        CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
        return response(response, charset);
    }

    static String response(CloseableHttpResponse response, Charset charset) throws IOException {
        String respContent = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = response.getEntity();
            respContent = EntityUtils.toString(he, charset);
            EntityUtils.consume(he);
        } else {
            System.out.println("HTTP Status:" + response.getStatusLine().getStatusCode());
        }
        response.close();
        return respContent;
    }
}