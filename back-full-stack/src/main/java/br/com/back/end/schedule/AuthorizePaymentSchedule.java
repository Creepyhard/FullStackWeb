package br.com.back.end.schedule;

import br.com.back.end.model.transactions.HistoryTransactions;
import br.com.back.end.model.transactions.StatusTransaction;
import br.com.back.end.service.HistoryTransactionsService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class AuthorizePaymentSchedule {

    private final HistoryTransactionsService htService;

    public AuthorizePaymentSchedule(HistoryTransactionsService htService) {
        this.htService = htService;
    }

    @Scheduled(cron = "0 0 6 * * *")
     public void executePaymentSchedule() {
        Date date = new Date(new java.util.Date().getTime());
        List<HistoryTransactions> historyTransactions =  htService.getAllTodayPendingTransfer(date);
        for(HistoryTransactions ht : historyTransactions) {
            htService.updateHistoryTransactionsService("Scheduled", StatusTransaction.PROCESSED,ht);
        }
        System.out.println(historyTransactions +  " " + date);
    }
}