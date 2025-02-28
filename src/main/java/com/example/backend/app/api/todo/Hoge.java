package com.example.backend.app.api.todo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//TODO: 入れ子のリソースでのテスト。後で削除
@Data
public class Hoge implements Serializable {
    private static final long serialVersionUID = 7363737083974407750L;
    
    
    // 作成日時2
    @Schema(description = "作成日時2")
    // @JsonPropertyDescription("作成日時2")
    private Date createdAt2;
    
    // 作成日時3
    @Schema(description = "作成日時3")
    // @JsonPropertyDescription("作成日時2")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt3;
}
