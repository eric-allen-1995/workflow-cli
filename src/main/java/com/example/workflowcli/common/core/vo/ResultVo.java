package com.example.workflowcli.common.core.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultVo {
    private int code; //状态码
    private String msg; //返回消息
    private Object data;// 返回数据

    public ResultVo(CodeEnums code) {
        this.code = code.code;
        this.msg = code.message;
    }

    public ResultVo(CodeEnums code, Object data) {
        this.code = code.code;
        this.msg = code.message;
        this.data = data;
    }
}
