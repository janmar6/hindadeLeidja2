package org.example.oopprojekt2;

class Toode implements Comparable<Toode>{
    private String name;
    private double price;
    private double kiloHind;
    private String pood;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getKiloHind() {
        return kiloHind;
    }

    public String getPood() {
        return pood;
    }

    public Toode(String name, double price, double kiloHind, String pood) {
        this.name = name;
        this.price = price;
        this.kiloHind = kiloHind;
        this.pood = pood;
    }

    public Toode(String name, double price, String pood) {
        this.name = name;
        this.price = price;
        this.kiloHind = -1.0;
        this.pood = pood;
    }

    @Override
    public int compareTo(Toode o) {
        if(this.getKiloHind() >= o.getKiloHind()){
            return 1;
        } else {
            return -1;
        }
    }

    public String valjastaIlusalt() {
        String format = "%-45s %-5.2f€ %-5.2f€/kg %-6s %n";
        return String.format(format, name, price, kiloHind, pood);
    }


    @Override
    public String toString() {
        return name +
                ", " + price +
                " (" + kiloHind +
                ") " + pood;
    }
}
