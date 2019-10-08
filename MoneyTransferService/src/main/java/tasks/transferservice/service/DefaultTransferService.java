package tasks.transferservice.service;

import static java.lang.String.format;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.amountOf;

import java.util.Objects;
import java.util.function.Function;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.InsufficientFundsException;
import tasks.transferservice.domain.exception.InvalidTransferException;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.repository.AccountRepository;

@Service
public class DefaultTransferService implements TransferService {

    private static Logger LOG = LoggerFactory.getLogger(DefaultTransferService.class);

    @Inject
    private AccountRepository accountRepository;

    @Override
    public void transfer(ExecuteTransferRequest transferRequest) {
        LOG.info("Processing transfer request {}", transferRequest);

        Account creditAccount = doGetAccountByAccountNumber(transferRequest, ExecuteTransferRequest::getCreditAccount);
        Account debitAccount = doGetAccountByAccountNumber(transferRequest, ExecuteTransferRequest::getDebitAccount);

        if (!Objects.equals(creditAccount.getAccountCurrency(), debitAccount.getAccountCurrency())) {
            LOG.info("Accounts {} and {} have different currencies: {} and {}",
                creditAccount.getAccountNumber(), debitAccount.getAccountNumber(), creditAccount.getAccountCurrency(), debitAccount.getAccountCurrency());
            throw new InvalidTransferException(transferRequest, "Counterparty accounts have different currencies");
        }

        try {
            creditAccount.credit(amountOf(transferRequest.getTransferAmount()));
            debitAccount.debit(amountOf(transferRequest.getTransferAmount()));

            accountRepository.update(creditAccount);
            accountRepository.update(debitAccount);
        } catch (InsufficientFundsException e) {
            LOG.info("Debit account does not have enough funds to complete transfer {}", transferRequest, e);
            throw new InvalidTransferException(transferRequest, "Debit account does not have enough funds to complete transfer");
        }

    }

    private Account doGetAccountByAccountNumber(ExecuteTransferRequest transferRequest, Function<ExecuteTransferRequest, String> extractAccountNumber) {
        String accountNumber = extractAccountNumber.apply(transferRequest);
        Account account = accountRepository.findByAccountNumber(anAccountNumber(accountNumber));
        if (account == null) {
            LOG.info("Account {} does not exist. Can not execute transfer {}", accountNumber, transferRequest);
            throw new InvalidTransferException(transferRequest, format("Account '%s' does not exist", accountNumber));
        }
        return account;
    }
}
