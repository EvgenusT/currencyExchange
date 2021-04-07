package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@ApiModel(value = "Json запрос для формирования заявки пользователя")
@NoArgsConstructor

public class RequestDeal {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty(notes = "Валюта сделки", example = "USD, EUR, RUR, BTC")
    private String currency;
    @ApiModelProperty(value = "Сумма сделки")
    private String sum;
    @ApiModelProperty(value = "телефон клиента")
    private String tel;
    @ApiModelProperty(notes = "Тип опрерации", example = "buy, sale")
    private String typeOfOperation;

    public RequestDeal(String currency, String sum, String tel, String typeOfOperation) {
        this.currency = currency;
        this.sum = sum;
        this.tel = tel;
        this.typeOfOperation = typeOfOperation;
    }
}
