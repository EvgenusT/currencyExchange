package evgen_Tantsura.currencyExchange.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "exchange_Rates")
@Data
public class ExchangeRates {

    @Id
    @GeneratedValue
    private int id;
    private String ccy;
    private String base_ccy;
    private String buy;
    private String sale;
    private LocalDateTime dateAndTime;
    @Column(scale = 4, precision = 19)
    private BigDecimal myBuy;
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
