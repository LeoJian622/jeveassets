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

public class Fuzzwork extends AbstractPricing {

    private static final Logger LOG = LoggerFactory.getLogger(Fuzzwork.class);

    public Fuzzwork() {
        super(2);
    }

    @Override
    public PricingFetchCN getPricingFetchImplementation() {
        return PricingFetchCN.FUZZWORK;
    }

    protected int getBatchSize() {
        return 1000;
    }

    @Override
    public List<PriceType> getSupportedPricingTypes() {
        List<PriceType> types = new ArrayList();
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
        List<LocationType> types = new ArrayList();
        types.add(LocationType.REGION);
        types.add(LocationType.STATION);
        return types;
    }

    public List<Long> getSupportedLocations(LocationType locationType) {
        if (this.getSupportedLocationTypes().contains(locationType)) {
            if (locationType != LocationType.STATION) {
                return new ArrayList();
            }

            List<Long> list = new ArrayList();
            list.add(60003760L);
            list.add(60008494L);
            list.add(60011866L);
            list.add(60004588L);
            list.add(60005686L);
        }

        return null;
    }

    protected Map<Integer, PriceContainer> fetchPrices(Collection<Integer> typeIDs) {
        Map<Integer, PriceContainer> returnMap = new HashMap();
        if (typeIDs.isEmpty()) {
            return returnMap;
        } else if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A location is required for Fuzzwork");
        } else {
            LocationType locationType = this.getPricingOptions().getLocationType();
            if (!this.getSupportedLocationTypes().contains(locationType)) {
                throw new UnsupportedOperationException(locationType + " is not supported by Fuzzwork");
            } else {
                if (locationType == LocationType.STATION) {
                    long locationID = this.getPricingOptions().getLocationID();
                    if (locationID != 60003760L && locationID != 60008494L && locationID != 60011866L && locationID != 60004588L && locationID != 60005686L) {
                        throw new UnsupportedOperationException(locationID + " is not supported by Fuzzwork");
                    }
                }

                try {
                    Map<Integer, FuzzworkPrice> results = (Map)this.getGSON().fromJson(this.getCall(typeIDs).execute().body().string(), (new TypeToken<Map<Integer, FuzzworkPrice>>() {
                    }).getType());
                    if (results == null) {
                        LOG.error("Error fetching price", new Exception("results is null"));
                        this.addFailureReasons(typeIDs, "results is null");
                        return returnMap;
                    }

                    Iterator var5 = results.entrySet().iterator();

                    while(var5.hasNext()) {
                        Map.Entry<Integer, FuzzworkPrice> entry = (Map.Entry)var5.next();
                        returnMap.put(entry.getKey(), ((FuzzworkPrice)entry.getValue()).getPriceContainer());
                    }
                } catch (IOException | JsonParseException | IllegalArgumentException var7) {
                    LOG.error("Error fetching price", var7);
                    this.addFailureReasons(typeIDs, var7.getMessage());
                }

                return returnMap;
            }
        }
    }

    public Call getCall(Collection<Integer> typeIDs) {
        Request.Builder request = (new Request.Builder()).url(this.getURL(typeIDs)).addHeader("User-Agent", this.getPricingOptions().getUserAgent());
        Iterator var3 = this.getPricingOptions().getHeaders().entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var3.next();
            request.addHeader((String)entry.getKey(), (String)entry.getValue());
        }

        return this.getClient().newCall(request.build());
    }

    protected String getURL(Collection<Integer> itemIDs) {
        StringBuilder query = new StringBuilder();
        LocationType locationType = this.getPricingOptions().getLocationType();
        if (locationType == LocationType.STATION) {
            long locationID = this.getPricingOptions().getLocationID();
            if (locationID != 60003760L && locationID != 60008494L && locationID != 60011866L && locationID != 60004588L && locationID != 60005686L) {
                throw new UnsupportedOperationException(locationID + " is not supported by Fuzzwork");
            }

            query.append("station=");
            query.append(this.getPricingOptions().getLocationID());
        } else {
            if (locationType != LocationType.REGION) {
                throw new UnsupportedOperationException(locationType.name() + " is not supported by Fuzzwork");
            }

            query.append("region=");
            query.append(this.getPricingOptions().getLocationID());
        }

        query.append("&types=");
        boolean comma = false;

        for(Iterator var5 = itemIDs.iterator(); var5.hasNext(); comma = true) {
            Integer i = (Integer)var5.next();
            if (comma) {
                query.append(',');
            }

            query.append(i);
        }

        return "https://market.fuzzwork.co.uk/aggregates/?" + query.toString();
    }

    private static class FuzzworkPriceData {
        public double weightedAverage;
        public double max;
        public double min;
        public double stddev;
        public double median;
        public double volume;
        public double orderCount;
        public double percentile;

        private FuzzworkPriceData() {
        }
    }

    private static class FuzzworkPrice {
        public FuzzworkPriceData buy;
        public FuzzworkPriceData sell;

        private FuzzworkPrice() {
        }

        PriceContainer getPriceContainer() {
            PriceContainer.PriceContainerBuilder builder = new PriceContainer.PriceContainerBuilder();
            builder.putPrice(PriceType.BUY_HIGH, this.buy.max);
            builder.putPrice(PriceType.BUY_LOW, this.buy.min);
            builder.putPrice(PriceType.BUY_MEAN, this.buy.weightedAverage);
            builder.putPrice(PriceType.BUY_MEDIAN, this.buy.median);
            builder.putPrice(PriceType.BUY_PERCENTILE, this.buy.percentile);
            builder.putPrice(PriceType.SELL_HIGH, this.sell.max);
            builder.putPrice(PriceType.SELL_LOW, this.sell.min);
            builder.putPrice(PriceType.SELL_MEAN, this.sell.weightedAverage);
            builder.putPrice(PriceType.SELL_MEDIAN, this.sell.median);
            builder.putPrice(PriceType.SELL_PERCENTILE, this.sell.percentile);
            return builder.build();
        }
    }
}
