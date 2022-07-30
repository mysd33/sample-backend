package com.example.fw.common.logging;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.Assert;

/**
 * ロガーのデフォルト実装 
 */
public class DefaultLogger implements ApplicationLogger, MonitoringLogger, AuditLogger {
	private final MessageSource messageSource;
	private final Logger delegateLogger;

	private static final String ERRORCODE_FORMAT = "ERROR_CODE:{0} MESSAGE:{1}";
	private static final String UNDEFINED_MESSAGE_FORMAT = "UNDEFINED-MESSAGE id:{0} arg:{1}";

	public DefaultLogger(final Logger delegateLogger, final MessageSource messageSource) {
		Assert.notNull(delegateLogger, "delagateLoggerはNullです。");
		Assert.notNull(messageSource, "messageSourceはNullです。");
		this.delegateLogger = delegateLogger;
		this.messageSource = messageSource;
	}

	@Override
	public void trace(final String messageId, final Object... args) {
		if (delegateLogger.isTraceEnabled()) {
			delegateLogger.trace(getMessage(messageId, args));
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return delegateLogger.isDebugEnabled();
	}

	@Override
	public void debug(final String format, final Object... args) {
		delegateLogger.debug(format, args);
	}

	@Override
	public void info(final String messageId, final Object... args) {
		if (delegateLogger.isInfoEnabled()) {
			delegateLogger.info(getMessage(messageId, args));
		}
	}

	@Override
	public void warn(final String messageId, final Object... args) {
		if (delegateLogger.isWarnEnabled()) {
			delegateLogger.warn(getMessageWithMessageId(messageId, args));
		}
	}

	@Override
	public void warn(final String messageId, final Throwable t, final Object... args) {
		if (delegateLogger.isWarnEnabled()) {
			delegateLogger.warn(getMessageWithMessageId(messageId, args), t);
		}
	}

	@Override
	public void warn(final String messageId, final String format, final Throwable t, final Object... args) {
		if (delegateLogger.isWarnEnabled()) {
			delegateLogger.warn(getMessageWithMessageIdAndFormat(messageId, format, args), t);
		}

	}
	
	@Override
	public void error(final String messageId, final Object... args) {
		if (delegateLogger.isErrorEnabled()) {
			delegateLogger.error(getMessageWithMessageId(messageId, args));
		}
	}

	@Override
	public void error(final String messageId, final Throwable t, final Object... args) {
		if (delegateLogger.isErrorEnabled()) {
			delegateLogger.error(getMessageWithMessageId(messageId, args), t);
		}
	}
	
	@Override
	public void error(final String messageId, final String format, final Throwable t, final Object... args) {
		if (delegateLogger.isErrorEnabled()) {
			delegateLogger.error(getMessageWithMessageIdAndFormat(messageId, format, args), t);
		}
	}
	
	@Override
	public void audit(final String messageId, final Object... args) {
		String message = messageSource.getMessage(messageId, args, Locale.getDefault());
		// TODO: 監査証跡として共通で出力するものを設定
		String commonInfo = "[AuditInfo]";
		StringBuilder sb = new StringBuilder(commonInfo).append(message);
		delegateLogger.info(sb.toString());
	}

	private String getMessage(final String messageId, final Object... args) {
		String message;		
		try {
			message = messageSource.getMessage(messageId, args, Locale.getDefault());
		} catch (NoSuchMessageException e) {
			message = MessageFormat.format(UNDEFINED_MESSAGE_FORMAT, messageId, Arrays.toString(args));
		}
		return message;
	}

	private String getMessageWithMessageId(final String messageId, final Object... args) {
		String message;
		try {
			message = MessageFormat.format(ERRORCODE_FORMAT, messageId,
					messageSource.getMessage(messageId, args, Locale.getDefault()));
		} catch (NoSuchMessageException e) {
			message = MessageFormat.format(UNDEFINED_MESSAGE_FORMAT, messageId, Arrays.toString(args));
		}
		return message;
	}

	private String getMessageWithMessageIdAndFormat(final String messageId, final String format, final Object... args) {
		String message;
		try {
			message = MessageFormat.format(ERRORCODE_FORMAT, messageId,
					messageSource.getMessage(messageId, args, Locale.getDefault()));
		} catch (NoSuchMessageException e) {
			message = MessageFormat.format(UNDEFINED_MESSAGE_FORMAT, messageId, Arrays.toString(args));
		}
		return MessageFormat.format(format, message);
	}

}
