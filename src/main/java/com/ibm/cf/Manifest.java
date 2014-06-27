/*
 * Copyright IBM Corp. 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.cf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Manifest {
  private static final Logger LOG = Logger.getLogger(Manifest.class.getName());
  private String appName;
  private int memory;
  private int instances = 1;
  private String buildpack;
  private String host;
  private String domain;
  private String path;
  private int timeout = 60;
  private String command;
  private List<String> services = new ArrayList<String>();
  private Map<String, String> envVars = new HashMap<String, String>();
  private boolean noRoute;
  
  public Manifest() {};
  
  public Manifest(HttpServletRequest request) {
    this.appName = request.getParameter("appName");
    this.memory = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("memory"), "128"));
    this.instances = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("instances"), "1"));
    this.buildpack = request.getParameter("buildpack");
    this.host = request.getParameter("host");
    this.domain = request.getParameter("domain");
    this.path = request.getParameter("path");
    this.timeout = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("timeout"), "60"));
    this.command = request.getParameter("command");
    this.noRoute = "on".equalsIgnoreCase(request.getParameter("route")) ? true : false;
    String servicesStr = request.getParameter("services");
    if (servicesStr.length() != 0) {
      this.services = Arrays.asList(request.getParameter("services").split(","));
    }
    ObjectMapper mapper = new ObjectMapper();
    try {
      TypeReference<HashMap<String,String>> typeRef = new TypeReference<HashMap<String,String>>() {};
      this.envVars = mapper.readValue(request.getParameter("envVars"), typeRef); 
    } catch (Exception e) {
      LOG.warn("Error parsing JSON object for environment variables", e);
    } 
  }
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }
  public int getMemory() {
    return memory;
  }
  public void setMemory(int memory) {
    this.memory = memory;
  }
  public int getInstances() {
    return instances;
  }
  public void setInstances(int instances) {
    this.instances = instances;
  }
  public String getBuildpack() {
    return buildpack;
  }
  public void setBuildpack(String buildpack) {
    this.buildpack = buildpack;
  }
  public String getHost() {
    return host;
  }
  public void setHost(String host) {
    this.host = host;
  }
  public String getDomain() {
    return domain;
  }
  public void setDomain(String domain) {
    this.domain = domain;
  }
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public int getTimeout() {
    return timeout;
  }
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }
  public String getCommand() {
    return command;
  }
  public void setCommand(String command) {
    this.command = command;
  }
  public List<String> getServices() {
    return services;
  }
  public void setServices(List<String> services) {
    this.services = services;
  }
  public Map<String, String> getEnvVars() {
    return envVars;
  }
  public void setEnvVars(Map<String, String> envVars) {
    this.envVars = envVars;
  }
  public boolean isNoRoute() {
    return noRoute;
  }
  public void setNoRoute(boolean noRoute) {
    this.noRoute = noRoute;
  }
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("---\n");
    buf.append("applications:\n");
    buf.append("- name: " + appName + "\n");
    if(memory > 0) {
      buf.append("  memory: " + memory + "M\n");
    }
    if(instances > 1) {
      buf.append("  instances: " + instances + "\n");
    }
    if(!StringUtils.isBlank(path)) {
      buf.append("  path: " + path + "\n" );
    }
    if(!StringUtils.isBlank(buildpack)) {
      buf.append("  buildpack: " + buildpack + "\n");
    }
    if(!StringUtils.isBlank(host)) {
      buf.append("  host: " + host + "\n");
    }
    if(!StringUtils.isBlank(domain)) {
      buf.append("  domain: " + domain + "\n");
    }
    if(!StringUtils.isBlank(command)) {
      buf.append("  command: " + command + "\n");  
    }
    if(timeout != 0) {
      buf.append("  timeout: " + timeout + "\n");
    }
    if(noRoute) {
      buf.append("  no-route: " + noRoute + "\n");
    }
    if(services.size() > 0) {
      buf.append("  services:\n");
      for(String service : services) {
        buf.append("  - " + service + "\n");
      }
    }
    Set<String> keys = envVars.keySet();
    if(keys.size() > 0) {
      buf.append("  env:\n");
      for(String key : keys) {
        buf.append("    " + key + ": " + envVars.get(key) + "\n");
      }
    }
    return buf.toString();
  }
  
  
}

