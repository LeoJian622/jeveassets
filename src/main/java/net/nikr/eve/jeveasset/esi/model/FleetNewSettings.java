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
 * new_settings object
 */
@ApiModel(description = "new_settings object")
public class FleetNewSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_IS_FREE_MOVE = "is_free_move";
    @SerializedName(SERIALIZED_NAME_IS_FREE_MOVE)
    private Boolean isFreeMove;

    public static final String SERIALIZED_NAME_MOTD = "motd";
    @SerializedName(SERIALIZED_NAME_MOTD)
    private String motd;

    public FleetNewSettings isFreeMove(Boolean isFreeMove) {

        this.isFreeMove = isFreeMove;
        return this;
    }

    /**
     * Should free-move be enabled in the fleet
     * 
     * @return isFreeMove
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "Should free-move be enabled in the fleet")
    public Boolean getIsFreeMove() {
        return isFreeMove;
    }

    public void setIsFreeMove(Boolean isFreeMove) {
        this.isFreeMove = isFreeMove;
    }

    public FleetNewSettings motd(String motd) {

        this.motd = motd;
        return this;
    }

    /**
     * New fleet MOTD in CCP flavoured HTML
     * 
     * @return motd
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "New fleet MOTD in CCP flavoured HTML")
    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FleetNewSettings fleetNewSettings = (FleetNewSettings) o;
        return Objects.equals(this.isFreeMove, fleetNewSettings.isFreeMove)
                && Objects.equals(this.motd, fleetNewSettings.motd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFreeMove, motd);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FleetNewSettings {\n");
        sb.append("    isFreeMove: ").append(toIndentedString(isFreeMove)).append("\n");
        sb.append("    motd: ").append(toIndentedString(motd)).append("\n");
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
