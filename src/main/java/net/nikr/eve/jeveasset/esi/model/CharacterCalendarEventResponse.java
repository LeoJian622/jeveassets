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
 * Full details of a specific event
 */
@ApiModel(description = "Full details of a specific event")
public class CharacterCalendarEventResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_DATE = "date";
    @SerializedName(SERIALIZED_NAME_DATE)
    private OffsetDateTime date;

    public static final String SERIALIZED_NAME_DURATION = "duration";
    @SerializedName(SERIALIZED_NAME_DURATION)
    private Integer duration;

    public static final String SERIALIZED_NAME_EVENT_ID = "event_id";
    @SerializedName(SERIALIZED_NAME_EVENT_ID)
    private Integer eventId;

    public static final String SERIALIZED_NAME_IMPORTANCE = "importance";
    @SerializedName(SERIALIZED_NAME_IMPORTANCE)
    private Integer importance;

    public static final String SERIALIZED_NAME_OWNER_ID = "owner_id";
    @SerializedName(SERIALIZED_NAME_OWNER_ID)
    private Integer ownerId;

    public static final String SERIALIZED_NAME_OWNER_NAME = "owner_name";
    @SerializedName(SERIALIZED_NAME_OWNER_NAME)
    private String ownerName;

    /**
     * owner_type string
     */
    @JsonAdapter(OwnerTypeEnum.Adapter.class)
    public enum OwnerTypeEnum {
        EVE_SERVER("eve_server"),

        CORPORATION("corporation"),

        FACTION("faction"),

        CHARACTER("character"),

        ALLIANCE("alliance");

        private String value;

        OwnerTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static OwnerTypeEnum fromValue(String value) {
            for (OwnerTypeEnum b : OwnerTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<OwnerTypeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final OwnerTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public OwnerTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return OwnerTypeEnum.fromValue(value);
            }
        }
    }

    public static final String SERIALIZED_NAME_OWNER_TYPE = "owner_type";
    @SerializedName(SERIALIZED_NAME_OWNER_TYPE)
    private String ownerType;
    private OwnerTypeEnum ownerTypeEnum;

    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName(SERIALIZED_NAME_RESPONSE)
    private String response;

    public static final String SERIALIZED_NAME_TEXT = "text";
    @SerializedName(SERIALIZED_NAME_TEXT)
    private String text;

    public static final String SERIALIZED_NAME_TITLE = "title";
    @SerializedName(SERIALIZED_NAME_TITLE)
    private String title;

    public CharacterCalendarEventResponse date(OffsetDateTime date) {

        this.date = date;
        return this;
    }

    /**
     * date string
     * 
     * @return date
     **/
    @ApiModelProperty(required = true, value = "date string")
    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public CharacterCalendarEventResponse duration(Integer duration) {

        this.duration = duration;
        return this;
    }

    /**
     * Length in minutes
     * 
     * @return duration
     **/
    @ApiModelProperty(required = true, value = "Length in minutes")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public CharacterCalendarEventResponse eventId(Integer eventId) {

        this.eventId = eventId;
        return this;
    }

    /**
     * event_id integer
     * 
     * @return eventId
     **/
    @ApiModelProperty(required = true, value = "event_id integer")
    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public CharacterCalendarEventResponse importance(Integer importance) {

        this.importance = importance;
        return this;
    }

    /**
     * importance integer
     * 
     * @return importance
     **/
    @ApiModelProperty(required = true, value = "importance integer")
    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public CharacterCalendarEventResponse ownerId(Integer ownerId) {

        this.ownerId = ownerId;
        return this;
    }

    /**
     * owner_id integer
     * 
     * @return ownerId
     **/
    @ApiModelProperty(required = true, value = "owner_id integer")
    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public CharacterCalendarEventResponse ownerName(String ownerName) {

        this.ownerName = ownerName;
        return this;
    }

    /**
     * owner_name string
     * 
     * @return ownerName
     **/
    @ApiModelProperty(required = true, value = "owner_name string")
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public CharacterCalendarEventResponse ownerType(OwnerTypeEnum ownerTypeEnum) {

        this.ownerTypeEnum = ownerTypeEnum;
        return this;
    }

    public CharacterCalendarEventResponse ownerTypeString(String ownerType) {

        this.ownerType = ownerType;
        return this;
    }

    /**
     * owner_type string
     * 
     * @return ownerType
     **/
    @ApiModelProperty(required = true, value = "owner_type string")
    public OwnerTypeEnum getOwnerType() {
        if (ownerTypeEnum == null) {
            ownerTypeEnum = OwnerTypeEnum.fromValue(ownerType);
        }
        return ownerTypeEnum;
    }

    public String getOwnerTypeString() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEnum ownerTypeEnum) {
        this.ownerTypeEnum = ownerTypeEnum;
    }

    public void setOwnerTypeString(String ownerType) {
        this.ownerType = ownerType;
    }

    public CharacterCalendarEventResponse response(String response) {

        this.response = response;
        return this;
    }

    /**
     * response string
     * 
     * @return response
     **/
    @ApiModelProperty(required = true, value = "response string")
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public CharacterCalendarEventResponse text(String text) {

        this.text = text;
        return this;
    }

    /**
     * text string
     * 
     * @return text
     **/
    @ApiModelProperty(required = true, value = "text string")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CharacterCalendarEventResponse title(String title) {

        this.title = title;
        return this;
    }

    /**
     * title string
     * 
     * @return title
     **/
    @ApiModelProperty(required = true, value = "title string")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterCalendarEventResponse characterCalendarEventResponse = (CharacterCalendarEventResponse) o;
        return Objects.equals(this.date, characterCalendarEventResponse.date)
                && Objects.equals(this.duration, characterCalendarEventResponse.duration)
                && Objects.equals(this.eventId, characterCalendarEventResponse.eventId)
                && Objects.equals(this.importance, characterCalendarEventResponse.importance)
                && Objects.equals(this.ownerId, characterCalendarEventResponse.ownerId)
                && Objects.equals(this.ownerName, characterCalendarEventResponse.ownerName)
                && Objects.equals(this.ownerType, characterCalendarEventResponse.ownerType)
                && Objects.equals(this.response, characterCalendarEventResponse.response)
                && Objects.equals(this.text, characterCalendarEventResponse.text)
                && Objects.equals(this.title, characterCalendarEventResponse.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, duration, eventId, importance, ownerId, ownerName, ownerType, response, text, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CharacterCalendarEventResponse {\n");
        sb.append("    date: ").append(toIndentedString(date)).append("\n");
        sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
        sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
        sb.append("    importance: ").append(toIndentedString(importance)).append("\n");
        sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
        sb.append("    ownerName: ").append(toIndentedString(ownerName)).append("\n");
        sb.append("    ownerType: ").append(toIndentedString(ownerType)).append("\n");
        sb.append("    response: ").append(toIndentedString(response)).append("\n");
        sb.append("    text: ").append(toIndentedString(text)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
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
