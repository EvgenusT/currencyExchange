package evgen_Tantsura.currencyExchange.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@ApiModel(value = "Json запрос для получения отчета за период")
@NoArgsConstructor
public class ReportDeal {

    @Id
    @GeneratedValue
    private int id;
    @ApiModelProperty(notes = "Валюта сделки", example = "USD, EUR, RUR, BTC")
    private String currency;
    @ApiModelProperty(notes = "Дата начала периода")
    private LocalDateTime beginning;
    @ApiModelProperty(notes = "Дата окончания периода")
    private LocalDateTime end;
}
