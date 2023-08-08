package net.nikr.eve.jeveasset.ceve;

import uk.me.candle.eve.pricing.options.PricingOptions;
import uk.me.candle.eve.pricing.options.impl.DefaultPricingOptions;

/**
 * @author Leojan
 * @date 2023-08-07 16:39
 */

public class PricingFactoryCN {
    public PricingFactoryCN() {
    }

    public static Pricing getPricing(PricingFetchCN pricingFetch) {
        return getPricing(pricingFetch, new DefaultPricingOptions());
    }

    public static Pricing getPricing(PricingFetchCN pricingFetch, PricingOptions options) {
        return pricingFetch.getPricing(options);
    }
}
