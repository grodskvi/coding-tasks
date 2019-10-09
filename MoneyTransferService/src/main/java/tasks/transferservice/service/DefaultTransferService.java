package tasks.transferservice.service;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.InsufficientFundsException;
import tasks.transferservice.domain.exception.InvalidTransferException;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.repository.AccountRepository;

import javax.inject.Inject;
import java.util.Objects;

import static java.lang.String.format;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.amountOf;

@Service
public class DefaultTransferService implements TransferService {

    private static Logger LOG = LoggerFactory.getLogger(DefaultTransferService.class);

    private AccountRepository accountRepository;

    @Inject
    public DefaultTransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void transfer(ExecuteTransferRequest transferRequest) {
        LOG.info("Processing transfer request {}", transferRequest);

        String accountNumberA = greaterOf(transferRequest.getCreditAccount(), transferRequest.getDebitAccount());
        String accountNumberB = accountNumberA.equals(transferRequest.getCreditAccount()) ?
                transferRequest.getDebitAccount() :
                transferRequest.getCreditAccount();

        Account accountA = null;
        Account accountB = null;
        try {
            accountA = accountRepository.lockForUpdate(anAccountNumber(accountNumberA));
            checkAccountNotNull(accountA, accountNumberA, transferRequest);

            accountB = accountRepository.lockForUpdate(anAccountNumber(accountNumberB));
            checkAccountNotNull(accountB, accountNumberB, transferRequest);

            boolean isAccountACredit = accountA.hasAccountNumberValueOf(transferRequest.getCreditAccount());
            Account creditAccount = isAccountACredit ? accountA : accountB;
            Account debitAccount = isAccountACredit ? accountB : accountA;

            doTransfer(creditAccount, debitAccount, transferRequest);

            accountRepository.update(creditAccount);
            accountRepository.update(debitAccount);
        } catch (InsufficientFundsException e) {
            LOG.info("Debit account does not have enough funds to complete transfer {}", transferRequest, e);
            throw new InvalidTransferException(transferRequest, "Debit account does not have enough funds to complete transfer");
        } finally {
            accountRepository.unlock(accountB);
            accountRepository.unlock(accountA);
        }
    }

    private void checkAccountNotNull(Account account, String accountNumber, ExecuteTransferRequest transferRequest) {
        if (account == null) {
            LOG.info("Account {} does not exist. Can not execute transfer {}", accountNumber, transferRequest);
            throw new InvalidTransferException(transferRequest, format("Account '%s' does not exist", accountNumber));
        }
    }

    private <T extends Comparable<T>> T greaterOf(T left, T right) {
        return left.compareTo(right) > 0 ? left : right;
    }

    private void doTransfer(Account creditAccount, Account debitAccount, ExecuteTransferRequest transferRequest) throws InsufficientFundsException {
        if (!Objects.equals(creditAccount.getAccountCurrency(), debitAccount.getAccountCurrency())) {
            LOG.info("Accounts {} and {} have different currencies: {} and {}",
                    creditAccount.getAccountNumber(), debitAccount.getAccountNumber(), creditAccount.getAccountCurrency(), debitAccount.getAccountCurrency());
            throw new InvalidTransferException(transferRequest, "Counterparty accounts have different currencies");
        }

        creditAccount.credit(amountOf(transferRequest.getTransferAmount()));
        debitAccount.debit(amountOf(transferRequest.getTransferAmount()));
    }
}
