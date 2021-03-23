package evgen_Tantsura.currencyExchange.repository;

import evgen_Tantsura.currencyExchange.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface DealRepository extends JpaRepository<Deal, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Deal SET status = ?1 WHERE TEL = ?2 AND OTP_PASS = ?3", nativeQuery = true)
    public void updateStatus(String status, String tel, String otpPass);

    @Query(value = "SELECT TEL, OTP_PASS FROM Deal WHERE TEL = ?1 AND OTP_PASS = ?2", nativeQuery = true)
    public Map<String, String> checkStatus(String tel, String otpPass);

}
