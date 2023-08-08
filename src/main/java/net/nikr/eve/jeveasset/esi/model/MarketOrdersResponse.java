/*
 * EVE Swagger Interface
 * An OpenAPI for EVE Online
 *
 * The version of the OpenAPI document: 1.17
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package net.nikr.eve.jeveasset.esi.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * 200 ok object
 */
@ApiModel(description = "200 ok object")
public class MarketOrdersResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_DURATION = "duration";
    @SerializedName(SERIALIZED_NAME_DURATION)
    private Integer duration;

    public static final String SERIALIZED_NAME_IS_BUY_ORDER = "is_buy_order";
    @SerializedName(SERIALIZED_NAME_IS_BUY_ORDER)
    private Boolean isBuyOrder;

    public static final String SERIALIZED_NAME_ISSUED = "issued";
    @SerializedName(SERIALIZED_NAME_ISSUED)
    private OffsetDateTime issued;

    public static final String SERIALIZED_NAME_LOCATION_ID = "location_id";
    @SerializedName(SERIALIZED_NAME_LOCATION_ID)
    private Long locationId;

    public static final String SERIALIZED_NAME_MIN_VOLUME = "min_volume";
    @SerializedName(SERIALIZED_NAME_MIN_VOLUME)
    private Integer minVolume;

    public static final String SERIALIZED_NAME_ORDER_ID = "order_id";
    @SerializedName(SERIALIZED_NAME_ORDER_ID)
    private Long orderId;

    public static final String SERIALIZED_NAME_PRICE = "price";
    @SerializedName(SERIALIZED_NAME_PRICE)
    private Double price;

    /**
     * range string
     */
    @JsonAdapter(RangeEnum.Adapter.class)
    public enum RangeEnum {
        STATION("station"),

        REGION("region"),

        SOLARSYSTEM("solarsystem"),

        _1("1"),

        _2("2"),

        _3("3"),

        _4("4"),

        _5("5"),

        _10("10"),

        _20("20"),

        _30("30"),

        _40("40");

        private String value;

        RangeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static RangeEnum fromValue(String value) {
            for (RangeEnum b : RangeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<RangeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final RangeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public RangeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return RangeEnum.fromValue(value);
            }
        }
    }

    public static final String SERIALIZED_NAME_RANGE = "range";
    @SerializedName(SERIALIZED_NAME_RANGE)
    private String range;
    private RangeEnum rangeEnum;

    public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
    @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
    private Integer systemId;

    public static final String SERIALIZED_NAME_TYPE_ID = "type_id";
    @SerializedName(SERIALIZED_NAME_TYPE_ID)
    private Integer typeId;

    public static final String SERIALIZED_NAME_VOLUME_REMAIN = "volume_remain";
    @SerializedName(SERIALIZED_NAME_VOLUME_REMAIN)
    private Integer volumeRemain;

    public static final String SERIALIZED_NAME_VOLUME_TOTAL = "volume_total";
    @SerializedName(SERIALIZED_NAME_VOLUME_TOTAL)
    private Integer volumeTotal;

    public MarketOrdersResponse duration(Integer duration) {

        this.duration = duration;
        return this;
    }

    /**
     * duration integer
     * 
     * @return duration
     **/
    @ApiModelProperty(required = true, value = "duration integer")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public MarketOrdersResponse isBuyOrder(Boolean isBuyOrder) {

        this.isBuyOrder = isBuyOrder;
        return this;
    }

    /**
     * is_buy_order boolean
     * 
     * @return isBuyOrder
     **/
    @ApiModelProperty(required = true, value = "is_buy_order boolean")
    public Boolean getIsBuyOrder() {
        return isBuyOrder;
    }

    public void setIsBuyOrder(Boolean isBuyOrder) {
        this.isBuyOrder = isBuyOrder;
    }

    public MarketOrdersResponse issued(OffsetDateTime issued) {

        this.issued = issued;
        return this;
    }

    /**
     * issued string
     * 
     * @return issued
     **/
    @ApiModelProperty(required = true, value = "issued string")
    public OffsetDateTime getIssued() {
        return issued;
    }

    public void setIssued(OffsetDateTime issued) {
        this.issued = issued;
    }

    public MarketOrdersResponse locationId(Long locationId) {

        this.locationId = locationId;
        return this;
    }

    /**
     * location_id integer
     * 
     * @return locationId
     **/
    @ApiModelProperty(required = true, value = "location_id integer")
    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public MarketOrdersResponse minVolume(Integer minVolume) {

        this.minVolume = minVolume;
        return this;
    }

    /**
     * min_volume integer
     * 
     * @return minVolume
     **/
    @ApiModelProperty(required = true, value = "min_volume integer")
    public Integer getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(Integer minVolume) {
        this.minVolume = minVolume;
    }

    public MarketOrdersResponse orderId(Long orderId) {

        this.orderId = orderId;
        return this;
    }

    /**
     * order_id integer
     * 
     * @return orderId
     **/
    @ApiModelProperty(required = true, value = "order_id integer")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public MarketOrdersResponse price(Double price) {

        this.price = price;
        return this;
    }

    /**
     * price number
     * 
     * @return price
     **/
    @ApiModelProperty(required = true, value = "price number")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MarketOrdersResponse range(RangeEnum rangeEnum) {

        this.rangeEnum = rangeEnum;
        return this;
    }

    public MarketOrdersResponse rangeString(String range) {

        this.range = range;
        return this;
    }

    /**
     * range string
     * 
     * @return range
     **/
    @ApiModelProperty(required = true, value = "range string")
    public RangeEnum getRange() {
        if (rangeEnum == null) {
            rangeEnum = RangeEnum.fromValue(range);
        }
        return rangeEnum;
    }

    public String getRangeString() {
        return range;
    }

    public void setRange(RangeEnum rangeEnum) {
        this.rangeEnum = rangeEnum;
    }

    public void setRangeString(String range) {
        this.range = range;
    }

    public MarketOrdersResponse systemId(Integer systemId) {

        this.systemId = systemId;
        return this;
    }

    /**
     * The solar system this order was placed
     * 
     * @return systemId
     **/
    @ApiModelProperty(required = true, value = "The solar system this order was placed")
    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public MarketOrdersResponse typeId(Integer typeId) {

        this.typeId = typeId;
        return this;
    }

    /**
     * type_id integer
     * 
     * @return typeId
     **/
    @ApiModelProperty(required = true, value = "type_id integer")
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public MarketOrdersResponse volumeRemain(Integer volumeRemain) {

        this.volumeRemain = volumeRemain;
        return this;
    }

    /**
     * volume_remain integer
     * 
     * @return volumeRemain
     **/
    @ApiModelProperty(required = true, value = "volume_remain integer")
    public Integer getVolumeRemain() {
        return volumeRemain;
    }

    public void setVolumeRemain(Integer volumeRemain) {
        this.volumeRemain = volumeRemain;
    }

    public MarketOrdersResponse volumeTotal(Integer volumeTotal) {

        this.volumeTotal = volumeTotal;
        return this;
    }

    /**
     * volume_total integer
     * 
     * @return volumeTotal
     **/
    @ApiModelProperty(required = true, value = "volume_total integer")
    public Integer getVolumeTotal() {
        return volumeTotal;
    }

    public void setVolumeTotal(Integer volumeTotal) {
        this.volumeTotal = volumeTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarketOrdersResponse marketOrdersResponse = (MarketOrdersResponse) o;
        return Objects.equals(this.duration, marketOrdersResponse.duration)
                && Objects.equals(this.isBuyOrder, marketOrdersResponse.isBuyOrder)
                && Objects.equals(this.issued, marketOrdersResponse.issued)
                && Objects.equals(this.locationId, marketOrdersResponse.locationId)
                && Objects.equals(this.minVolume, marketOrdersResponse.minVolume)
                && Objects.equals(this.orderId, marketOrdersResponse.orderId)
                && Objects.equals(this.price, marketOrdersResponse.price)
                && Objects.equals(this.range, marketOrdersResponse.range)
                && Objects.equals(this.systemId, marketOrdersResponse.systemId)
                && Objects.equals(this.typeId, marketOrdersResponse.typeId)
                && Objects.equals(this.volumeRemain, marketOrdersResponse.volumeRemain)
                && Objects.equals(this.volumeTotal, marketOrdersResponse.volumeTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, isBuyOrder, issued, locationId, minVolume, orderId, price, range, systemId,
                typeId, volumeRemain, volumeTotal);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MarketOrdersResponse {\n");
        sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
        sb.append("    isBuyOrder: ").append(toIndentedString(isBuyOrder)).append("\n");
        sb.append("    issued: ").append(toIndentedString(issued)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    minVolume: ").append(toIndentedString(minVolume)).append("\n");
        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    range: ").append(toIndentedString(range)).append("\n");
        sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    volumeRemain: ").append(toIndentedString(volumeRemain)).append("\n");
        sb.append("    volumeTotal: ").append(toIndentedString(volumeTotal)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
