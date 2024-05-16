package org.example.oopprojekt2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


//Selle klassiga otsitakse kõik soovitud tooted Prisma e-poest

public class Prisma extends Kaabitseja{
    @Override
    public ArrayList<Toode> kaabitse(String tooteNimi) throws IOException {

        ArrayList<Toode> tooted = new ArrayList<>();

        try{
            //Prismast saab otsida vaid tingimusel, kui kasutajal on arvutis chrome ja selle kõige viimane versioon
            //programm avab tagaplaanil chromeclienti ja saadab päringu vajalikule URL'ile. Nüüd kuna browser suudab
            //renderida javascripti, mis päringu response annab, siis saab info sobivale kujule ja on võīmalik leida
            //sobivad tooted.
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

            ChromeOptions options = new ChromeOptions();

            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--headless");

            ChromeDriver driver = new ChromeDriver(options);

            driver.get("https://www.prismamarket.ee/products/search/"+tooteNimi+"?sort_order=price&sort_dir=asc");


            List<WebElement> elements = driver.findElements(By.className("js-shelf-item"));


            //vajalikke html elementide seest info saamine
            for(WebElement element : elements) {
                String nimi = element.findElement(By.className("name")).getText();
                String hindStringina = element.findElement(By.className("js-info-price")).getText().split("\n")[0];
                String kilohindStringina = element.findElement(By.className("unit-price")).getText();
//
//            System.out.println(nimi);
//            System.out.println(hindStringina);
//            System.out.println(kilohindStringina);
//            System.out.println();


                double hind = Double.parseDouble(hindStringina.split(" ")[0] + "." + hindStringina.split(" ")[1]);

                double kilohind = hind;
                //juhul kui kilohinda pole
                if(kilohindStringina.length() != 0){
                    kilohind = Double.parseDouble(kilohindStringina.split(" ")[0].replace(",","."));
                }

                tooted.add(new Toode(nimi, hind, kilohind, "Prisma"));

            }

            return tooted;


        }  catch (SessionNotCreatedException | IllegalStateException ignored) {
            System.out.println("Kasutajal pole viimast chrome versiooni");
            return tooted;
        }

    }

    public static void main(String[] args) throws IOException {
        Prisma prisma = new Prisma();
        System.out.println(prisma.kaabitse("kana"));
    }
    //https://www.prismamarket.ee/products/search/leib?sort_order=price&sort_dir=asc


}
