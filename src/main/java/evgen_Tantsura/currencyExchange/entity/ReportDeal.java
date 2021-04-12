package evgen_Tantsura.currencyExchange.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@ApiModel(value = "Json запрос для получения отчета за период")
@NoArgsConstructor
public class ReportDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty(notes = "Валюта сделки", example = "USD, EUR, RUR, BTC")
    @NotEmpty(message = "поле должно быть заполнено")
    private String currency;
    @ApiModelProperty(notes = "Дата начала периода")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "поле должно быть заполнено")
    private LocalDateTime beginning;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(notes = "Дата окончания периода")
    @NotNull(message = "поле должно быть заполнено")
    private LocalDateTime end;

    public ReportDeal(String currency, LocalDateTime beginning, LocalDateTime end) {
        this.currency = currency;
        this.beginning = beginning;
        this.end = end;
    }

}
