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
import java.util.Objects;

/**
 * movement object
 */
@ApiModel(description = "movement object")
public class FleetMemberMovement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * If a character is moved to the &#x60;fleet_commander&#x60; role, neither
     * &#x60;wing_id&#x60; or &#x60;squad_id&#x60; should be specified. If a
     * character is moved to the &#x60;wing_commander&#x60; role, only
     * &#x60;wing_id&#x60; should be specified. If a character is moved to the
     * &#x60;squad_commander&#x60; role, both &#x60;wing_id&#x60; and
     * &#x60;squad_id&#x60; should be specified. If a character is moved to the
     * &#x60;squad_member&#x60; role, both &#x60;wing_id&#x60; and
     * &#x60;squad_id&#x60; should be specified.
     */
    @JsonAdapter(RoleEnum.Adapter.class)
    public enum RoleEnum {
        FLEET_COMMANDER("fleet_commander"),

        WING_COMMANDER("wing_commander"),

        SQUAD_COMMANDER("squad_commander"),

        SQUAD_MEMBER("squad_member");

        private String value;

        RoleEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static RoleEnum fromValue(String value) {
            for (RoleEnum b : RoleEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<RoleEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final RoleEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public RoleEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return RoleEnum.fromValue(value);
            }
        }
    }

    public static final String SERIALIZED_NAME_ROLE = "role";
    @SerializedName(SERIALIZED_NAME_ROLE)
    private String role;
    private RoleEnum roleEnum;

    public static final String SERIALIZED_NAME_SQUAD_ID = "squad_id";
    @SerializedName(SERIALIZED_NAME_SQUAD_ID)
    private Long squadId;

    public static final String SERIALIZED_NAME_WING_ID = "wing_id";
    @SerializedName(SERIALIZED_NAME_WING_ID)
    private Long wingId;

    public FleetMemberMovement role(RoleEnum roleEnum) {

        this.roleEnum = roleEnum;
        return this;
    }

    public FleetMemberMovement roleString(String role) {

        this.role = role;
        return this;
    }

    /**
     * If a character is moved to the &#x60;fleet_commander&#x60; role, neither
     * &#x60;wing_id&#x60; or &#x60;squad_id&#x60; should be specified. If a
     * character is moved to the &#x60;wing_commander&#x60; role, only
     * &#x60;wing_id&#x60; should be specified. If a character is moved to the
     * &#x60;squad_commander&#x60; role, both &#x60;wing_id&#x60; and
     * &#x60;squad_id&#x60; should be specified. If a character is moved to the
     * &#x60;squad_member&#x60; role, both &#x60;wing_id&#x60; and
     * &#x60;squad_id&#x60; should be specified.
     * 
     * @return role
     **/
    @ApiModelProperty(required = true, value = "If a character is moved to the `fleet_commander` role, neither `wing_id` or `squad_id` should be specified. If a character is moved to the `wing_commander` role, only `wing_id` should be specified. If a character is moved to the `squad_commander` role, both `wing_id` and `squad_id` should be specified. If a character is moved to the `squad_member` role, both `wing_id` and `squad_id` should be specified.")
    public RoleEnum getRole() {
        if (roleEnum == null) {
            roleEnum = RoleEnum.fromValue(role);
        }
        return roleEnum;
    }

    public String getRoleString() {
        return role;
    }

    public void setRole(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public void setRoleString(String role) {
        this.role = role;
    }

    public FleetMemberMovement squadId(Long squadId) {

        this.squadId = squadId;
        return this;
    }

    /**
     * squad_id integer minimum: 0
     * 
     * @return squadId
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "squad_id integer")
    public Long getSquadId() {
        return squadId;
    }

    public void setSquadId(Long squadId) {
        this.squadId = squadId;
    }

    public FleetMemberMovement wingId(Long wingId) {

        this.wingId = wingId;
        return this;
    }

    /**
     * wing_id integer minimum: 0
     * 
     * @return wingId
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "wing_id integer")
    public Long getWingId() {
        return wingId;
    }

    public void setWingId(Long wingId) {
        this.wingId = wingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FleetMemberMovement fleetMemberMovement = (FleetMemberMovement) o;
        return Objects.equals(this.role, fleetMemberMovement.role)
                && Objects.equals(this.squadId, fleetMemberMovement.squadId)
                && Objects.equals(this.wingId, fleetMemberMovement.wingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, squadId, wingId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FleetMemberMovement {\n");
        sb.append("    role: ").append(toIndentedString(role)).append("\n");
        sb.append("    squadId: ").append(toIndentedString(squadId)).append("\n");
        sb.append("    wingId: ").append(toIndentedString(wingId)).append("\n");
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
