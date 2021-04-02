package evgen_Tantsura.currencyExchange.repository;

import evgen_Tantsura.currencyExchange.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DealRepository extends JpaRepository<Deal, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Deal SET status = ?1 WHERE TEL = ?2 AND OTP_PASS = ?3", nativeQuery = true)
    void updateStatus(String status, String tel, String otpPass);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Deal SET status = ?1 WHERE TEL = ?2 AND STATUS = 'Новая'", nativeQuery = true)
    void cancellationStatus(String status, String tel);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Deal WHERE ID = ?1 AND TEL = ?2 AND STATUS = 'Новая'", nativeQuery = true)
    void remove(String id, String tel);

    @Query(value = "SELECT TEL, OTP_PASS FROM Deal WHERE TEL = ?1 AND OTP_PASS = ?2", nativeQuery = true)
    Map<String, String> checkStatus(String tel, String otpPass);

    @Query(value = "SELECT ID, TEL FROM Deal WHERE ID = ?1 AND TEL = ?2 AND STATUS = 'Новая'", nativeQuery = true)
    Map<String, String> checkBooleanDeal(String id, String tel);

    @Query(value = "SELECT COUNT(*) FROM Deal WHERE CURRENCY = ?1 AND STATUS = 'Выполнена' AND DATE_DEAL >= CURDATE() " +
            "AND TYPE_OF_OPERATION  = ?2", nativeQuery = true)
    int countDeals(String currency, String type);

    @Query(value = "SELECT SUM(SUM_DEAL) FROM Deal WHERE CURRENCY = ?1 AND STATUS = 'Выполнена' AND DATE_DEAL >= CURDATE() " +
            "AND TYPE_OF_OPERATION  = ?2", nativeQuery = true)
    BigDecimal sumDeals(String currency, String type);

    @Query(value = "SELECT SUM(INCOME) FROM Deal WHERE CURRENCY = ?1 AND STATUS = 'Выполнена' AND DATE_DEAL >= CURDATE() " +
            "AND TYPE_OF_OPERATION  = ?2", nativeQuery = true)
    BigDecimal sumIncomForCurrency(String currency, String type);

    @Query(value = "SELECT * FROM Deal WHERE CURRENCY = ?1 AND DATE_DEAL >= ?2 AND DATE_DEAL <= ?3", nativeQuery = true)
    List<Deal> findAllByCurrencyAndPeriod(String currency, LocalDateTime beginning, LocalDateTime end);

    @Query(value = "SELECT * FROM Deal WHERE ID = ?1", nativeQuery = true)
    Map<String, String> findById(String id);

}
