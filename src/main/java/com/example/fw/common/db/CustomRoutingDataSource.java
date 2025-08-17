package com.example.fw.common.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.example.fw.common.logging.ApplicationLogger;
import com.example.fw.common.logging.LoggerFactory;
import com.example.fw.common.message.CommonFrameworkMessageIds;

import lombok.extern.slf4j.Slf4j;

/**
 * TransactionalアノテーションのreadOnly属性によって、データソースを切り替えるためのクラス
 */
@Slf4j
public class CustomRoutingDataSource extends AbstractRoutingDataSource {
    private static final ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);

    public enum DataSourceType {
        // 読み取り専用トランザクション用のデータソース
        READ,
        // 書き込みトランザクション用のデータソース
        WRITE
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSourceType = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ? DataSourceType.READ
                : DataSourceType.WRITE;
        appLogger.info(CommonFrameworkMessageIds.I_FW_RDB_0001, dataSourceType);
        return dataSourceType;

    }

}
