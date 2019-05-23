package com.lanling.identifyImg;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * The Class
 *
 * @author Lanling
 * on 2019/3/22
 */
public class CapUtilTest {

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 100; i++) {
            get();
            Thread.sleep(1000);
        }
    }

    public static void get() {
        try {
            String img = "https://www.mxpaas.com/verifyCode.do";

            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet method = new HttpGet(img);
            CloseableHttpResponse response = client.execute(method);
            InputStream is = response.getEntity().getContent();
            String filename = "D:/Cache/" + System.currentTimeMillis() + ".png";
            File file = new File(filename);
            file.createNewFile();
            FileOutputStream output = new FileOutputStream(filename);
            IOUtils.copy(is, output);
            output.flush();
            output.close();
            is.close();

            String url = "http://63.211.111.82:19952/captcha/v3";
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            byte[] params = FileUtils.readFileToByteArray(new File(filename));
            post.setEntity(new ByteArrayEntity(params));
            response = client.execute(post);
            String resp = EntityUtils.toString(response.getEntity());
            System.out.println(resp);

        } catch (Exception e) {

        }
    }

}
