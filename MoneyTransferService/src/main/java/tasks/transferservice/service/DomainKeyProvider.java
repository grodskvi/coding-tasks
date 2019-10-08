package tasks.transferservice.service;

import java.util.UUID;

import org.jvnet.hk2.annotations.Service;

@Service
public class DomainKeyProvider {

    public String nextDomainKey() {
        return UUID.randomUUID().toString();
    }
}
