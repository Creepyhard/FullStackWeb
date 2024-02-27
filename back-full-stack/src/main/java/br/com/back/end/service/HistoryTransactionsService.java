package br.com.back.end.service;

import br.com.back.end.DTO.mapper.UserMapper;
import br.com.back.end.exception.ResourceNotFoundException;
import br.com.back.end.model.User;
import br.com.back.end.model.transactions.HistoryTransactions;
import br.com.back.end.model.transactions.StatusTransaction;
import br.com.back.end.repository.HistoryTransactionsRepository;
import br.com.back.end.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class HistoryTransactionsService {

    private final HistoryTransactionsRepository historyTransactionsRepository;
    
    private final UserRepository userRepository;

    public HistoryTransactionsService(HistoryTransactionsRepository historyTransactionsRepository, UserRepository userRepository, UserMapper userMapper) {
        this.historyTransactionsRepository = historyTransactionsRepository;
        this.userRepository = userRepository;
    }

    public List<HistoryTransactions> getAllHistoryTransactions() {
        return historyTransactionsRepository.findAll();
    }

    public List<HistoryTransactions> getAllTodayPendingTransfer(Date today) {
        return historyTransactionsRepository.findTodayPendingTransfer(today);
    }

    public HistoryTransactions addHistoryTransactionsService(@RequestBody HistoryTransactions historyTransactions) {
        return historyTransactionsRepository.save(historyTransactions);
    }

    public ResponseEntity<HistoryTransactions> findIdService(@PathVariable long id) {
        return historyTransactionsRepository.findById(id).map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
    }

    public ResponseEntity<Object> deleteHistoryTransactionsService(@PathVariable long id) {
        return historyTransactionsRepository.findById(id).map(record -> {
            historyTransactionsRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
    }


    public ResponseEntity<HistoryTransactions> updateHistoryTransactionsService(String origin, StatusTransaction status, HistoryTransactions htDetails) {
        HistoryTransactions history = historyTransactionsRepository.findById(htDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("There is no such history :" + htDetails.getId() + " Confirm the data entered!!"));
        User userOrigin = userRepository.findById(history.getIdUserCO().getId())
                .orElseThrow(() -> new ResourceNotFoundException("This idCO does not exist!"));;
        User userDestination = userRepository.findById(history.getIdUserCD().getId())
                .orElseThrow(() -> new ResourceNotFoundException("This idCD does not exist!"));;
        BigDecimal value = history.getValue();

        if (status.equals(StatusTransaction.PROCESSED) && origin.equals("Scheduled")) {
            history.setStatus(StatusTransaction.PROCESSED);
            userDestination.setBalance(userDestination.getBalance().add(value));
            userOrigin.setBlockedBalance(userOrigin.getBlockedBalance().subtract(value));
            userRepository.save(userOrigin);
            userRepository.save(userDestination);
        } else if (status.equals(StatusTransaction.ADVANCEDPAYMENT)) {
            history.setStatus(StatusTransaction.ADVANCEDPAYMENT);
            userDestination.setBalance(userDestination.getBalance().add(value));
            userOrigin.setBlockedBalance(userOrigin.getBlockedBalance().subtract(value));
            userRepository.save(userOrigin);
            userRepository.save(userDestination);
        } else if (status.equals(StatusTransaction.CANCELLED)) {
            history.setStatus(StatusTransaction.CANCELLED);
            userOrigin.setBalance(userOrigin.getBalance().add(value));
            userOrigin.setBlockedBalance(userOrigin.getBlockedBalance().subtract(value));
            userRepository.save(userOrigin);
        }
        HistoryTransactions updateHistory = historyTransactionsRepository.save(history);
        return ResponseEntity.ok(updateHistory);
    }

}
