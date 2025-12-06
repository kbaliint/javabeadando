package com.beadando.forexapp.service;

import com.beadando.forexapp.mnbsoap.MNBArfolyamServiceSoap;
import com.beadando.forexapp.mnbsoap.MNBArfolyamServiceSoapImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MnbSoapService {

    private MNBArfolyamServiceSoap soapClient;
    private boolean soapAvailable = true;

    // =======================
    // KONSTRUKTOR
    // =======================
    public MnbSoapService() {
        try {
            this.soapClient = new MNBArfolyamServiceSoapImpl()
                    .getCustomBindingMNBArfolyamServiceSoap();
            System.out.println("✅ MNB SOAP initialized successfully");
        } catch (Exception e) {
            System.out.println("⚠ MNB SOAP initialization failed, fallback mode activated");
            soapAvailable = false;
            e.printStackTrace();
        }
    }

    // =======================
    // UTOLSÓ N NAP (CONTROLLERNEK!)
    // =======================
    public String getLastNDays(String currency, int days) {

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days);

        return getRatesByDate(start.toString(), end.toString(), currency);
    }

    // =======================
    // UTOLSÓ 10 NAP GRAFIKON
    // =======================
    public Map<String, Double> getLast10Days(String currency) {

        Map<String, Double> result = new LinkedHashMap<>();

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(14);

        String xml = getRatesByDate(start.toString(), end.toString(), currency);

        if (xml == null || xml.isEmpty()) {
            return getFallbackHistory(currency);
        }

        Map<String, Double> parsed = parseRatesFromXml(xml);
        if (parsed.isEmpty()) return getFallbackHistory(currency);

        List<Map.Entry<String, Double>> list = new ArrayList<>(parsed.entrySet());
        list.sort(Map.Entry.<String, Double>comparingByKey().reversed());

        int limit = Math.min(10, list.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Double> e = list.get(i);
            result.put(e.getKey(), e.getValue());
        }

        return result;
    }

    // =======================
    // VALUTÁK LISTÁJA
    // =======================
    public String getCurrencies() {

        if (!soapAvailable) {
            return "<Currs><Curr>EUR</Curr><Curr>USD</Curr><Curr>GBP</Curr></Currs>";
        }

        try {
            return soapClient.getCurrencies();
        } catch (Exception e) {
            System.out.println("SOAP FAILED -> fallback currencies");
            soapAvailable = false;
            return "<Currs><Curr>EUR</Curr><Curr>USD</Curr><Curr>GBP</Curr></Currs>";
        }
    }

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

    // =======================
    // AKTUÁLIS ÁRFOLYAMOK
    // =======================
    public String getCurrentExchangeRates() {

        if (!soapAvailable) {
            return getFallbackCurrentRates();
        }

        try {
            return soapClient.getCurrentExchangeRates();
        } catch (Exception e) {
            System.out.println("SOAP FAILED -> fallback current rates");
            soapAvailable = false;
            return getFallbackCurrentRates();
        }
    }

    // =======================
    // IDŐSZAK LEKÉRÉS
    // =======================
    public String getRatesByDate(String startDate, String endDate, String currency) {

        // HUF-RA NINCS SOAP -> FALLBACK
        if ("HUF".equals(currency)) {
            return buildFakeXml(startDate, endDate, currency);
        }

        if (!soapAvailable) {
            return buildFakeXml(startDate, endDate, currency);
        }

        System.out.println("SOAP SEND -> " + currency + " " + startDate + " -> " + endDate);

        try {
            return soapClient.getExchangeRates(startDate, endDate, currency);
        } catch (Exception e) {
            System.out.println("⚠ SOAP FAILED -> switching to fallback");
            soapAvailable = false;
            return buildFakeXml(startDate, endDate, currency);
        }
    }


    // =======================
    // XML BASED PARSER
    // =======================
    public Map<String, Double> parseRatesFromXml(String xml) {

        Map<String, Double> map = new LinkedHashMap<>();
        if (xml == null || xml.isEmpty()) return map;

        Pattern pattern = Pattern.compile(
                "<Day date=\"(.*?)\">[\\s\\S]*?<Rate unit=\"\\d+\" curr=\".*?\">(.*?)</Rate>"
        );

        Matcher matcher = pattern.matcher(xml);

        while (matcher.find()) {
            String date = matcher.group(1);
            String rawValue = matcher.group(2).replace(",", ".");
            Double value = Double.parseDouble(rawValue);
            map.put(date, value);
        }

        return map;
    }


    // =======================
    // FALLBACK ADATOK
    // =======================
    private Map<String, Double> getFallbackHistory(String currency) {

        Map<String, Double> map = new LinkedHashMap<>();

        double value = getFallbackRate(currency);

        for (int i = 0; i < 10; i++) {
            map.put(LocalDate.now().minusDays(i).toString(), value);
        }

        return map;
    }


    private double getFallbackRate(String currency) {
        return switch (currency) {
            case "HUF" -> 1.0;
            case "EUR" -> 390;
            case "USD" -> 360;
            case "GBP" -> 450;
            default -> 400;
        };
    }


    private String getFallbackCurrentRates() {
        return """
                <Rates>
                    <Rate unit="1" curr="EUR">390</Rate>
                    <Rate unit="1" curr="USD">360</Rate>
                    <Rate unit="1" curr="GBP">450</Rate>
                </Rates>
               """;
    }

    private String buildFakeXml(String start, String end, String currency) {

        StringBuilder sb = new StringBuilder("<Days>");
        LocalDate s = LocalDate.parse(start);

        for (int i = 0; i < 10; i++) {
            sb.append("<Day date=\"")
                    .append(s.plusDays(i))
                    .append("\"><Rate unit=\"1\" curr=\"")
                    .append(currency)
                    .append("\">")
                    .append(getFallbackRate(currency))
                    .append("</Rate></Day>");
        }

        sb.append("</Days>");
        return sb.toString();
    }
}
