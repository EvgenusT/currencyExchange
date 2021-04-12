package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@ApiModel(value = "Json запрос для удаления сделки (в статусе - Новая)")
@NoArgsConstructor
public class DeleteDeal {

    @Id
    private int id;
    @NotEmpty(message = "поле должно быть заполнено")
    @Size(min = 10, max = 10, message = "введите правильно номер телефона")
    @ApiModelProperty(value = "телефон клиента")
    private String tel;

}
