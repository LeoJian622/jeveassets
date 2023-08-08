package net.nikr.eve.jeveasset.ceve;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.nikr.eve.jeveasset.ceve.impl.EveMarketer;
import net.nikr.eve.jeveasset.ceve.impl.Fuzzwork;
import okhttp3.Call;
import okhttp3.Request;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.candle.eve.pricing.PriceContainer;
import uk.me.candle.eve.pricing.options.LocationType;
import uk.me.candle.eve.pricing.options.PriceType;

import java.io.IOException;
import java.util.*;

/**
 * @author Leojan
 * @date 2023-08-07 11:09
 */

public class CeveMarket extends AbstractPricing {

    private static final Logger LOG = LoggerFactory.getLogger(CeveMarket.class);

    public CeveMarket() {
        super(2);
    }

    @Override
    protected Map<Integer, PriceContainer> fetchPrices(Collection<Integer> typeIDs) {
        Map<Integer, PriceContainer> returnMap = new HashMap();
        if (typeIDs.isEmpty()) {
            return returnMap;
        } else if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A system is required for ceve Market");
        } else {
            LocationType locationType = this.getPricingOptions().getLocationType();
            if (!this.getSupportedLocationTypes().contains(locationType)) {
                throw new UnsupportedOperationException(locationType + " is not supported by ceve Market");
            } else {
                try {

                    String xmlString = this.getCall(typeIDs).execute().body().string();
                    String jsonString = XML.toJSONObject(xmlString).getJSONObject("evec_api").getJSONObject("marketstat").getJSONArray("type").toString();
                    List<CeveMarketPrice> results = this.getGSON().fromJson(jsonString, (new TypeToken<List<CeveMarketPrice>>() {
                    }).getType());
                    if (results == null) {
                        LOG.error("Error fetching price", new Exception("results is null"));
                        this.addFailureReasons(typeIDs, "results is null");
                        return returnMap;
                    }

                    Iterator var5 = results.iterator();

                    while(var5.hasNext()) {
                        CeveMarketPrice item = (CeveMarketPrice) var5.next();
                        returnMap.put(item.getTypeID(), item.getPriceContainer());
                    }

                    if (typeIDs.size() != returnMap.size()) {
                        List<Integer> errors = new ArrayList(typeIDs);
                        errors.removeAll(returnMap.keySet());
                        PriceContainer container = (new PriceContainer.PriceContainerBuilder()).build();
                        Iterator var7 = errors.iterator();

                        while(var7.hasNext()) {
                            Integer typeID = (Integer)var7.next();
                            returnMap.put(typeID, container);
                        }
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

        while (var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var3.next();
            request.addHeader((String) entry.getKey(), (String) entry.getValue());
        }
        return this.getClient().newCall(request.build());
    }

    protected String getURL(Collection<Integer> itemIDs) {
        StringBuilder query = new StringBuilder();

        query.append("&typeid=");
        boolean comma = false;

        Integer i;
        for (Iterator iterator = itemIDs.iterator(); iterator.hasNext(); query.append(i)) {
            i = (Integer) iterator.next();
            if (comma) {
                query.append("&typeid=");
            } else {
                comma = true;
            }

        }

        LocationType locationType = this.getPricingOptions().getLocationType();
        if (locationType == LocationType.STATION) {
            throw new UnsupportedOperationException(locationType + " is not supported by EveMarketer");
        } else {
            if (locationType == LocationType.SYSTEM) {
                query.append("&usesystem=");
                query.append(this.getPricingOptions().getLocationID());
            } else {
                if (locationType != LocationType.REGION) {
                    throw new UnsupportedOperationException(locationType.name() + " is not supported by EveMarketer");
                }

                query.append("&regionlimit=");
                query.append(this.getPricingOptions().getLocationID());
            }
            return "https://www.ceve-market.org/api/marketstat?" + query.toString();
        }
    }

    @Override
    protected int getBatchSize() {
        return 200;
    }


    @Override
    public PricingFetchCN getPricingFetchImplementation() {
        return PricingFetchCN.CEVE_MARKET;
    }

    @Override
    public List<PriceType> getSupportedPricingTypes() {
        List<PriceType> types = new ArrayList();
        types.add(PriceType.BUY_MEDIAN);
        types.add(PriceType.BUY_PERCENTILE);
        types.add(PriceType.BUY_HIGH);
        types.add(PriceType.BUY_LOW);
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
        types.add(LocationType.SYSTEM);
        return types;
    }

    @Override
    public List<Long> getSupportedLocations(LocationType locationType) {
        return this.getSupportedLocationTypes().contains(locationType) ? new ArrayList() : null;
    }

    private static class CeveMarketPriceData {
        public double max;
        public double min;
        public double stddev;
        public double median;
        public double volume;
        public double percentile;

        private CeveMarketPriceData() {
        }
    }


    private static class CeveMarketPrice {
         CeveMarketPriceData buy;
         CeveMarketPriceData sell;
         Integer id;

        private CeveMarketPrice() {
        }

        public int getTypeID() {
            return id;
        }

        PriceContainer getPriceContainer() {
            PriceContainer.PriceContainerBuilder builder = new PriceContainer.PriceContainerBuilder();
            builder.putPrice(PriceType.BUY_HIGH, this.buy.max);
            builder.putPrice(PriceType.BUY_LOW, this.buy.min);
            builder.putPrice(PriceType.BUY_MEAN, this.buy.stddev);
            builder.putPrice(PriceType.BUY_MEDIAN, this.buy.median);
            builder.putPrice(PriceType.BUY_PERCENTILE, this.buy.percentile);
            builder.putPrice(PriceType.SELL_HIGH, this.sell.max);
            builder.putPrice(PriceType.SELL_LOW, this.sell.min);
            builder.putPrice(PriceType.SELL_MEAN, this.sell.stddev);
            builder.putPrice(PriceType.SELL_MEDIAN, this.sell.median);
            builder.putPrice(PriceType.SELL_PERCENTILE, this.sell.percentile);
            return builder.build();
        }
    }
}
