package com.beadando.forexapp.service;

import com.beadando.forexapp.mnbsoap.MNBArfolyamServiceSoap;
import com.beadando.forexapp.mnbsoap.MNBArfolyamServiceSoapImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MnbSoapService {

    private final MNBArfolyamServiceSoap soapClient;

    public MnbSoapService() {
        this.soapClient = new MNBArfolyamServiceSoapImpl()
                .getCustomBindingMNBArfolyamServiceSoap();
    }

    public String getCurrencies() {
        try {
            return soapClient.getCurrencies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCurrentExchangeRates() {
        try {
            return soapClient.getCurrentExchangeRates();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---- XML → List<String> (valutakódok) ----
    public List<String> parseCurrencies(String xml) {
        List<String> list = new ArrayList<>();
        if (xml == null) return list;

        Pattern p = Pattern.compile("<Curr>(.*?)</Curr>");
        Matcher m = p.matcher(xml);
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    // ---- XML → List<RateItem> (mai árfolyamok táblázathoz, ha kell) ----
    public List<RateItem> parseRates(String xml) {
        List<RateItem> list = new ArrayList<>();
        if (xml == null) return list;

        Pattern p = Pattern.compile("Rate unit=\"(.*?)\" curr=\"(.*?)\">(.*?)</Rate>");
        Matcher m = p.matcher(xml);

        while (m.find()) {
            list.add(new RateItem(
                    m.group(2),                         // currency
                    Integer.parseInt(m.group(1)),       // unit
                    m.group(3)                          // value
            ));
        }
        return list;
    }

    // -----------------------------------------------------------------
    //      TÖRTÉNETI ADATOK LEKÉRDEZÉSE GRAFIKONHOZ
    //      FONTOS: start/end dátumot **változtatás nélkül** küldjük tovább!
    //      HTML <input type="date"> -> "YYYY-MM-DD" (ez kell a szervernek)
    // -----------------------------------------------------------------
    public String getRatesByDate(String startDate, String endDate, String currency) {
        System.out.println("SOAP SEND -> " + currency + " " + startDate + " -> " + endDate);
        try {
            return soapClient.getExchangeRates(startDate, endDate, currency);
        } catch (Exception e) {
            e.printStackTrace();      // itt látszik, ha még mindig dátumhibát ad
            return null;
        }
    }

    // XML → (dátum -> érték) a grafikonhoz
    public Map<String, Double> parseRatesFromXml(String xml) {

        Map<String, Double> map = new LinkedHashMap<>();
        if (xml == null || xml.isEmpty()) return map;

        Pattern pattern = Pattern.compile(
                "<Day date=\"(.*?)\">\\s*<Rate unit=\"\\d+\" curr=\".*?\">(.*?)</Rate>",
                Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(xml);

        while (matcher.find()) {
            String date = matcher.group(1);
            String rawValue = matcher.group(2).replace(",", ".");

            Double value = Double.parseDouble(rawValue);
            map.put(date, value);
        }

        System.out.println("PARSED RATES = " + map);
        return map;
    }


}
