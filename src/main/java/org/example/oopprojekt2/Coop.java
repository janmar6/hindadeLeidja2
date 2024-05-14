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

public class Coop extends Kaabitseja {

    // Meetod, mis kogub tooteid Coopi veebipoest.
    public ArrayList<Toode> kaabitse(String tooteNimi) throws IOException {
        ArrayList<Toode> tooted = new ArrayList<>();

        String encodedSearchTerm = URLEncoder.encode(tooteNimi, "UTF-8");
        // URL, kust otsitakse tooteid
        String url = "https://coophaapsalu.ee/?s=" + encodedSearchTerm  + "&post_type=product&dgwt_wcas=1";
        URL urlObject = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");

        // HTTP vastuse staatuskood
        int resStatus = connection.getResponseCode();
        InputStream inputStream = urlObject.openStream();

        if (resStatus == 200) {
            // Loeb HTML lehe sisu
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\Z");
            String htmlContent = scanner.next();
            scanner.close();

            Document doc = Jsoup.parse(htmlContent);
            //klassi järgi html elementide otsimine
            ArrayList<Element> products = doc.getElementsByClass("type-product");

            // Läbib kõik tooted
            for (Element product : products) {
                String nimi = product.getElementsByClass("woocommerce-loop-product__title").text().strip();
                double hind = Double.parseDouble(product.getElementsByClass("woocommerce-Price-amount amount").text().split(" ")[0].replace(",", "."));
                double kiloHind = Double.parseDouble(product.getElementsByClass("perweight").text().split("€")[0].replace(",", "."));

                // Lisab toote, kui tootenimi sisaldab otsingusõna
                if (nimi.toLowerCase(Locale.ROOT).contains(tooteNimi.toLowerCase())) {
                    tooted.add(new Toode(nimi, hind, kiloHind, "COOP"));
                }
            }
        }
        return tooted;
    }

}
