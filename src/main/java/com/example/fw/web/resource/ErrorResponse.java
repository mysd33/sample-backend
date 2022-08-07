package com.example.fw.web.resource;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse  implements Serializable { 
	private static final long serialVersionUID = -707495429327768166L;

    private final String code;
    private final String message;

}
