package com.example.workflowcli.workflow.config;

import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Create: 2019/11/26 11:08
 * @Description:
 */
@RestController
@RequestMapping("/login")
public class FlowableController  {
    /**
     * 获取默认的管理员信息
     * @return
     */
    @RequestMapping(value = "/rest/account", method = RequestMethod.GET, produces = "application/json")
    public UserRepresentation getAccount() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("admin");
        userRepresentation.setEmail("Eric.Allen@tom.com");
        userRepresentation.setFullName("Eric.Allen");
        userRepresentation.setLastName("Allen");
        userRepresentation.setFirstName("Eric");
        List<String> privileges = new ArrayList<>();
        privileges.add(DefaultPrivileges.ACCESS_MODELER);
        privileges.add(DefaultPrivileges.ACCESS_IDM);
        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
        privileges.add(DefaultPrivileges.ACCESS_TASK);
        privileges.add(DefaultPrivileges.ACCESS_REST_API);
        userRepresentation.setPrivileges(privileges);
        return userRepresentation;
    }
}
