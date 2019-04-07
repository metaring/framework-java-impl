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

import com.metaring.framework.type.Email;

final class EmailImpl implements Email {

    private String username;
    private String domainName;
    private String domainLocation;

    EmailImpl(String username, String domainName, String domainLocation) {
        this.username = username;
        this.domainName = domainName;
        this.domainLocation = domainLocation;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getDomainName() {
        return this.domainName;
    }

    @Override
    public String getDomainLocation() {
        return this.domainLocation;
    }

    @Override
    public String toString() {
        return username + "@" + domainName + "." + domainLocation;
    }

    public String toJson() {
        return "\"" + this.toString() + "\"";
    }
}
