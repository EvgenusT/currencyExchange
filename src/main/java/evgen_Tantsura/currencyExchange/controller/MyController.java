package evgen_Tantsura.currencyExchange.controller;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import evgen_Tantsura.currencyExchange.utils.CONST;
import evgen_Tantsura.currencyExchange.utils.ReceivingCourses;
import evgen_Tantsura.currencyExchange.utils.WorkWithDeal;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("currencyExchange")
public class MyController {

    ReceivingCourses receivingCourses = new ReceivingCourses();
    WorkWithDeal workWithDeal = new WorkWithDeal();

    @Autowired
    ExchangeRatesRepository exchangeRatesRepository;

    @Autowired
    DealRepository dealRepository;

    @GetMapping("/openingDay")
    public String requestCurrencyExchange() throws IOException, JSONException {
        receivingCourses.getCurrencyRates(exchangeRatesRepository, receivingCourses.processingJSON());
        return receivingCourses.getTextCurrencyRates(exchangeRatesRepository);
    }

    @GetMapping("/request")
    public String requestCurrencyDeal(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        return workWithDeal.saveTheDeal(dealRepository, exchangeRatesRepository, reguestMap);
    }

    @GetMapping("/response")
    public String responseCurrencyDeal(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        String otpPassClient = reguestMap.get(CONST.OTP_PASS);
        String tel = reguestMap.get(CONST.TEL);
        Map<String, String> checkStatusMap = dealRepository.checkStatus(tel, otpPassClient);
        String resultCheckOTP = null;
        if (!checkStatusMap.isEmpty()) {
            workWithDeal.updateTheDeal(dealRepository, reguestMap);
            resultCheckOTP = CONST.ORDER_OK;
        } else {
            workWithDeal.cancellationTheDeal(dealRepository, reguestMap);
            resultCheckOTP = CONST.ORDER_NOT_OK;
        }
        return resultCheckOTP;
    }

    @GetMapping("/delete")
    public String deleteDeal(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        String response = null;

        if (workWithDeal.checkForRemove(dealRepository, reguestMap)) {
            workWithDeal.removeTheDeal(dealRepository, reguestMap);
            response = "Заявка №: " + reguestMap.get(CONST.ID) + " - удалена";
        } else
            response = "Заявка не удалена. Проверьте параметры!";
        return response;
    }

    @GetMapping("/closingDay")
    public String closing() throws IOException, JSONException {
        return workWithDeal.countTransactionsByCurrency(dealRepository);
    }

    @GetMapping("/report")
    public List<Deal> reportForThePeriod(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException, ParseException {
        List<Deal> report = workWithDeal.report(dealRepository, reguestMap);
        return report;
    }
}
