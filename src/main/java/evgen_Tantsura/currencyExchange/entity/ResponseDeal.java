package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@ApiModel(value = "Json запрос для подтверждения заявки пользователя")
@NoArgsConstructor
public class ResponseDeal {


    @ApiModelProperty(value = "телефон клиента")
    @NotEmpty(message = "поле должно быть заполнено")
    @Size(min = 10, max = 10, message = "введите правильно номер телефона")
    private String tel;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "OTP пароль")
    @NotEmpty(message = "поле должно быть заполнено")
    @Size(min = 6, max = 6, message = "введите правильно OTP пароль")
    private String otpPass;


}
