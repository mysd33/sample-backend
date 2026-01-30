package com.example.backend.app.api.todo;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//TODO: 入れ子Listのリソースのテスト。後で削除
@Data
public class Hoge2 implements Serializable {
    private static final long serialVersionUID = 5416826544273426754L;

    @NotBlank
    @Schema(description = "ふが")
    // @JsonPropertyDescription("ふが")
    private String fuga;

    // 作成日
    @Schema(description = "作成日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;
}
