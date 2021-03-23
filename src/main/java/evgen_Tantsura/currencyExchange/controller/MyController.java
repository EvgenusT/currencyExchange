package evgen_Tantsura.currencyExchange.controller;

import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import evgen_Tantsura.currencyExchange.utils.ReceivingCourses;
import evgen_Tantsura.currencyExchange.utils.WorkWithApplication;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("currencyExchange")
public class MyController {

    ReceivingCourses receivingCourses = new ReceivingCourses();
    WorkWithApplication workWithApplication = new WorkWithApplication();

    @Autowired
    ExchangeRatesRepository exchangeRatesRepository;

    @Autowired
    DealRepository dealRepository;

    @GetMapping("/startWork")
    public String requestCurrencyExchange() throws IOException, JSONException {
        receivingCourses.getCurrencyRates(exchangeRatesRepository, receivingCourses.processingJSON());
        return receivingCourses.getTextCurrencyRates(exchangeRatesRepository);
    }

    @GetMapping("/application")
    public String requestCurrencyPurchase(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        return workWithApplication.savingTheApplication(dealRepository, exchangeRatesRepository, reguestMap);
    }

    @GetMapping("/confirmation")
    public String responseCurrencyPurchase(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        String otpPassClient = reguestMap.get("otpPass");
        String tel = reguestMap.get("tel");
        Map<String, String> stringObjectMap = dealRepository.checkStatus(tel, otpPassClient);

        if (!stringObjectMap.isEmpty()) {
            workWithApplication.updateTheApplication(dealRepository, reguestMap);
        }
        return "Покупка успешно выполнена";
    }

}
