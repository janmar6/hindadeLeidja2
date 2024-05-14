package org.example.oopprojekt2;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Siin toimub käivitamine ja klasside kooskõlastamine
public class Peaklass {
    private static ExecutorService executor;
    public static ExecutorService printdots() {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                System.out.print("Otsin tooteid ");
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.print(".");
                    Thread.sleep(500); // Punktide printimise vahe
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return executor;
    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        // Kasutajalt toodete küsimine.
        System.out.print("Sisestage toode/tooted, mida soovite otsida (komaga või semikooloniga eraldatud): ");
        String tooteOtsing = scanner.nextLine();

        Kasutaja kasutaja;

        tooteOtsing = tooteOtsing.replace(",", ";");
        String[] tootedSplititud = tooteOtsing.split(";");
        ArrayList<String> toodeteList = new ArrayList<>();
        for (String toode: tootedSplititud) {
            toode = toode.strip();
            // väljastame otsingud ekraanile.

            toodeteList.add(toode);
        }
        kasutaja = new Kasutaja(toodeteList);

        System.out.println();

        // Annab kasutajale tagasisidet, et hetkel otsib
        printdots();

        ArrayList<Toode> tulemused;
        // Väljastame ekraanile kõik tulemused või mitme toote puhul iga toote kõige odavama.
        if (tooteOtsing.contains(",") || tooteOtsing.contains(";")) {
            tulemused = kasutaja.getKoigeOdavamad();
        } else {
            tulemused = kasutaja.getKoikTootedOfFirst();
        }
        // Paneb executori kinni -> lõpetab punktide printimise.
        executor.shutdownNow();
        valjastaTooted(tulemused, kasutaja);
    }
    private static void valjastaTooted(ArrayList<Toode> tooted, Kasutaja kasutaja) {
        if (tooted.isEmpty() || tooted.get(0) == null) {
            System.out.println("Kahjuks ei leidnud midagi.");
            return;
        }
        System.out.println(" Kõik leitud!\n");
        kasutaja.kuvaTooted(tooted);
    }
}
