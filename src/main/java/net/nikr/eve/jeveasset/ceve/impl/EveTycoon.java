package net.nikr.eve.jeveasset.ceve.impl;

import com.google.gson.JsonParseException;
import net.nikr.eve.jeveasset.ceve.AbstractPricing;
import net.nikr.eve.jeveasset.ceve.PricingFetchCN;
import net.nikr.eve.jeveasset.io.online.Updater;
import okhttp3.Call;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.candle.eve.pricing.PriceContainer;
import uk.me.candle.eve.pricing.options.LocationType;
import uk.me.candle.eve.pricing.options.PriceType;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Leojan
 * @date 2023-08-07 17:07
 */

public class EveTycoon  extends AbstractPricing {
    private static final Logger LOG = LoggerFactory.getLogger(EveTycoon.class);
    private static final int STACK = 50;
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(50);

    public EveTycoon() {
        super(1);
    }

    @Override
    public PricingFetchCN getPricingFetchImplementation() {
        return PricingFetchCN.EVE_TYCOON;
    }

    @Override
    public List<PriceType> getSupportedPricingTypes() {
        List<PriceType> types = new ArrayList();
        types.add(PriceType.BUY_PERCENTILE);
        types.add(PriceType.BUY_HIGH);
        types.add(PriceType.SELL_PERCENTILE);
        types.add(PriceType.SELL_LOW);
        return types;
    }

    @Override
    public List<LocationType> getSupportedLocationTypes() {
        List<LocationType> types = new ArrayList();
        types.add(LocationType.REGION);
        types.add(LocationType.SYSTEM);
        types.add(LocationType.STATION);
        return types;
    }

    public List<Long> getSupportedLocations(LocationType locationType) {
        return this.getSupportedLocationTypes().contains(locationType) ? new ArrayList() : null;
    }

    protected int getBatchSize() {
        return 50;
    }

    protected Map<Integer, PriceContainer> fetchPrices(Collection<Integer> typeIDs) {
        Map<Integer, PriceContainer> returnMap = new HashMap();
        if (typeIDs.isEmpty()) {
            return returnMap;
        } else if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A location is required for EveTycoon");
        } else {
            LocationType locationType = this.getPricingOptions().getLocationType();
            if (!this.getSupportedLocationTypes().contains(locationType)) {
                throw new UnsupportedOperationException(locationType + " is not supported by EveTycoon");
            } else {
                Map<Integer, Future<String>> futures = new HashMap();
                Iterator var5 = typeIDs.iterator();

                while(var5.hasNext()) {
                    Integer typeID = (Integer)var5.next();
                    futures.put(typeID, THREAD_POOL.submit(new Updater(typeID)));
                }

                try {
                    int done = 0;

                    while(done < futures.size()) {
                        done = 0;
                        Iterator var16 = futures.entrySet().iterator();

                        while(var16.hasNext()) {
                            Map.Entry<Integer, Future<String>> entry = (Map.Entry)var16.next();
                            Future<String> future = (Future)entry.getValue();
                            Integer typeID = (Integer)entry.getKey();
                            if (future.isDone()) {
                                ++done;
                                if (!returnMap.containsKey(typeID)) {
                                    String body = (String)future.get();
                                    if (body != null) {
                                        try {
                                            TycoonPrice tycoonPrice = (TycoonPrice)this.getGSON().fromJson(body, TycoonPrice.class);
                                            if (tycoonPrice != null) {
                                                returnMap.put(entry.getKey(), tycoonPrice.getPriceContainer());
                                            }
                                        } catch (JsonParseException var12) {
                                            LOG.error(var12.getMessage(), var12);
                                        }
                                    }
                                }
                            }
                        }

                        Thread.sleep(500L);
                    }
                } catch (InterruptedException var13) {
                    LOG.error(var13.getMessage(), var13);
                } catch (ExecutionException var14) {
                    LOG.error(var14.getMessage(), var14);
                }

                return returnMap;
            }
        }
    }

    private String getURL(Integer typeID) {
        if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A location is required for EveTycoon");
        } else {
            StringBuilder query = new StringBuilder();
            query.append("https://evetycoon.com/api/v1/market/stats/");
            query.append(this.getPricingOptions().getRegionID());
            query.append("/");
            query.append(typeID);
            query.append("/");
            if (this.getPricingOptions().getLocationType() == LocationType.STATION) {
                query.append("?locationId=");
                query.append(this.getPricingOptions().getLocationID());
            } else if (this.getPricingOptions().getLocationType() == LocationType.SYSTEM) {
                query.append("?systemId=");
                query.append(this.getPricingOptions().getLocationID());
            }

            return query.toString();
        }
    }

    public Call getCall(Integer typeID) {
        Request.Builder request = (new Request.Builder()).url(this.getURL(typeID)).addHeader("User-Agent", this.getPricingOptions().getUserAgent());
        Iterator var3 = this.getPricingOptions().getHeaders().entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var3.next();
            request.addHeader((String)entry.getKey(), (String)entry.getValue());
        }

        return this.getClient().newCall(request.build());
    }

    private static class TycoonPrice {
        public int typeID;
        public long buyVolume;
        public long sellVolume;
        public long buyOrders;
        public long sellOrders;
        public long buyOutliers;
        public long sellOutliers;
        public double buyThreshold;
        public double sellThreshold;
        public double buyAvgFivePercent;
        public double sellAvgFivePercent;
        public double maxBuy;
        public double minSell;

        private TycoonPrice() {
        }

        int getTypeID() {
            return this.typeID;
        }

        PriceContainer getPriceContainer() {
            PriceContainer.PriceContainerBuilder builder = new PriceContainer.PriceContainerBuilder();
            builder.putPrice(PriceType.BUY_HIGH, this.maxBuy);
            builder.putPrice(PriceType.BUY_PERCENTILE, this.buyAvgFivePercent);
            builder.putPrice(PriceType.SELL_LOW, this.minSell);
            builder.putPrice(PriceType.SELL_PERCENTILE, this.sellAvgFivePercent);
            return builder.build();
        }
    }

    private class Updater implements Callable<String> {
        private final Call call;

        public Updater(Integer typeID) {
            this.call = EveTycoon.this.getCall(typeID);
        }

        public String call() throws Exception {
            return this.call.execute().body().string();
        }
    }


}
