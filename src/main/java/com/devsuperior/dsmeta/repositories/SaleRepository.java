package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.projection.SaleReportProjection;
import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query(nativeQuery = true, value =
            "SELECT tb_sales.id,tb_sales.date,tb_sales.amount, tb_seller.name AS nameSeller " +
            "FROM tb_sales " +
            "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date >= :min AND tb_sales.date <= :max " +
            "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%',:name,'%'))",
            countQuery =
            "SELECT COUNT(*) " +
            "FROM tb_sales " +
            "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date >= :min AND tb_sales.date <= :max " +
            "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    Page<SaleReportProjection> getReport(LocalDate min, LocalDate max, String name, Pageable pageable);

    @Query(nativeQuery = true, value =
            "SELECT tb_seller.name as sellerName, SUM(tb_sales.amount )AS total " +
            "FROM tb_seller " +
            "INNER JOIN tb_sales ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date >= :minDate AND tb_sales.date <= :maxDate " +
            "GROUP BY sellerName")
    List<SaleSummaryProjection> getSummary(LocalDate minDate, LocalDate maxDate);
}
