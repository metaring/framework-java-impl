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

import com.metaring.framework.type.factory.RealDigitSeriesFactory;
import com.metaring.framework.type.series.RealDigitSeries;

final class RealDigitSeriesFactoryImpl implements RealDigitSeriesFactory {

    private final Function<String, Stream<String>> fromJsonToStringsFunction;

    RealDigitSeriesFactoryImpl(Function<String, Stream<String>> fromJsonToStringsFunction) {
        this.fromJsonToStringsFunction = fromJsonToStringsFunction;
    }

    @Override
    public RealDigitSeries create(Iterable<Double> iterable) {
        return new RealDigitSeriesImpl(iterable);
    }

    @Override
    public RealDigitSeries create(Double... digits) {
        return create(digits == null ? new ArrayList<>() : Arrays.asList(digits));
    }

    @Override
    public RealDigitSeries fromJson(String json) {
        Stream<String> strings = fromJsonToStringsFunction.apply(json);
        return strings == null ? null : create(strings.map(Double::new).toArray(Double[]::new));
    }
}
