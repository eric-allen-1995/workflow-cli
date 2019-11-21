package com.example.workflowcli.workflow.api;

import com.example.workflowcli.common.core.annotation.RunningLog;
import com.example.workflowcli.common.core.base.BaseController;
import com.example.workflowcli.common.core.vo.CodeEnums;
import com.example.workflowcli.common.core.vo.ResultVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Create: 2019/11/21 15:49
 * @Description:
 */
@Slf4j
@RunningLog
@RestController
@RequestMapping("/uploadapi")
public class ProcessUploadApi extends BaseController {

    @ResponseBody
    @ApiOperation(value = "上传流程图")
    @RequestMapping(value = "/uploadBpmn", method = RequestMethod.POST)
    public ResultVo uploadBpmn(HttpServletRequest request){
        return new ResultVo(CodeEnums.UNDEVELOPED);
    }
}
