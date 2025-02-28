package com.example.backend.app.api.todo;

import java.io.Serializable;
import java.util.Date;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Todoリソースクラス
 *
 */
@Data
public class TodoResource implements Serializable {
    private static final long serialVersionUID = -8098772003890701846L;

    // ID
    @Schema(description = "Todo ID")
    private String todoId;

    // タイトル
    @Schema(description = "タイトル")
    @NotNull
    @Size(min = 1, max = 30)
    private String todoTitle;

    // 完了かどうか
    @Schema(description = "完了フラグ")
    private boolean finished;

    // 作成日時
    @Schema(description = "作成日時")
    // @JsonPropertyDescription("作成日時") // @Schemaのdescrptionがあれば定義不要
    private Date createdAt;
    
    
    // TODO: 入れ子のリソースでのテスト。後で削除
    @Schema(description = "ほげ")
    private Hoge hoge;
}