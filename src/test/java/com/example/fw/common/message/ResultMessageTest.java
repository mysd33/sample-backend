package com.example.fw.common.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResultMessageTest {

    // TODO: 仮のテスト
    @Test
    void test() {
        String code = "i.ex.0001";
        String[] args = new String[] { "aaa", "bbb" };

        ResultMessage actual = ResultMessage.builder().type(ResultMessageType.INFO).code(code).args(args).build();
        assertTrue(actual.isInfo());
        assertEquals(code, actual.getCode());
        assertEquals(args, actual.getArgs());
    }

}
