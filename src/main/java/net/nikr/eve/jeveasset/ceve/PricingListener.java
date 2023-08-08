package net.nikr.eve.jeveasset.ceve;


/**
 * @author Leojan
 * @date 2023-08-07 16:56
 */

public interface PricingListener {
    void priceUpdated(int var1, Pricing var2);

    void priceUpdateFailed(int var1, Pricing var2);
}
