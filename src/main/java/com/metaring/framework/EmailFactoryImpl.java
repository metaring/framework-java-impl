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
import com.metaring.framework.type.factory.EmailFactory;

final class EmailFactoryImpl implements EmailFactory {

    @Override
    public Email create(String username, String domainName, String domainLocation) {
        if(username == null || username.trim().isEmpty()) {
            return null;
        }
        if(domainName == null || domainName.trim().isEmpty()) {
            return null;
        }
        if(domainLocation == null || domainLocation.trim().isEmpty()) {
            return null;
        }
        return new EmailImpl(username, domainName, domainLocation);
    }

    @Override
    public Email create(String email) {
        if(email == null || email.trim().isEmpty()) {
            return null;
        }
        if(!email.contains("@")) {
            return null;
        }
        if(!email.contains(".")) {
            return null;
        }
        if(email.startsWith("\"")) {
            email = email.substring(1);
        }
        if(email.endsWith("\"")) {
            email = email.substring(0, email.length() - 1);
        }
        int atIndex = email.indexOf("@");
        int dotIndex = email.indexOf(".");
        while(dotIndex <= atIndex) {
            dotIndex = email.indexOf(".", dotIndex + 1);
        }
        String username = email.substring(0, atIndex);
        String domainName = email.substring(atIndex + 1, dotIndex);
        String domainLocation = email.substring(dotIndex + 1);
        return new EmailImpl(username, domainName, domainLocation);
    }

    @Override
    public Email fromJson(String json) {
        return create(json);
    }

}
