package tasks.transferservice.service;

import static java.lang.String.format;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.utils.FlowControlUtils.findFirstMatch;
import static tasks.transferservice.utils.FlowControlUtils.findFirstNotEqualTo;
import static tasks.transferservice.utils.ComporatorUtils.maxOf;

import java.util.Objects;
import java.util.function.BiPredicate;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.AccountNotFoundException;
import tasks.transferservice.domain.exception.InsufficientFundsException;
import tasks.transferservice.domain.exception.InvalidTransferException;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.repository.AccountRepository;

@Service
public class DefaultTransferService implements TransferService {

    private static final BiPredicate<String, Account> ACCOUNT_NUMBER_MATCHES = (accountNumber, account) -> account.hasAccountNumberValueOf(accountNumber);

    private static Logger LOG = LoggerFactory.getLogger(DefaultTransferService.class);

    private AccountRepository accountRepository;

    @Inject
    public DefaultTransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void transfer(ExecuteTransferRequest transferRequest) {
        LOG.info("Processing transfer request {}", transferRequest);

        String accountNumberA = maxOf(transferRequest.getCreditAccount(), transferRequest.getDebitAccount());
        String accountNumberB = findFirstNotEqualTo(accountNumberA,
            transferRequest.getCreditAccount(),
            transferRequest.getDebitAccount());

        Account accountA = null;
        Account accountB = null;
        try {
            accountA = accountRepository.lockForUpdate(anAccountNumber(accountNumberA));
            accountB = accountRepository.lockForUpdate(anAccountNumber(accountNumberB));

            Account creditAccount = findFirstMatch(transferRequest.getCreditAccount(),
                ACCOUNT_NUMBER_MATCHES, accountA, accountB);
            Account debitAccount = findFirstMatch(transferRequest.getDebitAccount(),
                ACCOUNT_NUMBER_MATCHES, accountA, accountB);

            doTransfer(creditAccount, debitAccount, transferRequest);

            accountRepository.update(creditAccount);
            accountRepository.update(debitAccount);
        } catch (InsufficientFundsException e) {
            LOG.info("Debit account does not have enough funds to complete transfer {}", transferRequest, e);
            throw new InvalidTransferException(transferRequest, "Debit account does not have enough funds to complete transfer");
        } catch (AccountNotFoundException e) {
            LOG.info("Can not execute transfer {}: {}", transferRequest, e.getMessage());
            throw new InvalidTransferException(transferRequest, format("Can not execute transfer %s. %s", transferRequest.getTransferRequestId(), e.getMessage()));
        } finally {
            accountRepository.unlock(accountB);
            accountRepository.unlock(accountA);
        }
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
