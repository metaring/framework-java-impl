/**
 *    Copyright 2019 MetaRing s.r.l.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.metaring.framework;

final class ExecutionEnvironmentImpl implements ExecutionEnvironment, ExecutionEnvironmentProvider {
    public static final ExecutionEnvironment TEST = new ExecutionEnvironmentImpl("TEST");
    public static final ExecutionEnvironment DEVELOPMENT = new ExecutionEnvironmentImpl("DEVELOPMENT");
    public static final ExecutionEnvironment PRODUCTION = new ExecutionEnvironmentImpl("PRODUCTION");
    private String name;

    private ExecutionEnvironmentImpl(String name) {
        this.name = name;
    }

    public ExecutionEnvironmentImpl() {
        this(null);
    }

    public ExecutionEnvironment TEST() {
        return TEST;
    }

    public ExecutionEnvironment DEVELOPMENT() {
        return DEVELOPMENT;
    }

    public ExecutionEnvironment PRODUCTION() {
        return PRODUCTION;
    }

    public ExecutionEnvironment getByName(String executionEnvironmentName) {
        if (executionEnvironmentName != null) {
            if (executionEnvironmentName.equalsIgnoreCase("TEST")) {
                return TEST;
            }
            if (executionEnvironmentName.equalsIgnoreCase("DEVELOPMENT")) {
                return DEVELOPMENT;
            }
            if (executionEnvironmentName.equalsIgnoreCase("PRODUCTION")) {
                return PRODUCTION;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
