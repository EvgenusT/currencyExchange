package evgen_Tantsura.currencyExchange.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql(value = {"/create-exchangeRates-dateNow-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ExchangeRatesRepositoryTest {

    @Autowired
    private ExchangeRatesRepository exchangeRatesRepository;

    @Test
    public void shouldGetTheRateByCurrencyUSD() {
        Map<String, Object> actualResult = exchangeRatesRepository.getTheRateByCurrency("USD");
        Assert.assertEquals(actualResult.get("ccy"), "USD");
        Assert.assertEquals(actualResult.get("base_ccy"), "UAH");
        Assert.assertEquals(actualResult.get("buy"), "27.55000");
        Assert.assertEquals(actualResult.get("my_buy"), new BigDecimal("27.4122"));
        Assert.assertEquals(actualResult.get("my_sale"), new BigDecimal("28.0898"));
        Assert.assertEquals(actualResult.get("sale"), "27.95000");
    }

    @Test
    public void shouldGetTheRateByCurrencyEUR() {
        Map<String, Object> actualResult = exchangeRatesRepository.getTheRateByCurrency("EUR");
        Assert.assertEquals(actualResult.get("ccy"), "EUR");
        Assert.assertEquals(actualResult.get("base_ccy"), "UAH");
        Assert.assertEquals(actualResult.get("buy"), "32.25000");
        Assert.assertEquals(actualResult.get("my_buy"), new BigDecimal("32.0887"));
        Assert.assertEquals(actualResult.get("my_sale"), new BigDecimal("33.0143"));
        Assert.assertEquals(actualResult.get("sale"), "32.85000");
    }

    @Test
    public void shouldGetTheRateByCurrencyRUR() {
        Map<String, Object> actualResult = exchangeRatesRepository.getTheRateByCurrency("RUR");
        Assert.assertEquals(actualResult.get("ccy"), "RUR");
        Assert.assertEquals(actualResult.get("base_ccy"), "UAH");
        Assert.assertEquals(actualResult.get("buy"), "0.35700");
        Assert.assertEquals(actualResult.get("my_buy"), new BigDecimal("0.3552"));
        Assert.assertEquals(actualResult.get("my_sale"), new BigDecimal("0.3889"));
        Assert.assertEquals(actualResult.get("sale"), "0.38700");
    }

    @Test
    public void shouldGetTheRateByCurrencyBTC() {
        Map<String, Object> actualResult = exchangeRatesRepository.getTheRateByCurrency("BTC");
        Assert.assertEquals(actualResult.get("ccy"), "BTC");
        Assert.assertEquals(actualResult.get("base_ccy"), "USD");
        Assert.assertEquals(actualResult.get("buy"), "54939.7646");
        Assert.assertEquals(actualResult.get("my_buy"), new BigDecimal("54665.0658"));
        Assert.assertEquals(actualResult.get("my_sale"), new BigDecimal("61026.5123"));
        Assert.assertEquals(actualResult.get("sale"), "60722.8978");
    }

}