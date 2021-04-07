package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@ApiModel(value = "Json запрос для подтверждения заявки пользователя")
@NoArgsConstructor
public class ResponseDeal {


    @ApiModelProperty(value = "телефон клиента")
    private String tel;
    @Id
    @ApiModelProperty(value = "OTP пароль")
    private String otpPass;


}
