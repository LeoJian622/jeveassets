package net.nikr.eve.jeveasset.ceve;


import uk.me.candle.eve.pricing.PricingFetchListener;
import uk.me.candle.eve.pricing.options.LocationType;
import uk.me.candle.eve.pricing.options.PriceType;
import uk.me.candle.eve.pricing.options.PricingOptions;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Leojan
 * @date 2023-08-07 16:47
 */

public interface Pricing {
    void updatePrices(Set<Integer> var1);

    Double getPriceCache(int var1, PriceType var2);

    Double getPrice(int var1, PriceType var2);

    boolean removePricingListener(PricingListener var1);

    boolean addPricingListener(PricingListener var1);

    boolean removePricingFetchListener(PricingFetchListener var1);

    boolean addPricingFetchListener(PricingFetchListener var1);

    void clearPrice(int var1);

    PricingFetchCN getPricingFetchImplementation();

    void setPricingOptions(PricingOptions var1);

    PricingOptions getPricingOptions();

    long getNextUpdateTime(int var1);

    void writeCache() throws IOException;

    void cancelSingle(int var1);

    void cancelAll();

    void resetAllAttemptCounters();

    void resetAttemptCounter(int var1);

    int getFailedAttempts(int var1);

    List<String> getFetchErrors(int var1);

    List<PriceType> getSupportedPricingTypes();

    List<LocationType> getSupportedLocationTypes();

    List<Long> getSupportedLocations(LocationType var1);
}
