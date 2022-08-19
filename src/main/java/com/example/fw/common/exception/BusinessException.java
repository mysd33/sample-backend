package com.example.fw.common.exception;

import org.springframework.util.Assert;

import com.example.fw.common.message.ResultMessage;
import com.example.fw.common.message.ResultMessageType;

import lombok.Getter;

/**
 * 業務エラーを表す例外クラス
 */
public class BusinessException extends RuntimeException implements ErrorCodeProvider {

	private static final long serialVersionUID = -2790663044706077174L;

	@Getter
	private final String code;

	@Getter
	private final Object[] args;

	// エラーメッセージオブジェクトを返却する
	@Getter
	private final ResultMessage resultMessage;

	/**
	 * コンストラクタ
	 * 
	 * @param code エラーコード
	 */
	public BusinessException(final String code) {
		this(code, new Object[0]);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param code エラーコード
	 * @param args エラーコードに対応するメッセージの置換文字列
	 */
	public BusinessException(final String code, final Object... args) {
		this(null, code, args);
	}

	/**
	 * コンストラクタ
	 *
	 * @param cause 原因となったエラーオブジェクト
	 * @param code  エラーコード
	 * @param args  エラーコードに対応するメッセージの置換文字列
	 */
	public BusinessException(final Throwable cause, final String code, final Object... args) {
		super(cause);
		Assert.notNull(code, "codeがNullです。");
		Assert.notNull(args, "argsがNullです。");
		this.code = code;
		this.args = args;
		this.resultMessage = ResultMessage.builder().type(ResultMessageType.WARN).code(code).args(args).build();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param resultMessage エラーメッセージオブジェクト
	 */
	public BusinessException(final ResultMessage resultMessage) {
		this(null, resultMessage);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param cause         原因となったエラーオブジェクト
	 * @param resultMessage エラーメッセージオブジェクト
	 */
	public BusinessException(final Throwable cause, final ResultMessage resultMessage) {
		super(cause);
		Assert.notNull(resultMessage, "resutlMessageがNullです。");
		this.code = resultMessage.getCode();
		this.args = resultMessage.getArgs();
		this.resultMessage = resultMessage;
	}

	@Override
	public String getMessage() {
		return this.resultMessage.toString();
	}

}
