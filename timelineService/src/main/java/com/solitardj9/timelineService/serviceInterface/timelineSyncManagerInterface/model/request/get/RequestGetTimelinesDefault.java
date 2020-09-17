package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use= JsonTypeInfo.Id.NAME,
        //include = JsonTypeInfo.As.PROPERTY,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
    @Type(value = RequestGetTimelines.class, name = "get"),
    @Type(value = RequestGetTimelinesByTime.class, name = "getByTime"),
    @Type(value = RequestGetTimelinesByPeriod.class, name = "getByPeriod")
})
public class RequestGetTimelinesDefault {
	//
	@JsonProperty(value = "type") private String type;
	
	private List<String> timelines;
	
	public RequestGetTimelinesDefault() {
	}
	
	public RequestGetTimelinesDefault(String type, List<String> timelines) {
		this.type = type;
		this.timelines = timelines;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<String> getTimelines() {
		return timelines;
	}

	public void setTimelines(List<String> timelines) {
		this.timelines = timelines;
	}

	@Override
	public String toString() {
		return "RequestGetTimelinesDefault [type=" + type + ", timelines=" + timelines + "]";
	}
}