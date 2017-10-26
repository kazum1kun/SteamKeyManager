package Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Contains all HTML related utilities
 */
final class WebParser {
    // Get the Steam Store URL that corresponds to the name of the game
    static String findURLOfGame(String game, String lang) {
        // Convert the name string to URI-safe letters
        try {
            game = URLEncoder.encode(game, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // Seriously, does there exist any modern platform that have not made it to UTF-8 yet??
        }

        String searchURL = "";
        // Put together the base search URL for the game (with appropriate language and region)
        switch (lang) {
            case "en_US":
                searchURL = "http://store.steampowered.com/search/suggest?term=" + game +
                        "&f=games&cc=US&l=english&no_violence=0&no_sex=0";
                break;
            case "zh_CN":
                searchURL = "http://store.steampowered.com/search/suggest?term=" + game +
                        "&f=games&cc=CN&l=chinese&no_violence=0&no_sex=0";
                break;
            case "ja_JP":
                searchURL = "http://store.steampowered.com/search/suggest?term=" + game +
                        "&f=games&cc=JP&l=japanese&no_violence=0&no_sex=0";
                break;
        }

        // Alright, it turns out Jsoup cannot fetch the complete web page for me (even this simple one like this!!)
        // So I first get the web page with Apache HttpClient, then parse it with Jsoup
        // Finally get the very URL we wanted
        HttpGet request = new HttpGet(searchURL);
        HttpClient client = HttpClientBuilder.create().build();
        String entityContents = "";
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            entityContents = EntityUtils.toString(entity);
        } catch (IOException ex) {
            //TODO: Err
        } finally {
            request.releaseConnection();
        }

        Document document = Jsoup.parse(entityContents);
        Element link = document.select("a").first();
        return link.attr("href");
    }
}
