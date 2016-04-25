package stock;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OptionalDataException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.Math.*;

/**
 * Created by harasevichkostyantyn on 4/24/16.
 */
public class Stock {


    private static OptionalDataException x;


    private TreeMap<String, String> getSymbolsAndNames(
            String fileName) {
        TreeMap<String, String> result = new TreeMap<String, String>();
        try {
            Scanner sc = new Scanner(new File(fileName));
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                int indexOfComma = line.indexOf(",");
                String name = line.substring(0, indexOfComma);
                String symbol = line.substring(indexOfComma + 1);
                result.put(symbol, name);
            };
            sc.close();
        }
        catch(IOException e) {
            System.out.println("ERROR reading from file: " + e);
            System.out.println("returning emptying string for url");
            e.printStackTrace();
        }
        return result;
    }

    private Scanner createScanner(String dataSource, boolean isFile) {
        Scanner sc = null;
        try {
            if(isFile) {
                sc =new Scanner(new File(dataSource));
            }
            else {
                URL stockPage = new URL(dataSource);
                sc =new Scanner(new InputStreamReader(stockPage.openStream()));
            }
        }
        catch(IOException e) {
            System.out.println("ERROR reading from web page: " + e);
            System.out.println("no data available");
            e.printStackTrace();
        }
        return sc;
    }

    private ArrayList<StockData> createStocks(String dataSource, boolean isFile) {
        ArrayList<StockData> result = new ArrayList<StockData>();
        Scanner sc = createScanner(dataSource, isFile);
        while(sc.hasNextLine()) {
            String data = sc.nextLine();
            // trim off the symbol
            int indexLastComma = data.lastIndexOf(",");
            String symbol = data.substring(indexLastComma + 1);
            // check for quotes
            assert symbol.charAt(0) == '"'
                    && symbol.charAt(symbol.length() - 1) == '"' :
                    "Error in symbol format. Missing quotes at start and end: " + symbol;
            // trim quotes
            symbol = symbol.substring(1, symbol.length() - 1);

            // don't need symbol in raw data line so trim it off
            data = data.substring(0, indexLastComma);
            result.add(new StockData(getSymbolsAndNames("sample_stock.txt").get(symbol), symbol, data));
        }
        return result;
    }






    public static void main(String [] args){
        Stock s = new Stock();

        ArrayList<StockData> stockDatas = s.createStocks("sample_stock_data.txt",true);
        double shareQuantityAcum = 0.0;
        double tradePriceAcum = 0.0;
        double sharesQuantity = 0.0;
        double price = 0.0;
        for (StockData data: stockDatas
             ) {
            String stockSymbol = data.getSymbol();
        String stockType = data.getType();
        Double fixedDividend = data.getFDividend();
         Double tickerPrice = data.getTickedPrice();
        Double lastDividend  = data.getLastDividend();
        Double parValue = data.getParValue();
            sharesQuantity = data.getFDividend();


            //Dividend Yield

            double dividendYield = -1.0;

        if("COMMON".equals(stockType)){
            dividendYield = lastDividend / tickerPrice;
        }else{
            dividendYield = (fixedDividend * parValue ) / tickerPrice;
        }

            //P/E Ratio
        double peRatio = -1.0;
         peRatio = tickerPrice / dividendYield;


            //Geometric Mean
        int n = stockDatas.size()-1;
        double log = log(peRatio);
        double GM_log =log+log;
        double gmean =exp(GM_log / n);






       tradePriceAcum += (tickerPrice * sharesQuantity);
        shareQuantityAcum += sharesQuantity;


        // Stock Price

        double stockPrice = tradePriceAcum / shareQuantityAcum;

            String pattern = "Stock Object [stockSymbol: %s, stockType: %s, fixedDividend: %7.2f,lastDividend: %7.0f,  parValue: %7.2f, ---- dividendYield: %7.2f, gmean: %7.2f, peRatio: %7.2f, tradePriceAcum: %7.2f, stockPrice: %7.2f]";
            System.out.println(String.format(pattern, stockSymbol, stockType, fixedDividend,lastDividend, parValue, dividendYield,gmean,peRatio,tradePriceAcum,stockPrice));

    }


    }


}
