package com.ledgerlib.core;

public enum EntrySide {
    DEBIT, CREDIT;

    public int sigNum() {
        if (this == CREDIT)
            return 1;

        return -1;
    }

}
