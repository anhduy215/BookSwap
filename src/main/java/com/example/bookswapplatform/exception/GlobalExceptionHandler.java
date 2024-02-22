package com.example.bookswapplatform.exception;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.annotation.Nonnull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // bắt lỗi khi method không hợp lệ
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  @Nonnull final HttpHeaders headers,
                                                                  @Nonnull final HttpStatusCode status,
                                                                  @Nonnull final WebRequest request) {

        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    // bắt lỗi khi
    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex,
                                                         @Nonnull final HttpHeaders headers,
                                                         @Nonnull final HttpStatusCode status,
                                                         @Nonnull final WebRequest request) {

        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    // bắt lỗi khi giá trị không đúng định dạng
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                        @Nonnull final HttpHeaders headers,
                                                        @Nonnull final HttpStatusCode status,
                                                        @Nonnull final WebRequest request) {

        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // bắt lỗi khi request thiếu
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                     @Nonnull final HttpHeaders headers,
                                                                     @Nonnull final HttpStatusCode status,
                                                                     @Nonnull final WebRequest request) {

        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // bắt lỗi khi thiếu parameter
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, @Nonnull final HttpHeaders headers, @Nonnull final HttpStatusCode status, @Nonnull final WebRequest request) {

        //
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    //bắt lỗi khi gọi sai method
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {

        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    //bắt lỗi khi @Validated thất bại
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getConstraintName() + ": " + ex.getMessage());

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, @Nonnull final HttpHeaders headers, @Nonnull final HttpStatusCode status, @Nonnull final WebRequest request) {
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, @Nonnull final HttpHeaders headers, @Nonnull final HttpStatusCode status, @Nonnull final WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, @Nonnull final HttpHeaders headers, @Nonnull final HttpStatusCode status, @Nonnull final WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        List<String> errs = new ArrayList<String>();
        errs.add(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Resource Not Found" ,errs);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Object> handleParseException (ParseException ex) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler(CancelException.class)
    public ResponseEntity<Object> handleCancelOrderException (CancelException ex) {
        List<String> errs = new ArrayList<String>();
        errs.add(ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Cancel Order Error" ,errs);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
