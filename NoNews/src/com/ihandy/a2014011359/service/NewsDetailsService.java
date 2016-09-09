package com.ihandy.a2014011359.service;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.URL;

        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;

        import android.text.TextUtils;
        import android.util.Log;

        import com.ihandy.a2014011359.tool.HttpRequest;

public class NewsDetailsService {
    public static String getNewsDetails(String url, String news_title,
                                        String news_date) {
        String data = "";
        try {
            /*URL cs = new URL(url);
            Log.i("URL", url);
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                data += inputLine;
            }*/
            HttpRequest request = HttpRequest.get(url);
            String body = request.body();
//            int start = body.indexOf("<section class=\"article\">");
//            if (start != -1) {
//                int end = body.indexOf("</section>", start);
//                Log.i("<body>", start + ", " + end);
//                data = body.substring(start, end + 10);
//            }
//            else {
                data = body;
//            }
        } catch (HttpRequest.HttpRequestException e) {
            e.printStackTrace();
        }
        return data;
    }
}
