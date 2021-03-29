package evgen_Tantsura.currencyExchange.controller;

import com.google.gson.Gson;
import evgen_Tantsura.currencyExchange.entity.Deal;
import evgen_Tantsura.currencyExchange.repository.DealRepository;
import evgen_Tantsura.currencyExchange.utils.CONST;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MyController myController;

    @Autowired
    DealRepository dealRepository;

    @Test
    public void shouldControllerOk() throws Exception {
        assertThat(myController).isNotNull();
    }

    @Test
    public void shouldOkRequestCurrencyExchange() throws Exception {
        this.mockMvc.perform(get("/currencyExchange/openingDay"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldOkRequestCurrencyDeal() throws Exception {
        myController.requestCurrencyExchange();
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("currency", "USD");
        map.put("sum", "2400");
        map.put("tel", "0504520387");
        map.put("typeOfOperation", "sale");
        String json = gson.toJson(map);

        this.mockMvc.perform(get("/currencyExchange/request").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldOkResponseCurrencyDeal() throws Exception {
        Deal deal = new Deal("0504520366", CONST.NEW, new BigDecimal(100.0000),
                "USD", new BigDecimal(10000.00000), new BigDecimal(10.00000),
                LocalDateTime.now(), "buy", "111111");
        dealRepository.save(deal);

        Gson gson = new Gson();
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("tel", "0504520366");
        mapForTest.put("otpPass", "111111");
        String json = gson.toJson(mapForTest);

        this.mockMvc.perform(get("/currencyExchange/response").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сделка успешна")));
    }

    @Test
    void shouldNotOkResponseCurrencyDeal() throws Exception {
        Deal deal = new Deal("0504520366", CONST.NEW, new BigDecimal(100.0000),
                "USD", new BigDecimal(10000.00000), new BigDecimal(10.00000),
                LocalDateTime.now(), "buy", "111111");
        dealRepository.save(deal);

        Gson gson = new Gson();
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("tel", "0504520366");
        mapForTest.put("otpPass", "111110");
        String json = gson.toJson(mapForTest);

        this.mockMvc.perform(get("/currencyExchange/response").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OTP пароль не верный, сделка отменена")));
    }


    @Test
    void shouldOkDeleteDeal() throws Exception {
        Deal deal = new Deal("0504520367", CONST.NEW, new BigDecimal(100.0000),
                "USD", new BigDecimal(10000.00000), new BigDecimal(10.00000),
                LocalDateTime.now(), "buy", "222222");
        dealRepository.save(deal);

        Gson gson = new Gson();
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("id", "3");
        mapForTest.put("tel", "0504520367");
        String json = gson.toJson(mapForTest);

        this.mockMvc.perform(get("/currencyExchange/delete").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Заявка №: 3 - удалена")));
    }

    @Test
    void shouldNotOkDeleteDeal() throws Exception {
        Deal deal = new Deal("0504520366", CONST.NEW, new BigDecimal(100.0000),
                "USD", new BigDecimal(10000.00000), new BigDecimal(10.00000),
                LocalDateTime.now(), "buy", "111111");
        dealRepository.save(deal);

        Gson gson = new Gson();
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("id", "1");
        mapForTest.put("tel", "0504520365");
        String json = gson.toJson(mapForTest);

        this.mockMvc.perform(get("/currencyExchange/delete").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Заявка не удалена. Проверьте параметры!")));

    }

    @Test
    void shouldOkClosing() throws Exception {
        this.mockMvc.perform(get("/currencyExchange/closingDay"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReportForThePeriod() throws Exception {
        Deal deal = new Deal("0504520366", CONST.NEW, new BigDecimal(100.0000),
                "USD", new BigDecimal(10000.00000), new BigDecimal(10.00000),
                LocalDateTime.now(), "buy", "111111");
        dealRepository.save(deal);

        Gson gson = new Gson();
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("currency", "USD");
        mapForTest.put("beginning", "2021-03-24 00:00:00");
        mapForTest.put("end", "2021-03-30 23:59:59");
        String json = gson.toJson(mapForTest);

        this.mockMvc.perform(get("/currencyExchange/report").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }
}