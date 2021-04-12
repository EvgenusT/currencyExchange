package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.utils.CONST;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ReceivingCoursesTest {

    @Autowired
    private ReceivingCourses receivingCourses;

    @Test
    public void shouldGetExternalServiceFromPrivat() throws MalformedURLException, JSONException {
        List<Map<String, String>> list = receivingCourses.processingJSON();
        Assert.assertNotNull(list);
    }

    @Test
    public void shouldParameterURLPrivat() {
        String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
        Assert.assertEquals(url, CONST.URL_API);
    }

    @Test
    public void shouldGetTheNumberOfCurrencies() throws MalformedURLException, JSONException {
        List<Map<String, String>> list = receivingCourses.processingJSON();
        Assert.assertEquals(list.size(), 4);
    }
}