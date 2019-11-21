package com.example.workflowcli.common.config.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

/**
 * @Create: 2019/11/21 17:31
 * @Description:
 */
@Component
public class RGsonHttpMessageConvert extends GsonHttpMessageConverter {
    public RGsonHttpMessageConvert() {
        super();
        this.setGson(gson());
    }
    private Gson gson(){
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class,new JsonToGsonSerializer());
        return builder.create();
    }
}
