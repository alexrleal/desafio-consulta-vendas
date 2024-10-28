package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.projection.SaleReportProjection;
import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    @Transactional(readOnly = true)
    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<SaleReportDTO> getReport(String min, String max, String name, Pageable pageable) {
        LocalDate defaultDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDate dateMax = parseOrDefaultDate(max, defaultDate);
        LocalDate dateMin = parseOrDefaultDate(min, dateMax.minusYears(1L));
        if (name == null || name.isEmpty()) name = "";
        Page<SaleReportProjection> projections = repository.getReport(dateMin, dateMax, name, pageable);
        return projections.map(SaleReportDTO::new);
    }

    @Transactional(readOnly = true)
    public List<SaleSummaryDTO> getSummary(String min, String max, Pageable pageable) {
        LocalDate defaultDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDate dateMax = parseOrDefaultDate(max, defaultDate);
        LocalDate dateMin = parseOrDefaultDate(min, dateMax.minusYears(1L));
        List<SaleSummaryProjection> projections = repository.getSummary(dateMin, dateMax);
        return projections.stream().map(SaleSummaryDTO::new).collect(Collectors.toList());
    }

    private LocalDate parseOrDefaultDate(String dateStr, LocalDate defaultDate) {
        try {
            return (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : defaultDate;
        } catch (DateTimeParseException e) {
            return defaultDate;
        }
    }

}
