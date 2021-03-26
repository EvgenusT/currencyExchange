package evgen_Tantsura.currencyExchange.utils;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.entity.ExchangeRates;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorkWithDeal {

    StringBuffer sb = new StringBuffer();
    String request = "";

    public String saveTheDeal(DealRepository dealRepository, ExchangeRatesRepository exchangeRatesRepository,
                              Map<String, String> reguestMap) {
        String currency = reguestMap.get(CONST.CURRENCY);
        String sum = reguestMap.get(CONST.SUM);
        String tel = reguestMap.get(CONST.TEL);
        String typeOfOperation = reguestMap.get(CONST.TYPE_OF_OPERATION);
        String otpPassword = null;

        if (typeOfOperation.equals(CONST.SALE)) {
            ExchangeRates exchangeRates = exchangeRatesRepository.getTheCurrencySaleRate(currency);
            BigDecimal sumInCurrency = new BigDecimal(sum);
            BigDecimal mySumInUAH = sumInCurrency.multiply(exchangeRates.getMySale());
            BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(exchangeRates.getSale()));
            BigDecimal income = mySumInUAH.subtract(sumInUAH);
            otpPassword = createOtpPassword();

            Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, mySumInUAH, income,
                    LocalDateTime.now(), typeOfOperation, otpPassword);
            dealRepository.save(deal);

        } else if (typeOfOperation.equals(CONST.BUY)
        ) {
            ExchangeRates exchangeRates = exchangeRatesRepository.getTheCurrencyPurchaseRate(currency);
            BigDecimal sumInCurrency = new BigDecimal(sum);
            BigDecimal mySumInUAH = sumInCurrency.multiply(exchangeRates.getMyBuy());
            BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(exchangeRates.getBuy()));
            BigDecimal income = sumInUAH.subtract(mySumInUAH);

            otpPassword = createOtpPassword();

            Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, mySumInUAH, income,
                    LocalDateTime.now(), typeOfOperation, otpPassword);
            dealRepository.save(deal);
        }
        return otpPassword;
    }

    public void updateTheDeal(DealRepository dealRepository, Map<String, String> reguestMap) {
        String tel = reguestMap.get(CONST.TEL);
        String otpPass = reguestMap.get(CONST.OTP_PASS);
        dealRepository.updateStatus(CONST.COMPLETED, tel, otpPass);
    }

    public void cancellationTheDeal(DealRepository dealRepository, Map<String, String> reguestMap) {
        String tel = reguestMap.get(CONST.TEL);
        dealRepository.cancellationStatus(CONST.REJECTED, tel);
    }

    public void removeTheDeal(DealRepository dealRepository, Map<String, String> reguestMap) {
        dealRepository.remove(reguestMap.get(CONST.ID), reguestMap.get(CONST.TEL));

    }

    public boolean checkForRemove(DealRepository dealRepository, Map<String, String> reguestMap) {
        boolean check = false;
        Map<String, String> checkBooleanDeal = dealRepository.checkBooleanDeal
                (reguestMap.get(CONST.ID), reguestMap.get(CONST.TEL));
        if (!checkBooleanDeal.isEmpty())
            check = true;
        return check;
    }

    public String countTransactionsByCurrency(DealRepository dealRepository) {
        List<String> current = Arrays.asList("USD", "EUR", "RUR", "BTC");
        for (String value : current) {
            int count = dealRepository.countDeals(value, CONST.BUY);
            BigDecimal sumDeals = dealRepository.sumDeals(value, CONST.BUY);
            sb.append("Кількість угод з ПРИДБАННЯ, у валюті: ").append(value).append(" = ").
                    append(count).append(" на суму: ").append(sumDeals).append("\n");
        }
        sb.append("----------------------------\n");
        for (String s : current) {
            int count = dealRepository.countDeals(s, CONST.SALE);
            BigDecimal sumDeals = dealRepository.sumDeals(s, CONST.SALE);
            sb.append("Кількість угод з ПРОДАЖУ, у валюті: ").append(s).append(" = ").
                    append(count).append(" на суму: ").append(sumDeals).append("\n");
        }
        sb.append("----------------------------\n");
        request = sb.toString();
        return request;
    }

    public List<Deal> report(DealRepository dealRepository, Map<String, String> reguestMap) throws ParseException {
        String currency = reguestMap.get(CONST.CURRENCY);
        LocalDateTime beginning = convertFormatDate(reguestMap.get(CONST.BEGINNING));
        LocalDateTime end = convertFormatDate(reguestMap.get(CONST.END));
        List<Deal> allByCurrencyAndPeriod = dealRepository.findAllByCurrencyAndPeriod(currency, beginning, end);
        return allByCurrencyAndPeriod;
    }

    private LocalDateTime convertFormatDate(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }

    private String createOtpPassword() {
        Random randomOtp = new Random();
        int otp = randomOtp.nextInt(999999);
        return String.format("%06d", otp);
    }
}
