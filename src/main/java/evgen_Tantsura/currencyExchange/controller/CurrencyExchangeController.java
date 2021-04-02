package evgen_Tantsura.currencyExchange.controller;

import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.repository.ExchangeRatesRepository;
import evgen_Tantsura.currencyExchange.service.ReceivingCourses;
import evgen_Tantsura.currencyExchange.service.WorkWithDeal;
import evgen_Tantsura.currencyExchange.utils.CONST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("currencyExchange")
@Api(value = "CurrencyExchangeController", description = "Приложение, имитирующее работу пункта по продаже валюты")
public class CurrencyExchangeController {

    ReceivingCourses receivingCourses = new ReceivingCourses();
    WorkWithDeal workWithDeal = new WorkWithDeal();

    @Autowired
    ExchangeRatesRepository exchangeRatesRepository;

    @Autowired
    DealRepository dealRepository;

    @GetMapping("/openingDay")
    @ApiOperation(value = "Открытие рабочего дня")
    public String requestCurrencyExchange() throws IOException, JSONException {
        receivingCourses.getCurrencyRates(exchangeRatesRepository, receivingCourses.processingJSON());
        return receivingCourses.getTextCurrencyRates(exchangeRatesRepository);
    }

    @PostMapping("/request")
    @ApiOperation(value = "Заявка на продажу/покупку валюты от пользователя")
    public String requestCurrencyDeal(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        return workWithDeal.saveTheDeal(dealRepository, exchangeRatesRepository, reguestMap);
    }

    @PostMapping("/response")
    @ApiOperation(value = "Подтверждение заявки пользователем")
    public String responseCurrencyDeal(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException {
        Map<String, String> checkStatusMap = dealRepository.checkStatus(reguestMap.get(CONST.TEL), reguestMap.get(CONST.OTP_PASS));
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

    @PostMapping("/delete")
    @ApiOperation(value = "Удаление заявки.")
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
    @ApiOperation(value = "Закрытие рабочего дня, формирование отчетов")
    public String closing() throws IOException, JSONException {
        return workWithDeal.generatingASalesReport(dealRepository);
    }

    @GetMapping("/report")
    @ApiOperation(value = "Формирование ответов за необходимый период")
    public List<Deal> reportForThePeriod(@RequestBody Map<String, String> reguestMap) throws IOException, JSONException, ParseException {
        List<Deal> report = workWithDeal.report(dealRepository, reguestMap);
        return report;
    }
}
