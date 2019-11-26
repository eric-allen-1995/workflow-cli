/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.workflowcli.workflow.config;

import org.flowable.ui.modeler.rest.app.EditorGroupsResource;
import org.flowable.ui.modeler.rest.app.EditorUsersResource;
import org.flowable.ui.modeler.rest.app.StencilSetResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ComponentScan(
        value = {
                "org.flowable.ui.modeler.rest.app",
//                "org.flowable.ui.common.rest"
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = EditorUsersResource.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = EditorGroupsResource.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = StencilSetResource.class)
        }
)
@EnableAsync
/**
 * @Create: 2019/11/26 10:57
 * @Description: 移除 IDM 方面的配置,使用自定义接口
 */
public class AppDispatcherServletConfiguration implements WebMvcRegistrations {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDispatcherServletConfiguration.class);

    @Bean
    public SessionLocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LOGGER.debug("Configuring localeChangeInterceptor");
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        LOGGER.debug("Creating requestMappingHandlerMapping");
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        requestMappingHandlerMapping.setRemoveSemicolonContent(false);
        Object[] interceptors = {localeChangeInterceptor()};
        requestMappingHandlerMapping.setInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }
}
