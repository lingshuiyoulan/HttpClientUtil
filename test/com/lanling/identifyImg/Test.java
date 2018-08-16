package com.lanling.identifyImg;

import com.lanling.httpclient.simple.util.HttpUtil;

import java.io.IOException;

/**
 * The Class
 *
 * @author Lanling
 *         on 2018/6/14
 */
public class Test {
    public static void main(String[] args) {
        String url = "http://39.104.25.59:50001/api/verify";
//        String url = "http://192.168.199.114:21002/api/verify";
        String param = "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\n" +
                "HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\n" +
                "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAoAG4DASIA\n" +
                "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\n" +
                "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\n" +
                "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\n" +
                "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\n" +
                "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\n" +
                "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\n" +
                "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\n" +
                "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3+iqM\n" +
                "mrW0cjRtHeEqSCVspmHHoQuD9RVHU/FNlpli9w8V2WHEaPbSRh27DcygD1+gPWldIDcorh4vEXi+\n" +
                "8t3vLTRIPsvLJvVtxXGRgFgW4I5A57V0kGq3ktvFI2h3yM6Biu+HgkdOXB/MA+wpKSYGpRWf/aN1\n" +
                "/wBAa+/77g/+OVz/AIn8Wajo32XydO8nzd+fte1s4x02OfXv7UOSSuB2FFZ/269f5Y9InRz0aeWN\n" +
                "UH1Ksx/JT/Wuf1XxPrGn+IbLTfsdiPtHl8ea753OV+9gY6f3Tj36UOSQHYUVz+t6trGlaPPe/YrE\n" +
                "eXt5+0u/VgPu7Fz19RVzw7qU2r6DbX1wsayy7twjBC8MRxkn0p8yvYDUooopgFFFFABWH4n8Ox+I\n" +
                "bFUD+XdQ5MLk/Lk4yCPQ4HPUfodyud1ebxVb6g7aZa2d1aOAERzhkwOScsvUk9CeFHTvMrW1A5vS\n" +
                "PEmo+GbqHSNdhYW6gbXPzPGp6YIJDKOenI/DFXvFV9dap4gtfDdhPJEG/wCPlkOMhhkg9MgJk4zg\n" +
                "5x1FOfQNc8S3ttPr4trW3tjxbxcl+QTyGOMgYzu4x071HrWi6wni461ooiuXBG9Nyjy2CAFWBI6q\n" +
                "QeOee3BOfvWt0Aw/EWhJpuq2WiWF5cslyVfy52+RXZigbj6emQO5zga2tf8AEy+JunW8PyvbeXvL\n" +
                "8A7SZTjH+ycfX86o2kc+rfESD7TdreeUVlMtscxptXeFXr8obC+/1NaVlY6ncePb7VTY71idkhfz\n" +
                "AkRIGwZb5iflBztBIbAOOcJLt3GdZPqU1v5rNpd2Yo8ky+ZCFwP4uZBgd+cV56LzUNS8eXGoaNZM\n" +
                "1xEM+Rc4QrhBG24bh3PTNeiR6YjSLNeSNdzKQy+YMJGeoKJ0GDnBOWwcbjXO+EtE1G11nUtU1S38\n" +
                "iefO1VdSp3MWboSeCFxz371ck20hHO+LtR8QS2Vva6zY21sjyGSMxHJJUYP8R/vV6NpMElto1jbz\n" +
                "Ltlit40dc5wQoBHFc34t0TUdc1nTYkt92nxY82VXVWXcw39T2VQRx+ddhRFNSYBRRRWgBRRRQAUU\n" +
                "UUAFcb4i8O6HPqQmmOoPe3BAENs28uSTgncCF4B6kDCHHQ0UVM9gLmh+EYNOtVF1taUjEixn5X6Z\n" +
                "3NgMwOPun5cYBUkbj0kcaRRrHGioiAKqqMAAdABRRTSS2AdRRRTAKKKKACiiigAooooA/9k=";

        long st = System.currentTimeMillis();
        try {
            System.out.println(HttpUtil.postText(url, param));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long ed = System.currentTimeMillis();
        System.out.println((ed -st )/1000);
    }
}
