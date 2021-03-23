package evgen_Tantsura.currencyExchange.utils;

import com.google.gson.Gson;
import evgen_Tantsura.currencyExchange.entity.ExchangeRates;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class ReceivingCourses {

    public static final BigDecimal margin = new BigDecimal(0.0500);

    public List<Map<String, String>> processingJSON() throws MalformedURLException, JSONException {
        URL startURL = new URL("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");
        String json_source = getStringRates(startURL);
        JSONArray jsonArr = new JSONArray(json_source);
        JSONObject jsonObject;
        List<Map<String, String>> listRatesInJson = new ArrayList<>();

        for (int i = 0; i < jsonArr.length(); i++) {
            jsonObject = jsonArr.getJSONObject(i);
            Map<String, String> map = new Gson().fromJson(jsonObject.toString(), Map.class);
            listRatesInJson.add(map);
        }
        return listRatesInJson;
    }

    public String getStringRates(URL startURL) throws MalformedURLException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        try {
            urlConnection = (HttpURLConnection) startURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    public void getCurrencyRates(ExchangeRatesRepository exchangeRatesRepository, List<Map<String, String>> listJson) {
        ExchangeRates newExchangeRates = null;
        for (int i = 0; i < listJson.size(); i++) {
            Map<String, String> stringMap = listJson.get(i);
            BigDecimal buy = new BigDecimal(stringMap.get("buy"));
            BigDecimal sale = new BigDecimal(stringMap.get("sale"));
            newExchangeRates = new ExchangeRates(stringMap.get("ccy"),
                    stringMap.get("base_ccy"), stringMap.get("buy"), stringMap.get("sale"), LocalDateTime.now(),
                    buy.multiply(margin).add(buy), sale.multiply(margin).add(sale));
            exchangeRatesRepository.save(newExchangeRates);
        }
    }

    public String getTextCurrencyRates(ExchangeRatesRepository exchangeRatesRepository) {
        List<String> current = Arrays.asList("USD", "EUR", "RUR", "BTC");
        StringBuffer sb = new StringBuffer("\n");
        String request = "";
        for (int i = 0; i < current.size(); i++) {
            Map<String, Object> line = exchangeRatesRepository.getTheRateByCurrency(current.get(i));
            request = "Курс: " + line.get("CCY") + " к " + line.get("BASE_CCY") + " по состоянию на: "
                    + line.get("DATE_AND_TIME") + ": Продажа - " + line.get("MY_SALE") + " Покупка - " + line.get("MY_BUY") + " \n";
            sb.append(request);
        }
        request = sb.toString();
        return request;
    }
}


