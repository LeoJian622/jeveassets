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
import java.util.Objects;

/**
 * 200 ok object
 */
@ApiModel(description = "200 ok object")
public class CorporationAssetsLocationsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_ITEM_ID = "item_id";
    @SerializedName(SERIALIZED_NAME_ITEM_ID)
    private Long itemId;

    public static final String SERIALIZED_NAME_POSITION = "position";
    @SerializedName(SERIALIZED_NAME_POSITION)
    private CorporationAssetsLocationsPosition position;

    public CorporationAssetsLocationsResponse itemId(Long itemId) {

        this.itemId = itemId;
        return this;
    }

    /**
     * item_id integer
     * 
     * @return itemId
     **/
    @ApiModelProperty(required = true, value = "item_id integer")
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public CorporationAssetsLocationsResponse position(CorporationAssetsLocationsPosition position) {

        this.position = position;
        return this;
    }

    /**
     * Get position
     * 
     * @return position
     **/
    @ApiModelProperty(required = true, value = "")
    public CorporationAssetsLocationsPosition getPosition() {
        return position;
    }

    public void setPosition(CorporationAssetsLocationsPosition position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CorporationAssetsLocationsResponse corporationAssetsLocationsResponse = (CorporationAssetsLocationsResponse) o;
        return Objects.equals(this.itemId, corporationAssetsLocationsResponse.itemId)
                && Objects.equals(this.position, corporationAssetsLocationsResponse.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, position);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CorporationAssetsLocationsResponse {\n");
        sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
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
