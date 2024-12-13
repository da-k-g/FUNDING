package com.web.common.global;

public class HttpResponseEntity {

    public static class ResponseResult<T> {
        private boolean success;
        private String message;
        private T data;

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
