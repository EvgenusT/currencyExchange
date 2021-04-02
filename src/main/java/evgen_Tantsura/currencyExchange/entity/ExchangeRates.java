package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "exchange_Rates")
@Data
@NoArgsConstructor
@ApiModel(value = "Курс валют")
public class ExchangeRates {

    @Id
    @GeneratedValue (strategy = GenerationType.TABLE)
    private int id;
    @ApiModelProperty(notes = "Валюта", example = "USD, EUR, RUR, BTC")
    private String ccy;
    @ApiModelProperty(notes = "Валюта конвертации", example = "UAH, USD")
    private String base_ccy;
    @ApiModelProperty(notes = "Покупка")
    private String buy;
    @ApiModelProperty(notes = "Продажа")
    private String sale;
    @ApiModelProperty(notes = "Дата и время получения курсов Приват Банка")
    private LocalDateTime dateAndTime;
    @Column(scale = 4, precision = 19)
    @ApiModelProperty(notes = "Курс покупки с учетом маржи")
    private BigDecimal myBuy;
    @ApiModelProperty(notes = "Курс продажи с учетом маржи")
    @Column(scale = 4, precision = 19)
    private BigDecimal mySale;

    public ExchangeRates(String ccy, String base_ccy, String buy, String sale, LocalDateTime dateAndTime, BigDecimal myBuy, BigDecimal mySale) {
        this.ccy = ccy;
        this.base_ccy = base_ccy;
        this.buy = buy;
        this.sale = sale;
        this.dateAndTime = dateAndTime;
        this.myBuy = myBuy;
        this.mySale = mySale;
    }
}
