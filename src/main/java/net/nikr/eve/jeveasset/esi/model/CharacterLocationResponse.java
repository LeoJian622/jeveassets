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
public class CharacterLocationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_SOLAR_SYSTEM_ID = "solar_system_id";
    @SerializedName(SERIALIZED_NAME_SOLAR_SYSTEM_ID)
    private Integer solarSystemId;

    public static final String SERIALIZED_NAME_STATION_ID = "station_id";
    @SerializedName(SERIALIZED_NAME_STATION_ID)
    private Integer stationId;

    public static final String SERIALIZED_NAME_STRUCTURE_ID = "structure_id";
    @SerializedName(SERIALIZED_NAME_STRUCTURE_ID)
    private Long structureId;

    public CharacterLocationResponse solarSystemId(Integer solarSystemId) {

        this.solarSystemId = solarSystemId;
        return this;
    }

    /**
     * solar_system_id integer
     * 
     * @return solarSystemId
     **/
    @ApiModelProperty(required = true, value = "solar_system_id integer")
    public Integer getSolarSystemId() {
        return solarSystemId;
    }

    public void setSolarSystemId(Integer solarSystemId) {
        this.solarSystemId = solarSystemId;
    }

    public CharacterLocationResponse stationId(Integer stationId) {

        this.stationId = stationId;
        return this;
    }

    /**
     * station_id integer
     * 
     * @return stationId
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "station_id integer")
    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public CharacterLocationResponse structureId(Long structureId) {

        this.structureId = structureId;
        return this;
    }

    /**
     * structure_id integer
     * 
     * @return structureId
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "structure_id integer")
    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {
        this.structureId = structureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterLocationResponse characterLocationResponse = (CharacterLocationResponse) o;
        return Objects.equals(this.solarSystemId, characterLocationResponse.solarSystemId)
                && Objects.equals(this.stationId, characterLocationResponse.stationId)
                && Objects.equals(this.structureId, characterLocationResponse.structureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solarSystemId, stationId, structureId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CharacterLocationResponse {\n");
        sb.append("    solarSystemId: ").append(toIndentedString(solarSystemId)).append("\n");
        sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
        sb.append("    structureId: ").append(toIndentedString(structureId)).append("\n");
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
