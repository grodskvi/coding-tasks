package tasks.transferservice.service;

import org.jvnet.hk2.annotations.Contract;

import tasks.transferservice.domain.rest.ExecuteTransferRequest;

@Contract
public interface TransferService {
    void transfer(ExecuteTransferRequest transferRequest);
}
