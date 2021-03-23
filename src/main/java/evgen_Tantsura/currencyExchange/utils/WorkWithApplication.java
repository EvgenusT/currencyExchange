package evgen_Tantsura.currencyExchange.utils;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

public class WorkWithApplication {

    public String savingTheApplication(DealRepository dealRepository, ExchangeRatesRepository exchangeRatesRepository,
                                       Map<String, String> reguestMap) {
        String currency = reguestMap.get("currency");
        String sum = reguestMap.get("sum");
        String tel = reguestMap.get("tel");
        String theCurrencyPurchaseRate = exchangeRatesRepository.getTheCurrencySaleRate(currency);
        BigDecimal sumInCurrency = new BigDecimal(sum);
        BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(theCurrencyPurchaseRate));
        String otpPassword = createOtpPassword();

        Deal deal = new Deal(tel, CONST.NEW, sumInCurrency, currency, sumInUAH, LocalDateTime.now(), otpPassword);
        dealRepository.save(deal);

        return otpPassword;
    }

    public void updateTheApplication(DealRepository dealRepository, Map<String, String> reguestMap) {
        String tel = reguestMap.get("tel");
        String otpPass = reguestMap.get("otpPass");
        dealRepository.updateStatus(CONST.COMPLETED, tel, otpPass);
    }

    public String createOtpPassword() {
        Random randomOtp = new Random();
        int otp = randomOtp.nextInt(999999);
        return String.format("%06d", otp);
    }


}
