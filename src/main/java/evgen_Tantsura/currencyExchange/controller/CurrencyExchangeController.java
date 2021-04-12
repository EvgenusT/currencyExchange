package evgen_Tantsura.currencyExchange.controller;

import evgen_Tantsura.currencyExchange.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Api(value = "CurrencyExchangeController", description = "Приложение, имитирующее работу пункта по продаже валюты")
public interface CurrencyExchangeController {

    @ApiOperation(value = "Открытие рабочего дня")
    public String requestCurrencyExchange() throws IOException, JSONException;

    @ApiOperation(value = "Заявка на продажу/покупку валюты от пользователя")
    public String requestCurrencyDeal(@RequestBody @Valid RequestDeal requestDeal) throws IOException, JSONException;

    @ApiOperation(value = "Подтверждение заявки пользователем")
    public String responseCurrencyDeal(@RequestBody @Valid  ResponseDeal newResponseDeal) throws IOException, JSONException;

    @ApiOperation(value = "Удаление заявки.")
    public String deleteDeal(@RequestBody @Valid  DeleteDeal newDeleteDeal) throws IOException, JSONException;

    @ApiOperation(value = "Закрытие рабочего дня, формирование отчетов")
    public String closing() throws IOException, JSONException;

    @ApiOperation(value = "Формирование ответов за необходимый период")
    public List<Deal> reportForThePeriod(@RequestBody @Valid  ReportDeal newReportDeal) throws IOException, JSONException, ParseException;
}
