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
 * active_total object
 */
@ApiModel(description = "active_total object")
public class FactionWarfareLeaderboardCorporationActiveTotalVictoryPoints implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_AMOUNT = "amount";
    @SerializedName(SERIALIZED_NAME_AMOUNT)
    private Integer amount;

    public static final String SERIALIZED_NAME_CORPORATION_ID = "corporation_id";
    @SerializedName(SERIALIZED_NAME_CORPORATION_ID)
    private Integer corporationId;

    public FactionWarfareLeaderboardCorporationActiveTotalVictoryPoints amount(Integer amount) {

        this.amount = amount;
        return this;
    }

    /**
     * Amount of victory points
     * 
     * @return amount
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "Amount of victory points")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public FactionWarfareLeaderboardCorporationActiveTotalVictoryPoints corporationId(Integer corporationId) {

        this.corporationId = corporationId;
        return this;
    }

    /**
     * corporation_id integer
     * 
     * @return corporationId
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "corporation_id integer")
    public Integer getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(Integer corporationId) {
        this.corporationId = corporationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FactionWarfareLeaderboardCorporationActiveTotalVictoryPoints factionWarfareLeaderboardCorporationActiveTotalVictoryPoints = (FactionWarfareLeaderboardCorporationActiveTotalVictoryPoints) o;
        return Objects.equals(this.amount, factionWarfareLeaderboardCorporationActiveTotalVictoryPoints.amount)
                && Objects.equals(this.corporationId,
                        factionWarfareLeaderboardCorporationActiveTotalVictoryPoints.corporationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, corporationId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FactionWarfareLeaderboardCorporationActiveTotalVictoryPoints {\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    corporationId: ").append(toIndentedString(corporationId)).append("\n");
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
