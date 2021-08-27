package top.slomo.miaosha.result;

public class CodeMsg {

    private int code;
	private String msg;
	
	//通用的错误码
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
	//登录模块 5002XX
	public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
	public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
	public static CodeMsg TOKEN_EMPTY = new CodeMsg(500310, "TOKEN不存在");
	
	//商品模块 5003XX
	
	//订单模块 5004XX
	public static CodeMsg NOT_ALLOWED_ORDER = new CodeMsg(500410, "无权查看非本人订单");
	
	//秒杀模块 5005XX
	public static CodeMsg STOCK_NOT_ENOUGH = new CodeMsg(500501, "库存不足");
	public static CodeMsg ALREADY_MIAOSHA = new CodeMsg(500502, "秒杀已经成功，请勿重复秒杀");
	public static CodeMsg MIAOSHA_FAILED = new CodeMsg(500502, "秒杀失败");

	private CodeMsg( ) {
	}
			
	private CodeMsg( int code,String msg ) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
