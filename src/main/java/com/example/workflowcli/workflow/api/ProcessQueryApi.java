package com.example.workflowcli.workflow.api;

import com.example.workflowcli.common.core.annotation.RunningLog;
import com.example.workflowcli.common.core.base.BaseController;
import com.example.workflowcli.common.core.dto.ProDefiDto;
import com.example.workflowcli.common.core.dto.ProInstDto;
import com.example.workflowcli.common.core.dto.ProMoldDto;
import com.example.workflowcli.common.core.dto.ProTaskDto;
import com.example.workflowcli.common.util.ObjectUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Create: 2019/11/21 15:49
 * @Description:
 */
@Slf4j
@RunningLog
@RestController
@RequestMapping("/queryApi")
public class ProcessQueryApi extends BaseController {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private RepositoryService repositoryService;

    /* 流程定义 */
    @ResponseBody
    @ApiOperation(value = "查询流程定义列表(可分页)")
    @RequestMapping(value = "/queryProceDefiList", method = RequestMethod.GET)
    public List<ProDefiDto> queryProceDefiList() {
        List<ProDefiDto> result = new ArrayList<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();
        if (list.size() != 0) {
            result = ObjectUtil.convert2Dto(new ProDefiDto(), list);
        }
        return result;
    }

    @ResponseBody
    @ApiOperation(value = "查询流程定义详情(带模糊查询)")
    @RequestMapping(value = "/queryProceDefiDetail/{defiName}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "defiName", value = "定义名称", required = true, dataType = "String")
    public List<ProDefiDto> queryProceDefiDetail(@PathVariable("defiName") String defiName) {
        List<ProDefiDto> result = new ArrayList<>();
        if (StringUtils.isNotBlank(defiName)) {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionNameLike(defiName).list();
            if (list.size() != 0) {
                result = ObjectUtil.convert2Dto(new ProDefiDto(), list);
            }
        }
        return result;
    }

    /* 流程实例 */
    @ResponseBody
    @ApiOperation(value = "查询流程实例列表(可分页)")
    @RequestMapping(value = "/queryProceInstList", method = RequestMethod.GET)
    public List<ProInstDto> queryProceInstList() {
        List<ProInstDto> result = new ArrayList<>();
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        if (list.size() != 0) {
            result = ObjectUtil.convert2Dto(new ProInstDto(), list);
        }
        return result;
    }

    @ResponseBody
    @ApiOperation(value = "查询流程实例详情(带模糊查询)")
    @RequestMapping(value = "/queryProceInstDetail/{instName}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "instName", value = "实例名称", required = true, dataType = "String")
    public ProInstDto queryProceInstDetail(@PathVariable("instName") String instName) {
        if (StringUtils.isNotBlank(instName)) {
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceNameLike(instName).singleResult();
            if (instance != null) {
                return new ProInstDto(instance);
            }
        }
        return null;
    }

    /* 流程模型 */
    @ResponseBody
    @ApiOperation(value = "查询流程模型列表(可分页)")
    @RequestMapping(value = "/queryProceMoldList", method = RequestMethod.GET)
    public List<ProMoldDto> queryProceMoldList() {
        List<ProMoldDto> result = new ArrayList<>();
        List<Model> list = repositoryService.createModelQuery().list();
        if (list.size() != 0) {
            result = ObjectUtil.convert2Dto(new ProMoldDto(), list);
        }
        return result;
    }

    /* 流程任务 */
    @ResponseBody
    @ApiOperation(value = "查询流程任务列表(个人,可分页)")
    @RequestMapping(value = "/queryProceTaskList/{userId}", method = RequestMethod.GET)
    public List<ProTaskDto> queryProceTaskList(@PathVariable("userId") String userId) {
        List<ProTaskDto> result = new ArrayList<>();
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
        if (list.size() != 0) {
            result = ObjectUtil.convert2Dto(new ProTaskDto(), list);
        }
        return result;
    }

    @ResponseBody
    @ApiOperation(value = "查询流程任务详情")
    @RequestMapping(value = "/queryProceTaskDetail/{taskId}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")
    public ProTaskDto queryProceTaskDetail(@PathVariable("taskId") String taskId) {
        if (StringUtils.isNotBlank(taskId)) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                return new ProTaskDto(task);
            }
        }
        return null;
    }

}
