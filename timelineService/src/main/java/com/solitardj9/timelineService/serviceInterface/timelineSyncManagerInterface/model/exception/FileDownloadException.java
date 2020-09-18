package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.exception;

public class FileDownloadException extends RuntimeException {

	private static final long serialVersionUID = 7838878537066493808L;

	public FileDownloadException(String message) {
        super(message);
    }
    
    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}