package br.com.back.end.model.transactions;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tblTaxTransfer")
public class TaxTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private int homeDay;

    @Column
    private int finalDay;

    @Column
    private BigDecimal fixValue;

    @Column
    private BigDecimal ratePercentage;
}
