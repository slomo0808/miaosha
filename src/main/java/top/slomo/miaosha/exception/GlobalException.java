package top.slomo.miaosha.exception;

import top.slomo.miaosha.result.CodeMsg;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class GlobalException extends RuntimeException {
    private final CodeMsg codeMsg;

    public GlobalException(CodeMsg msg) {
        super(msg.getMsg());
        this.codeMsg = msg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
