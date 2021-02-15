package com.rymcu.forest.dto.result;

import lombok.Data;

import java.io.Serializable;

/**
 * Result 接口返回数据格式
 *
 * @author suwen
 * @date 2021/2/15 10:57
 */
@Data
public final class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 成功标志 */
  private boolean success = true;

  /** 返回处理消息 */
  private String message = "操作成功！";

  /** 返回代码 */
  private Integer code = 0;

  /** 返回数据对象 data */
  private T result;

  /** 时间戳 */
  private long timestamp = System.currentTimeMillis();

  public static final Integer RETURN_CODE_SUCCESS = 200;
  public static final Integer RETURN_CODE_NO_AUTH = 500;
  public static final Integer RETURN_CODE_SERVER_ERROR = 500;

  public Result<T> success(String message) {
    this.message = message;
    this.code = RETURN_CODE_SUCCESS;
    this.success = true;
    return this;
  }

  public static <T> Result<T> OK() {
    Result<T> r = new Result<T>();
    r.setSuccess(true);
    r.setCode(RETURN_CODE_SUCCESS);
    r.setMessage("成功");
    return r;
  }

  public static <T> Result<T> OK(T data) {
    Result<T> r = new Result<T>();
    r.setSuccess(true);
    r.setCode(RETURN_CODE_SUCCESS);
    r.setResult(data);
    return r;
  }

  public static <T> Result<T> OK(String msg, T data) {
    Result<T> r = new Result<T>();
    r.setSuccess(true);
    r.setCode(RETURN_CODE_SUCCESS);
    r.setMessage(msg);
    r.setResult(data);
    return r;
  }

  public static Result<Object> error(String msg) {
    return error(RETURN_CODE_SERVER_ERROR, msg);
  }

  public static Result<Object> error(int code, String msg) {
    Result<Object> r = new Result<Object>();
    r.setCode(code);
    r.setMessage(msg);
    r.setSuccess(false);
    return r;
  }

  /** 无权限访问返回结果 */
  public static Result<Object> noAuth(String msg) {
    return error(RETURN_CODE_NO_AUTH, msg);
  }
}
