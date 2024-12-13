package com.web.common.global;

public class HttpResponseEntity {
            //서버에서 클라이언트로 표준화된 형식으로 응답 객체를 생성
    public static class ResponseResult<T> {
        private boolean success;		// 성공여부 
        private String message;			// 응답 메세지
        private T data;					// 요청 처리 데이터

        public ResponseResult(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public static <T> ResponseResult<T> success() {
            return new ResponseResult<>(true, "Operation successful", null);
        }

        public static <T> ResponseResult<T> success(String message) {
            return new ResponseResult<>(true, message, null);
        }

        public static <T> ResponseResult<T> success(String message, T data) {
            return new ResponseResult<>(true, message, data);
        }

        public static <T> ResponseResult<T> failure(String message) {
            return new ResponseResult<>(false, message, null);
        }

        // Getters and Setters
    }
}
