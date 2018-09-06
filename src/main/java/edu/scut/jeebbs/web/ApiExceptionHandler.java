package edu.scut.jeebbs.web;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final ErrorAttributes errorAttributes;
    private final ErrorProperties errorProperties;

    public ApiExceptionHandler(ErrorAttributes errorAttributes,
                               ErrorProperties errorProperties) {
        this.errorAttributes = errorAttributes;
        this.errorProperties = errorProperties;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> constraintViolationExceptionHandler(
            HttpServletRequest request, ConstraintViolationException ex) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request));
        body.put("status", BAD_REQUEST.value());

        String error = (String) body.get("error");
        if ("None".equalsIgnoreCase(error)) {
            body.put("error", "Constraint violations error");
        }

        String message = (String) body.get("message");
        if ("No message available".equalsIgnoreCase(message)) {
            body.put("message", ex.getMessage());
        }
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                     boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }

    private boolean isIncludeStackTrace(HttpServletRequest request) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        return !"false".equalsIgnoreCase(parameter);
    }

    private ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }


}
