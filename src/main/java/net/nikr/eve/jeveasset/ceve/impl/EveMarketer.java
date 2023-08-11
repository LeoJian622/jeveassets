package net.nikr.eve.jeveasset.ceve.impl;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.nikr.eve.jeveasset.ceve.AbstractPricing;
import net.nikr.eve.jeveasset.ceve.PricingFetchCN;
import okhttp3.Call;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.candle.eve.pricing.PriceContainer;
import uk.me.candle.eve.pricing.options.LocationType;
import uk.me.candle.eve.pricing.options.PriceType;

import java.io.IOException;
import java.util.*;

/**
 * @author Leojan
 * @date 2023-08-07 17:06
 */

public class EveMarketer extends AbstractPricing {

    private static final Logger LOG = LoggerFactory.getLogger(EveMarketer.class);

    public EveMarketer() {
        super(2);
    }

    @Override
    public PricingFetchCN getPricingFetchImplementation() {
        return PricingFetchCN.EVEMARKETER;
    }

    @Override
    public List<PriceType> getSupportedPricingTypes() {
        List<PriceType> types = new ArrayList<>();
        types.add(PriceType.BUY_MEAN);
        types.add(PriceType.BUY_MEDIAN);
        types.add(PriceType.BUY_PERCENTILE);
        types.add(PriceType.BUY_HIGH);
        types.add(PriceType.BUY_LOW);
        types.add(PriceType.SELL_MEAN);
        types.add(PriceType.SELL_MEDIAN);
        types.add(PriceType.SELL_PERCENTILE);
        types.add(PriceType.SELL_HIGH);
        types.add(PriceType.SELL_LOW);
        return types;
    }

    @Override
    public List<LocationType> getSupportedLocationTypes() {
        List<LocationType> types = new ArrayList<>();
        types.add(LocationType.REGION);
        types.add(LocationType.SYSTEM);
        return types;
    }

    @Override
    public List<Long> getSupportedLocations(LocationType locationType) {
        if (this.getSupportedLocationTypes().contains(locationType)) {
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    protected int getBatchSize() {
        return 200;
    }

    @Override
    protected Map<Integer, PriceContainer> fetchPrices(Collection<Integer> typeIDs) {
        HashMap<Integer, PriceContainer> returnMap = new HashMap<Integer, PriceContainer>();
        if (typeIDs.isEmpty()) {
            return returnMap;
        }
        if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A location is required for EveMarketer");
        }
            LocationType locationType = this.getPricingOptions().getLocationType();
            if (!this.getSupportedLocationTypes().contains(locationType)) {
                throw new UnsupportedOperationException(locationType + " is not supported by EveMarketer");
            }
                try {
                    List<Type> results = this.getGSON().fromJson(this.getCall(typeIDs).execute().body().string(), (new TypeToken<List<Type>>() {
                    }).getType());
                    if (results == null) {
                        LOG.error("Error fetching price", new Exception("results is null"));
                        this.addFailureReasons(typeIDs, "results is null");
                        return returnMap;
                    }
                    for (Type item : results) {
                        returnMap.put(item.getTypeID(), item.getPriceContainer());
                    }
                    if (typeIDs.size() != returnMap.size()) {
                        List<Integer> errors = new ArrayList<>(typeIDs);
                        errors.removeAll(returnMap.keySet());
                        PriceContainer container = (new PriceContainer.PriceContainerBuilder()).build();
                        for (Integer typeID : errors) {
                            returnMap.put(typeID, container);
                        }
                    }
                } catch (IOException | JsonParseException | IllegalArgumentException ex) {
                    LOG.error("Error fetching price", ex);
                    this.addFailureReasons(typeIDs, ex.getMessage());
                }
                return returnMap;
        }

    public Call getCall(Collection<Integer> typeIDs) {
        Request.Builder request = (new Request.Builder()).url(this.getURL(typeIDs)).addHeader("User-Agent", this.getPricingOptions().getUserAgent());
        for (Map.Entry<String, String> entry : this.getPricingOptions().getHeaders().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }
        return this.getClient().newCall(request.build());
    }

    protected String getURL(Collection<Integer> typeIDs) {
        StringBuilder query = new StringBuilder();
        query.append("&typeid=");
        boolean comma = false;
        for (Integer i : typeIDs) {
            if (comma) {
                query.append(',');
            } else {
                comma = true;
            }
            query.append(i);
        }
        LocationType locationType = this.getPricingOptions().getLocationType();
        if (locationType == LocationType.STATION) {
            throw new UnsupportedOperationException(locationType + " is not supported by EveMarketer");
        }
            if (locationType == LocationType.SYSTEM) {
                query.append("&usesystem=");
                query.append(this.getPricingOptions().getLocationID());
            } else if (locationType == LocationType.REGION) {
                query.append("&regionlimit=");
                query.append(this.getPricingOptions().getLocationID());
            } else {
                throw new UnsupportedOperationException(locationType.name() + " is not supported by EveMarketer");
            }
            return "https://api.evemarketer.com/ec/marketstat/json?" + query;
    }

    private static class ForQuery {
        boolean bid;
        List<Integer> types = new ArrayList<Integer>();
        List<Integer> regions = new ArrayList<Integer>();
        List<Integer> systems = new ArrayList<Integer>();
        Integer hours;
        Integer minq;

        private ForQuery() {
        }
    }

    private static class TypeStat {
        ForQuery forQuery;
        double max;
        double median;
        long generated;
        double variance;
        double min;
        double avg;
        double stdDev;
        double fivePercent;
        boolean highToLow;
        long volume;
        double wavg;

        private TypeStat() {
        }
    }

    private static class Type {
        TypeStat buy;
        TypeStat sell;

        private Type() {
        }

        public int getTypeID() {
            return this.buy.forQuery.types.get(0);
        }

        public PriceContainer getPriceContainer() {
            PriceContainer.PriceContainerBuilder builder = new PriceContainer.PriceContainerBuilder();
            builder.putPrice(PriceType.BUY_MEAN, this.buy.avg);
            builder.putPrice(PriceType.BUY_MEDIAN, this.buy.median);
            builder.putPrice(PriceType.BUY_PERCENTILE, this.buy.fivePercent);
            builder.putPrice(PriceType.BUY_HIGH, this.buy.max);
            builder.putPrice(PriceType.BUY_LOW, this.buy.min);
            builder.putPrice(PriceType.SELL_MEAN, this.sell.avg);
            builder.putPrice(PriceType.SELL_MEDIAN, this.sell.median);
            builder.putPrice(PriceType.SELL_PERCENTILE, this.sell.fivePercent);
            builder.putPrice(PriceType.SELL_HIGH, this.sell.max);
            builder.putPrice(PriceType.SELL_LOW, this.sell.min);
            return builder.build();
        }
    }
}
