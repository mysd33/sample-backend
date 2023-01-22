package com.example.fw.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.fw.common.message.ResultMessage;
import com.example.fw.common.message.ResultMessageType;

class SystemExceptionTest {

	@Test
	void test() {
		try {
			throw new SystemException(new RuntimeException(), "e.ex.9001", "test");
		} catch (SystemException e) {
			assertEquals("e.ex.9001", e.getCode());
			assertEquals("test", e.getArgs()[0]);
			
			ResultMessage expected = ResultMessage.builder()
					.type(ResultMessageType.ERROR)
					.code("e.ex.9001").args(new String[] {"test"}).build();
			assertEquals(expected, e.getResultMessage());
		}
	}

}
