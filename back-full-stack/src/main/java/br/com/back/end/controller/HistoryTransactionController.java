package br.com.back.end.controller;

import br.com.back.end.model.transactions.HistoryTransactions;
import br.com.back.end.service.HistoryTransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping(value = "/history")
public class HistoryTransactionController {

    private HistoryTransactionsService historyTransactionsService;

    public HistoryTransactionController(HistoryTransactionsService historyTransactionsService) {
        this.historyTransactionsService = historyTransactionsService;
    }

    @GetMapping
    public List<HistoryTransactions> listHistoryTransactions() {
        return historyTransactionsService.getAllHistoryTransactions();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<HistoryTransactions> findId(@PathVariable long id) {
        return historyTransactionsService.findIdService(id);
    }
    @PostMapping
    public HistoryTransactions addHistoryTransactions(@RequestBody HistoryTransactions ht) {
        return historyTransactionsService.addHistoryTransactionsService(ht);
    }

}
