package com.xulio.gal;

import java.util.concurrent.atomic.AtomicInteger;

public class Funds {
    private final AtomicInteger totalFunds = new AtomicInteger(0);

    public void addFunds (int amount) {
        if (amount > 0) {
            totalFunds.addAndGet(amount);
        }
    }

    public int getTotalFunds () {
        return totalFunds.get();
    }
}
