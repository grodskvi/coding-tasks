package tasks.transferservice.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.repository.AccountRepository;
import tasks.transferservice.repository.InMemoryAccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.AccountOperationType.CREDIT;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.common.Currency.EUR;

public class DefaultTransferServiceIntegrationTest {

    private static final int THREAD_POOL_SIZE = 20;

    private static ExecutorService executorService;
    private static Random random = new Random();

    private DefaultTransferService transferService;
    private AccountRepository accountRepository;


    @BeforeClass
    public static void beforeClass() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @AfterClass
    public static void afterClass() {
        executorService.shutdown();
    }

    @Before
    public void setUp() {
        accountRepository = new InMemoryAccountRepository();
        transferService = new DefaultTransferService(accountRepository);
    }

    @Test
    public void ensuresConsistencyOfTransfersBetweenTwoAccounts() {
        Account accountA = Account.anAccount("account_a", EUR);
        Account accountB = Account.anAccount("account_b", EUR);
        Amount initialAmount = amountOf("1000000");

        accountA.credit(initialAmount);
        accountB.credit(initialAmount);
        accountRepository.save(accountA);
        accountRepository.save(accountB);

        List<ExecuteTransferRequest> requests = IntStream.range(0, 10000)
                .mapToObj(i -> createTransferRequest(accountA, accountB))
                .collect(Collectors.toList());

        List<Future<Void>> transferFutures = submitTasks(
                requests,
                request -> () -> {
                    transferService.transfer(request);
                    return null;
                });

        transferFutures.forEach((future) -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Account updatedAccountA = accountRepository.findByAccountNumber(anAccountNumber("account_a"));
        Account updatedAccountB = accountRepository.findByAccountNumber(anAccountNumber("account_b"));

        Amount totalBalance = updatedAccountA.getBalance()
                .increaseBy(updatedAccountB.getBalance());
        Amount expectedBalance = initialAmount.increaseBy(initialAmount);

        assertThat(totalBalance)
                .isEqualTo(expectedBalance);

        assertThat(sumAccountOperations(updatedAccountA))
                .isEqualTo(updatedAccountA.getBalance().getValue());
        assertThat(sumAccountOperations(updatedAccountB)).
                isEqualTo(updatedAccountB.getBalance().getValue());
    }

    private BigDecimal sumAccountOperations(Account account) {
        return account.getAccountStatement().stream()
                .map(operation -> {
                    BigDecimal amountValue = operation.getAmount().getValue();
                    return operation.getOperationType() == CREDIT ? amountValue : amountValue.negate();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private <T, R> List<Future<R>> submitTasks(List<T> items, Function<T, Callable<R>> task) {
        return items.stream()
                .map(item -> executorService.submit(task.apply(item)))
                .collect(toList());
    }

    private static ExecuteTransferRequest createTransferRequest(Account accountA, Account accountB) {
        Account creditAccount = random.nextBoolean() ? accountA : accountB;
        Account debitAccount = creditAccount == accountA ? accountB : accountA;

        ExecuteTransferRequest request = new ExecuteTransferRequest();
        request.setCreditAccount(creditAccount.getAccountNumber().getValue());
        request.setDebitAccount(debitAccount.getAccountNumber().getValue());

        BigDecimal transferAmount = BigDecimal.valueOf((int) (random.nextDouble() * 10000))
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_CEILING);
        request.setTransferAmount(transferAmount);

        return request;
    }

}