package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.remove;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(
        use= JsonTypeInfo.Id.NAME,
        //include = JsonTypeInfo.As.PROPERTY,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
    @Type(value = RequestClear.class, name = "remove"),
    @Type(value = RequestClear.class, name = "removeByTimes"),
    @Type(value = RequestClear.class, name = "removeByPeriod"),
    @Type(value = RequestClear.class, name = "removeByBefore"),
    @Type(value = RequestClear.class, name = "clear")
})
public class RequestRemoveDefault {
	//
	@JsonProperty(value = "type") private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RequestDeleteDefault [type=" + type + "]";
	}
}