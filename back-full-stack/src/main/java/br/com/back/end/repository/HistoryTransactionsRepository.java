package br.com.back.end.repository;

import br.com.back.end.model.transactions.HistoryTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryTransactionsRepository extends JpaRepository <HistoryTransactions, Long> {
}
