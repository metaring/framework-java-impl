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
import com.metaring.framework.util.log.Logger;
import com.metaring.framework.util.log.LoggerFactory;

final class LoggerFactoryImpl implements LoggerFactory {
    private LogMessageTypeProvider logMessageTypeProvider;
    private Long defaultStackPosition;

    public LoggerFactoryImpl(LogMessageTypeProvider logMessageTypeProvider) {
        this.logMessageTypeProvider = logMessageTypeProvider;
        this.defaultStackPosition = 3l;
    }

    public Logger create(String loggedSystemName) {
        Logger logger = this.create(loggedSystemName, null, this.defaultStackPosition);
        return logger;
    }

    public Logger create(String loggedSystemName, LogMessageType minimumLogMessageType) {
        Logger logger = this.create(loggedSystemName, minimumLogMessageType, this.defaultStackPosition);
        return logger;
    }

    public Logger create(String loggedSystemName, Long stackPosition) {
        Logger logger = this.create(loggedSystemName, null, stackPosition);
        return logger;
    }

    public Logger create(String loggedSystemName, LogMessageType minimumLogMessageType, Long stackPosition) {
        if (loggedSystemName == null) {
            return null;
        }
        if (minimumLogMessageType == null) {
            minimumLogMessageType = this.logMessageTypeProvider.INFO();
        }
        LoggerImpl logger = new LoggerImpl(loggedSystemName, minimumLogMessageType, stackPosition);
        return logger;
    }
}
