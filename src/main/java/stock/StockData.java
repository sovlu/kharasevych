package stock;

/**
 * Created by harasevichkostyantyn on 4/25/16.
 */
import java.util.Scanner;


public class StockData {

    public static final int NA_FIELD = 999999999;
    private static final int MAX_LENGTH = 5;

    private double percentChange;
    private double percentVolume;
    private double yield;
    private double priceToEarningsRatio;
    private double marketCap;
    private String symbol;
    private String name;

    // expected format is "<change> - <percent change>%",<averageVolume>,<volume>
    // <yield>,<P/E ratio>,<marketCap>B
    public StockData(String name, String symbol, String rawData) {
        this.symbol =symbol;
        this.name = name;
        Scanner sc = new Scanner(rawData);
        sc.useDelimiter("[\",]+");
        setPercentChange(sc.next());
        setVolume(sc);
        yield = trim(sc.nextDouble());
        setPE(sc);
        setMarketCap(sc);
    }


    private void setMarketCap(Scanner sc) {
        // ensure no more than 4 digits

        String rawCap = sc.next();
        // assume last char is B
        rawCap = rawCap.substring(0, rawCap.length() - 1);
        marketCap = Double.parseDouble(rawCap);
        marketCap = trim(marketCap);
    }

    private double trim(double value) {
        String org = "" + value;
        if(org.length() > MAX_LENGTH)
            org = org.substring(0,MAX_LENGTH);
        return Double.parseDouble(org);
    }

    // PE could be NA if no earnings, set RATIO to NO_EARNINGS
    private void setPE(Scanner sc) {
        if(sc.hasNextDouble()) {
            priceToEarningsRatio = sc.nextDouble();
            priceToEarningsRatio = trim(priceToEarningsRatio);
        }
        else {
            sc.next();
            priceToEarningsRatio = NA_FIELD;
        }
    }


    // assume sc is ready to read average volume, volume
    private void setVolume(Scanner sc) {
        double aveVol = sc.nextDouble();
        double volume = sc.nextDouble();
        percentVolume = (volume - aveVol) / aveVol * 100;
        percentVolume = (int) (percentVolume * 100) / 100.0;
        percentVolume = trim(percentVolume);
    }

    // expect change info to have " - " in middle
    private void setPercentChange(String changeInfo) {
        int posLastSpace = changeInfo.lastIndexOf(" ");
        // extra 1 to skip last space, cut off %
        changeInfo = changeInfo.substring(posLastSpace + 1, changeInfo.length() - 1);
        percentChange = Double.parseDouble(changeInfo);
        percentChange = trim(percentChange);
    }

    public double getParValue() {
        return percentChange;
    }

    public double getPercentVolume() {
        return percentVolume;
    }
    public double getFDividend() {
        return yield;
    }

    public double getLastDividend() {
        return priceToEarningsRatio;
    }

    public double getTickedPrice() {
        return marketCap;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return name;
    }


    public String toString() {
        return name + " " + symbol + " percentChange: " + percentChange
                + "%, percent volume: " + percentVolume
                + "%, P/E ratio: " + priceToEarningsRatio
                + ", dividend yield: " + yield
                + ", market cap in billions: " + marketCap;
    }
}
