package com.web.common.errors.exception;

/**
 * 특정 리소스나 엔티티를 찾을 수 없을 때 발생하는 "Not Found" 예외를 나타내는 커스텀 예외 클래스입니다. 이 클래스는
 * {@link RuntimeException}을 상속하여 실행 중 예외로 처리됩니다.
 */
public class NotFoundException extends RuntimeException {
	/**
	 * 주어진 상세 메시지를 사용하여 새로운 NotFoundException 객체를 생성합니다. 주로 예외가 발생한 이유를 설명하는 에러 메시지를
	 * 제공할 때 사용됩니다.
	 *
	 * @param message 예외에 대한 상세 메시지
	 */
	public NotFoundException(String message) {
		super(message);
	}

	/**
	 * 주어진 상세 메시지와 원인(cause)을 사용하여 새로운 NotFoundException 객체를 생성합니다. 이 생성자는 예외의 원인(다른
	 * 예외 객체)을 함께 전달하여 더 자세한 디버깅 정보를 제공합니다.
	 *
	 * @param message 예외에 대한 상세 메시지
	 * @param cause   예외의 원인이 되는 다른 예외 객체
	 */
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
