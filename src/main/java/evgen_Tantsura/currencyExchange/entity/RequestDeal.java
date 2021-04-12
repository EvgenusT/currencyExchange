package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@ApiModel(value = "Json запрос для формирования заявки пользователя")
@NoArgsConstructor

public class RequestDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty(notes = "Валюта сделки", example = "USD, EUR, RUR, BTC")
    @NotEmpty(message = "поле должно быть заполнено")
    private String currency;
    @ApiModelProperty(value = "Сумма сделки")
    @NotEmpty(message = "поле должно быть заполнено")
    @Min(value = 0, message = "сумма не может быть равна 0")
    private String sum;
    @ApiModelProperty(value = "телефон клиента")
    @NotEmpty(message = "поле должно быть заполнено")
    @NotNull
    @Size(min = 10, max = 10, message = "введите правильно номер телефона")
    private String tel;
    @ApiModelProperty(notes = "Тип опрерации", example = "buy, sale")
    @NotEmpty(message = "поле должно быть заполнено")
    private String typeOfOperation;

    public RequestDeal(String currency, String sum, String tel, String typeOfOperation) {
        this.currency = currency;
        this.sum = sum;
        this.tel = tel;
        this.typeOfOperation = typeOfOperation;
    }
}
