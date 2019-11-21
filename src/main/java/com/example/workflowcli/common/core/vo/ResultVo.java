package com.example.workflowcli.common.core.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultVo {
    private int code;
    private String msg;
    private Object data;// 返回数据

    public ResultVo(CodeEnums code) {
        this.code = code.code;
        this.msg = code.message;
    }

    /**
     * 自定义返回体
     * @param code 枚举
     * @param data 数据
     * @return
     */
    public ResultVo CUSTOM(CodeEnums code,Object data) {
        ResultVo resultVo =  new ResultVo(code);
        resultVo.setData(data);
        return resultVo;
    }

}
