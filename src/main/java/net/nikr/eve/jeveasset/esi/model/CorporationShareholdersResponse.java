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
 * 200 ok object
 */
@ApiModel(description = "200 ok object")
public class CorporationShareholdersResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SERIALIZED_NAME_SHARE_COUNT = "share_count";
    @SerializedName(SERIALIZED_NAME_SHARE_COUNT)
    private Long shareCount;

    public static final String SERIALIZED_NAME_SHAREHOLDER_ID = "shareholder_id";
    @SerializedName(SERIALIZED_NAME_SHAREHOLDER_ID)
    private Integer shareholderId;

    /**
     * shareholder_type string
     */
    @JsonAdapter(ShareholderTypeEnum.Adapter.class)
    public enum ShareholderTypeEnum {
        CHARACTER("character"),

        CORPORATION("corporation");

        private String value;

        ShareholderTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ShareholderTypeEnum fromValue(String value) {
            for (ShareholderTypeEnum b : ShareholderTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<ShareholderTypeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final ShareholderTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public ShareholderTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return ShareholderTypeEnum.fromValue(value);
            }
        }
    }

    public static final String SERIALIZED_NAME_SHAREHOLDER_TYPE = "shareholder_type";
    @SerializedName(SERIALIZED_NAME_SHAREHOLDER_TYPE)
    private String shareholderType;
    private ShareholderTypeEnum shareholderTypeEnum;

    public CorporationShareholdersResponse shareCount(Long shareCount) {

        this.shareCount = shareCount;
        return this;
    }

    /**
     * share_count integer
     * 
     * @return shareCount
     **/
    @ApiModelProperty(required = true, value = "share_count integer")
    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public CorporationShareholdersResponse shareholderId(Integer shareholderId) {

        this.shareholderId = shareholderId;
        return this;
    }

    /**
     * shareholder_id integer
     * 
     * @return shareholderId
     **/
    @ApiModelProperty(required = true, value = "shareholder_id integer")
    public Integer getShareholderId() {
        return shareholderId;
    }

    public void setShareholderId(Integer shareholderId) {
        this.shareholderId = shareholderId;
    }

    public CorporationShareholdersResponse shareholderType(ShareholderTypeEnum shareholderTypeEnum) {

        this.shareholderTypeEnum = shareholderTypeEnum;
        return this;
    }

    public CorporationShareholdersResponse shareholderTypeString(String shareholderType) {

        this.shareholderType = shareholderType;
        return this;
    }

    /**
     * shareholder_type string
     * 
     * @return shareholderType
     **/
    @ApiModelProperty(required = true, value = "shareholder_type string")
    public ShareholderTypeEnum getShareholderType() {
        if (shareholderTypeEnum == null) {
            shareholderTypeEnum = ShareholderTypeEnum.fromValue(shareholderType);
        }
        return shareholderTypeEnum;
    }

    public String getShareholderTypeString() {
        return shareholderType;
    }

    public void setShareholderType(ShareholderTypeEnum shareholderTypeEnum) {
        this.shareholderTypeEnum = shareholderTypeEnum;
    }

    public void setShareholderTypeString(String shareholderType) {
        this.shareholderType = shareholderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CorporationShareholdersResponse corporationShareholdersResponse = (CorporationShareholdersResponse) o;
        return Objects.equals(this.shareCount, corporationShareholdersResponse.shareCount)
                && Objects.equals(this.shareholderId, corporationShareholdersResponse.shareholderId)
                && Objects.equals(this.shareholderType, corporationShareholdersResponse.shareholderType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareCount, shareholderId, shareholderType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CorporationShareholdersResponse {\n");
        sb.append("    shareCount: ").append(toIndentedString(shareCount)).append("\n");
        sb.append("    shareholderId: ").append(toIndentedString(shareholderId)).append("\n");
        sb.append("    shareholderType: ").append(toIndentedString(shareholderType)).append("\n");
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
