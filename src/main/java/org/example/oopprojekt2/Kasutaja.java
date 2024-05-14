package org.example.oopprojekt2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Siin isikustame tooted kasutajale ja teeme iga toote otsimise kaabitsejatega
//Vaata meetodeid alt.
public class Kasutaja {
    private double minKoguSum;
    private ArrayList<String> soovitudTooted;
    private ArrayList<Kaabitseja> kaabitsejad;

    public Kasutaja(ArrayList<String> soovitudTooted) {
        this.minKoguSum = 0.0;
        this.soovitudTooted = soovitudTooted;
        this.kaabitsejad = new ArrayList<>(Arrays.asList(new Coop(), new Rimi()));
    }

    // Mitme toote samaaegne otsimine nt. kartul(rimist ja coopist), majonees(rimist
    // ja coopist)
    public ArrayList<Toode> getKoigeOdavamad() throws IOException, ExecutionException, InterruptedException {
        List<CompletableFuture<Toode>> futureToodes = new ArrayList<>();

        // Iga soovitud toote otsimine
        for (String soovitudToode : soovitudTooted) {
            // Loob iga kaabitseja jaoks CompletableFuture
            List<CompletableFuture<ArrayList<Toode>>> kaabitseFutures = new ArrayList<>(kaabitsejad.stream()
                    .map(kaabitseja -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return kaabitseja.kaabitse(soovitudToode);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .toList());

            Collections.reverse(kaabitseFutures);

            // Ühendab kõigi CompletableFuture tulemused
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    kaabitseFutures.toArray(new CompletableFuture[0]));

            // Loob CompletableFuture odavaima toote saamiseks
            CompletableFuture<Toode> cheapestToodeFuture = allFutures.thenApply(v -> kaabitseFutures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .min(Toode::compareTo)
                    .orElse(null));

            // Lisab odavaima toote CompletableFuture'i listi
            futureToodes.add(cheapestToodeFuture);
        }

        // Ootab kõigi CompletableFuture lõppemist ja tagastab tulemused
        return (ArrayList<Toode>) CompletableFuture.allOf(futureToodes.toArray(new CompletableFuture[0]))
                .thenApply(v -> futureToodes.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .get();
    }

    // Iga coopist ja rimist samaaegne otsimine
    public ArrayList<Toode> getKoikTootedOfFirst() throws ExecutionException, InterruptedException {
        // Annab soovitud toodete esimese toote koik vastused.

        // Loob listi CompletableFuture objektide hoidmiseks
        List<CompletableFuture<List<Toode>>> futures = new ArrayList<>();

        // Loob fikseeritud suurusega niidipargiga ExecutorService'i
        ExecutorService executor = Executors.newFixedThreadPool(2); // Niitide arv

        // Lisab CompletableFuture iga kaabitse meetodi kutse jaoks
        CompletableFuture<List<Toode>> rimiFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return new Rimi().kaabitse(soovitudTooted.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor);
        CompletableFuture<List<Toode>> coopFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return new Coop().kaabitse(soovitudTooted.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor);

        // Ühendab kõigi CompletableFuture tulemused
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(rimiFuture, coopFuture);

        // Ootab kõigi CompletableFuture lõppemist
        allFutures.get();

        // Hangib tulemused
        List<Toode> rimiTooted = rimiFuture.get();
        List<Toode> coopTooted = coopFuture.get();

        // Ühendab tulemused mõlemast allikast
        ArrayList<Toode> koikTooted = new ArrayList<>();
        koikTooted.addAll(rimiTooted);
        koikTooted.addAll(coopTooted);

        // Sorteerib listi
        koikTooted.sort(Toode::compareTo);

        // Tagastab tulemused
        executor.shutdown();
        return koikTooted;
    }

    // Kuvan kõik leitud tooted viisakalt
    public void kuvaTooted(ArrayList<Toode> tooted) {
        this.minKoguSum = 0;
        // kui tühi, ära tee midagi
        if (tooted.isEmpty()) {
            System.out.println("Ei leidnud ühtegi toodet.");
            return;
        }
        String format = "| %-50s | %-7s | %-10s | %-5s |\n";
        System.out.format(format, "Toote nimi", "Hind", "Kilo hind", "Pood");

//        for (Toode toode : tooted) {
            // if (toode.getName().toLowerCase().contains(tooteOtsinguTermin.toLowerCase()))
            // {
//            toode.valjastaIlusalt();
            // }
//        }
//        System.out.println("nyyd sorditud.");
        // Sort the list
//        tooted.sort(Toode::compareTo);

        // Trükib filtreeritud tulemused
        for (Toode toode : tooted) {
            toode.valjastaIlusalt();
            this.minKoguSum += toode.getPrice();
        }
        System.out.printf("Kokku läheb maksma: %.2f %n", minKoguSum);
    }
}
