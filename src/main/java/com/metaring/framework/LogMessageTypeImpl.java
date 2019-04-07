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

import com.metaring.framework.util.log.LogMessageType;
import com.metaring.framework.util.log.LogMessageTypeProvider;

final class LogMessageTypeImpl implements LogMessageType, LogMessageTypeProvider {
    public static final LogMessageType ENTERING = new LogMessageTypeImpl("ENTERING", 0l);
    public static final LogMessageType EXITING = new LogMessageTypeImpl("EXITING", 0l);
    public static final LogMessageType INFO = new LogMessageTypeImpl("INFO", 0l);
    public static final LogMessageType DEBUG = new LogMessageTypeImpl("DEBUG", 0l);
    public static final LogMessageType CONFIG = new LogMessageTypeImpl("CONFIG", 0l);
    public static final LogMessageType WARNING = new LogMessageTypeImpl("WARNING", 0l);
    public static final LogMessageType SEVERE = new LogMessageTypeImpl("SEVERE", 0l);
    private String name;
    private Long priorityLevel;

    public LogMessageTypeImpl() {
        this(null, null);
    }

    private LogMessageTypeImpl(String name, Long priorityLevel) {
        this.name = name;
        this.priorityLevel = priorityLevel;
    }

    public String getName() {
        return this.name;
    }

    public Long getPriorityLevel() {
        return this.priorityLevel;
    }

    public LogMessageType getByName(String logMessageTypeName) {
        if (logMessageTypeName != null) {
            if (logMessageTypeName.equalsIgnoreCase("ENTERING")) {
                return ENTERING;
            }
            if (logMessageTypeName.equalsIgnoreCase("EXITING")) {
                return EXITING;
            }
            if (logMessageTypeName.equalsIgnoreCase("INFO")) {
                return INFO;
            }
            if (logMessageTypeName.equalsIgnoreCase("DEBUG")) {
                return DEBUG;
            }
            if (logMessageTypeName.equalsIgnoreCase("CONFIG")) {
                return CONFIG;
            }
            if (logMessageTypeName.equalsIgnoreCase("WARNING")) {
                return WARNING;
            }
            if (logMessageTypeName.equalsIgnoreCase("SEVERE")) {
                return SEVERE;
            }
        }
        return null;
    }

    public LogMessageType getByPriorityLevel(Long priorityLevel) {
        if (priorityLevel != null) {
            if (priorityLevel == INFO.getPriorityLevel()) {
                return INFO;
            }
            if (priorityLevel == DEBUG.getPriorityLevel()) {
                return DEBUG;
            }
            if (priorityLevel == CONFIG.getPriorityLevel()) {
                return CONFIG;
            }
            if (priorityLevel == WARNING.getPriorityLevel()) {
                return WARNING;
            }
            if (priorityLevel == SEVERE.getPriorityLevel()) {
                return SEVERE;
            }
        }
        return null;
    }

    public LogMessageType ENTERING() {
        return ENTERING;
    }

    public LogMessageType EXITING() {
        return EXITING;
    }

    public LogMessageType INFO() {
        return INFO;
    }

    public LogMessageType DEBUG() {
        return DEBUG;
    }

    public LogMessageType CONFIG() {
        return CONFIG;
    }

    public LogMessageType WARNING() {
        return WARNING;
    }

    public LogMessageType SEVERE() {
        return SEVERE;
    }

    public String toString() {
        return this.name;
    }

    public String toJson() {
        return this.name;
    }
}
