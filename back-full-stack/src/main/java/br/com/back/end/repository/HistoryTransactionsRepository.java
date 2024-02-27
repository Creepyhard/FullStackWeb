package br.com.back.end.repository;

import br.com.back.end.model.transactions.HistoryTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface HistoryTransactionsRepository extends JpaRepository <HistoryTransactions, Long> {

    @Query("SELECT h FROM HistoryTransactions h WHERE h.dateTransfer = :day AND h.status = 1")
    public List<HistoryTransactions> findTodayPendingTransfer(@Param("day")Date day);
}
