package evgen_Tantsura.currencyExchange.controller;

import evgen_Tantsura.currencyExchange.entity.*;
import evgen_Tantsura.currencyExchange.service.ReceivingCourses;
import evgen_Tantsura.currencyExchange.service.WorkWithDeal;
import evgen_Tantsura.currencyExchange.utils.CONST;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("currencyExchange")
public class CurrencyExchangeControllerImpl implements CurrencyExchangeController {

    @Autowired
    ReceivingCourses receivingCourses;

    @Autowired
    WorkWithDeal workWithDeal;

    @GetMapping("/openingDay")
    public String requestCurrencyExchange() throws IOException, JSONException {
        receivingCourses.getCurrencyRates(receivingCourses.processingJSON());
        return receivingCourses.getTextCurrencyRates();
    }

    @PostMapping("/request")
    public String requestCurrencyDeal(@Valid @RequestBody RequestDeal newRequestDeal) throws IOException, JSONException {
        return workWithDeal.saveTheDeal(newRequestDeal);
    }

    @PostMapping("/response")
    public String responseCurrencyDeal(@RequestBody @Valid ResponseDeal newResponseDeal) throws IOException, JSONException {
        Map<String, String> checkStatusMap = workWithDeal.checkStatusDeal(newResponseDeal);
        String resultCheckOTP = null;
        if (!checkStatusMap.isEmpty()) {
            workWithDeal.updateTheDeal(newResponseDeal);
            resultCheckOTP = CONST.ORDER_OK;
        } else {
            workWithDeal.cancellationTheDeal(newResponseDeal);
            resultCheckOTP = CONST.ORDER_NOT_OK;
        }
        return resultCheckOTP;
    }

    @PostMapping("/delete")
    public String deleteDeal(@RequestBody @Valid DeleteDeal newDeleteDeal) throws IOException, JSONException {

        String response = null;

        if (workWithDeal.checkForRemove(newDeleteDeal)) {
            workWithDeal.removeTheDeal(newDeleteDeal);
            response = "Заявка №: " + newDeleteDeal.getId() + " - удалена";
        } else
            response = "Заявка не удалена. Проверьте параметры!";
        return response;
    }

    @GetMapping("/closingDay")
    public String closing() throws IOException, JSONException {
        return workWithDeal.generatingASalesReport();
    }

    @PostMapping("/report")
    public List<Deal> reportForThePeriod(@RequestBody @Valid ReportDeal newReportDeal) throws IOException, JSONException, ParseException {

        List<Deal> report = workWithDeal.report(newReportDeal);
        return report;
    }
}
