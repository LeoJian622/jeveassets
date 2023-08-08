package net.nikr.eve.jeveasset.ceve.impl;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.nikr.eve.jeveasset.ceve.AbstractPricing;
import net.nikr.eve.jeveasset.ceve.PricingFetchCN;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.candle.eve.pricing.PriceContainer;
import uk.me.candle.eve.pricing.options.LocationType;
import uk.me.candle.eve.pricing.options.NamedPriceLocation;
import uk.me.candle.eve.pricing.options.PriceType;
import uk.me.candle.eve.pricing.options.impl.NamedLocation;

import java.io.IOException;
import java.util.*;

/**
 * @author Leojan
 * @date 2023-08-07 17:07
 */

public class Janice  extends AbstractPricing {    
    private static final Logger LOG = LoggerFactory.getLogger(Janice.class);

    public Janice() {
        super(1);
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
        types.add(LocationType.STATION);
        return types;
    }

    public List<Long> getSupportedLocations(LocationType locationType) {
        if (this.getSupportedLocationTypes().contains(locationType)) {
            ArrayList<Long> list = new ArrayList();
            list.add(2L);
            list.add(3L);
            list.add(4L);
            list.add(5L);
            list.add(6L);
            list.add(114L);
            list.add(115L);
            return list;
        } else {
            return null;
        }
    }

    @Override
    public PricingFetchCN getPricingFetchImplementation() {
        return PricingFetchCN.JANICE;
    }

    protected int getBatchSize() {
        return 100;
    }

    protected Map<Integer, PriceContainer> fetchPrices(Collection<Integer> typeIDs) {
        Map<Integer, PriceContainer> returnMap = new HashMap();
        if (typeIDs.isEmpty()) {
            return returnMap;
        } else if (this.getPricingOptions().getLocation() == null) {
            throw new UnsupportedOperationException("A location is required for Janice");
        } else {
            LocationType locationType = this.getPricingOptions().getLocationType();
            if (!this.getSupportedLocationTypes().contains(locationType)) {
                throw new UnsupportedOperationException(locationType + " is not supported by Janice");
            } else {
                long locationID = this.getPricingOptions().getLocationID();
                if (locationID != 2L && locationID != 3L && locationID != 4L && locationID != 5L && locationID != 6L && locationID != 114L && locationID != 115L) {
                    throw new UnsupportedOperationException(locationID + " is not supported by Janice");
                } else {
                    try {
                        List<PricerItem> results = (List)this.getGSON().fromJson(this.getCall(typeIDs).execute().body().string(), (new TypeToken<List<PricerItem>>() {
                        }).getType());
                        if (results == null) {
                            LOG.error("Error fetching price", new Exception("results is null"));
                            this.addFailureReasons(typeIDs, "results is null");
                            return returnMap;
                        }

                        Iterator var7 = results.iterator();

                        while(var7.hasNext()) {
                            PricerItem item = (PricerItem)var7.next();
                            returnMap.put(item.getTypeID(), item.getPriceContainer());
                        }

                        if (typeIDs.size() != returnMap.size()) {
                            List<Integer> errors = new ArrayList(typeIDs);
                            errors.removeAll(returnMap.keySet());
                            PriceContainer container = new PriceContainer();
                            Iterator var9 = errors.iterator();

                            while(var9.hasNext()) {
                                Integer typeID = (Integer)var9.next();
                                returnMap.put(typeID, container);
                            }
                        }
                    } catch (IOException | JsonParseException | IllegalArgumentException var11) {
                        LOG.error("Error fetching price", var11);
                        this.addFailureReasons(typeIDs, var11.getMessage());
                    }

                    return returnMap;
                }
            }
        }
    }

    public Call getCall(Collection<Integer> typeIDs) {
        StringBuilder body = new StringBuilder();
        Iterator var3 = typeIDs.iterator();

        while(var3.hasNext()) {
            Integer typeID = (Integer)var3.next();
            body.append(typeID);
            body.append("\r\n");
        }

        Request.Builder request = (new Request.Builder()).url("https://janice.e-351.com/api/rest/v2/pricer?market=" + this.getPricingOptions().getLocationID()).post(RequestBody.create(body.toString(), MediaType.get("text/plain"))).addHeader("User-Agent", this.getPricingOptions().getUserAgent());
        Iterator var7 = this.getPricingOptions().getHeaders().entrySet().iterator();

        while(var7.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var7.next();
            request.addHeader((String)entry.getKey(), (String)entry.getValue());
        }

        return this.getClient().newCall(request.build());
    }

    private static class ItemType {
        Integer eid;
        String name;
        Double volume;
        Double packagedVolume;

        private ItemType() {
        }
    }

    private static class PricerItemValues {
        Double buyPrice;
        Double splitPrice;
        Double sellPrice;
        Double buyPrice5DayMedian;
        Double splitPrice5DayMedian;
        Double sellPrice5DayMedian;
        Double buyPrice30DayMedian;
        Double splitPrice30DayMedian;
        Double sellPrice30DayMedian;

        private PricerItemValues() {
        }
    }

    private static class PricerMarket {
        Integer id;
        String name;

        private PricerMarket() {
        }
    }

    private static class PricerItem {
        String date;
        PricerMarket market;
        Integer buyOrderCount;
        Long buyVolume;
        Integer sellOrderCount;
        Long sellVolume;
        PricerItemValues immediatePrices;
        PricerItemValues top5AveragePrices;
        ItemType itemType;

        private PricerItem() {
        }

        public PriceContainer getPriceContainer() {
            PriceContainer.PriceContainerBuilder builder = new PriceContainer.PriceContainerBuilder();
            builder.putPrice(PriceType.BUY_HIGH, this.immediatePrices.buyPrice);
            builder.putPrice(PriceType.BUY_PERCENTILE, this.top5AveragePrices.buyPrice);
            builder.putPrice(PriceType.SELL_LOW, this.immediatePrices.sellPrice);
            builder.putPrice(PriceType.SELL_PERCENTILE, this.top5AveragePrices.sellPrice);
            return builder.build();
        }

        public Integer getTypeID() {
            return this.itemType.eid;
        }
    }

    public static enum JaniceLocation {
        JITA_4_4("Jita 4-4", 2),
        R1O_GN("R1O-GN", 3),
        PERIMETER_TTT("Perimeter TTT", 4),
        JITA_4_4_AND_PERIMETER_TTT("Jita 4-4 + Perimeter TTT", 5),
        NPC("NPC", 6),
        MJ_5F9("MJ-5F9", 114),
        AMARR("Amarr", 115);

        private final NamedPriceLocation priceLocation;

        private JaniceLocation(String name, Integer marketID) {
            this.priceLocation = new NamedLocation(name, (long)marketID, (long)marketID);
        }

        public NamedPriceLocation getPriceLocation() {
            return this.priceLocation;
        }

        public static NamedPriceLocation getLocation(long locationID) {
            JaniceLocation[] var2 = values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                JaniceLocation janiceLocation = var2[var4];
                if (locationID == janiceLocation.getPriceLocation().getLocationID()) {
                    return janiceLocation.getPriceLocation();
                }
            }

            return null;
        }
    }
}
