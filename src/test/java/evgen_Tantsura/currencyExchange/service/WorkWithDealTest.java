package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.entity.*;
import evgen_Tantsura.currencyExchange.repository.*;
import evgen_Tantsura.currencyExchange.utils.CONST;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WorkWithDealTest {

    private WorkWithDeal workWithDeal;

    private DealRepository dealRepository;
    private ExchangeRatesRepository exchangeRatesRepository;
    private RequestDealRepository requestDealRepository;
    private ResponseDealRepository responseDealRepository;
    private DeleteDealRepository deleteDealRepository;
    private ReportDealRepository reportDealRepository;

    private RequestDeal requestDeal;
    private ResponseDeal responseDeal;
    private DeleteDeal deleteDeal;
    private ReportDeal reportDeal;

    @Before
    public void init() {

        this.exchangeRatesRepository = Mockito.mock(ExchangeRatesRepository.class);
        this.dealRepository = Mockito.mock(DealRepository.class);
        this.requestDealRepository = Mockito.mock(RequestDealRepository.class);
        this.responseDealRepository = Mockito.mock(ResponseDealRepository.class);
        this.deleteDealRepository = Mockito.mock(DeleteDealRepository.class);
        this.reportDealRepository = Mockito.mock(ReportDealRepository.class);
        this.workWithDeal = new WorkWithDeal(exchangeRatesRepository, dealRepository, requestDealRepository,
                responseDealRepository, deleteDealRepository, reportDealRepository);

        this.requestDeal = new RequestDeal();
        this.responseDeal = new ResponseDeal();
        this.deleteDeal = new DeleteDeal();
        this.reportDeal = new ReportDeal();
    }

    @Test
    public void shouldSaveTheDeal() {
        requestDeal.setId(1);
        requestDeal.setCurrency("USD");
        requestDeal.setSum("100");
        requestDeal.setTel("0504520366");
        requestDeal.setTypeOfOperation("sale");

        Mockito.when(exchangeRatesRepository.getTheCurrencyRate("USD")).thenReturn(new ExchangeRates("USD", "UAH", "111",
                "120", LocalDateTime.of(2021, 04, 12, 9, 00), new BigDecimal("112"), new BigDecimal("121")));

        String otpPass = workWithDeal.saveTheDeal(requestDeal);

        Mockito.verify(requestDealRepository, Mockito.times(1)).save(requestDeal);
        Mockito.verify(dealRepository, Mockito.times(1)).save(new Deal("0504520366", CONST.NEW,
                new BigDecimal("100"), "USD", new BigDecimal("12100"), new BigDecimal("100"),
                LocalDateTime.now(), "sale", otpPass));
    }

    @Test
    public void shouldUpdateTheDeal() {
        responseDeal.setOtpPass("1111111");
        responseDeal.setTel("0504520366");
        workWithDeal.updateTheDeal(responseDeal);
        Mockito.verify(responseDealRepository, Mockito.times(1)).save(responseDeal);
        Mockito.verify(dealRepository, Mockito.times(1)).updateStatus(CONST.COMPLETED, "0504520366", "1111111");
    }

    @Test
    public void shouldCancellationTheDealOk() {
        responseDeal.setOtpPass("123456");
        responseDeal.setTel("0504520366");
        workWithDeal.cancellationTheDeal(responseDeal);
        Mockito.verify(responseDealRepository, Mockito.times(1)).save(responseDeal);
        Mockito.verify(dealRepository, Mockito.times(1)).cancellationStatus(CONST.REJECTED, "0504520366");
    }

    @Test
    public void shouldRemoveTheDealOk() {
        deleteDeal.setId(1);
        deleteDeal.setTel("0504520366");
        workWithDeal.removeTheDeal(deleteDeal);
        Mockito.verify(deleteDealRepository, Mockito.times(1)).save(deleteDeal);
        Mockito.verify(dealRepository, Mockito.times(1)).remove(1, "0504520366");
    }

    @Test
    public void shouldSetCheckStatusDealOk() {
        responseDeal.setOtpPass("1111111");
        responseDeal.setTel("0504520366");
        workWithDeal.checkStatusDeal(responseDeal);
        Mockito.verify(dealRepository, Mockito.times(1)).checkStatus("0504520366", "1111111");
    }

    @Test
    public void shouldCheckForRemoveOk() {
        deleteDeal.setId(1);
        deleteDeal.setTel("0504520366");

        Map<String, String> expected = new HashMap<>();
        expected.put("id", "1");
        expected.put("tel", "0504520366");

        Mockito.when(dealRepository.checkBooleanDeal(1, "0504520366")).thenReturn(expected);
        boolean expectedBoolean = workWithDeal.checkForRemove(deleteDeal);
        Mockito.verify(dealRepository, Mockito.times(1)).checkBooleanDeal(1, "0504520366");
        assertTrue(expectedBoolean);
    }

    @Test
    public void shouldCheckStatusDeal() {
        responseDeal.setOtpPass("111111");
        responseDeal.setTel("0504520366");

        Map<String, String> expected = new HashMap<>();
        expected.put("tel", "0504520366");
        expected.put("otpPass", "111111");

        Mockito.when(dealRepository.checkStatus("0504520366", "111111")).thenReturn(expected);

        Map<String, String> actual = workWithDeal.checkStatusDeal(responseDeal);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReport() throws ParseException {
        reportDeal.setCurrency("USD");
        reportDeal.setBeginning(LocalDateTime.of(2021, 04, 12, 9, 00));
        reportDeal.setEnd(LocalDateTime.of(2021, 04, 12, 18, 00));

        requestDeal.setId(1);
        requestDeal.setCurrency("USD");
        requestDeal.setSum("100");
        requestDeal.setTel("0504520366");
        requestDeal.setTypeOfOperation("sale");

        Mockito.when(exchangeRatesRepository.getTheCurrencyRate("USD")).thenReturn(new ExchangeRates("USD", "UAH", "111",
                "120", LocalDateTime.of(2021, 04, 12, 8, 00), new BigDecimal("112"), new BigDecimal("121")));

        String otpPass = workWithDeal.saveTheDeal(requestDeal);

        List<Deal> expected = new ArrayList<>();
        expected.add(new Deal("0504520366", CONST.NEW,
                new BigDecimal("100"), "USD", new BigDecimal("12100"), new BigDecimal("100"),
                LocalDateTime.now(), "sale", otpPass));

        Mockito.when(dealRepository.findAllByCurrencyAndPeriod("USD",
                LocalDateTime.of(2021, 04, 12, 9, 00),
                (LocalDateTime.of(2021, 04, 12, 18, 00))))
                .thenReturn(expected);

        List<Deal> actual = workWithDeal.report(reportDeal);
        Mockito.verify(reportDealRepository, Mockito.times(1)).save(reportDeal);
        assertEquals(expected, actual);
    }

}