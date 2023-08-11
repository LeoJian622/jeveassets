package net.nikr.eve.jeveasset.ceve;

import net.nikr.eve.jeveasset.ceve.impl.*;
import uk.me.candle.eve.pricing.options.LocationType;
import uk.me.candle.eve.pricing.options.PriceType;
import uk.me.candle.eve.pricing.options.PricingOptions;

import java.util.List;

/**
 * @author Leojan
 * @date 2023-08-07 15:59
 */

public enum PricingFetchCN {
    EVEMARKETER {
        public Pricing getNewInstance() {
            return new EveMarketer();
        }
    },
    FUZZWORK {
        public Pricing getNewInstance() {
            return new Fuzzwork();
        }
    },
    EVE_TYCOON {
        public Pricing getNewInstance() {
            return new EveTycoon();
        }
    },
    JANICE {
        public Pricing getNewInstance() {
            return new Janice();
        }
    },
    CEVE_MARKET {
        public Pricing getNewInstance() {
            return new CeveMarket();
        }
    };

    private Pricing pricing;

    private PricingFetchCN() {
    }

    public Pricing getPricing(PricingOptions options) {
        if (this.pricing == null) {
            this.pricing = this.getNewInstance();
        } else {
            this.pricing.resetAllAttemptCounters();
        }

        if (options != null) {
            this.pricing.setPricingOptions(options);
        }

        return this.pricing;
    }

    protected abstract Pricing getNewInstance();

    public List<PriceType> getSupportedPricingTypes() {
        return this.getNewInstance().getSupportedPricingTypes();
    }

    public List<LocationType> getSupportedLocationTypes() {
        return this.getNewInstance().getSupportedLocationTypes();
    }
}
