package evgen_Tantsura.currencyExchange.repository;

import evgen_Tantsura.currencyExchange.entity.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ExchangeRatesRepository extends JpaRepository<ExchangeRates, Integer> {

    @Query(value = "SELECT MY_BUY FROM exchange_Rates WHERE id = " +
            "(SELECT max(id) FROM exchange_Rates WHERE CCY = ?1 AND date_And_Time >= CURDATE())", nativeQuery = true)
    public String getTheCurrencyPurchaseRate(String currency);

    @Query(value = "SELECT MY_SALE FROM exchange_Rates WHERE id = " +
            "(SELECT max(id) FROM exchange_Rates WHERE CCY = ?1 AND date_And_Time >= CURDATE())", nativeQuery = true)
    public String getTheCurrencySaleRate(String currency);

    @Query(value = "SELECT * FROM exchange_Rates WHERE id = " +
            "(SELECT max(id) FROM exchange_Rates WHERE CCY = ?1 AND date_And_Time >= CURDATE())", nativeQuery = true)
    public Map<String, Object> getTheRateByCurrency(String currency);
}