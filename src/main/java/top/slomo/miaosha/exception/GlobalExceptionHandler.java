package top.slomo.miaosha.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;

import java.util.List;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> bindExceptionHandler(Exception e) {
        e.printStackTrace();
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getAllErrors();
            ObjectError err = allErrors.get(0);
            CodeMsg cm = CodeMsg.BIND_ERROR.fillArgs(err.getDefaultMessage());
            return Result.error(cm);
        }

        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCodeMsg());
        }

        return Result.error(CodeMsg.SERVER_ERROR);

    }
}
