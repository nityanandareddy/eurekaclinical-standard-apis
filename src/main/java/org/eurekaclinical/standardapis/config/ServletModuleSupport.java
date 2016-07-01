package org.eurekaclinical.standardapis.config;

/*-
 * #%L
 * Eureka! Clinical Standard APIs
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import org.eurekaclinical.standardapis.props.EurekaClinicalProperties;

/**
 *
 * @author Andrew Post
 */
public final class ServletModuleSupport {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServletModuleSupport.class);
    private static final String CAS_CALLBACK_PATH = "/proxyCallback";
    private final EurekaClinicalProperties properties;
    private final String contextPath;

    public ServletModuleSupport(String contextPath,
            EurekaClinicalProperties inProperties) {
        this.properties = inProperties;
        this.contextPath = contextPath;
    }

    public Map<String, String> getCasProxyFilterInitParamsForWebApp() {
        Map<String, String> params = getCasProxyFilterInitParams();
        if (LOGGER.isDebugEnabled()) {
            this.printParams(params);
        }
        return params;
    }

    public Map<String, String> getCasProxyFilterInitParamsForWebService() {
        Map<String, String> params = getCasProxyFilterInitParams();
        params.put("redirectAfterValidation", "false");
        if (LOGGER.isDebugEnabled()) {
            this.printParams(params);
        }
        return params;
    }

    public Map<String, String> getCasAuthenticationFilterInitParams() {
        Map<String, String> params = new HashMap<>();
        params.put("casServerLoginUrl", this.getCasLoginUrl());
        params.put("serverName", this.properties.getProxyCallbackServer());
        params.put("renew", "false");
        params.put("gateway", "false");
        if (LOGGER.isDebugEnabled()) {
            this.printParams(params);
        }
        return params;
    }

    public Map<String, String> getServletRequestWrapperFilterInitParams() {
        Map<String, String> params = new HashMap<>();
        params.put("roleAttribute", "authorities");
        if (LOGGER.isDebugEnabled()) {
            this.printParams(params);
        }
        return params;
    }
    
    protected void printParams(Map<String, String> inParams) {
        for (Map.Entry<String, String> entry : inParams.entrySet()) {
            LOGGER.debug(entry.getKey() + " -> " + entry.getValue());
        }
    }
    
    protected String getCasLoginUrl() {
        return this.properties.getCasLoginUrl();
    }

    private String getProxyCallbackUrl() {
        return this.properties.getProxyCallbackServer() + this.contextPath
                + CAS_CALLBACK_PATH;
    }

    private String getProxyReceptorUrl() {
        return CAS_CALLBACK_PATH;
    }
    
    private Map<String, String> getCasProxyFilterInitParams() {
        Map<String, String> params = new HashMap<>();
        params.put("acceptAnyProxy", "true");
        params.put("proxyCallbackUrl", this.getProxyCallbackUrl());
        params.put("proxyReceptorUrl", this.getProxyReceptorUrl());
        params.put("casServerUrlPrefix", this.properties.getCasUrl());
        params.put("serverName", this.properties.getProxyCallbackServer());
        return params;
    }

}