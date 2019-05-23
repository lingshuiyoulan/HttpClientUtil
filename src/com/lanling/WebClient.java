package com.lanling;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class
 *
 * @author Lanling
 * on 2019/3/18
 */
public class WebClient {

    //创建一个HttpContext对象，用来保存Cookie
    private HttpClientContext ctx = HttpClientContext.create();
    private HttpClient httpClient;

    public static WebClient getDefaultWebClient() {
        //构造自定义Header信息
        Set<Header> headers = new HashSet<>();
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
        headers.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
        headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9"));

        WebClient webClient = new WebClient();
        webClient.httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        return webClient;
    }


    public HttpUriRequest newRequest(String url, NameValuePair... params) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        builder.setCharset(StandardCharsets.UTF_8);
        if (params != null) builder.setParameters(params);
        URI uri = builder.build();
        HttpUriRequest httpMethod = new HttpGet(uri);
        httpMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
        return httpMethod;
    }

    public String execute(HttpUriRequest httpMethod) throws IOException {
        HttpResponse response = this.httpClient.execute(httpMethod, ctx);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }


    public String doGet(String url, NameValuePair... params) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        builder.setCharset(StandardCharsets.UTF_8);
        if (params != null) builder.setParameters(params);
        URI uri = builder.build();
        HttpUriRequest httpMethod = new HttpGet(uri);
        httpMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response = this.httpClient.execute(httpMethod, ctx);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    public String doPost(String url, NameValuePair... params) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (params != null) request.setEntity(new UrlEncodedFormEntity(Arrays.asList(params), StandardCharsets.UTF_8));
        HttpResponse response = this.httpClient.execute(request, ctx);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }


    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        long s = System.currentTimeMillis();
        template();
        long e = System.currentTimeMillis();
        System.out.println("共耗时：" + (e - s) / 1000 + " s");
    }

    private static void template() throws IOException, URISyntaxException, InterruptedException {
        String username = "15157100081";
        String pwd = "qq123456789";

        WebClient webClient = WebClient.getDefaultWebClient();
        String response = webClient.doPost("http://www.tbquan88.com/1/user/login_handler",
                new BasicNameValuePair("user_name", username), new BasicNameValuePair("password", pwd));
        System.out.println(response);
        for (int i = 0; i < 10; i++) {
            q(webClient);
            Thread.sleep(2000);
        }
    }

    private static void q(WebClient webClient) throws IOException, URISyntaxException {
        String grabOrderUrl = "http://www.tbquan88.com/1/task/claim_task?task_type=DIANFU";
        HttpUriRequest request = webClient.newRequest(grabOrderUrl);
        request.addHeader("Referer", "http://www.tbquan88.com/1/main");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        String response = webClient.execute(request);
        System.out.println(response);

    }
}
