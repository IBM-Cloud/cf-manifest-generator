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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Manifest {
  private String appName;
  private int memory;
  private int instances = 1;
  private String buildpack;
  private String host;
  private String domain;
  private String path;
  private int timeout = 60;
  private String command;
  private List<String> services;
  private Map<String, String> envVars;
  private boolean noRoute;
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

