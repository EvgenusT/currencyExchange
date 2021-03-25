package evgen_Tantsura.currencyExchange.utils;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            String theCurrencySaleRate = exchangeRatesRepository.getTheCurrencySaleRate(currency);
            BigDecimal sumInCurrency = new BigDecimal(sum);
            BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(theCurrencySaleRate));
            otpPassword = createOtpPassword();

            Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, sumInUAH,
                    LocalDateTime.now(), typeOfOperation, otpPassword);
            dealRepository.save(deal);

        } else if (typeOfOperation.equals(CONST.BUY)
        ) {
            String theCurrencyPurchaseRateRate = exchangeRatesRepository.getTheCurrencyPurchaseRate(currency);
            BigDecimal sumInCurrency = new BigDecimal(sum);
            BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(theCurrencyPurchaseRateRate));
            otpPassword = createOtpPassword();

            Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, sumInUAH,
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
        for (int i = 0; i < current.size(); i++) {
            int count = dealRepository.countDeals(current.get(i), CONST.BUY);
            BigDecimal sumDeals = dealRepository.sumDeals(current.get(i), CONST.BUY);
            sb.append("Кількість угод з ПРИДБАННЯ, у валюті: " + current.get(i) + " = "
                    + count + " на суму: " + sumDeals + "\n");
        }
        sb.append("----------------------------\n");
        for (int i = 0; i < current.size(); i++) {
            int count = dealRepository.countDeals(current.get(i), CONST.SALE);
            BigDecimal sumDeals = dealRepository.sumDeals(current.get(i), CONST.SALE);
            sb.append("Кількість угод з ПРОДАЖУ, у валюті: " + current.get(i) + " = "
                    + count + " на суму: " + sumDeals + "\n");
        }
        sb.append("----------------------------\n");
        request = sb.toString();
        return request;
    }

    public String createOtpPassword() {
        Random randomOtp = new Random();
        int otp = randomOtp.nextInt(999999);
        return String.format("%06d", otp);
    }
}
