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

    @Override
    protected int getBatchSize() {
        return 1000;
    }

    @Override
    public List<PriceType> getSupportedPricingTypes() {
        ArrayList<PriceType> types = new ArrayList<>();
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
        ArrayList<LocationType> types = new ArrayList<>();
        types.add(LocationType.REGION);
        types.add(LocationType.STATION);
        return types;
    }

    @Override
    public List<Long> getSupportedLocations(LocationType locationType) {
        if (this.getSupportedLocationTypes().contains(locationType)) {
            if (locationType == LocationType.STATION) {
                ArrayList<Long> list = new ArrayList<>();
                list.add(60003760L);
                list.add(60008494L);
                list.add(60011866L);
                list.add(60004588L);
                list.add(60005686L);
            } else {
                return new ArrayList<>();
            }
        }
        return null;
    }

    @Override
    protected Map<Integer, PriceContainer> fetchPrices(Collection<Integer> typeIDs) {
        long locationID;
        HashMap<Integer, PriceContainer> returnMap = new HashMap<>();
        if (typeIDs.isEmpty()) {
            return returnMap;
        }
        if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A location is required for Fuzzwork");
        }
        LocationType locationType = this.getPricingOptions().getLocationType();
        if (!this.getSupportedLocationTypes().contains(locationType)) {
            throw new UnsupportedOperationException(locationType + " is not supported by Fuzzwork");
        }
        if (locationType == LocationType.STATION && (locationID = this.getPricingOptions().getLocationID()) != 60003760L && locationID != 60008494L && locationID != 60011866L && locationID != 60004588L && locationID != 60005686L) {
            throw new UnsupportedOperationException(locationID + " is not supported by Fuzzwork");
        }
        try {
            Map<Integer,FuzzworkPrice> results = this.getGSON().fromJson(this.getCall(typeIDs).execute().body().string(), new TypeToken<Map<Integer, FuzzworkPrice>>(){}.getType());
            if (results == null) {
                LOG.error("Error fetching price", new Exception("results is null"));
                this.addFailureReasons(typeIDs, "results is null");
                return returnMap;
            }
            for (Map.Entry<Integer,FuzzworkPrice> entry : results.entrySet()) {
                returnMap.put(entry.getKey(), entry.getValue().getPriceContainer());
            }
        }
        catch (JsonParseException | IOException | IllegalArgumentException ex) {
            LOG.error("Error fetching price", ex);
            this.addFailureReasons(typeIDs, ex.getMessage());
        }
        return returnMap;
    }

    public Call getCall(Collection<Integer> typeIDs) {
        Request.Builder request = new Request.Builder().url(this.getURL(typeIDs)).addHeader("User-Agent", this.getPricingOptions().getUserAgent());
        for (Map.Entry<String, String> entry : this.getPricingOptions().getHeaders().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
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
        } else if (locationType == LocationType.REGION) {
            query.append("region=");
            query.append(this.getPricingOptions().getLocationID());
        } else {
            throw new UnsupportedOperationException(locationType.name() + " is not supported by Fuzzwork");
        }
        query.append("&types=");
        boolean comma = false;
        for (Integer i : itemIDs) {
            if (comma) {
                query.append(',');
            }
            query.append(i);
            comma = true;
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

