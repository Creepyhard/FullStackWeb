package br.com.back.end.service;

import br.com.back.end.DTO.mapper.UserMapper;
import br.com.back.end.exception.ResourceNotFoundException;
import br.com.back.end.model.User;
import br.com.back.end.model.transactions.HistoryTransactions;
import br.com.back.end.model.transactions.StatusTransaction;
import br.com.back.end.repository.HistoryTransactionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HistoryTransactionsService {

    private final HistoryTransactionsRepository historyTransactionsRepository;
    
    private final UserService userService;

    private final UserMapper userMapper;
    public HistoryTransactionsService(HistoryTransactionsRepository historyTransactionsRepository, UserService userService, UserMapper userMapper) {
        this.historyTransactionsRepository = historyTransactionsRepository;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public List<HistoryTransactions> getAllHistoryTransactions() {
        return historyTransactionsRepository.findAll();
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


    public ResponseEntity<HistoryTransactions> updateHistoryTransactionsService(Long id, StatusTransaction status, HistoryTransactions htDetails) {
        HistoryTransactions history = historyTransactionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such history :" + id + " Confirm the data entered!!"));

        BigDecimal value = htDetails.getValue();
        User userOrigin = new User();
        User userDestination = new User();
        userOrigin = userMapper.convertAccountDTOToUser(userService.findIdService(htDetails.getIdUserCO().getId()));
        userDestination = userMapper.convertAccountDTOToUser(userService.findIdService(htDetails.getIdUserCD().getId()));
        if (status.equals(StatusTransaction.PROCESSED)) {
            history.setStatus(StatusTransaction.PROCESSED);
            userDestination.setBalance(userDestination.getBalance().add(value));
            userOrigin.setBlockedBalance(userOrigin.getBlockedBalance().subtract(value));
            //User updateUserOrigin = userService.addUserService(userOrigin);
            //User updateUserDestination = userService.addUserService(userDestination);
            ResponseEntity.ok(userOrigin);
            ResponseEntity.ok(userDestination);
        } else if (status.equals(StatusTransaction.CANCELLED)) {
            history.setStatus(StatusTransaction.CANCELLED);
            userOrigin.setBalance(userOrigin.getBalance().add(value));
            userOrigin.setBlockedBalance(userOrigin.getBlockedBalance().subtract(value));
            //User updateUser = userService.addUserService(userOrigin);
            ResponseEntity.ok(userOrigin);
        }
        HistoryTransactions updateHistory = historyTransactionsRepository.save(history);
        return ResponseEntity.ok(updateHistory);
    }

}
