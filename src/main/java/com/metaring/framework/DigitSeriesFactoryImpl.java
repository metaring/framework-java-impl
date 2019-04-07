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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import com.metaring.framework.type.factory.DigitSeriesFactory;
import com.metaring.framework.type.series.DigitSeries;

final class DigitSeriesFactoryImpl implements DigitSeriesFactory {

    private final Function<String, Stream<String>> fromJsonToStringsFunction;

    DigitSeriesFactoryImpl(Function<String, Stream<String>> fromJsonToStringsFunction) {
        this.fromJsonToStringsFunction = fromJsonToStringsFunction;
    }

    @Override
    public DigitSeries create(Iterable<Long> iterable) {
        return new DigitSeriesImpl(iterable);
    }

    @Override
    public DigitSeries create(Long... digits) {
        return create(digits == null ? new ArrayList<>() : Arrays.asList(digits));
    }

    @Override
    public DigitSeries fromJson(String json) {
        Stream<String> strings = fromJsonToStringsFunction.apply(json);
        return strings == null ? null : create(strings.map(Long::new).toArray(Long[]::new));
    }
}
