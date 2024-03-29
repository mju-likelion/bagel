package org.mjulikelion.bagel.controller;

import static org.mjulikelion.bagel.errorcode.ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_ERROR;
import static org.mjulikelion.bagel.errorcode.ErrorCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.errorcode.ErrorCode;
import org.mjulikelion.bagel.errorcode.ValidationErrorCode;
import org.mjulikelion.bagel.exception.ApplicationAlreadyExistException;
import org.mjulikelion.bagel.exception.DateRangeException;
import org.mjulikelion.bagel.exception.FileStorageException;
import org.mjulikelion.bagel.exception.InvalidDataException;
import org.mjulikelion.bagel.exception.JpaException;
import org.mjulikelion.bagel.exception.RedisException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    //DateRangeException 예외를 처리하는 핸들러(날짜 범위에 맞지 않는 경우)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(DateRangeException.class)
    public ResponseEntity<ResponseDto<Void>> handleDateRangeException(DateRangeException dateRangeException) {
        log.error("DateRangeException: {}", dateRangeException.getMessage());
        ErrorCode errorCode = ErrorCode.DATE_RANGE_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.FORBIDDEN);
    }

    //MethodArgumentNotValidException 예외를 처리하는 핸들러(Body(dto)의 Validation에 실패한 경우)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
        if (fieldError == null) {
            return new ResponseEntity<>(
                    ResponseDto.res(HttpStatus.BAD_REQUEST.toString(), methodArgumentNotValidException.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        ValidationErrorCode validationErrorCode = ValidationErrorCode.resolveAnnotation(fieldError.getCode());
        String code = validationErrorCode.getCode();
        String message = validationErrorCode.getMessage() + " : " + fieldError.getDefaultMessage();
        log.error("MethodArgumentNotValidException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.BAD_REQUEST);
    }

    //HandlerMethodValidationException 예외를 처리하는 핸들러(HandlerMethod의 Validation에 실패한 경우(controller의 메소드의 파라미터에 대한 Validation))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ResponseDto<Void>> handleHandlerMethodValidationException(
            HandlerMethodValidationException handlerMethodValidationException) {
        ValidationErrorCode errorCode = ValidationErrorCode.VALIDATION;
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : "
                + handlerMethodValidationException.getDetailMessageArguments()[0].toString();
        log.error("HandlerMethodValidationException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.BAD_REQUEST);
    }

    //HttpMessageNotReadableException 예외를 처리하는 핸들러(Body가 잘못된 경우(json 형식이 잘못된 경우))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Void>> handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException httpMessageNotReadableException) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_FORMAT_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + httpMessageNotReadableException.getMessage();
        log.error("HttpMessageNotReadableException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.BAD_REQUEST);
    }

    //HttpRequestMethodNotSupportedException 예외를 처리하는 핸들러(요청의 메소드가 잘못된 경우)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto<Void>> handleHttpRequestMethodNotSupportedException(
            org.springframework.web.HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        log.error("HttpRequestMethodNotSupportedException: {}", httpRequestMethodNotSupportedException.getMessage());
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.METHOD_NOT_ALLOWED);
    }

    //NoResourceFoundException 예외를 처리하는 핸들러(리소스를 찾을 수 없는 경우(URI가 잘못된 경우))
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handleNoResourceFoundException(
            NoResourceFoundException noResourceFoundException) {
        log.error("NoResourceFoundException: {}", noResourceFoundException.getMessage());
        ErrorCode errorCode = ErrorCode.NO_RESOURCE_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.NOT_FOUND);
    }

    //Exception 예외를 처리하는 핸들러(해당 클래스에 정의되지 않은 예외를 처리하는 핸들러)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + exception.getClass();
        log.error("Exception: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // FileStorageException 예외를 처리하는 핸들러(파일 저장에 실패한 경우)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ResponseDto<Void>> handleFileStorageException(FileStorageException fileStorageException) {
        ErrorCode errorCode = fileStorageException.getErrorCode();
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + fileStorageException.getMessage();
        log.error("FileStorageException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ApplicationAlreadyExistsException 예외를 처리하는 핸들러(이미 지원서가 존재하는 경우)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ApplicationAlreadyExistException.class)
    public ResponseEntity<ResponseDto<Void>> handleApplicationAlreadyExistsException(
            ApplicationAlreadyExistException applicationAlreadyExistsException) {
        log.error("ApplicationAlreadyExistsException: {}", applicationAlreadyExistsException.getMessage());
        ErrorCode errorCode = applicationAlreadyExistsException.getErrorCode();
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.CONFLICT);
    }

    // InvalidDataException 예외를 처리하는 핸들러(유효하지 않은 데이터가 요청된 경우)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseDto<Void>> handleInvalidDataException(InvalidDataException invalidDataException) {
        ErrorCode errorCode = invalidDataException.getErrorCode();
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        log.error("InvalidDataException: {} : {}", message, invalidDataException.getMessage());
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.BAD_REQUEST);
    }

    //JpaException 예외를 처리하는 핸들러(SQL 오류가 발생한 경우)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(JpaException.class)
    public ResponseEntity<ResponseDto<Void>> handleJpaException(JpaException jpaException) {
        ErrorCode errorCode = jpaException.getErrorCode();
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + jpaException.getMessage();
        log.error("SqlException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //HttpMediaTypeNotAcceptableException
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ResponseDto<Void>> handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException) {
        ErrorCode errorCode = HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + httpMediaTypeNotAcceptableException.getMessage();
        log.error("HttpMediaTypeNotAcceptableException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.NOT_ACCEPTABLE);
    }

    //HttpMediaTypeNotSupportedException
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDto<Void>> handleHttpMediaTypeNotSupportedException(
            org.springframework.web.HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException) {
        ErrorCode errorCode = HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR;
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + httpMediaTypeNotSupportedException.getMessage();
        log.error("HttpMediaTypeNotSupportedException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    //RedisException 예외를 처리하는 핸들러(Redis 오류가 발생한 경우)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RedisException.class)
    public ResponseEntity<ResponseDto<Void>> handleRedisException(RedisException redisException) {
        ErrorCode errorCode = redisException.getErrorCode();
        String code = errorCode.getCode();
        String message = errorCode.getMessage() + " : " + redisException.getMessage();
        log.error("RedisException: {}", message);
        return new ResponseEntity<>(ResponseDto.res(code, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
