package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projection.SaleReportProjection;

import java.time.LocalDate;

public class SaleReportDTO {
    private Long id;
    private LocalDate date;
    private Double amount;
    private String nameSeller;

    public SaleReportDTO(Long id, LocalDate date, Double amount, String nameSeller) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.nameSeller = nameSeller;
    }

    public SaleReportDTO(Sale sale) {
        id = sale.getId();
        date = sale.getDate();
        amount = sale.getAmount();
        nameSeller = sale.getSeller().getName();
    }

    public SaleReportDTO(SaleReportProjection sale) {
        id = sale.getId();
        date = sale.getDate();
        amount = sale.getAmount();
        nameSeller = sale.getNameSeller();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getNameSeller() {
        return nameSeller;
    }

}
