package org.example.oopprojekt2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
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
        this.kaabitsejad = new ArrayList<>();
        kaabitsejad.add(new Prisma());
        kaabitsejad.add(new Rimi());
        kaabitsejad.add(new Coop());
    }

    
// Function to return a map of products for each query
    public Map<String, List<Toode>> getProducts() throws ExecutionException, InterruptedException {
        Map<String, CompletableFuture<List<Toode>>> queryFutures = new HashMap<>();

        // Create a CompletableFuture for each search query and each kaabitseja
        for (String query : soovitudTooted) {
            List<CompletableFuture<List<Toode>>> futures = new ArrayList<>();
            for (Kaabitseja kaabitseja : kaabitsejad) {
                CompletableFuture<List<Toode>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return kaabitseja.kaabitse(query);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                futures.add(future);
            }
            // Combine futures for the same query
            CompletableFuture<List<Toode>> combinedFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .flatMap(List::stream)
                            .sorted(Toode::compareTo)
                            .collect(Collectors.toList()));
            queryFutures.put(query, combinedFuture);
        }

        // Wait for all query futures to complete and collect the results into a map
        return queryFutures.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    try {
                        return entry.getValue().get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }));
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

        CompletableFuture<List<Toode>> prismaFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return new Prisma().kaabitse(soovitudTooted.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor);

        // Ühendab kõigi CompletableFuture tulemused
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(rimiFuture, coopFuture, prismaFuture);

        // Ootab kõigi CompletableFuture lõppemist
        allFutures.get();

        // Hangib tulemused
        List<Toode> rimiTooted = rimiFuture.get();
        List<Toode> coopTooted = coopFuture.get();
        List<Toode> prismaTooted = prismaFuture.get();

        // Ühendab tulemused mõlemast allikast
        ArrayList<Toode> koikTooted = new ArrayList<>();
        koikTooted.addAll(rimiTooted);
        koikTooted.addAll(coopTooted);
        koikTooted.addAll(prismaTooted);

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
