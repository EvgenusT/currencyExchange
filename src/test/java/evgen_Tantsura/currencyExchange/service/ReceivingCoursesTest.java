package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import evgen_Tantsura.currencyExchange.utils.CONST;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceivingCoursesTest {

    ReceivingCourses receivingCourses = new ReceivingCourses();

    @Autowired
    ExchangeRatesRepository exchangeRatesRepository;

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

    @Test
    @Sql(value = {"/create-exchangeRates-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldGetTextCurrencyRatesOk() throws MalformedURLException {
        String stringRates = receivingCourses.getTextCurrencyRates(exchangeRatesRepository);

        String s = "\n" + "Курс: USD к UAH по состоянию на: 2021-03-31 14:02:49.11672: Продажа - 28.0898 Покупка - 27.4122 \n" +
                "Курс: EUR к UAH по состоянию на: 2021-03-31 14:02:49.196725: Продажа - 33.0143 Покупка - 32.0887 \n" +
                "Курс: RUR к UAH по состоянию на: 2021-03-31 14:02:49.200727: Продажа - 0.3889 Покупка - 0.3552 \n" +
                "Курс: BTC к USD по состоянию на: 2021-03-31 14:02:49.204728: Продажа - 61026.5123 Покупка - 54665.0658 " + "\n";
        Assert.assertEquals(stringRates, s);

    }

}