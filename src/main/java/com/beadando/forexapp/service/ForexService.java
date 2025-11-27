package com.beadando.forexapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ForexService {

    private static final String API_URL = "https://open.er-api.com/v6/latest/%s";

    public Double getCurrentPrice(String instrument) {

        String[] parts = instrument.split("_");
        String base = parts[0];
        String quote = parts[1];

        String url = String.format(API_URL, base);
        RestTemplate rest = new RestTemplate();
        String json = rest.getForObject(url, String.class);

        // példa: "USD":1.0832
        Pattern pattern = Pattern.compile("\"" + quote + "\":([0-9.]+)");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return null;
    }
    public Double getPreviousPrice(String instrument) {
        Double now = getCurrentPrice(instrument);
        if (now == null) return null;

        // szimulált előző ár (kicsi eltérés)
        return now + (Math.random() - 0.5) * 0.01;
    }

}
