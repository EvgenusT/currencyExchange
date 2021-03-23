package evgen_Tantsura.currencyExchange.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

public class Deal {
    @Id
    @GeneratedValue
    private int id;
    private String tel;
    private String status;
    @Column(scale = 4, precision = 19)
    private BigDecimal sumDeal;
    private String currency;
    @Column(scale = 4, precision = 19)
    private BigDecimal sumDealInUah;
    private LocalDateTime dateDeal;
    private String otpPass;

    public Deal(String tel, String status, BigDecimal sumDeal, String currency, BigDecimal sumDealInUah,
                LocalDateTime dateDeal, String otpPass) {
        this.tel = tel;
        this.status = status;
        this.sumDeal = sumDeal;
        this.currency = currency;
        this.sumDealInUah = sumDealInUah;
        this.dateDeal = dateDeal;
        this.otpPass = otpPass;
    }
}
