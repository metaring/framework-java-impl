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

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.metaring.framework.type.DataRepresentation;
import com.metaring.framework.type.Email;
import com.metaring.framework.type.factory.DataRepresentationFactory;
import com.metaring.framework.type.factory.DigitSeriesFactory;
import com.metaring.framework.type.factory.EmailFactory;
import com.metaring.framework.type.factory.EmailSeriesFactory;
import com.metaring.framework.type.factory.RealDigitSeriesFactory;
import com.metaring.framework.type.factory.TextSeriesFactory;
import com.metaring.framework.type.factory.TruthSeriesFactory;
import com.metaring.framework.type.series.DigitSeries;
import com.metaring.framework.type.series.EmailSeries;
import com.metaring.framework.type.series.RealDigitSeries;
import com.metaring.framework.type.series.TextSeries;
import com.metaring.framework.type.series.TruthSeries;
import com.metaring.framework.util.log.LogMessageType;
import com.metaring.framework.util.log.LogMessageTypeProvider;

class DataRepresentationImpl implements DataRepresentation, DataRepresentationFactory {

    private static final Gson GSON = new GsonBuilder().create();

    private JsonElement instance;
    protected final TextSeriesFactory textSeriesFactory;
    protected final DigitSeriesFactory digitSeriesFactory;
    protected final RealDigitSeriesFactory realDigitSeriesFactory;
    protected final TruthSeriesFactory truthSeriesFactory;
    protected final EmailFactory emailFactory;
    protected final EmailSeriesFactory emailSeriesFactory;
    protected final LogMessageTypeProvider logMessageTypeProvider;

    DataRepresentationImpl(String filenameOrContent, TextSeriesFactory textSeriesFactory, DigitSeriesFactory digitSeriesFactory, RealDigitSeriesFactory realDigitSeriesFactory, TruthSeriesFactory truthSeriesFactory, EmailFactory emailFactory, EmailSeriesFactory emailSeriesFactory, LogMessageTypeProvider logMessageTypeProvider) {
        super();
        this.textSeriesFactory = textSeriesFactory;
        this.digitSeriesFactory = digitSeriesFactory;
        this.realDigitSeriesFactory = realDigitSeriesFactory;
        this.truthSeriesFactory = truthSeriesFactory;
        this.emailFactory = emailFactory;
        this.emailSeriesFactory = emailSeriesFactory;
        this.logMessageTypeProvider = logMessageTypeProvider;
        instance = reinit(filenameOrContent);
    }

