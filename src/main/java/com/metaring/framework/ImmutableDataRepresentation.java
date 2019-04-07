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
import com.metaring.framework.type.Email;
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

class ImmutableDataRepresentation extends DataRepresentationImpl {
    ImmutableDataRepresentation(String filenameOrContent, TextSeriesFactory textSeriesFactory,
            DigitSeriesFactory digitSeriesFactory, RealDigitSeriesFactory realDigitSeriesFactory,
            TruthSeriesFactory truthSeriesFactory, EmailFactory emailFactory, EmailSeriesFactory emailSeriesFactory,
            LogMessageTypeProvider logMessageTypeProvider) {
        super(filenameOrContent, textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory,
                emailSeriesFactory, logMessageTypeProvider);
    }

    @Override
    public DataRepresentation merge(DataRepresentation extension) {
        throw new IllegalAccessError("This item is read-only");
    }

    @Override
    public DataRepresentation merge(DataRepresentation extension2, DataRepresentation... extensions) {
        throw new IllegalAccessError("This item is read-only");
    }

    @Override
    public final DataRepresentation add(String property, Boolean value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Boolean... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, CoreType value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, CoreType... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, DigitSeries value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Double value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Double... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Iterable<?> values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Long value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Long... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, RealDigitSeries value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, String value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, String... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, TextSeries value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, TruthSeries value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Email value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, Email... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, EmailSeries value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, LogMessageType value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, DataRepresentation value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String property, DataRepresentation... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Boolean value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Boolean... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(CoreType value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(CoreType... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(DataRepresentation value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(DataRepresentation... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

   @Override
   public final DataRepresentation add(DigitSeries values) {
       throw new IllegalAccessError("This item is read-only!");
   }

    @Override
    public final DataRepresentation add(Double value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Double... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Email value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Email... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(EmailSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Iterable<?> values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Long value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(Long... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(RealDigitSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(String... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(TextSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation add(TruthSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Boolean value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Boolean... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, CoreType value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, CoreType... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, DataRepresentation value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, DataRepresentation... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, DigitSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Double value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Double... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Email value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Email... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, EmailSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Iterable<?> values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Long value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, Long... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, RealDigitSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, String value) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, String... values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, TextSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation set(int position, TruthSeries values) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation remove(int position, int... otherPositions) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation remove(String property, String... properties) {
        throw new IllegalAccessError("This item is read-only!");
    }

    @Override
    public final DataRepresentation get(int position) {
        try {
            return new ImmutableDataRepresentation(super.get(position).toJson(), textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory,
                    emailSeriesFactory, logMessageTypeProvider);
        }
        catch(Exception e) {
            return null;
        }
    }

    @Override
    public final DataRepresentation get(String property) {
        try {
            return new ImmutableDataRepresentation(super.get(property).toJson(), textSeriesFactory, digitSeriesFactory, realDigitSeriesFactory, truthSeriesFactory, emailFactory,
                    emailSeriesFactory, logMessageTypeProvider);
        }
        catch(Exception e) {
            return null;
        }
    }
}