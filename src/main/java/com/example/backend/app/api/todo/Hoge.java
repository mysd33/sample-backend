package com.example.backend.app.api.todo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

//TODO: 入れ子のリソースでのテスト。後で削除
@Data
public class Hoge {

    // 作成日時2
    @Schema(description = "作成日時2")
    // @JsonPropertyDescription("作成日時2")
    private LocalDateTime createdAt2;

    // 作成日時3
    @Schema(description = "作成日時3")
    private LocalDateTime createdAt3;
}
