package com.example.fw.common.message;

import java.io.Serial;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * メッセージを保持するクラス
 *
 */
@Data
@Builder
public class ResultMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 5316854177235795645L;

    private ResultMessageType type; // メッセージの種類

    private String code; // コード

    private String[] args; // 置換文字列

    private String message; // メッセージ（直接メッセージ格納する場合は優先出力）

    /**
     * 情報レベルのメッセージかどうか
     * 
     * @return 情報レベルのメッセージかどうか
     */
    public boolean isInfo() {
        return type == ResultMessageType.INFO;
    }

    /**
     * 警告レベルのメッセージかどうか
     * 
     * @return 警告レベルのメッセージかどうか
     */
    public boolean isWarn() {
        return type == ResultMessageType.WARN;
    }

    /**
     * エラーレベルのメッセージかどうか
     * 
     * @return 警告レベルのメッセージかどうか
     */
    public boolean isError() {
        return type == ResultMessageType.ERROR;
    }

}
