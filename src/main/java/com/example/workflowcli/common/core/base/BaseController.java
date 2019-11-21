package com.example.workflowcli.common.core.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseController extends BaseLogEnable {

    /**
     * 设置成功响应代码
     */
    protected ResponseEntity<Map> setSuccessModelMap(Object data) {
        return setModelMap(HttpStatus.OK.value(), data);
    }

    /**
     * 设置响应代码
     */
    protected ResponseEntity<Map> setModelMap(Integer code) {
        return setModelMap(code, null);
    }

    /**
     * 设置响应代码
     */
    protected ResponseEntity<Map> setModelMap(Integer code, Object data) {
        Map<String, Object> map = new HashMap<>();
        if (data != null) {
            map.put("content", data);
        }
        map.put("code", code);
        map.put("timestamp", System.currentTimeMillis());
        if (code.equals(HttpStatus.OK.value())) {
            return ResponseEntity.ok(map);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    /**
     * 设置响应代码
     */
    protected ResponseEntity<Map> setAccessModelMap(Integer code, Object data) {
        Map<String, Object> map = new HashMap<>();
        if (data != null) {
            map.put("content", data);
        }
        map.put("code", code);
        map.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(map);
    }


}
