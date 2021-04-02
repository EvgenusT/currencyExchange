package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql(value = {"/create-deal-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-exchangeRates-dateNow-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class WorkWithDealTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ExchangeRatesRepository exchangeRatesRepository;

    private WorkWithDeal workWithDeal = new WorkWithDeal();

    @Test
    public void shouldLengthOtpPasswordSale() {
        Map<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("currency", "USD");
        bodyRequest.put("sum", "2400");
        bodyRequest.put("tel", "0504520387");
        bodyRequest.put("typeOfOperation", "sale");
        String result = workWithDeal.saveTheDeal(dealRepository, exchangeRatesRepository, bodyRequest);
        Assert.assertEquals(result.length(), 6);
    }

    @Test
    public void shouldLengthOtpPasswordBuy() {
        Map<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("currency", "USD");
        bodyRequest.put("sum", "2400");
        bodyRequest.put("tel", "0504520387");
        bodyRequest.put("typeOfOperation", "buy");
        String result = workWithDeal.saveTheDeal(dealRepository, exchangeRatesRepository, bodyRequest);
        Assert.assertEquals(result.length(), 6);
    }

    @Test
    public void shouldCheckForRemove() {
        Map<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("id", "1");
        bodyRequest.put("tel", "0504520376");
        boolean result = workWithDeal.checkForRemove(dealRepository, bodyRequest);
        Assert.assertEquals(result, true);
    }

    @Test
    public void shouldCheckForRemoveNotOk() {
        Map<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("id", "2");
        bodyRequest.put("tel", "0504520376");
        boolean result = workWithDeal.checkForRemove(dealRepository, bodyRequest);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldCountTransactionsByCurrency() {
        String actualResult = workWithDeal.generatingASalesReport(dealRepository);
        String expectedResult = "Кількість угод з ПРИДБАННЯ, у валюті: USD = 0 на суму: null\t Прибуток складає: null грн.\n" +
                "Кількість угод з ПРИДБАННЯ, у валюті: EUR = 0 на суму: null\t Прибуток складає: null грн.\n" +
                "Кількість угод з ПРИДБАННЯ, у валюті: RUR = 1 на суму: 100.0000\t Прибуток складає: 28.50 грн.\n" +
                "Кількість угод з ПРИДБАННЯ, у валюті: BTC = 0 на суму: null\t Прибуток складає: null грн.\n" +
                "----------------------------\n" +
                "Кількість угод з ПРОДАЖУ, у валюті: USD = 0 на суму: null\t Прибуток складає: null грн.\n" +
                "Кількість угод з ПРОДАЖУ, у валюті: EUR = 0 на суму: null\t Прибуток складає: null грн.\n" +
                "Кількість угод з ПРОДАЖУ, у валюті: RUR = 1 на суму: 200.0000\t Прибуток складає: 20.15 грн.\n" +
                "Кількість угод з ПРОДАЖУ, у валюті: BTC = 0 на суму: null\t Прибуток складає: null грн.\n" +
                "----------------------------\n";

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldReportOk() throws ParseException {

        Map<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("currency", "USD");
        bodyRequest.put("beginning", "2021-03-24 00:00:00");
        bodyRequest.put("end", "2021-04-01 00:00:00");
        List<Deal> actualResult = workWithDeal.report(dealRepository, bodyRequest);
        Assert.assertEquals("218948", actualResult.get(0).getOtpPass());
        Assert.assertEquals("111111", actualResult.get(1).getOtpPass());
        Assert.assertEquals("555555", actualResult.get(2).getOtpPass());
        Assert.assertEquals(1, actualResult.get(0).getId());
        Assert.assertEquals(3, actualResult.get(1).getId());
        Assert.assertEquals(7, actualResult.get(2).getId());
    }

}