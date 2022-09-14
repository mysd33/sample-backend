package com.example.backend.api;

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

	//ID
	private String todoId;

	//タイトル
	@NotNull
	@Size(min = 1, max = 30)
	private String todoTitle;

	//完了かどうか
	private boolean finished;

	//作成日時
	private Date createdAt;
}