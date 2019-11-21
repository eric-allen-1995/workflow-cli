package com.example.workflowcli.workflow.api;


import com.example.workflowcli.common.core.annotation.RunningLog;
import com.example.workflowcli.common.core.base.BaseController;
import com.example.workflowcli.common.core.vo.CodeEnums;
import com.example.workflowcli.common.core.vo.ResultVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Create: 2019/11/21 15:49
 * @Description:
 */
@Slf4j
@RunningLog
@RestController
@RequestMapping("/handleApi")
public class ProcessHandleApi extends BaseController {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private RepositoryService repositoryService;


    /* 流程部署相关 */
    @ResponseBody
    @ApiOperation(value = "部署流程,")
    @RequestMapping(value = "/deployProcess", method = RequestMethod.POST)
    @ApiImplicitParam(name = "classPath", value = "流程图资源路径", required = true, dataType = "String")
    public Map<String, Object> deployProcess(HttpServletRequest request,
                                             @PathVariable("classPath") String classPath) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(classPath)) {
            Deployment deploy = repositoryService.createDeployment().addClasspathResource(classPath).deploy();
            map.put("processId", deploy.getId());
            map.put("processName", deploy.getName());
            map.put("deployTime", deploy.getDeploymentTime());
            map.put("processVersion", deploy.getEngineVersion());
        }
        return map;
    }

    /* 流程实例相关 */
    @ResponseBody
    @ApiOperation(value = "启动流程,")
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    @ApiImplicitParam(name = "classPath", value = "流程图资源路径", required = true, dataType = "String")
    public ResultVo startProcess(HttpServletRequest request,
                                 @PathVariable("classPath") String classPath) {
        //1.DefinitionId
        String definitionId = "";
        //2.BusinessKey
        String businessKey = "";
        //3.Variables
        Map<String,Object> variables = new HashMap<>();
        //4.Start
        runtimeService.startProcessInstanceById(definitionId,businessKey,variables);
        //5.validate the process isRun?
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (task != null){
            return new ResultVo(CodeEnums.START_SUCCESS);
        }
        return new ResultVo(CodeEnums.START_ERROR);
    }


    /* 流程任务相关 */
    @ResponseBody
    @ApiOperation(value = "处理任务,")
    @RequestMapping(value = "/dealWithTask", method = RequestMethod.POST)
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")
    public ResultVo dealWithTask(HttpServletRequest request,
                                   @PathVariable("taskId") String taskId) {
        //1.UserID
        String assignee = "";
        //2.Variables
        Map<String,Object> variables = new HashMap<>();
        //3.Claim
        taskService.claim(taskId,assignee);
        //4.Complete
        taskService.complete(taskId,variables);
        //5.Assignee is consistent for the task current node
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (assignee.equals(task.getAssignee())){
            return new ResultVo(CodeEnums.START_SUCCESS);
        }
        return new ResultVo(CodeEnums.START_ERROR);
    }

}
