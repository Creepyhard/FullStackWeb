package br.com.back.end.model.transactions;

import br.com.back.end.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "tblHistoryTransactions")
@Data
public class HistoryTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "idUserCO", referencedColumnName = "id", nullable = false)
    private User idUserCO;

    @ManyToOne
    @JoinColumn(name = "idUserCD", referencedColumnName = "id", nullable = false)
    private User idUserCD;

    @Column
    private BigDecimal value;

    @Column
    private BigDecimal tax;

    @Column
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dateTransfer;

    @Column
    private Timestamp dateSchedule;

    @Column
    private StatusTransaction status;

    public Timestamp getDateSchedule() {
        Long datetime = System.currentTimeMillis();
        return new Timestamp(datetime);
    }
}
