package com.example.fw.common.logging;

import java.util.List;

import org.springframework.boot.autoconfigure.context.MessageSourceProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * spring.messages.basenameの設定をもとにログのメッセージ定義ファイルを拡張するための設定クラス。
 */
@Slf4j
@RequiredArgsConstructor
public class MessageResourceExtensions {
    private static final ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);
    private final MessageSourceProperties messageSourceProperties;

    /**
     * spring.messages.basenameの設定がある場合にログのメッセージ定義ファイルを拡張する。
     */
    @PostConstruct
    public void init() {
        List<String> baseNames = messageSourceProperties.getBasename();
        if (baseNames == null || baseNames.isEmpty()) {
            appLogger.debug("spring.messages.basenameは設定されていません。ログのメッセージ定義ファイルの追加は行いません。");
            return;
        }
        LoggerFactory.addMessageSourceBaseName(baseNames);
        appLogger.debug("spring.messages.basenameの設定をもとにログのメッセージ定義ファイルの追加を行いました。baseNames={}", baseNames);
    }

}
