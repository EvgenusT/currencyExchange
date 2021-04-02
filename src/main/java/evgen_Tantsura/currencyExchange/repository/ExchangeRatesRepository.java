package evgen_Tantsura.currencyExchange.repository;

import evgen_Tantsura.currencyExchange.entity.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ExchangeRatesRepository extends JpaRepository<ExchangeRates, Integer> {

    @Query(value = "SELECT * FROM exchange_Rates WHERE ID = " +
            "(SELECT max(ID) FROM exchange_Rates WHERE CCY = ?1 AND date_And_Time >= CURDATE())", nativeQuery = true)
    ExchangeRates getTheCurrencyRate(String currency);

    @Query(value = "SELECT * FROM exchange_Rates WHERE ID = " +
            "(SELECT max(ID) FROM exchange_Rates WHERE CCY = ?1 AND date_And_Time >= CURDATE())", nativeQuery = true)
    Map<String, Object> getTheRateByCurrency(String currency);
}
