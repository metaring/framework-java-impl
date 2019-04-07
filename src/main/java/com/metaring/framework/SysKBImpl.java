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

import com.metaring.framework.type.DataRepresentation;
import com.metaring.framework.type.factory.DigitSeriesFactory;
import com.metaring.framework.type.factory.EmailFactory;
import com.metaring.framework.type.factory.EmailSeriesFactory;
import com.metaring.framework.type.factory.RealDigitSeriesFactory;
import com.metaring.framework.type.factory.TextSeriesFactory;
import com.metaring.framework.type.factory.TruthSeriesFactory;
import com.metaring.framework.util.log.LogMessageType;
import com.metaring.framework.util.log.LogMessageTypeProvider;
import com.metaring.framework.util.log.Logger;
import com.metaring.framework.util.log.LoggerFactory;

final class SysKBImpl extends ImmutableDataRepresentation implements SysKB, SysKBFactory {
    private String filenameOrContent;
    private final ExecutionEnvironmentProvider executionEnvironmentProvider;
    private final LoggerFactory loggerFactory;
    private String systemName;
    private LogMessageType systemLoggerMessageType;
    private Logger systemLogger;
    private Boolean systemTestMode;
    private String systemDataRepresentationPropertyName;
    private String systemNamePropertyName;
    private String systemExecutionEnvironmentPropertyName;
    private String systemLoggerLevelPropertyName;
    private String systemTestModePropertyName;
    private ExecutionEnvironment executionEnvironment;

    private SysKBImpl(String filenameOrContent, TextSeriesFactory textSeriesFactory, DigitSeriesFactory digitSeriesFactory, RealDigitSeriesFactory realDigitSeriesFactory, TruthSeriesFactory truthSeriesFactory, EmailFactory emailFactory, EmailSeriesFactory emailSeriesFactory, LogMessageTypeProvider logMessageTypeProvider, ExecutionEnvironmentProvider executionEnvironmentProvider, LoggerFactory loggerFactory, String systemDataRepresentationPropertyName, String systemNamePropertyName, String systemExecutionEnvironmentPropertyName, String systemLoggerLevelPropertyName, String systemTestModePropertyName) {
        super(filenameOrContent, textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory, emailSeriesFactory, logMessageTypeProvider);
        this.filenameOrContent = filenameOrContent;
        this.executionEnvironmentProvider = executionEnvironmentProvider;
        this.loggerFactory = loggerFactory;
        this.systemDataRepresentationPropertyName = systemDataRepresentationPropertyName;
        this.systemNamePropertyName = systemNamePropertyName;
        this.systemExecutionEnvironmentPropertyName = systemExecutionEnvironmentPropertyName;
        this.systemLoggerLevelPropertyName = systemLoggerLevelPropertyName;
        this.systemTestModePropertyName = systemTestModePropertyName;
        initProperties();
    }

    SysKBImpl(TextSeriesFactory textSeriesFactory, DigitSeriesFactory digitSeriesFactory, RealDigitSeriesFactory realDigitSeriesFactory, TruthSeriesFactory truthSeriesFactory, EmailFactory emailFactory, EmailSeriesFactory emailSeriesFactory, LogMessageTypeProvider logMessageTypeProvider, ExecutionEnvironmentProvider executionEnvironmentProvider, LoggerFactory loggerFactory, String systemDataRepresentationPropertyName, String systemNamePropertyName, String systemExecutionEnvironmentPropertyName, String systemLoggerLevelPropertyName, String systemTestModePropertyName) {
        this(null, textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory, emailSeriesFactory, logMessageTypeProvider, executionEnvironmentProvider, loggerFactory, systemDataRepresentationPropertyName, systemNamePropertyName, systemExecutionEnvironmentPropertyName, systemLoggerLevelPropertyName, systemTestModePropertyName);
    }

    public ExecutionEnvironment getSystemExecutionEnvironment() {
        return this.executionEnvironment;
    }

    public void reinit() {
        reinit(filenameOrContent);
        initProperties();
    }

    private void initProperties() {
        this.executionEnvironment = null;
        this.systemName = null;
        this.systemLoggerMessageType = null;
        this.systemLogger = null;
        if(!hasProperty(systemDataRepresentationPropertyName)) {
            return;
        }
        DataRepresentation systemDataRepresentation = get(systemDataRepresentationPropertyName);
        this.executionEnvironment = executionEnvironmentProvider.getByName(systemDataRepresentation.getText(systemExecutionEnvironmentPropertyName));
        this.systemName = systemDataRepresentation.getText(systemNamePropertyName);
        this.systemLoggerMessageType = systemDataRepresentation.getLogMessageType(systemLoggerLevelPropertyName);
        this.systemTestMode = systemDataRepresentation.getTruth(systemTestModePropertyName);
        this.systemTestMode = this.systemTestMode != null && this.systemTestMode != false;
    }

    public SysKB load(String filenameOrContent) {
        if (filenameOrContent == null && Thread.currentThread().getStackTrace()[2].getClassName().equals(this.getClass().getPackage().getName() + ".Core")) {
            filenameOrContent = "";
        }
        return new SysKBImpl(filenameOrContent, textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory, emailSeriesFactory, logMessageTypeProvider, executionEnvironmentProvider, loggerFactory, systemDataRepresentationPropertyName, systemNamePropertyName, systemExecutionEnvironmentPropertyName, systemLoggerLevelPropertyName, systemTestModePropertyName);
    }

    public SysKB create(String systemName, LogMessageType logMessageType, Boolean systemTestMode) {
        SysKBImpl sysKB = new SysKBImpl("", textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory, emailSeriesFactory, logMessageTypeProvider, executionEnvironmentProvider, loggerFactory, systemDataRepresentationPropertyName, systemNamePropertyName, systemExecutionEnvironmentPropertyName, systemLoggerLevelPropertyName, systemTestModePropertyName);
        sysKB.systemName = systemName;
        sysKB.systemLoggerMessageType = logMessageType;
        sysKB.systemTestMode = systemTestMode;
        return sysKB;
    }

    public SysKB create(String systemName, LogMessageType logMessageType) {
        return create(systemName, logMessageType, false);
    }

    public SysKB create(String systemName, Boolean systemTestMode) {
        return create(systemName, logMessageTypeProvider.INFO(), systemTestMode);
    }

    public SysKB create(String systemName) {
        return create(systemName, logMessageTypeProvider.INFO(), false);
    }

    public SysKB create(DataRepresentation dataRepresentation) {
        return new SysKBImpl(dataRepresentation.toJson(), textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory, emailSeriesFactory, logMessageTypeProvider, executionEnvironmentProvider, loggerFactory, systemDataRepresentationPropertyName, systemNamePropertyName, systemExecutionEnvironmentPropertyName, systemLoggerLevelPropertyName, systemTestModePropertyName);
    }

    public String getSystemName() {
        return this.systemName;
    }

    public Logger getSystemLogger() {
        if (this.systemLogger == null) {
            this.systemLogger = this.createSystemLogger();
        }
        return this.systemLogger;
    }

    public LogMessageType getSystemLoggerMessageType() {
        return this.systemLoggerMessageType;
    }

    public Boolean isSystemInTestMode() {
        return systemTestMode;
    }

    public Logger createSystemLogger() {
        return this.loggerFactory.create(this.getSystemName(), this.getSystemLoggerMessageType());
    }
}