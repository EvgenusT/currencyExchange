package evgen_Tantsura.currencyExchange.repository;

import evgen_Tantsura.currencyExchange.utils.CONST;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql(value = {"/create-deal-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ExchangeRatesRepository exchangeRatesRepository;

    @Test
    public void shouldUpdateStatus() {
        DealRepository dealRepository = this.dealRepository;
        dealRepository.updateStatus(CONST.COMPLETED, "0504520367", "222222");
        Map<String, String> stringStringMap = dealRepository.findById("4");
        assertTrue(stringStringMap.get("status").equals(CONST.COMPLETED));

    }

    @Test
    public void shouldCancellationStatusOk() {
        DealRepository dealRepository = this.dealRepository;
        dealRepository.cancellationStatus(CONST.REJECTED, "0504520367");
        Map<String, String> requestForId = dealRepository.findById("4");
        assertTrue(requestForId.get("status").equals(CONST.REJECTED));
    }

    @Test
    public void shouldRemoveOk() {
        DealRepository dealRepository = this.dealRepository;
        dealRepository.remove(5, "0504520368");
        Map<String, String> requestForId = dealRepository.findById("5");
        assertFalse(requestForId.isEmpty());
    }

    @Test
    public void shouldCheckRequestIsNotNull() {
        Assert.assertTrue(!(dealRepository.checkStatus("0504520369", "444444")).isEmpty());
    }
    @Test
    public void shouldCheckRequestIsNull() {
        Assert.assertTrue((dealRepository.checkStatus("0504520369", "222222222")).isEmpty());
    }

    @Test
    public void shouldCheckBooleanDealIsNotNull() {
        Assert.assertTrue(!dealRepository.checkBooleanDeal(6, "0504520369").isEmpty());
    }

    @Test
    public void shouldCheckBooleanDealIsNull() {
        Assert.assertTrue(dealRepository.checkBooleanDeal(611, "0504520369").isEmpty());
    }

    @Test
    public void ShouldCountDealsOk() {
        int actualResult = dealRepository.countDeals("USD", "sale");
        int expectedResult = 1;
        Assert.assertEquals(expectedResult, actualResult);
    }
    @Test
    public void shouldCountDealsNotOk() {
        int actualResult = dealRepository.countDeals("USD", "buy");
        int expectedResult = 10;
        Assert.assertNotEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldSumDealsOk() {
        BigDecimal actualResult = dealRepository.sumDeals("USD", CONST.BUY);
        BigDecimal expectedResult = new BigDecimal(100.0000).setScale(4);
        Assert.assertEquals(expectedResult, actualResult);

    }

    @Test
    public void sumIncomForCurrencyByBuy() {
        BigDecimal actualResult = dealRepository.sumIncomForCurrency("USD", CONST.BUY).setScale(4);
        BigDecimal expectedResult = new BigDecimal(28.50).setScale(4);
        Assert.assertEquals(expectedResult, actualResult);

    }

    @Test
    public void sumIncomForCurrencyBySale() {
        BigDecimal actualResult = dealRepository.sumIncomForCurrency("USD", CONST.SALE).setScale(4);
        BigDecimal expectedResult = new BigDecimal(28.50).setScale(4);
        Assert.assertEquals(expectedResult, actualResult);
    }
}