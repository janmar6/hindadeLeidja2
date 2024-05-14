import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rimi extends Kaabitseja {

    // Meetod, mis kogub tooteid Rimi veebipoest.
    public ArrayList<Toode> kaabitse(String tooteNimi) throws IOException {

        String encodedSearchTerm = URLEncoder.encode(tooteNimi, "UTF-8");
        // URL, kust otsitakse tooteid
        String url = "https://www.rimi.ee/epood/ee/otsing?currentPage=1&pageSize=60&query=" + encodedSearchTerm ;

        URL urlObject = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");

        // HTTP vastuse staatuskood
        int resStatus = connection.getResponseCode();

        // Avab ühenduse URL-iga
        InputStream inputStream = urlObject.openStream();
        ArrayList<Toode> tooted = new ArrayList<>();

        if(resStatus == 200){
            // Loeb HTML lehe sisu
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            scanner.useDelimiter("\\Z");
            String htmlContent = scanner.next();

            // Sulgeb skanneri ja sisendi voogu
            scanner.close();
            Document doc = Jsoup.parse(htmlContent);
            inputStream.close();

            // Leiab HTML elemendid, mis sisaldavad toodete nimesid, hindu ja hinnaühiku hinda
            ArrayList<Element> nimiElements = doc.getElementsByClass("card__name");
            ArrayList<Element> hindElements = doc.getElementsByClass("card__price");
            ArrayList<Element> hindYhikuKohtaElements = doc.getElementsByClass("card__price-per");

            // Määrab väikseima listi suuruse, et vältida väljaspool määra andmetele ligipääsu
            int vaikseimaArraylistSuurus = Math.min(nimiElements.size(), hindElements.size());
            vaikseimaArraylistSuurus = Math.min(vaikseimaArraylistSuurus, hindYhikuKohtaElements.size());

            // Läbib kõik tooted
            for (int i = 0; i < vaikseimaArraylistSuurus; i++) {
                String nimi = nimiElements.get(i).text();
                String hindString = hindElements.get(i).text();
                String yhikuHindString = hindYhikuKohtaElements.get(i).text();

                // Formaadib hinna sobivaks kujundiks
                String formattedPrice = hindString.replaceFirst(" ", ".").replaceAll(" .*", "");
                double hind = Double.parseDouble(formattedPrice);

                // Formaadib hinnaühiku hinnad ja muudab koma punktiks
                String formattedYhikuPrice = yhikuHindString.replace(',', '.').replaceAll("[^\\d.]", "");
                double yhikuHind = Double.parseDouble(formattedYhikuPrice);

                // Lisab toote, kui tootenimi sisaldab otsingusõna, et vältida tooteid, mida kasutaja ei otsinud.
                if (nimi.toLowerCase(Locale.ROOT).contains(tooteNimi.toLowerCase())) {
                    tooted.add(new Toode(nimi, hind, yhikuHind, "RIMI"));
                }
            }
        } else {
            System.out.println("Viga päringu tegemisel");
        }

        return tooted;
    }
}
