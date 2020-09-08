package com.solitardj9.timelineService.systemInterface.inMemoryInterface.model;

public class InMemoryParamEnum {
    //
	public enum InMemoryStatus {
	    //
	    COONECTED(true),
	    DISCOONECTED(false)
	    ;
	    
	    private Boolean status;
	    
	    private InMemoryStatus(Boolean status) {
	        this.status = status;
	    }
	    
	    public Boolean getStatus() {
	        return status;
	    }
	    
	    @Override
	    public String toString() {
	        return status.toString();
	    }
	}
	
	public enum InMemoryMode {
	    //
	    AWS("aws"),
	    LOCAL("local")
	    ;
	    
	    private String mode;
	    
	    private InMemoryMode(String mode) {
	        this.mode = mode;
	    }
	    
	    public String getMode() {
	        return mode;
	    }
	    
	    @Override
	    public String toString() {
	        return mode;
	    }
	}
}