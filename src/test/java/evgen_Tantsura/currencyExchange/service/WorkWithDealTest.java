package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.entity.DeleteDeal;
import evgen_Tantsura.currencyExchange.entity.ReportDeal;
import evgen_Tantsura.currencyExchange.entity.RequestDeal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.DeleteDealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import evgen_Tantsura.currencyExchange.repository.ReportDealRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

//@RunWith(MockitoJUnitRunner.class)
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

    @Autowired
    ReportDealRepository reportDealRepository;

    @Autowired
    DeleteDealRepository newDeleteDealRepository;

    @Autowired
    private WorkWithDeal workWithDeal;


    @Test
    public void shouldLengthOtpPasswordSale() {
        RequestDeal requestDeal = new RequestDeal();
        requestDeal.setCurrency("USD");
        requestDeal.setSum("2400");
        requestDeal.setTel("0504520387");
        requestDeal.setTypeOfOperation("sale");
        String result = workWithDeal.saveTheDeal(requestDeal);
        Assert.assertEquals(result.length(), 6);
    }

    @Test
    public void shouldLengthOtpPasswordBuy() {
        RequestDeal requestDeal = new RequestDeal();
        requestDeal.setCurrency("USD");
        requestDeal.setSum("2400");
        requestDeal.setTel("0504520387");
        requestDeal.setTypeOfOperation("buy");
        String result = workWithDeal.saveTheDeal(requestDeal);
        Assert.assertEquals(result.length(), 6);
    }

    @Test
    public void shouldCheckForRemove() {
        DeleteDeal deleteDeal = new DeleteDeal();
        deleteDeal.setId(1);
        deleteDeal.setTel("0504520376");
        boolean result = workWithDeal.checkForRemove(deleteDeal);
        Assert.assertEquals(result, true);
    }

    @Test
    public void shouldCheckForRemoveNotOk() {
        DeleteDeal deleteDeal = new DeleteDeal();
        deleteDeal.setId(2);
        deleteDeal.setTel("0504520376");
        boolean result = workWithDeal.checkForRemove(deleteDeal);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldCountTransactionsByCurrency() {
        String actualResult = workWithDeal.generatingASalesReport();
        String expectedResult =
                "Кількість угод з ПРИДБАННЯ, у валюті: USD = 1 на суму: 100.0000\t Прибуток складає: 28.50 UAH\n" +
                        "Кількість угод з ПРИДБАННЯ, у валюті: EUR = 0 на суму: null\t Прибуток складає: null UAH\n" +
                        "Кількість угод з ПРИДБАННЯ, у валюті: RUR = 1 на суму: 100.0000\t Прибуток складає: 28.50 UAH\n" +
                        "Кількість угод з ПРИДБАННЯ, у валюті: BTC = 0 на суму: null\t Прибуток складає: null USD\n" +
                        "----------------------------\n" +
                        "Кількість угод з ПРОДАЖУ, у валюті: USD = 1 на суму: 100.0000\t Прибуток складає: 28.50 UAH\n" +
                        "Кількість угод з ПРОДАЖУ, у валюті: EUR = 0 на суму: null\t Прибуток складає: null UAH\n" +
                        "Кількість угод з ПРОДАЖУ, у валюті: RUR = 1 на суму: 200.0000\t Прибуток складає: 20.15 UAH\n" +
                        "Кількість угод з ПРОДАЖУ, у валюті: BTC = 0 на суму: null\t Прибуток складає: null USD\n" +
                        "----------------------------\n";

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldReportOk() throws ParseException {
        ReportDeal newReportDeal = new ReportDeal();
        String beginning = "2021-03-24T00:00:00";
        String end = "2021-04-02T00:00:00";
        newReportDeal.setCurrency("USD");
        newReportDeal.setBeginning(LocalDateTime.parse(beginning));
        newReportDeal.setEnd(LocalDateTime.parse(end));
        List<Deal> actualResult = workWithDeal.report(newReportDeal);

        Assert.assertEquals("218948", actualResult.get(0).getOtpPass());
        Assert.assertEquals("111111", actualResult.get(1).getOtpPass());
        Assert.assertEquals("454545", actualResult.get(2).getOtpPass());
        Assert.assertEquals(1, actualResult.get(0).getId());
        Assert.assertEquals(3, actualResult.get(1).getId());
        Assert.assertEquals(7, actualResult.get(2).getId());
    }

}