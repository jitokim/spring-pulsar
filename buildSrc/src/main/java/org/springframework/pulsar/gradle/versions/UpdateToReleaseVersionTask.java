/*
 * Copyright 2019-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.pulsar.gradle.versions;

import java.util.Objects;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import org.springframework.util.Assert;

public abstract class UpdateToReleaseVersionTask extends UpdateProjectVersionTask {

	@Input
	private String releaseVersion;

	@TaskAction
	public void updateToReleaseVersion() {
		updateVersionInGradleProperties(this.releaseVersion);
		updatePropertyInGradleProperties(SPRING_BOOT_VERSION_PROPERTY, calculateReleaseBootVersion());
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	private String calculateReleaseBootVersion() {
		String currentBootVersion = Objects.toString(getProject().findProperty(SPRING_BOOT_VERSION_PROPERTY), null);
		Assert.notNull(currentBootVersion, () -> "% property not found".formatted(SPRING_BOOT_VERSION_PROPERTY));
		VersionInfo bootVersionSegments = parseVersion(currentBootVersion);
		String releaseBootVersion = "%s.%s.%s".formatted(bootVersionSegments.major(), bootVersionSegments.minor(), bootVersionSegments.patch());
		String releaseVersionModifier = parseVersion(this.releaseVersion).modifier();
		if (releaseVersionModifier != null) {
			releaseBootVersion = releaseBootVersion + releaseVersionModifier;
		}
		return releaseBootVersion;
	}

}
