package com.beadando.forexapp.service;

import com.beadando.forexapp.mnbsoap.MNBArfolyamServiceSoap;
import com.beadando.forexapp.mnbsoap.MNBArfolyamServiceSoapImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            return "Hiba történt a SOAP hívás során!";
        }
    }

    public String getCurrentExchangeRates() {
        try {
            return soapClient.getCurrentExchangeRates();
        } catch (Exception e) {
            e.printStackTrace();
            return "Hiba történt a SOAP hívás során!";
        }
    }

    // ---- XML → Lista<String> (valuták) ----
    public List<String> parseCurrencies(String xml) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("<Curr>(.*?)</Curr>");
        Matcher m = p.matcher(xml);

        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    // ---- XML → Lista<RateItem> (árfolyamok) ----
    public List<RateItem> parseRates(String xml) {
        List<RateItem> list = new ArrayList<>();
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
}