    protected final JsonElement reinit(String filenameOrContent) {
        if (filenameOrContent == null || filenameOrContent.trim().isEmpty()) {
            return JsonNull.INSTANCE;
        }
        try(Reader reader = new FileReader(filenameOrContent)) {
            return GSON.fromJson(reader, JsonElement.class);
        } catch (Exception e1) {
            try(Reader reader = new InputStreamReader(this.getClass().getResourceAsStream(filenameOrContent))) {
                return GSON.fromJson(reader, JsonElement.class);
            } catch (Exception e2) {
                try(Reader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filenameOrContent))) {
                    return GSON.fromJson(reader, JsonElement.class);
                } catch (Exception e3) {
                    try(Reader reader = new InputStreamReader(new URL(filenameOrContent).openStream())) {
                        return GSON.fromJson(reader, JsonElement.class);
                    } catch (Exception e4) {
                        try {
                            return GSON.fromJson(filenameOrContent, JsonElement.class);
                        } catch (Exception e5) {
                            try {
                                return GSON.fromJson("\"" + filenameOrContent + "\"", JsonElement.class);
                            } catch (Exception e6) {
                                return JsonNull.INSTANCE;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public DataRepresentation merge(DataRepresentation extension) {
        if(extension == null || extension.isNull()) {
            return this;
        }
        TextSeries listOriginal = textSeriesFactory.create();
        if(!this.isNull() && this.hasProperties()) {
            listOriginal.addAll(this.getProperties());
        }
        if(!extension.isNull() && extension.hasProperties()) {
            listOriginal.addAll(extension.getProperties());
        }
        List<String> properties = listOriginal.asEnumerable().distinct().toList();
        Collections.sort(properties);
        for(String property: properties) {
            if(!this.hasProperty(property)) {
                this.add(property, extension.get(property));
            } else if(extension.hasProperty(property)) {
                this.add(property, this.hasProperties(property) && extension.hasProperties(property) ? this.get(property).merge(extension.get(property)) : extension.get(property));
            }
        }
        return this;
    }

    @Override
    public DataRepresentation merge(DataRepresentation extension2, DataRepresentation... extensions) {
        this.merge(extension2);
        for(int i=0; i<extensions.length; i++) {
            this.merge(extensions[i]);
        }
        return this;
    }

    @Override
    public final String toJson() {
        return instance == null ? "null" : instance.toString();
    }

    @Override
    public final Boolean hasProperty(String property) {
        try {
            return instance.getAsJsonObject().entrySet().stream().anyMatch(it -> it.getKey().equals(property));
        }
        catch (Exception e) {
        }
        return false;
    }

    @Override
    public final Boolean isNull(String property) {
        try {
            return instance.getAsJsonObject().get(property).isJsonNull();
        }
        catch (Exception e) {
        }
        return true;
    }

    @Override
    public final Boolean isNull() {
        try {
            return instance.isJsonNull();
        }
        catch (Exception e) {
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataRepresentation add(String property, Iterable<?> values) {
        if (values == null || !values.iterator().hasNext()) {
            getOrCreateJsonObject().add(property, values == null ? JsonNull.INSTANCE : new JsonArray());
            return this;
        }
        Object o = values.iterator().next();
        if (o instanceof String) {
            return add(property, textSeriesFactory.create((Iterable<String>) values));
        }
        else
            if (o instanceof Long) {
                return add(property, digitSeriesFactory.create((Iterable<Long>) values));
            }
            else
                if (o instanceof Double) {
                    return add(property, realDigitSeriesFactory.create((Iterable<Double>) values));
                }
                else
                    if (o instanceof Boolean) {
                        return add(property, truthSeriesFactory.create((Iterable<Boolean>) values));
                    }
                    else
                        if (o instanceof Email) {
                            return add(property, emailSeriesFactory.create((Iterable<Email>) values));
                        }
                        else
                            if (o instanceof CoreType) {
                                JsonArray jsonArray = new JsonArray();
                                Iterable<CoreType> types = (Iterable<CoreType>) values;
                                types.forEach(it -> jsonArray.add(toJsonElement(it)));
                                getOrCreateJsonObject().add(property, jsonArray);
                                return this;
                            }
        throw new IllegalArgumentException("values type is not recognized (" + o.getClass().getName() + ")");
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataRepresentation add(Iterable<?> values) {
        if (values == null || !values.iterator().hasNext()) {
            getOrCreateJsonArray().add(values == null ? JsonNull.INSTANCE : new JsonArray());
            return this;
        }
        Object o = values.iterator().next();
        if (o instanceof String) {
            return add(textSeriesFactory.create((Iterable<String>) values));
        }
        else
            if (o instanceof Long) {
                return add(digitSeriesFactory.create((Iterable<Long>) values));
            }
            else
                if (o instanceof Double) {
                    return add(realDigitSeriesFactory.create((Iterable<Double>) values));
                }
                else
                    if (o instanceof Boolean) {
                        return add(truthSeriesFactory.create((Iterable<Boolean>) values));
                    }
                    else
                        if (o instanceof Email) {
                            return add(emailSeriesFactory.create((Iterable<Email>) values));
                        }
                        else
                            if (o instanceof CoreType) {
                                JsonArray jsonArray = new JsonArray();
                                Iterable<CoreType> types = (Iterable<CoreType>) values;
                                types.forEach(it -> jsonArray.add(toJsonElement(it)));
                                getOrCreateJsonArray().add(jsonArray);
                                return this;
                            }
        throw new IllegalArgumentException("values type is not recognized (" + o.getClass().getName() + ")");
    }

    @Override
    public DataRepresentation remove(String property, String... properties) {
        try {
            JsonObject jsonObject = instance.getAsJsonObject();
            jsonObject.remove(property);
            for(String p : properties) {
                jsonObject.remove(p);
            }
        } catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, LogMessageType value) {
        try {
            getOrCreateJsonObject().add(property, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value.getName()));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isLogMessageType(String property) {
        return getLogMessageType(property) != null;
    }

    @Override
    public final LogMessageType getLogMessageType(String property) {
        try {
            return logMessageTypeProvider.getByName(instance.getAsJsonObject().get(property).getAsString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final LogMessageType getLogMessageType() {
        try {
            return logMessageTypeProvider.getByName(instance.getAsString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Boolean isLogMessageType() {
        return getLogMessageType() != null;
    }

    public DataRepresentation add(String property, String value) {
        try {
            getOrCreateJsonObject().add(property, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, String... values) {
        try {
            return add(property, textSeriesFactory.create(values));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, TextSeries value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isText(String property) {
        return get(property).isText();
    }

    @Override
    public final Boolean isTextSeries(String property) {
        return getTextSeries(property) != null;
    }

    @Override
    public final Boolean isText() {
        return isSimple() && !isNull() && !isDigit() && !isRealDigit() && !isTruth();
    }

    @Override
    public final Boolean isTextSeries() {
        return asTextSeries() != null;
    }

    @Override
    public final String asText() {
        try {
            return getCleanString(instance);
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TextSeries asTextSeries() {
        try {
            return textSeriesFactory.fromJson(instance.toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final String getText(String property) {
        try {
            return getCleanString(instance.getAsJsonObject().get(property));
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TextSeries getTextSeries(String property) {
        try {
            return textSeriesFactory.fromJson(instance.getAsJsonObject().get(property).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String property, Long value) {
        try {
            getOrCreateJsonObject().add(property, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, Long... values) {
        try {
            return add(property, digitSeriesFactory.create(values));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, DigitSeries value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isDigit(String property) {
        return getDigit(property) != null;
    }

    @Override
    public final Boolean isDigitSeries(String property) {
        return getDigitSeries(property) != null;
    }

    @Override
    public final Boolean isDigit() {
        return asDigit() != null;
    }

    @Override
    public final Boolean isDigitSeries() {
        return asDigit() != null;
    }

    @Override
    public final Long asDigit() {
        try {
            return instance.getAsLong();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final DigitSeries asDigitSeries() {
        try {
            return digitSeriesFactory.fromJson(instance.toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Long getDigit(String property) {
        try {
            return instance.getAsJsonObject().get(property).getAsLong();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final DigitSeries getDigitSeries(String property) {
        try {
            return digitSeriesFactory.fromJson(instance.getAsJsonObject().get(property).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String property, Double value) {
        try {
            getOrCreateJsonObject().add(property, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, Double... values) {
        try {
            return add(property, realDigitSeriesFactory.create(values));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, RealDigitSeries value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isRealDigit(String property) {
        return getRealDigit(property) != null;
    }

    @Override
    public final Boolean isRealDigitSeries(String property) {
        return getRealDigitSeries(property) != null;
    }

    @Override
    public final Boolean isRealDigit() {
        return asRealDigit() != null;
    }

    @Override
    public final Boolean isRealDigitSeries() {
        return asRealDigitSeries() != null;
    }

    @Override
    public final Double asRealDigit() {
        try {
            return instance.getAsDouble();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final RealDigitSeries asRealDigitSeries() {
        try {
            return realDigitSeriesFactory.fromJson(instance.getAsString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Double getRealDigit(String property) {
        try {
            return instance.getAsJsonObject().get(property).getAsDouble();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final RealDigitSeries getRealDigitSeries(String property) {
        try {
            return realDigitSeriesFactory.fromJson(instance.getAsJsonObject().get(property).getAsString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String property, Boolean value) {
        try {
            getOrCreateJsonObject().add(property, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, Boolean... values) {
        try {
            return add(property, truthSeriesFactory.create(values));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, TruthSeries value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isTruth(String property) {
        return getTruth(property) != null;
    }

    @Override
    public final Boolean isTruthSeries(String property) {
        return getTruthSeries(property) != null;
    }

    @Override
    public final Boolean isTruth() {
        return asTruth() != null;
    }

    @Override
    public final Boolean isTruthSeries() {
        return asTruthSeries() != null;
    }

    private final Boolean booleanize(JsonElement element) {
        try {
            String elementToString = element.toString();
            if(elementToString.equals("true") || elementToString.equals("false")) {
                return Boolean.parseBoolean(elementToString);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Boolean asTruth() {
        try {
            return booleanize(instance);
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TruthSeries asTruthSeries() {
        try {
            return truthSeriesFactory.fromJson(instance.toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Boolean getTruth(String property) {
        try {
            return booleanize(instance.getAsJsonObject().get(property));
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TruthSeries getTruthSeries(String property) {
        try {
            return truthSeriesFactory.fromJson(instance.getAsJsonObject().get(property).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String property, Email value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, Email... values) {
        try {
            return add(property, emailSeriesFactory.create(values));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, EmailSeries value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isEmail(String property) {
        return getEmail(property) != null;
    }

    @Override
    public final Boolean isEmailSeries(String property) {
        return getEmailSeries(property) != null;
    }

    @Override
    public final Boolean isEmail() {
        return asEmail() != null;
    }

    @Override
    public final Boolean isEmailSeries() {
        return asEmailSeries() != null;
    }

    @Override
    public final Email asEmail() {
        try {
            return emailFactory.fromJson(instance.getAsString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final EmailSeries asEmailSeries() {
        try {
            return emailSeriesFactory.fromJson(instance.toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Email getEmail(String property) {
        try {
            return emailFactory.fromJson(instance.getAsJsonObject().get(property).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final EmailSeries getEmailSeries(String property) {
        try {
            return emailSeriesFactory.fromJson(instance.getAsJsonObject().get(property).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String property, DataRepresentation value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, DataRepresentation... values) {
        try {
            if (values == null) {
                getOrCreateJsonObject().add(property, JsonNull.INSTANCE);
                return this;
            }
            JsonArray jsonArray = new JsonArray();
            Stream.of(values).forEach(it -> jsonArray.add(toJsonElement(it)));
            return add(property, jsonArray);
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean hasProperties(String property) {
        try {
            return get(property).hasProperties();
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public final Boolean hasProperties(int position) {
        try {
            return get(position).hasProperties();
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public final Boolean hasProperties() {
        return instance.isJsonObject();
    }

    @Override
    public DataRepresentation get(String property) {
        try {
            return this.fromJson(instance.getAsJsonObject().get(property).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String property, CoreType value) {
        try {
            getOrCreateJsonObject().add(property, toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String property, CoreType... values) {
        if (values == null || values.length == 0) {
            getOrCreateJsonObject().add(property, values == null ? JsonNull.INSTANCE : new JsonArray());
            return this;
        }
        try {
            JsonArray jsonArray = new JsonArray();
            for (CoreType value : values) {
                jsonArray.add(toJsonElement(value));
            }
            getOrCreateJsonObject().add(property, jsonArray);
        }
        catch (Exception e) {
        }
        return this;
    }

    public final <T extends GeneratedCoreType> T as(Class<T> generatedCoreTypeClass) {
        try {
            return get(instance, generatedCoreTypeClass);
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final <T extends GeneratedCoreType> T get(String property, Class<T> generatedCoreTypeClass) {
        try {
            return get(instance.getAsJsonObject().get(property), generatedCoreTypeClass);
        }
        catch (Exception e) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private final <T extends GeneratedCoreType> T get(JsonElement jsonElement, Class<T> generatedCoreTypeClass) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        try {
            Method method = generatedCoreTypeClass.getDeclaredMethod("fromJson", String.class);
            method.setAccessible(true);
            return (T) method.invoke(null, getCleanString(jsonElement));
        }
        catch (Exception e) {
            try {
                Method method = generatedCoreTypeClass.getMethod("fromJson", String.class);
                method.setAccessible(true);
                return (T) method.invoke(null, getCleanString(jsonElement));
            }
            catch (Exception ex) {
            }
        }
        return null;
    }

    @Override
    public final Boolean hasLength() {
        try {
            return instance.isJsonArray();
        }
        catch (Exception e) {
        }
        return false;
    }

    @Override
    public final Integer length() {
        try {
            return instance.getAsJsonArray().size();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Boolean isEmpty() {
        return (hasLength() && length() == 0) || (hasProperties() && getProperties().isEmpty());
    }

    @Override
    public final Boolean isEmpty(String property) {
        return (hasLength(property) && getLength(property) == 0) || (hasProperties(property) && getProperties(property).isEmpty());
    }

    @Override
    public final Boolean isEmpty(int position) {
        return (hasLength(position) && getLength(position) == 0) || (hasProperties(position) && getProperties(position).isEmpty());
    }

    @Override
    public final Boolean isNullOrEmpty() {
        return isNull() || isEmpty();
    }

    @Override
    public final Boolean isNullOrEmpty(String property) {
        return isNull(property) || isEmpty(property);
    }

    @Override
    public final Boolean isNullOrEmpty(int position) {
        return isNull(position) || isEmpty(position);
    }

    @Override
    public DataRepresentation remove(int position, int... otherPositions) {
        try {
            List<Integer> positions = new ArrayList<>();
            positions.add(position);
            if(otherPositions != null && otherPositions.length > 0) {
                for (int pos : otherPositions) {
                    positions.add(pos);
                }
            }
            Collections.sort(positions);
            Collections.reverse(positions);
            JsonArray jsonArray = instance.getAsJsonArray();
            for (int pos : positions) {
                jsonArray.remove(pos);
            }
        } catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isText(int position) {
        return get(position).isText();
    }

    @Override
    public final Boolean isTextSeries(int position) {
        return getTextSeries(position) != null;
    }

    @Override
    public final String getText(int position) {
        try {
            return getCleanString(instance.getAsJsonArray().get(position));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TextSeries getTextSeries(int position) {
        try {
            return textSeriesFactory.fromJson(instance.getAsJsonArray().get(position).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(String value) {
        try {
            getOrCreateJsonArray().add(value);
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(String... values) {
        try {
            for (String value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(TextSeries values) {
        try {
            for (String value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isDigit(int position) {
        return getDigit(position) != null;
    }

    @Override
    public final Boolean isDigitSeries(int position) {
        return getDigitSeries(position) != null;
    }

    @Override
    public final Long getDigit(int position) {
        try {
            return instance.getAsJsonArray().get(position).getAsLong();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final DigitSeries getDigitSeries(int position) {
        try {
            return digitSeriesFactory.fromJson(instance.getAsJsonArray().get(position).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(Long value) {
        try {
            getOrCreateJsonArray().add(value);
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(Long... values) {
        try {
            for (Long value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(DigitSeries values) {
        try {
            for (Long value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isRealDigit(int position) {
        return getRealDigit(position) != null;
    }

    @Override
    public final Boolean isRealDigitSeries(int position) {
        return getRealDigitSeries(position) != null;
    }

    @Override
    public final Double getRealDigit(int position) {
        try {
            return instance.getAsJsonArray().get(position).getAsDouble();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final RealDigitSeries getRealDigitSeries(int position) {
        try {
            return realDigitSeriesFactory.fromJson(instance.getAsJsonArray().get(position).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(Double value) {
        try {
            getOrCreateJsonArray().add(value);
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(Double... values) {
        try {
            for (Double value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(RealDigitSeries values) {
        try {
            for (Double value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isTruth(int position) {
        return getTruth(position) != null;
    }

    @Override
    public final Boolean isTruthSeries(int position) {
        return getTruthSeries(position) != null;
    }

    @Override
    public final Boolean getTruth(int position) {
        try {
            return booleanize(instance.getAsJsonArray().get(position));
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TruthSeries getTruthSeries(int position) {
        try {
            return truthSeriesFactory.fromJson(instance.getAsJsonArray().get(position).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(Boolean value) {
        try {
            getOrCreateJsonArray().add(value);
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(Boolean... values) {
        try {
            for (Boolean value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(TruthSeries values) {
        try {
            for (Boolean value : values) {
                getOrCreateJsonArray().add(value);
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public final Boolean isEmail(int position) {
        return getEmail(position) != null;
    }

    @Override
    public final Boolean isEmailSeries(int position) {
        return getEmailSeries(position) != null;
    }

    @Override
    public final Email getEmail(int position) {
        try {
            return emailFactory.create(instance.getAsJsonArray().get(position).getAsString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final EmailSeries getEmailSeries(int position) {
        try {
            return emailSeriesFactory.fromJson(instance.getAsJsonArray().get(position).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(Email value) {
        try {
            getOrCreateJsonArray().add(toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(Email... values) {
        try {
            for (Email value : values) {
                getOrCreateJsonArray().add(toJsonElement(value));
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(EmailSeries values) {
        try {
            for (Email value : values) {
                getOrCreateJsonArray().add(toJsonElement(value));
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation get(int position) {
        try {
            return fromJson(instance.getAsJsonArray().get(position).toString());
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public DataRepresentation add(DataRepresentation value) {
        try {
            getOrCreateJsonArray().add(toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(DataRepresentation... values) {
        try {
            for (DataRepresentation value : values) {
                getOrCreateJsonArray().add(toJsonElement(value));
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(CoreType value) {
        try {
            getOrCreateJsonArray().add(toJsonElement(value));
        }
        catch (Exception e) {
        }
        return this;
    }

    @Override
    public DataRepresentation add(CoreType... values) {
        try {
            for (CoreType value : values) {
                getOrCreateJsonArray().add(toJsonElement(value));
            }
        }
        catch (Exception e) {
        }
        return this;
    }

    private final DataRepresentation createWork(String json) {
        return new DataRepresentationImpl(json, textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory, emailSeriesFactory, logMessageTypeProvider);
    }

    @Override
    public final DataRepresentation create() {
        return createWork(null);
    }

    @Override
    public final DataRepresentation fromJson(String json) {
        return createWork(json);
    }

    @Override
    public final DataRepresentation fromObject(Object object) {
        if (object == null) {
            return null;
        }
        return fromJson(object instanceof CoreType ? ((CoreType) object).toJson() : GSON.toJson(object));
    }

    final static Stream<String> getStrings(String json) {
        if (json == null || json.trim().isEmpty() || json.replace(" ", "").toLowerCase().equals("null")) {
            return null;
        }
        JsonArray jsonArray = GSON.fromJson(json, JsonElement.class).getAsJsonArray();
        List<String> strings = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            strings.add(getCleanString(jsonElement));
        }
        return strings.parallelStream();
    }

    @Override
    public final TextSeries getProperties() {
        try {
            return textSeriesFactory.create(instance.getAsJsonObject().entrySet().parallelStream().map(it -> it.getKey()).toArray(String[]::new));
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final <T extends GeneratedCoreType> T get(int position, Class<T> generatedCoreTypeClass) {
        try {
            return get(instance.getAsJsonArray().get(position), generatedCoreTypeClass);
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final String toString() {
        return toJson();
    }

    private static final String getCleanString(JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive() ? jsonElement.getAsString() : jsonElement.toString();
    }

    @Override
    public final Boolean is(Class<? extends GeneratedCoreType> generatedCoreTypeClass) {
        return as(generatedCoreTypeClass) != null;
    }

    @Override
    public final Boolean is(String property, Class<? extends GeneratedCoreType> generatedCoreTypeClass) {
        return get(property, generatedCoreTypeClass) != null;
    }

    @Override
    public final Boolean is(int position, Class<? extends GeneratedCoreType> generatedCoreTypeClass) {
        return get(position, generatedCoreTypeClass) != null;
    }

    @Override
    public final Boolean isNull(int position) {
        JsonElement element = null;
        try {
            element = instance.getAsJsonArray().get(position);
        }
        catch (Exception e) {
        }
        return element == null || element.isJsonNull();
    }

    @Override
    public final Boolean isSimple() {
        JsonElement element = null;
        try {
            element = instance.getAsJsonPrimitive();
        }
        catch (Exception e) {
        }
        return element != null && element.isJsonPrimitive();
    }

    @Override
    public final Boolean isSimple(String property) {
        JsonElement element = null;
        try {
            element = instance.getAsJsonObject().get(property);
        }
        catch (Exception e) {
        }
        return element != null && element.isJsonPrimitive();
    }

    @Override
    public final Boolean isSimple(int position) {
        JsonElement element = null;
        try {
            element = instance.getAsJsonArray().get(position);
        }
        catch (Exception e) {
        }
        return element != null && element.isJsonPrimitive();
    }

    @Override
    public final Boolean hasLength(String property) {
        JsonElement element = null;
        try {
            element = instance.getAsJsonObject().get(property);
        }
        catch (Exception e) {
        }
        return element != null && element.isJsonArray();
    }

    @Override
    public final Boolean hasLength(int position) {
        JsonElement element = null;
        try {
            element = instance.getAsJsonArray().get(position);
        }
        catch (Exception e) {
        }
        return element != null && element.isJsonArray();
    }

    @Override
    public final Integer getLength(String property) {
        try {
            return instance.getAsJsonObject().get(property).getAsJsonArray().size();
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Integer getLength(int position) {
        try {
            return instance.getAsJsonArray().get(position).getAsJsonArray().size();
        }
        catch (Exception e) {
        }
        return null;
    }


    @Override
    public final DataRepresentation first() {
        return get(0);
    }

    @Override
    public final String firstAsText() {
        return get(0).asText();
    }

    @Override
    public final TextSeries firstAsTextSeries() {
        return get(0).asTextSeries();
    }

    @Override
    public final Long firstAsDigit() {
        return get(0).asDigit();
    }

    @Override
    public final DigitSeries firstAsDigitSeries() {
        return get(0).asDigitSeries();
    }

    @Override
    public final Double firstAsRealDigit() {
        return get(0).asRealDigit();
    }

    @Override
    public final RealDigitSeries firstAsRealDigitSeries() {
        return get(0).asRealDigitSeries();
    }

    @Override
    public final Boolean firstAsTruth() {
        return get(0).asTruth();
    }

    @Override
    public final TruthSeries firstAsTruthSeries() {
        return get(0).asTruthSeries();
    }

    @Override
    public final Email firstAsEmail() {
        return get(0).asEmail();
    }

    @Override
    public final EmailSeries firstAsEmailSeries() {
        return get(0).asEmailSeries();
    }

    @Override
    public final <T extends GeneratedCoreType> T firstAs(Class<T> generatedCoreTypeClass) {
        return get(0).as(generatedCoreTypeClass);
    }

    @Override
    public final TextSeries getProperties(int position) {
        try {
            return textSeriesFactory.create(instance.getAsJsonArray().get(position).getAsJsonObject().entrySet().parallelStream().map(it -> it.getKey()).toArray(String[]::new));
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final TextSeries getProperties(String property) {
        try {
            return textSeriesFactory.create(instance.getAsJsonObject().get(property).getAsJsonObject().entrySet().parallelStream().map(it -> it.getKey()).toArray(String[]::new));
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public final Iterator<DataRepresentation> iterator() {
        return hasProperties() ? new ObjectIterator(getProperties()) : new ArrayIterator(isNull() ? 0 : length());
    }

    private final JsonObject getOrCreateJsonObject() {
        return getOrCreateJsonObjectOrJsonArray(true).getAsJsonObject();
    }

    private final JsonArray getOrCreateJsonArray() {
        return getOrCreateJsonObjectOrJsonArray(false).getAsJsonArray();
    }

    private final JsonElement getOrCreateJsonObjectOrJsonArray(boolean object) {
        if (instance == null || instance instanceof JsonNull) {
            try {
                instance = object ? new JsonObject() : new JsonArray();
            }
            catch (Exception e) {
            }
        }
        return instance;
    }

    class ArrayIterator implements Iterator<DataRepresentation> {

        private int length;
        private int position = 0;

        ArrayIterator(int length) {
            this.length = length;
        }

        @Override
        public DataRepresentation next() {
            return get(position++);
        }

        @Override
        public boolean hasNext() {
            return position < length;
        }
    }

    class ObjectIterator implements Iterator<DataRepresentation> {

        private TextSeries properties;
        private int position = 0;

        ObjectIterator(TextSeries properties) {
            this.properties = properties;
        }

        @Override
        public DataRepresentation next() {
            return get(properties.get(position++));
        }

        @Override
        public boolean hasNext() {
            return position < properties.size();
        }
    }

    private final JsonElement toJsonElement(Object object) {
        return object == null ? JsonNull.INSTANCE : GSON.fromJson(object instanceof CoreType ? ((CoreType) object).toJson() : GSON.toJson(object), JsonElement.class);
    }

    @Override
    public DataRepresentation set(int position, Iterable<?> values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, String value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, String... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, TextSeries values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Long value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Long... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, DigitSeries values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Double value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Double... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, RealDigitSeries values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Boolean value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Boolean... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, TruthSeries values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Email value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, Email... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, EmailSeries values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, DataRepresentation value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, DataRepresentation... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }

    @Override
    public DataRepresentation set(int position, CoreType value) {
        getOrCreateJsonArray().set(position, toJsonElement(value));
        return this;
    }

    @Override
    public DataRepresentation set(int position, CoreType... values) {
        getOrCreateJsonArray().set(position, toJsonElement(values));
        return this;
    }
}