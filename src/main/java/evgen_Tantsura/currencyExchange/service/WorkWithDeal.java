package evgen_Tantsura.currencyExchange.service;

import evgen_Tantsura.currencyExchange.entity.*;
import evgen_Tantsura.currencyExchange.repository.*;
import evgen_Tantsura.currencyExchange.utils.CONST;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class WorkWithDeal {

    private final ExchangeRatesRepository exchangeRatesRepository;
    private final DealRepository dealRepository;
    private final RequestDealRepository requestDealRepository;
    private final ResponseDealRepository responseDealRepository;
    private final DeleteDealRepository deleteDealRepository;
    private final ReportDealRepository reportDealRepository;

    private StringBuffer sb = new StringBuffer();
    private String request = "";

    public WorkWithDeal(ExchangeRatesRepository exchangeRatesRepository, DealRepository dealRepository,
                        RequestDealRepository requestDealRepository, ResponseDealRepository responseDealRepository,
                        DeleteDealRepository deleteDealRepository, ReportDealRepository reportDealRepository) {
        this.exchangeRatesRepository = exchangeRatesRepository;
        this.dealRepository = dealRepository;
        this.requestDealRepository = requestDealRepository;
        this.responseDealRepository = responseDealRepository;
        this.deleteDealRepository = deleteDealRepository;
        this.reportDealRepository = reportDealRepository;
    }

    public String saveTheDeal(RequestDeal newRequestDeal) {

        requestDealRepository.save(newRequestDeal);

        String otpPassword = null;

        if (newRequestDeal.getTypeOfOperation().equals(CONST.SALE)) {
            ExchangeRates exchangeRates = exchangeRatesRepository.getTheCurrencyRate(newRequestDeal.getCurrency());
            BigDecimal sumInCurrency = new BigDecimal(newRequestDeal.getSum());
            BigDecimal mySumInBaseCcy = sumInCurrency.multiply(exchangeRates.getMySale());
            BigDecimal sumInBaseCcy = sumInCurrency.multiply(new BigDecimal(exchangeRates.getSale()));
            BigDecimal income = mySumInBaseCcy.subtract(sumInBaseCcy);
            otpPassword = createOtpPassword();

            dealRepository.save(new Deal(newRequestDeal.getTel(), CONST.NEW, sumInCurrency, newRequestDeal.getCurrency(), mySumInBaseCcy, income,
                    LocalDateTime.now(), newRequestDeal.getTypeOfOperation(), otpPassword));

        } else if (newRequestDeal.getTypeOfOperation().equals(CONST.BUY)
        ) {
            ExchangeRates exchangeRates = exchangeRatesRepository.getTheCurrencyRate(newRequestDeal.getCurrency());
            BigDecimal sumInCurrency = new BigDecimal(newRequestDeal.getSum());
            BigDecimal mySumInUAH = sumInCurrency.multiply(exchangeRates.getMyBuy());
            BigDecimal sumInUAH = sumInCurrency.multiply(new BigDecimal(exchangeRates.getBuy()));
            BigDecimal income = sumInUAH.subtract(mySumInUAH);

            otpPassword = createOtpPassword();
            dealRepository.save(new Deal(newRequestDeal.getTel(), CONST.NEW, sumInCurrency, newRequestDeal.getCurrency(), mySumInUAH, income,
                    LocalDateTime.now(), newRequestDeal.getTypeOfOperation(), otpPassword));
        }

        //тут может быть вызов функционала для отправки СМС с ОТП паролем на номер телефона клиента

        return otpPassword;
    }

    public Map<String, String> checkStatusDeal(ResponseDeal newResponseDeal) {
        return dealRepository.checkStatus(newResponseDeal.getTel(), newResponseDeal.getOtpPass());
    }


    public void updateTheDeal(ResponseDeal newResponseDeal) {
        responseDealRepository.save(newResponseDeal);
        dealRepository.updateStatus(CONST.COMPLETED, newResponseDeal.getTel(), newResponseDeal.getOtpPass());
    }

    public void cancellationTheDeal(ResponseDeal newResponseDeal) {
        responseDealRepository.save(newResponseDeal);
        dealRepository.cancellationStatus(CONST.REJECTED, newResponseDeal.getTel());
    }

    public void removeTheDeal(DeleteDeal newDeleteDeal) {
        deleteDealRepository.save(newDeleteDeal);
        dealRepository.remove(newDeleteDeal.getId(), newDeleteDeal.getTel());
    }

    public boolean checkForRemove(DeleteDeal newDeleteDeal) {
        boolean check = false;
        Map<String, String> checkBooleanDeal = dealRepository.checkBooleanDeal
                (newDeleteDeal.getId(), newDeleteDeal.getTel());
        if (!checkBooleanDeal.isEmpty())
            check = true;
        return check;
    }

    public String generatingASalesReport() {
        List<String> current = Arrays.asList("USD", "EUR", "RUR", "BTC");
        String baseCcy = "";

        for (String value : current) {
            int count = dealRepository.countDeals(value, CONST.BUY);
            BigDecimal sumDeals = dealRepository.sumDeals(value, CONST.BUY);
            BigDecimal income = dealRepository.sumIncomForCurrency(value, CONST.BUY);
            if (value.equals("BTC")) {
                baseCcy = "USD";
            } else baseCcy = "UAH";
            sb = stringFormation(CONST.BUY, value, count, sumDeals, income, baseCcy);
        }
        sb.append("----------------------------\n");
        for (String value : current) {
            int count = dealRepository.countDeals(value, CONST.SALE);
            BigDecimal sumDeals = dealRepository.sumDeals(value, CONST.SALE);
            BigDecimal income = dealRepository.sumIncomForCurrency(value, CONST.SALE);
            if (value.equals("BTC")) {
                baseCcy = "USD";
            } else baseCcy = "UAH";
            sb = stringFormation(CONST.SALE, value, count, sumDeals, income, baseCcy);
        }
        sb.append("----------------------------\n");

        request = sb.toString();
        return request;
    }

    private StringBuffer stringFormation(String typeDeal, String value, int count, BigDecimal sumDeals, BigDecimal income, String baseCcy) {
        if (typeDeal.equals(CONST.BUY)) {
            sb.append("Кількість угод з ПРИДБАННЯ, у валюті: ")
                    .append(value).append(" = ")
                    .append(count).append(" на суму: ")
                    .append(sumDeals)
                    .append("\t Прибуток складає: ")
                    .append(income)
                    .append(" ")
                    .append(baseCcy)
                    .append("\n");
        } else {
            sb.append("Кількість угод з ПРОДАЖУ, у валюті: ")
                    .append(value).append(" = ")
                    .append(count).append(" на суму: ")
                    .append(sumDeals)
                    .append("\t Прибуток складає: ")
                    .append(income)
                    .append(" ")
                    .append(baseCcy)
                    .append("\n");
        }
        return sb;
    }

    public List<Deal> report(ReportDeal newReportDeal) throws ParseException {
        reportDealRepository.save(newReportDeal);
        List<Deal> allByCurrencyAndPeriod = dealRepository.findAllByCurrencyAndPeriod(newReportDeal.getCurrency(),
                newReportDeal.getBeginning(), newReportDeal.getEnd());
        return allByCurrencyAndPeriod;
    }

    private String createOtpPassword() {
        Random randomOtp = new Random();
        int otp = randomOtp.nextInt(999999);
        return String.format("%06d", otp);
    }
}
