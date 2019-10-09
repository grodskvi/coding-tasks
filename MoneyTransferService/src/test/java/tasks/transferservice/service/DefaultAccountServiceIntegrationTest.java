package tasks.transferservice.service;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static tasks.transferservice.domain.common.AccountOperation.anAccountOperation;
import static tasks.transferservice.domain.common.AccountOperationType.CREDIT;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.entity.Account.anAccount;
import static tasks.transferservice.service.FutureSuiteResult.futureSuite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import lombok.Getter;
import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.common.AccountOperation;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.DuplicateAccountException;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.repository.AccountRepository;
import tasks.transferservice.repository.InMemoryAccountRepository;

public class DefaultAccountServiceIntegrationTest {

    private static final int THREAD_POOL_SIZE = 20;

    private static ExecutorService executorService;
    private static Random random = new Random();

    private DefaultAccountService accountService;
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
        accountService = new DefaultAccountService(accountRepository);
    }

    @Test
    public void ensuresAccountIsCreatedOnce() throws InterruptedException {
        IntStream.range(0, 10).forEach(id ->
                executeAccountIsCreatedOnceTestCase(String.valueOf(id), THREAD_POOL_SIZE));
    }

    @Test
    public void ensuresAllDepositsSumsUp() {
        AccountNumber accountNumber = AccountNumber.anAccountNumber("1");
        accountRepository.save(anAccount(accountNumber.getValue(), EUR));

        int requestsCount = THREAD_POOL_SIZE;
        List<DepositRequest> requests = IntStream.range(0, requestsCount)
                .mapToObj(i -> new DepositRequest(UUID.randomUUID().toString(), BigDecimal.valueOf(random.nextDouble() * 100)))
                .collect(toList());

        List<Future<Void>> depositFutures = submitTasks(
                requests,
                request -> () -> {
                    accountService.deposit(accountNumber, request);
                    return null;
                });

        depositFutures.forEach((future) -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Account account = accountRepository.findByAccountNumber(accountNumber);
        BigDecimal totalDepositAmount = requests.stream()
                .map(DepositRequest::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<AccountOperation> expectedOperations = requests.stream()
                .map(req -> anAccountOperation(CREDIT, amountOf(req.getAmount()), EUR))
                .collect(toList());

        assertThat(account.getBalance()).isEqualTo(amountOf(totalDepositAmount));
        assertThat(account.getAccountStatement()).containsExactlyInAnyOrderElementsOf(expectedOperations);
    }

    private void executeAccountIsCreatedOnceTestCase(String accountNumber, int requestsCount) {
        List<CreateAccountRequest> requests = IntStream.range(0, requestsCount)
                .mapToObj(i -> new CreateAccountRequest(UUID.randomUUID().toString(), accountNumber, "EUR"))
                .collect(toList());

        List<Future<Account>> accountFutures = submitTasks(
                requests,
                request -> () -> accountService.createAccount(request));

        FutureSuiteResult<Account> results = futureSuite(accountFutures);
        assertThat(results.getItems()).hasSize(1);
        assertThat(results.getErrors())
                .hasSize(requestsCount - 1)
                .allMatch(e -> e.getClass().equals(DuplicateAccountException.class));
    }

    private <T, R> List<Future<R>> submitTasks(List<T> items, Function<T, Callable<R>> task) {
        return items.stream()
                .map(item -> executorService.submit(task.apply(item)))
                .collect(toList());
    }
}

@Getter
class FutureSuiteResult<T> {
   private List<T> items;
    private List<Throwable> errors;

    private FutureSuiteResult(List<T> items, List<Throwable> errors) {
        this.items = items;
        this.errors = errors;
    }

    static <T> FutureSuiteResult<T> futureSuite(Collection<Future<T>> futures) {
        List<T> items = new ArrayList<>();
        List<Throwable> errors = new ArrayList<>();
        for (Future<T> future: futures) {
            try {
                T item = future.get();
                items.add(item);
            } catch (ExecutionException e) {
                errors.add(e.getCause());
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while waiting for future result", e);
            }
        }
        return new FutureSuiteResult<>(items, errors);
    }
}