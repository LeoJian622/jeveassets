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
public class CharacterOrdersResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_DURATION = "duration";
    @SerializedName(SERIALIZED_NAME_DURATION)
    private Integer duration;

    public static final String SERIALIZED_NAME_ESCROW = "escrow";
    @SerializedName(SERIALIZED_NAME_ESCROW)
    private Double escrow;

    public static final String SERIALIZED_NAME_IS_BUY_ORDER = "is_buy_order";
    @SerializedName(SERIALIZED_NAME_IS_BUY_ORDER)
    private Boolean isBuyOrder;

    public static final String SERIALIZED_NAME_IS_CORPORATION = "is_corporation";
    @SerializedName(SERIALIZED_NAME_IS_CORPORATION)
    private Boolean isCorporation;

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
     * Valid order range, numbers are ranges in jumps
     */
    @JsonAdapter(RangeEnum.Adapter.class)
    public enum RangeEnum {
        _1("1"),

        _10("10"),

        _2("2"),

        _20("20"),

        _3("3"),

        _30("30"),

        _4("4"),

        _40("40"),

        _5("5"),

        REGION("region"),

        SOLARSYSTEM("solarsystem"),

        STATION("station");

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

    public static final String SERIALIZED_NAME_REGION_ID = "region_id";
    @SerializedName(SERIALIZED_NAME_REGION_ID)
    private Integer regionId;

    public static final String SERIALIZED_NAME_TYPE_ID = "type_id";
    @SerializedName(SERIALIZED_NAME_TYPE_ID)
    private Integer typeId;

    public static final String SERIALIZED_NAME_VOLUME_REMAIN = "volume_remain";
    @SerializedName(SERIALIZED_NAME_VOLUME_REMAIN)
    private Integer volumeRemain;

    public static final String SERIALIZED_NAME_VOLUME_TOTAL = "volume_total";
    @SerializedName(SERIALIZED_NAME_VOLUME_TOTAL)
    private Integer volumeTotal;

    public CharacterOrdersResponse duration(Integer duration) {

        this.duration = duration;
        return this;
    }

    /**
     * Number of days for which order is valid (starting from the issued date).
     * An order expires at time issued + duration
     * 
     * @return duration
     **/
    @ApiModelProperty(required = true, value = "Number of days for which order is valid (starting from the issued date). An order expires at time issued + duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public CharacterOrdersResponse escrow(Double escrow) {

        this.escrow = escrow;
        return this;
    }

    /**
     * For buy orders, the amount of ISK in escrow
     * 
     * @return escrow
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "For buy orders, the amount of ISK in escrow")
    public Double getEscrow() {
        return escrow;
    }

    public void setEscrow(Double escrow) {
        this.escrow = escrow;
    }

    public CharacterOrdersResponse isBuyOrder(Boolean isBuyOrder) {

        this.isBuyOrder = isBuyOrder;
        return this;
    }

    /**
     * True if the order is a bid (buy) order
     * 
     * @return isBuyOrder
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "True if the order is a bid (buy) order")
    public Boolean getIsBuyOrder() {
        return isBuyOrder;
    }

    public void setIsBuyOrder(Boolean isBuyOrder) {
        this.isBuyOrder = isBuyOrder;
    }

    public CharacterOrdersResponse isCorporation(Boolean isCorporation) {

        this.isCorporation = isCorporation;
        return this;
    }

    /**
     * Signifies whether the buy/sell order was placed on behalf of a
     * corporation.
     * 
     * @return isCorporation
     **/
    @ApiModelProperty(required = true, value = "Signifies whether the buy/sell order was placed on behalf of a corporation.")
    public Boolean getIsCorporation() {
        return isCorporation;
    }

    public void setIsCorporation(Boolean isCorporation) {
        this.isCorporation = isCorporation;
    }

    public CharacterOrdersResponse issued(OffsetDateTime issued) {

        this.issued = issued;
        return this;
    }

    /**
     * Date and time when this order was issued
     * 
     * @return issued
     **/
    @ApiModelProperty(required = true, value = "Date and time when this order was issued")
    public OffsetDateTime getIssued() {
        return issued;
    }

    public void setIssued(OffsetDateTime issued) {
        this.issued = issued;
    }

    public CharacterOrdersResponse locationId(Long locationId) {

        this.locationId = locationId;
        return this;
    }

    /**
     * ID of the location where order was placed
     * 
     * @return locationId
     **/
    @ApiModelProperty(required = true, value = "ID of the location where order was placed")
    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public CharacterOrdersResponse minVolume(Integer minVolume) {

        this.minVolume = minVolume;
        return this;
    }

    /**
     * For buy orders, the minimum quantity that will be accepted in a matching
     * sell order
     * 
     * @return minVolume
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "For buy orders, the minimum quantity that will be accepted in a matching sell order")
    public Integer getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(Integer minVolume) {
        this.minVolume = minVolume;
    }

    public CharacterOrdersResponse orderId(Long orderId) {

        this.orderId = orderId;
        return this;
    }

    /**
     * Unique order ID
     * 
     * @return orderId
     **/
    @ApiModelProperty(required = true, value = "Unique order ID")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public CharacterOrdersResponse price(Double price) {

        this.price = price;
        return this;
    }

    /**
     * Cost per unit for this order
     * 
     * @return price
     **/
    @ApiModelProperty(required = true, value = "Cost per unit for this order")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CharacterOrdersResponse range(RangeEnum rangeEnum) {

        this.rangeEnum = rangeEnum;
        return this;
    }

    public CharacterOrdersResponse rangeString(String range) {

        this.range = range;
        return this;
    }

    /**
     * Valid order range, numbers are ranges in jumps
     * 
     * @return range
     **/
    @ApiModelProperty(required = true, value = "Valid order range, numbers are ranges in jumps")
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

    public CharacterOrdersResponse regionId(Integer regionId) {

        this.regionId = regionId;
        return this;
    }

    /**
     * ID of the region where order was placed
     * 
     * @return regionId
     **/
    @ApiModelProperty(required = true, value = "ID of the region where order was placed")
    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public CharacterOrdersResponse typeId(Integer typeId) {

        this.typeId = typeId;
        return this;
    }

    /**
     * The type ID of the item transacted in this order
     * 
     * @return typeId
     **/
    @ApiModelProperty(required = true, value = "The type ID of the item transacted in this order")
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public CharacterOrdersResponse volumeRemain(Integer volumeRemain) {

        this.volumeRemain = volumeRemain;
        return this;
    }

    /**
     * Quantity of items still required or offered
     * 
     * @return volumeRemain
     **/
    @ApiModelProperty(required = true, value = "Quantity of items still required or offered")
    public Integer getVolumeRemain() {
        return volumeRemain;
    }

    public void setVolumeRemain(Integer volumeRemain) {
        this.volumeRemain = volumeRemain;
    }

    public CharacterOrdersResponse volumeTotal(Integer volumeTotal) {

        this.volumeTotal = volumeTotal;
        return this;
    }

    /**
     * Quantity of items required or offered at time order was placed
     * 
     * @return volumeTotal
     **/
    @ApiModelProperty(required = true, value = "Quantity of items required or offered at time order was placed")
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
        CharacterOrdersResponse characterOrdersResponse = (CharacterOrdersResponse) o;
        return Objects.equals(this.duration, characterOrdersResponse.duration)
                && Objects.equals(this.escrow, characterOrdersResponse.escrow)
                && Objects.equals(this.isBuyOrder, characterOrdersResponse.isBuyOrder)
                && Objects.equals(this.isCorporation, characterOrdersResponse.isCorporation)
                && Objects.equals(this.issued, characterOrdersResponse.issued)
                && Objects.equals(this.locationId, characterOrdersResponse.locationId)
                && Objects.equals(this.minVolume, characterOrdersResponse.minVolume)
                && Objects.equals(this.orderId, characterOrdersResponse.orderId)
                && Objects.equals(this.price, characterOrdersResponse.price)
                && Objects.equals(this.range, characterOrdersResponse.range)
                && Objects.equals(this.regionId, characterOrdersResponse.regionId)
                && Objects.equals(this.typeId, characterOrdersResponse.typeId)
                && Objects.equals(this.volumeRemain, characterOrdersResponse.volumeRemain)
                && Objects.equals(this.volumeTotal, characterOrdersResponse.volumeTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, escrow, isBuyOrder, isCorporation, issued, locationId, minVolume, orderId, price,
                range, regionId, typeId, volumeRemain, volumeTotal);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CharacterOrdersResponse {\n");
        sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
        sb.append("    escrow: ").append(toIndentedString(escrow)).append("\n");
        sb.append("    isBuyOrder: ").append(toIndentedString(isBuyOrder)).append("\n");
        sb.append("    isCorporation: ").append(toIndentedString(isCorporation)).append("\n");
        sb.append("    issued: ").append(toIndentedString(issued)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    minVolume: ").append(toIndentedString(minVolume)).append("\n");
        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    range: ").append(toIndentedString(range)).append("\n");
        sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
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
