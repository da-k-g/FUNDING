package com.web.common.global;

/**
 * HTTP 응답 엔티티를 나타내는 클래스. 이 클래스는 HTTP 응답에 사용될 데이터 구조를 정의합니다.
 */
public class HttpResponseEntity {
	/**
	 * HTTP 응답 결과를 나타내는 내부 정적 클래스. 이 클래스는 응답 성공 여부, 메시지, 데이터 페이로드를 포함합니다.
	 *
	 * @param <T> 응답 데이터의 제네릭 타입
	 */
	public static class ResponseResult<T> {
		private boolean success; // 응답 성공 여부를 나타내는 필드
		private String message; // 응답에 대한 메시지를 포함하는 필드
		private T data; // 응답 데이터(페이로드)를 포함하는 필드

		/**
		 * ResponseResult 생성자.
		 *
		 * @param success 응답 성공 여부
		 * @param message 응답 메시지
		 * @param data    응답 데이터
		 */
		public ResponseResult(boolean success, String message, T data) {
			this.success = success;
			this.message = message;
			this.data = data;
		}

		/**
		 * 응답이 성공적일 때 기본 메시지를 포함하는 성공 응답 객체를 생성합니다.
		 *
		 * @param <T> 응답 데이터의 제네릭 타입
		 * @return 성공 응답 객체
		 */
		public static <T> ResponseResult<T> success() {
			return new ResponseResult<>(true, "Operation successful", null);
		}

		/**
		 * 응답이 성공적일 때 사용자 정의 메시지를 포함하는 성공 응답 객체를 생성합니다.
		 *
		 * @param message 사용자 정의 메시지
		 * @param <T>     응답 데이터의 제네릭 타입
		 * @return 성공 응답 객체
		 */
		public static <T> ResponseResult<T> success(String message) {
			return new ResponseResult<>(true, message, null);
		}

		/**
		 * 응답이 성공적일 때 사용자 정의 메시지와 데이터를 포함하는 성공 응답 객체를 생성합니다.
		 *
		 * @param message 사용자 정의 메시지
		 * @param data    응답 데이터
		 * @param <T>     응답 데이터의 제네릭 타입
		 * @return 성공 응답 객체
		 */
		public static <T> ResponseResult<T> success(String message, T data) {
			return new ResponseResult<>(true, message, data);
		}

		/**
		 * 응답이 실패했을 때 사용자 정의 메시지를 포함하는 실패 응답 객체를 생성합니다.
		 *
		 * @param message 실패 메시지
		 * @param <T>     응답 데이터의 제네릭 타입
		 * @return 실패 응답 객체
		 */
		public static <T> ResponseResult<T> failure(String message) {
			return new ResponseResult<>(false, message, null);
		}

		// Getter and Setter
	}
}
