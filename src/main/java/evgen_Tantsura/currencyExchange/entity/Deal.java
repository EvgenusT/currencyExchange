package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ApiModel(value = "Заявка пользователя")
public class Deal {
    @Id
    @GeneratedValue
    private int id;
    @ApiModelProperty(value = "телефон клиента")
    private String tel;
    @ApiModelProperty(notes = "Статус заявки", example = "Новая, Выполнена, Отменена")
    private String status;
    @Column(scale = 4, precision = 19)
    @ApiModelProperty(value = "Сумма сделки")
    private BigDecimal sumDeal;
    @ApiModelProperty(notes = "Валюта сделки", example = "USD, EUR, RUR, BTC")
    private String currency;
    @Column(scale = 4, precision = 19)
    @ApiModelProperty(notes = "Сумма сделки в грн.")
    private BigDecimal sumDealInBaseCyy;
    @ApiModelProperty(notes = "Сумма дохода от данной сделки")
    private BigDecimal income;
    @ApiModelProperty(notes = "Дата сделки")
    private LocalDateTime dateDeal;
    @ApiModelProperty(notes = "Тип опрерации", example = "buy, sale")
    private String typeOfOperation;
    @ApiModelProperty(notes = "Пароль подтверждения сделки, 6 значный", example = "123456")
    private String otpPass;

    public Deal(String tel, String status, BigDecimal sumDeal, String currency, BigDecimal sumDealInBaseCyy,
                BigDecimal income, LocalDateTime dateDeal, String typeOfOperation, String otpPass) {
        this.tel = tel;
        this.status = status;
        this.sumDeal = sumDeal;
        this.currency = currency;
        this.sumDealInBaseCyy = sumDealInBaseCyy;
        this.income = income;
        this.dateDeal = dateDeal;
        this.typeOfOperation = typeOfOperation;
        this.otpPass = otpPass;
    }

    public Deal(int id, String tel, String status, BigDecimal sumDeal, String currency,
                BigDecimal sumDealInBaseCyy, BigDecimal income, LocalDateTime dateDeal, String typeOfOperation, String otpPass) {
        this.id = id;
        this.tel = tel;
        this.status = status;
        this.sumDeal = sumDeal;
        this.currency = currency;
        this.sumDealInBaseCyy = sumDealInBaseCyy;
        this.income = income;
        this.dateDeal = dateDeal;
        this.typeOfOperation = typeOfOperation;
        this.otpPass = otpPass;
    }
}
