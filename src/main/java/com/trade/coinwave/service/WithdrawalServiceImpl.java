package com.trade.coinwave.service;

import com.trade.coinwave.domain.WithdrawalStatus;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.Withdrawal;
import com.trade.coinwave.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private final UserService userService;

    public WithdrawalServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Withdrawal requestWithdrawal(BigDecimal amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal processWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if (withdrawal.isEmpty()) {
            throw new Exception("Withdrawal not found");
        }
        Withdrawal withdrawalFromDb = withdrawal.get();
        withdrawalFromDb.setDate(withdrawal.get().getDate());
        if (accept) {
            withdrawalFromDb.setWithdrawalStatus(WithdrawalStatus.APPROVED);
        } else {
            withdrawalFromDb.setWithdrawalStatus(WithdrawalStatus.REJECTED);
        }
        return withdrawalRepository.save(withdrawalFromDb);

    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawals(User user) {
        return withdrawalRepository.findAll();
    }
}
