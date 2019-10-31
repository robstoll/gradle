/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.artifacts.verification.model;

import com.google.common.collect.ImmutableList;
import org.gradle.api.artifacts.component.ComponentIdentifier;

import java.util.List;

public class ComponentVerification {
    private final ComponentIdentifier component;
    private final List<ArtifactVerification> verifications;

    public ComponentVerification(ComponentIdentifier component, List<ArtifactVerification> verifications) {
        this.component = component;
        this.verifications = ImmutableList.copyOf(verifications);
    }

    public ComponentIdentifier getComponent() {
        return component;
    }

    public List<ArtifactVerification> getVerifications() {
        return verifications;
    }
}