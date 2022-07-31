package com.example.fw.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.fw.common.message.ResultMessage;
import com.example.fw.common.message.ResultMessageType;

class BusinessExceptionTest {

	//TODO: とりあえずのテスト
	@Test
	void test() {				
		try {
			throw new BusinessException(new Exception(), "w.ex.0001", "test");
		} catch (BusinessException e) {
			assertEquals("w.ex.0001", e.getCode());
			assertEquals("test", e.getArgs()[0]);
			
			ResultMessage expected = ResultMessage.builder()
					.type(ResultMessageType.WARN)
					.code("w.ex.0001").args(new Object[] {"test"}).build();
			assertEquals(expected, e.getResultMessage());
		}
		
	}

}
