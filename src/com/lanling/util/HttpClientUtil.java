package com.lanling.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.AbstractExecutionAwareRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class HttpClientUtil
 *
 * @author Lanling
 *         on 2017/8/24
 */
public class HttpClientUtil {
    private static HttpClientUtil util = null;
    private static CloseableHttpClient httpClient = null;
    private static HttpClientContext context = null;
    private static CookieStore cookieStore = null;

    public static HttpClientUtil getHttpClientUtil() {
        if (util == null) {
            synchronized (HttpClientUtil.class) {
                util = new HttpClientUtil();
            }
        }
        return util;
    }

    /**
     * 私有构造
     */
    private HttpClientUtil() {
        getHttpClient();
    }

    /**
     * 获取 httpclient
     */
    private void getHttpClient() {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();

        // 设置默认跳转以及存储cookie
        httpClient = HttpClientBuilder.create()
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
    }

    /**
     * 手动增加cookie
     *
     * @param name   name
     * @param value  value
     * @param domain damain
     * @param path   path
     */
    public void addCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookieStore.addCookie(cookie);
    }

    /**
     * get请求
     *
     * @param url   url
     * @param param param
     * @return respContent 字符串类型
     * @throws IOException e
     */
    public String get(String url, Map<String, Object> param) throws IOException {
        String params = map2String(param);
        HttpGet httpGet = new HttpGet(url + params);
        setHeader(httpGet);
        String respContent = null;
        CloseableHttpResponse resp = httpClient.execute(httpGet, context);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
        }
        resp.close();
        return respContent;
    }

    private String map2String(Map<String, Object> param) {
        if (param != null && param.size() > 0) {
            StringBuilder params = new StringBuilder("?");
            for (String key : param.keySet()) {
                params.append(key).append("=").append(param.get(key)).append("&");
            }
            return params.toString();
        } else return "";
    }

    /**
     * get请求
     *
     * @param url  url
     * @return in 输入流
     * @throws IOException e
     */
    public InputStream getAsStream(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        setHeader(httpGet);
        CloseableHttpResponse resp = httpClient.execute(httpGet, context);
        InputStream in;
        if (resp.getStatusLine().getStatusCode() == 200) {
            in = resp.getEntity().getContent();
        } else in = null;
        resp.close();
        return in;
    }

    /**
     * post请求
     *
     * @param url      url
     * @param param    form表单参数
     * @param encoding 编码 默认 utf-8
     * @return respContent 字符串类型
     * @throws IOException e
     */
    public String post(String url, Map<String, String> param, String encoding) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        setHeader(httpPost);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (param != null && param.size() > 0)
            httpPost.setEntity(new UrlEncodedFormEntity(map2ListNameValuePair(param), encoding == null ? "utf-8" : encoding));
        CloseableHttpResponse resp = httpClient.execute(httpPost, context);
        String respContent = null;
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
        }
        resp.close();
        return respContent;
    }

    private List<NameValuePair> map2ListNameValuePair(Map<String, String> param) {
        List<NameValuePair> params = new ArrayList<>();
        NameValuePair pair;
        for (String key : param.keySet()) {
            pair = new BasicNameValuePair(key, param.get(key));
            params.add(pair);
        }
        return params;
    }


    /**
     * post请求
     *
     * @param url      url
     * @param param    text文本类型参数
     * @param encoding 编码 默认 utf-8
     * @return respContent 字符串类型
     * @throws IOException e
     */
    public String postWithText(String url, String param, String encoding) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        setHeader(httpPost);
        httpPost.setHeader("Content-Type", "text/plain");
        if (param != null)
            httpPost.setEntity((new StringEntity(param, encoding == null ? "UTF-8" : encoding)));
        CloseableHttpResponse resp = httpClient.execute(httpPost, context);
        String respContent = null;
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
        }
        resp.close();
        return respContent;
    }

    /**
     * @param url   url
     * @param param JSONObject
     * @return respContent 字符串类型
     * @throws IOException e
     */
    public String postWithJson(String url, Object param) throws IOException {

        HttpPost httpPost = new HttpPost(url);
        setHeader(httpPost);
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity entity = new StringEntity(param.toString(), "UTF-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse resp = httpClient.execute(httpPost, context);
        String respContent = null;
        if (resp.getStatusLine().getStatusCode() == 200) {
            respContent = EntityUtils.toString(resp.getEntity(), "UTF-8");
        }
        resp.close();
        return respContent;
    }

    /**
     * 设置请求头 标志浏览器
     *
     * @param http AbstractExecutionAwareRequest
     */
    private void setHeader(AbstractExecutionAwareRequest http) {
        http.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        http.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        http.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.3357.400 QQBrowser/9.6.11858.400");
        http.setHeader("Accept-Encoding", "gzip, deflate, sdch");
    }

    /**
     * 打印cookie
     */
    public void printCookie() {
        System.out.println("************************************");
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
        }
    }

    public static String getSSL(String url, String encoding) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        String body = "";
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
//       CloseableHttpClient util = HttpClients.createDefault();

        //创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);


        //设置header信息
        //指定报文头【Content-type】、【User-Agent】

        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.3357.400 QQBrowser/9.6.11858.400");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpGet);
//        util.getCookieStore().getCookies();

        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }

    /**
     * 绕过验证
     *
     * @return SSLContext sc
     * @throws NoSuchAlgorithmException e
     * @throws KeyManagementException   e
     */
    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
