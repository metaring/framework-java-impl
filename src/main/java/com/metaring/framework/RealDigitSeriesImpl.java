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
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;

import com.metaring.framework.type.series.RealDigitSeries;

final class RealDigitSeriesImpl extends ArrayList<Double> implements RealDigitSeries {

    private static final long serialVersionUID = -33436455728516508L;
    private Enumerable<Double> internalEnumerable;

    RealDigitSeriesImpl(Iterable<Double> iterable) {
        super();
        this.addAll(StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList()));
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder("[");
        forEach(it -> sb.append(it.toString()).append(","));
        return sb.delete(sb.length() - 1, sb.length()).append("]").toString();
    }

    @Override
    public Double[] toArray() {
        return this.toArray(new Double[this.size()]);
    }

    @Override
    public Enumerable<Double> asEnumerable() {
        return internalEnumerable != null ? internalEnumerable : (internalEnumerable = Linq4j.asEnumerable(this));
    }

    @Override
    public boolean addAll(Enumerable<Double> enumerable) {
        return enumerable == null ? false : this.addAll(enumerable.toList());
    }

    @Override
    public boolean containsAll(Enumerable<Double> enumerable) {
        return enumerable == null ? false : this.containsAll(enumerable.toList());
    }

    @Override
    public boolean removeAll(Enumerable<Double> enumerable) {
        return enumerable == null ? false : this.removeAll(enumerable.toList());
    }

    @Override
    public boolean retainAll(Enumerable<Double> enumerable) {
        return enumerable == null ? false : this.retainAll(enumerable.toList());
    }

    @Override
    public boolean addAll(Double[] Doubles) {
        return Doubles == null ? false : this.addAll(Arrays.asList(Doubles));
    }

    @Override
    public boolean containsAll(Double[] Doubles) {
        return Doubles == null ? false : this.containsAll(Arrays.asList(Doubles));
    }

    @Override
    public boolean removeAll(Double[] Doubles) {
        return Doubles == null ? false : this.removeAll(Arrays.asList(Doubles));
    }

    @Override
    public boolean retainAll(Double[] Doubles) {
        return Doubles == null ? false : this.retainAll(Arrays.asList(Doubles));
    }

    private void recreateEnumerable() {
        if (internalEnumerable != null) {
            internalEnumerable = Linq4j.asEnumerable(this);
        }
    }

    @Override
    public boolean add(Double e) {
        boolean test = super.add(e);
        recreateEnumerable();
        return test;
    }

    @Override
    public void add(int index, Double element) {
        super.add(index, element);
        recreateEnumerable();
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        boolean test = super.addAll(c);
        recreateEnumerable();
        return test;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> c) {
        boolean test = super.addAll(index, c);
        recreateEnumerable();
        return test;
    }

    @Override
    public boolean remove(Object o) {
        boolean test = super.remove(o);
        recreateEnumerable();
        return test;
    }

    @Override
    public Double remove(int index) {
        Double test = super.remove(index);
        recreateEnumerable();
        return test;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean test = super.removeAll(c);
        recreateEnumerable();
        return test;
    }

    @Override
    public boolean removeIf(Predicate<? super Double> filter) {
        boolean test = super.removeIf(filter);
        recreateEnumerable();
        return test;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean test = super.retainAll(c);
        recreateEnumerable();
        return test;
    }

    @Override
    public void replaceAll(UnaryOperator<Double> operator) {
        super.replaceAll(operator);
        recreateEnumerable();
    }
}
