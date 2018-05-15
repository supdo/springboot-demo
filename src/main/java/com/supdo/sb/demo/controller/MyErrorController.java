package com.supdo.sb.demo.controller;

import com.supdo.sb.demo.plugin.Result;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MyErrorController extends BaseController implements ErrorController{

    private ErrorAttributes errorAttributes;

    private final static String ERROR_PATH = "/error";
    public static final String DEFAULT_ERROR_VIEW = "error";

    public MyErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * Supports the HTML Error View
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorHtml(HttpServletRequest request, Map<String, Object> map) {
        Map<String, Object> errors = new HashMap<>();
        try {
            errors =  getErrorAttributes(request, true);
            if(!errors.containsKey("exception")) errors.put("exception", "");
            String exception = errors.get("exception").toString();
            if(exception.equals(UnauthenticatedException.class.getName())){
                result.simple(true, "请登录系统。");
                result.setUrl(errors.get("path").toString());
                return "redirect:login";
            }else if(exception.equals(UnauthorizedException.class.getName())){
                result.simple(true, "您的权限不足。");
            }else {
                result.simple(true, "发生了异常。");
            }
            result.setItems(errors);
        }catch(Exception e){
            result.simple(true, "发生了异常。");
            errors.put("message", e.getMessage());
            errors.put("trace", e.getStackTrace().toString());
            errors.put("exception", e.getClass().getName());
            errors.put("error",e.getCause().toString());
            result.setItems(errors);
        }
        map.put("result", result);
        return render(DEFAULT_ERROR_VIEW);
    }

    /**
     * Supports other formats like JSON, XML
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public Result error(HttpServletRequest request) {
        Map<String, Object> errors = getErrorAttributes(request, getTraceParameter(request));
        HttpStatus status = getStatus(request);
        //ResponseEntity<Map<String, Object>> res = new ResponseEntity<Map<String, Object>>(body, status);
        result.simple(true, "发生了异常。");
        result.setItems(errors);
        result.setCode(status.toString());
        return result;
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
