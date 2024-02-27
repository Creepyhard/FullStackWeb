package br.com.back.end.model.transactions;

public enum StatusTransaction {

    PROCESSING(0), SCHEDULE(1), PROCESSED(2), DENIED(3), CANCELLED(4), ADVANCEDPAYMENT(5);

    private final int status;

    StatusTransaction(int status) {
        this.status = status;
    }
}
