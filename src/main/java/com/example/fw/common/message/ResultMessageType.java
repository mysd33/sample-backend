package com.example.fw.common.message;

import lombok.Getter;

/**
 * メッセージの種類を表すEnum
 *
 */
public enum ResultMessageType {
    INFO("info"), 
    WARN("warn"), 
    ERROR("error");

    @Getter
    private final String value;

    private ResultMessageType(final String value) {
        this.value = value;
    }

}
