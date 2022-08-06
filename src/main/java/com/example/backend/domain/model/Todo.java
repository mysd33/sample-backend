package com.example.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * Todoクラス
 */
@Data
@Builder
public class Todo implements Serializable {
	private static final long serialVersionUID = -8221174350955399012L;

	private String todoId;
	private String todoTitle;
	private boolean finished;
	private Date createdAt;
}