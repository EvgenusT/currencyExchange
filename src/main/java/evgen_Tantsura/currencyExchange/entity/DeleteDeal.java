package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@ApiModel(value = "Json запрос для удаления сделки (в статусе - Новая)")
@NoArgsConstructor
public class DeleteDeal {

    @Id
    private int id;
    @ApiModelProperty(value = "телефон клиента")
    private String tel;

}
