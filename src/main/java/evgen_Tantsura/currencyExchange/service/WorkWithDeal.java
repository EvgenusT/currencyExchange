package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.entity.ExchangeRates;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import evgen_Tantsura.currencyExchange.utils.CONST;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorkWithDeal {

    private StringBuffer sb = new StringBuffer();
    private String request = "";

    public String saveTheDeal(DealRepository dealRepository, ExchangeRatesRepository exchangeRatesRepository,
                              Map<String, String> reguestMap) {
        String currency = reguestMap.get(CONST.CURRENCY);
        String sum = reguestMap.get(CONST.SUM);
        String tel = reguestMap.get(CONST.TEL);
        String typeOfOperation = reguestMap.get(CONST.TYPE_OF_OPERATION);
        String otpPassword = null;

        if (typeOfOperation.equals(CONST.SALE)) {
            ExchangeRates exchangeRates = exchangeRatesRepository.getTheCurrencyRate(currency);
            BigDecimal sumInCurrency = new BigDecimal(sum);
            BigDecimal mySumInBaseCcy = sumInCurrency.multiply(exchangeRates.getMySale());
            BigDecimal sumInBaseCcy = sumInCurrency.multiply(new BigDecimal(exchangeRates.getSale()));
            BigDecimal income = mySumInBaseCcy.subtract(sumInBaseCcy);
            otpPassword = createOtpPassword();

            Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, mySumInBaseCcy, income,
                    LocalDateTime.now(), typeOfOperation, otpPassword);
            dealRepository.save(deal);

        } else if (typeOfOperation.equals(CONST.BUY)
        ) {
            ExchangeRates exchangeRates = exchangeRatesRepository.getTheCurrencyRate(currency);
            BigDecimal sumInCurrency = new BigDecimal(sum);
            BigDecimal mySumInUAH = sumInCurrency.multiply(exchangeRates.getMyBuy());
            BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(exchangeRates.getBuy()));
            BigDecimal income = sumInUAH.subtract(mySumInUAH);

            otpPassword = createOtpPassword();

            Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, mySumInUAH, income,
                    LocalDateTime.now(), typeOfOperation, otpPassword);
            dealRepository.save(deal);
        }

        //тут должен быть вызов функционала для отправки СМС с ОТП паролем на номер клиента

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

    public String generatingASalesReport(DealRepository dealRepository) {
        List<String> current = Arrays.asList("USD", "EUR", "RUR", "BTC");
        String baseCcy = "";

        for (String value : current) {
            int count = dealRepository.countDeals(value, CONST.BUY);
            BigDecimal sumDeals = dealRepository.sumDeals(value, CONST.BUY);
            BigDecimal income = dealRepository.sumIncomForCurrency(value, CONST.BUY);
            if (value.equals("BTC")) {
                baseCcy = "USD";
            } else baseCcy = "UAH";
            sb = stringFormation(value, count, sumDeals, income, baseCcy);
        }
        sb.append("----------------------------\n");
        for (String value : current) {
            int count = dealRepository.countDeals(value, CONST.SALE);
            BigDecimal sumDeals = dealRepository.sumDeals(value, CONST.SALE);
            BigDecimal income = dealRepository.sumIncomForCurrency(value, CONST.SALE);
            if (value.equals("BTC")) {
                baseCcy = "USD";
            } else baseCcy = "UAH";
            sb = stringFormation(value, count, sumDeals, income, baseCcy);
        }
        sb.append("----------------------------\n");

        request = sb.toString();
        return request;
    }

    private StringBuffer stringFormation(String value, int count, BigDecimal sumDeals, BigDecimal income, String baseCcy) {
        sb.append("Кількість угод з ПРИДБАННЯ, у валюті: ")
                .append(value).append(" = ")
                .append(count).append(" на суму: ")
                .append(sumDeals)
                .append("\t Прибуток складає: ")
                .append(income)
                .append(" ")
                .append(baseCcy)
                .append("\n");

        sb.append("----------------------------\n");

        return sb;
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
