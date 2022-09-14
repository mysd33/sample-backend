package com.example.backend.common.resource;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse  implements Serializable { 
	private static final long serialVersionUID = -707495429327768166L;

    private String code;
    private String message;
    private List<String> details;
    

}
