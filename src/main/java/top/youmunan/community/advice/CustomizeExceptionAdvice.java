package top.youmunan.community.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import top.youmunan.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice
public class CustomizeExceptionAdvice{

    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model) {
        HttpStatus status = getStatus(request);

        if(ex instanceof CustomizeException){
            model.addAttribute("errorMsg", ((CustomizeException) ex).getErrorMsg());
        }else{
            model.addAttribute("errorMsg", getStatus(request));
        }
        return new ModelAndView("error1");

    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
