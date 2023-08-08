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

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * route object
 */
@ApiModel(description = "route object")
public class PlanetRoute implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_CONTENT_TYPE_ID = "content_type_id";
    @SerializedName(SERIALIZED_NAME_CONTENT_TYPE_ID)
    private Integer contentTypeId;

    public static final String SERIALIZED_NAME_DESTINATION_PIN_ID = "destination_pin_id";
    @SerializedName(SERIALIZED_NAME_DESTINATION_PIN_ID)
    private Long destinationPinId;

    public static final String SERIALIZED_NAME_QUANTITY = "quantity";
    @SerializedName(SERIALIZED_NAME_QUANTITY)
    private Float quantity;

    public static final String SERIALIZED_NAME_ROUTE_ID = "route_id";
    @SerializedName(SERIALIZED_NAME_ROUTE_ID)
    private Long routeId;

    public static final String SERIALIZED_NAME_SOURCE_PIN_ID = "source_pin_id";
    @SerializedName(SERIALIZED_NAME_SOURCE_PIN_ID)
    private Long sourcePinId;

    public static final String SERIALIZED_NAME_WAYPOINTS = "waypoints";
    @SerializedName(SERIALIZED_NAME_WAYPOINTS)
    private List<Long> waypoints = null;

    public PlanetRoute contentTypeId(Integer contentTypeId) {

        this.contentTypeId = contentTypeId;
        return this;
    }

    /**
     * content_type_id integer
     * 
     * @return contentTypeId
     **/
    @ApiModelProperty(required = true, value = "content_type_id integer")
    public Integer getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(Integer contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public PlanetRoute destinationPinId(Long destinationPinId) {

        this.destinationPinId = destinationPinId;
        return this;
    }

    /**
     * destination_pin_id integer
     * 
     * @return destinationPinId
     **/
    @ApiModelProperty(required = true, value = "destination_pin_id integer")
    public Long getDestinationPinId() {
        return destinationPinId;
    }

    public void setDestinationPinId(Long destinationPinId) {
        this.destinationPinId = destinationPinId;
    }

    public PlanetRoute quantity(Float quantity) {

        this.quantity = quantity;
        return this;
    }

    /**
     * quantity number
     * 
     * @return quantity
     **/
    @ApiModelProperty(required = true, value = "quantity number")
    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public PlanetRoute routeId(Long routeId) {

        this.routeId = routeId;
        return this;
    }

    /**
     * route_id integer
     * 
     * @return routeId
     **/
    @ApiModelProperty(required = true, value = "route_id integer")
    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public PlanetRoute sourcePinId(Long sourcePinId) {

        this.sourcePinId = sourcePinId;
        return this;
    }

    /**
     * source_pin_id integer
     * 
     * @return sourcePinId
     **/
    @ApiModelProperty(required = true, value = "source_pin_id integer")
    public Long getSourcePinId() {
        return sourcePinId;
    }

    public void setSourcePinId(Long sourcePinId) {
        this.sourcePinId = sourcePinId;
    }

    public PlanetRoute waypoints(List<Long> waypoints) {

        this.waypoints = waypoints;
        return this;
    }

    public PlanetRoute addWaypointsItem(Long waypointsItem) {
        if (this.waypoints == null) {
            this.waypoints = new ArrayList<>();
        }
        this.waypoints.add(waypointsItem);
        return this;
    }

    /**
     * list of pin ID waypoints
     * 
     * @return waypoints
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "list of pin ID waypoints")
    public List<Long> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Long> waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanetRoute planetRoute = (PlanetRoute) o;
        return Objects.equals(this.contentTypeId, planetRoute.contentTypeId)
                && Objects.equals(this.destinationPinId, planetRoute.destinationPinId)
                && Objects.equals(this.quantity, planetRoute.quantity)
                && Objects.equals(this.routeId, planetRoute.routeId)
                && Objects.equals(this.sourcePinId, planetRoute.sourcePinId)
                && Objects.equals(this.waypoints, planetRoute.waypoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentTypeId, destinationPinId, quantity, routeId, sourcePinId, waypoints);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PlanetRoute {\n");
        sb.append("    contentTypeId: ").append(toIndentedString(contentTypeId)).append("\n");
        sb.append("    destinationPinId: ").append(toIndentedString(destinationPinId)).append("\n");
        sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
        sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
        sb.append("    sourcePinId: ").append(toIndentedString(sourcePinId)).append("\n");
        sb.append("    waypoints: ").append(toIndentedString(waypoints)).append("\n");
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
