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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.metaring.framework.util.log.LogMessageType;
import com.metaring.framework.util.log.Logger;

final class LoggerImpl implements Logger {
    private String loggedSystemName;
    private LogMessageType minimumLogLevel;
    private java.util.logging.Logger javaLogger;
    private String blank;
    private Long actualStackPosition;
    private int stackPosition;
    private static final String LOG_SEVERE_TAG = LogMessageTypeImpl.SEVERE.getName();

    public LoggerImpl(String loggedSystemName, LogMessageType minimumLogLevel, Long stackPosition) {
        this.blank = "";
        this.loggedSystemName = loggedSystemName;
        this.setActualStackPosition(stackPosition);
        this.setMinimumLogLevel(minimumLogLevel);
        this.setJavaLogger();
    }

    private void setJavaLogger() {
        this.javaLogger = java.util.logging.Logger.getLogger(this.loggedSystemName);
        java.util.logging.Logger parentLogger = this.javaLogger.getParent();
        if (parentLogger != null) {
            for (Handler h : parentLogger.getHandlers()) {
                parentLogger.removeHandler(h);
            }
        }
        for (Handler h : this.javaLogger.getHandlers()) {
            this.javaLogger.removeHandler(h);
        }
        ConsoleHandler consoleHandler = new ConsoleHandler() {

            @Override
            public void publish(LogRecord record) {
                String message = record.getMessage();
                if (message.contains((CharSequence) LOG_SEVERE_TAG)) {
                    System.err.println(message);
                }
                else {
                    System.out.println(message);
                }
            }
        };
        consoleHandler.setLevel(Level.ALL);
        this.javaLogger.addHandler(consoleHandler);
        this.javaLogger.setLevel(Level.ALL);
    }

    public String getLoggedSystemName() {
        return this.loggedSystemName;
    }

    public LogMessageType getMinimumLogLevel() {
        return this.minimumLogLevel;
    }

    public void setMinimumLogLevel(LogMessageType minimumLogLevel) {
        this.minimumLogLevel = minimumLogLevel;
    }

    public Long getActualStackPosition() {
        return this.actualStackPosition;
    }

    public void setActualStackPosition(Long actualStackPosition) {
        this.actualStackPosition = actualStackPosition;
        this.stackPosition = actualStackPosition.intValue();
    }

    public String entering(String prefix) {
        return this.print(LogMessageTypeImpl.ENTERING, prefix);
    }

    public String entering() {
        return this.print(LogMessageTypeImpl.ENTERING, this.blank);
    }

    public String exiting(String prefix) {
        return this.print(LogMessageTypeImpl.EXITING, prefix);
    }

    public String exiting() {
        return this.print(LogMessageTypeImpl.EXITING, this.blank);
    }

    public String info(String message) {
        return this.print(LogMessageTypeImpl.INFO, message);
    }

    public String debug(String message) {
        return this.print(LogMessageTypeImpl.DEBUG, message);
    }

    public String config(String parameterName, Object parameterValue) {
        String parameterValueString = "null";
        if (parameterValue != null) {
            parameterValueString = parameterValue instanceof String ? "\"" + parameterValue.toString() + "\"" : (parameterValue instanceof String ? "\"" + ((String) parameterValue) + "\"" : parameterValue.toString());
        }
        String messageString = parameterName + " = " + parameterValueString;
        String message = messageString;
        return this.print(LogMessageTypeImpl.CONFIG, message);
    }

    public String warning(String message) {
        return this.print(LogMessageTypeImpl.WARNING, message);
    }

    public String severe(String message) {
        return this.print(LogMessageTypeImpl.SEVERE, message);
    }

    private String print(LogMessageType messageType, String message) {
        String output = null;
        if (messageType == LogMessageTypeImpl.SEVERE || this.minimumLogLevel != null && messageType.getPriorityLevel() >= this.minimumLogLevel.getPriorityLevel()) {
            String messageString = "null";
            if (message != null && message != null) {
                messageString = message;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.getDateToString());
            sb.append("]");
            sb.append(" [");
            sb.append(messageType.getName());
            sb.append("]");
            sb.append(" ");
            sb.append(messageString);
            String outputString = sb.toString();
            output = outputString;
            this.javaLogger.log(Level.FINEST, outputString);
        }
        return output;
    }

    protected String[] getCallingInfos() {
        String[] callingInfos = null;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElements[this.stackPosition];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        String lineNumber = "" + stackTraceElement.getLineNumber() + "";
        callingInfos = new String[] { className, methodName, lineNumber };
        return callingInfos;
    }

    private String getDateToString() {
        String dateString = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss:SSS");
        dateString = simpleDateFormat.format(new Date());
        return dateString;
    }

}
