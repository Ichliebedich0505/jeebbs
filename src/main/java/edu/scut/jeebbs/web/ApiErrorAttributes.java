package edu.scut.jeebbs.web;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Jeebbs Api implementation of {@link ErrorAttributes}. Provides the following attributes
 * when possible:
 * <ul>
 * <li>timestamp - The time that the errors were extracted </li>
 * <li>status - The http status code</li>
 * <li>error - The error reason of http status code</li>
 * <li>exception - The class name of the root exception</li>
 * <li>message - The exception message</li>
 * <li>errors - Any {@link ObjectError}s from a {@link BindingResult} exception</li>
 * <li>trace - The exception stack trace</li>
 * <li>path - The URL path when the exception was raised</li>
 * </ul>
 */
@ApiModel(description = "Request error attributes")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiErrorAttributes
        implements ErrorAttributes, HandlerExceptionResolver, Ordered {
    private static final String ERROR_ATTRIBUTE = ApiErrorAttributes.class.getName()
            + ".ERROR";

    private final boolean includeException;

    public ApiErrorAttributes() {
        this(false);
    }

    public ApiErrorAttributes(boolean includeException) {
        this.includeException = includeException;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        storeErrorAttributes(request, ex);
        return null;
    }

    private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
        request.setAttribute(ERROR_ATTRIBUTE, ex);
    }

    /**
     * Returns a {@link Map} of the error attributes. The map can be used as the model of
     * an error page {@link ModelAndView}, or returned as a {@link ResponseBody}.
     *
     * @param webRequest        the source request
     * @param includeStackTrace if stack trace elements should be included
     * @return a map of error attributes
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                  boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        addStatus(errorAttributes, webRequest);
        addErrorDetails(errorAttributes, webRequest, includeStackTrace);
        addPath(errorAttributes, webRequest);
        return errorAttributes;
    }

    private void addStatus(Map<String, Object> errorAttributes,
                           RequestAttributes requestAttributes) {
        Integer status = getAttribute(requestAttributes,
                "javax.servlet.error.status_code");
        if (status == null) {
            errorAttributes.put("status", 999);
            errorAttributes.put("error", "None");
            return;
        }
        errorAttributes.put("status", status);
        try {
            errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());
        } catch (Exception ex) {
            // Unable to obtain a reason
            errorAttributes.put("error", "Http Status " + status);
        }
    }

    private void addErrorDetails(Map<String, Object> errorAttributes,
                                 WebRequest webRequest, boolean includeStackTrace) {
        Throwable error = getError(webRequest);
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
            if (this.includeException) {
                errorAttributes.put("exception", error.getClass().getName());
            }
            addErrorMessage(errorAttributes, error);
            if (includeStackTrace) {
                addStackTrace(errorAttributes, error);
            }
        }
        Object message = getAttribute(webRequest, "javax.servlet.error.message");
        if ((!StringUtils.isEmpty(message) || errorAttributes.get("message") == null)
                && !(error instanceof BindingResult)) {
            errorAttributes.put("message",
                    StringUtils.isEmpty(message) ? "No message available" : message);
        }
    }

    private void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            errorAttributes.put("message", error.getMessage());
            return;
        }
        if (result.getErrorCount() > 0) {
            errorAttributes.put("errors", result.getAllErrors());
            errorAttributes.put("message",
                    "Validation failed for object='" + result.getObjectName()
                            + "'. Error count: " + result.getErrorCount());
        } else {
            errorAttributes.put("message", "No errors");
        }
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void addPath(Map<String, Object> errorAttributes,
                         RequestAttributes requestAttributes) {
        String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

    /**
     * Return the underlying cause of the error or {@code null} if the error cannot be
     * extracted.
     *
     * @param webRequest the source request
     * @return the {@link Exception} that caused the error or {@code null}
     */
    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * for swagger2 comment
     */
    @ApiModelProperty(value = "The http status code", readOnly = true, required = true, example = "int", position = 1)
    private Integer status;
    @ApiModelProperty(value = "The error reason", readOnly = true, required = true, position = 2)
    private String error;
    @ApiModelProperty(value = "The URL path when the exception was raised", readOnly = true, required = true, position = 3)
    private String path;
    @ApiModelProperty(value = "The class name of the root exception (if configured)", readOnly = true, position = 4)
    private String exception;
    @ApiModelProperty(value = "The exception message", readOnly = true, example = "String", position = 5)
    private Object message;
    @ApiModelProperty(value = "Any {@link ObjectError}s from a {@link BindingResult} exception",
            readOnly = true, example = "String", reference = "List", position = 6)
    private List<ObjectError> errors;
    @ApiModelProperty(value = "The exception stack trace", readOnly = true, example = "String", position = 7)
    private String trace;
    @ApiModelProperty(value = "The time that the errors were extracted", readOnly = true, position = 8)
    private Date timestamp;
}
