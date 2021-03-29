package evgen_Tantsura.currencyExchange.service;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceivingCoursesTest {

    ReceivingCourses receivingCourses = new ReceivingCourses();

    @Test
    public void shouldGetExternalServiceFromPrivat() throws MalformedURLException, JSONException {
        List<Map<String, String>> list = receivingCourses.processingJSON();
        Assert.assertNotNull(list);
    }

    @Test
    public void shouldGetExternalServiceFromPrivatq() throws MalformedURLException, JSONException {
        List<Map<String, String>> list = receivingCourses.processingJSON();
        List<Map<String, String>> listTo = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("ccy", "USD");
        map.put("base_ccy", "UAH");
        map.put("buy", "27.65000");
        map.put("sale", "28.05000");
        Map<String, String> map2 = new HashMap<>();
        map2.put("ccy", "EUR");
        map2.put("base_ccy", "UAH");
        map2.put("buy", "32.50000");
        map2.put("sale", "33.10000");
        Map<String, String> map3 = new HashMap<>();
        map3.put("ccy", "RUR");
        map3.put("base_ccy", "UAH");
        map3.put("buy", "0.36000");
        map3.put("sale", "0.39000");
        Map<String, String> map4 = new HashMap<>();
        map4.put("ccy", "BTC");
        map4.put("base_ccy", "USD");
        map4.put("buy", "54884.9469");
        map4.put("sale", "60662.3097");
        listTo.add(map);
        listTo.add(map2);
        listTo.add(map3);
        listTo.add(map4);
        Assert.assertEquals(list, listTo);
    }

    @Test
    public void getStringRates() {
    }

    @Test
    public void getCurrencyRates() {
    }

    @Test
    public void getTextCurrencyRates() {
    }
}