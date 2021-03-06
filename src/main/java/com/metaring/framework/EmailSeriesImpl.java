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

import com.metaring.framework.type.Email;
import com.metaring.framework.type.series.EmailSeries;

final class EmailSeriesImpl extends ArrayList<Email> implements EmailSeries {

    private static final long serialVersionUID = -1729792280050033823L;
    private Enumerable<Email> internalEnumerable;

    EmailSeriesImpl(Iterable<Email> iterable) {
        super();
        this.addAll(StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList()));
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder("[");
        forEach(it -> sb.append(it.toJson()).append(","));
        return sb.delete(sb.length() - 1, sb.length()).append("]").toString();
    }

    @Override
    public Email[] toArray() {
        return this.toArray(new Email[this.size()]);
    }

    @Override
    public Enumerable<Email> asEnumerable() {
        return internalEnumerable != null ? internalEnumerable : (internalEnumerable = Linq4j.asEnumerable(this));
    }

    @Override
    public boolean addAll(Enumerable<Email> enumerable) {
        return enumerable == null ? false : this.addAll(enumerable.toList());
    }

    @Override
    public boolean containsAll(Enumerable<Email> enumerable) {
        return enumerable == null ? false : this.containsAll(enumerable.toList());
    }

    @Override
    public boolean removeAll(Enumerable<Email> enumerable) {
        return enumerable == null ? false : this.removeAll(enumerable.toList());
    }

    @Override
    public boolean retainAll(Enumerable<Email> enumerable) {
        return enumerable == null ? false : this.retainAll(enumerable.toList());
    }

    @Override
    public boolean addAll(Email[] array) {
        return array == null ? false : this.addAll(Arrays.asList(array));
    }

    @Override
    public boolean containsAll(Email[] array) {
        return array == null ? false : this.containsAll(Arrays.asList(array));
    }

    @Override
    public boolean removeAll(Email[] array) {
        return array == null ? false : this.removeAll(Arrays.asList(array));
    }

    @Override
    public boolean retainAll(Email[] array) {
        return array == null ? false : this.retainAll(Arrays.asList(array));
    }

    private void recreateEnumerable() {
        if (internalEnumerable != null) {
            internalEnumerable = Linq4j.asEnumerable(this);
        }
    }

    @Override
    public boolean add(Email e) {
        boolean test = super.add(e);
        recreateEnumerable();
        return test;
    }

    @Override
    public void add(int index, Email element) {
        super.add(index, element);
        recreateEnumerable();
    }

    @Override
    public boolean addAll(Collection<? extends Email> c) {
        boolean test = super.addAll(c);
        recreateEnumerable();
        return test;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Email> c) {
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
    public Email remove(int index) {
        Email test = super.remove(index);
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
    public boolean removeIf(Predicate<? super Email> filter) {
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
    public void replaceAll(UnaryOperator<Email> operator) {
        super.replaceAll(operator);
        recreateEnumerable();
    }
}
