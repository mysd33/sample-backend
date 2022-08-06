package com.example.backend.app;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Todoリソースクラス
 *
 */
@Data
public class TodoResource implements Serializable {
	private static final long serialVersionUID = -8098772003890701846L;

	private String todoId;

	@NotNull
	@Size(min = 1, max = 30)
	private String todoTitle;

	private boolean finished;

	private Date createdAt;
}