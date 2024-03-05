package com.geek.geekservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class RepairHubException extends RuntimeException{
    private HttpStatus status = null;
    private Object data = null; // Can be used to pass an ID (Integer) or any other useful info

    public RepairHubException(){
        super();
    }

    public RepairHubException(String message){
        super(message);
    }

    public RepairHubException(HttpStatus status, String message){
        this(message);
        this.status = status;
    }

    public RepairHubException(HttpStatus status, String message, Object data){
        this(status, message);
        this.data = data;
    }
}
