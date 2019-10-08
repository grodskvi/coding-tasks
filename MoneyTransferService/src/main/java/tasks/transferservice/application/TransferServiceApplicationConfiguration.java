package tasks.transferservice.application;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import tasks.transferservice.repository.AccountRepository;
import tasks.transferservice.repository.InMemoryAccountRepository;
import tasks.transferservice.service.AccountService;
import tasks.transferservice.service.DefaultAccountService;
import tasks.transferservice.validation.CreateAccountRequestValidator;
import tasks.transferservice.validation.DepositRequestValidator;

public class TransferServiceApplicationConfiguration extends AbstractBinder {

    @Override
    protected void configure() {
        bindAsContract(CreateAccountRequestValidator.class);
        bindAsContract(DepositRequestValidator.class);

        bind(DefaultAccountService.class).to(AccountService.class).in(Singleton.class);
        bind(InMemoryAccountRepository.class).to(AccountRepository.class).in(Singleton.class);
    }
}
