/*
 * Copyright 2015 the original author or authors.
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

package org.gradle.testkit.runner

import org.gradle.integtests.fixtures.daemon.DaemonLogsAnalyzer
import org.gradle.integtests.fixtures.daemon.DaemonsFixture
import org.gradle.integtests.fixtures.executer.*
import org.gradle.internal.nativeintegration.services.NativeServices
import org.gradle.test.fixtures.file.TestFile
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.testkit.runner.fixtures.GradleRunnerIntegTestRunner
import org.gradle.testkit.runner.internal.DefaultGradleRunner
import org.gradle.testkit.runner.internal.ToolingApiGradleExecutor
import org.gradle.util.SetSystemProperties
import org.junit.Rule
import org.junit.runner.RunWith
import spock.lang.Shared
import spock.lang.Specification

@RunWith(GradleRunnerIntegTestRunner)
abstract class AbstractGradleRunnerIntegrationTest extends Specification {

    @Shared
    IntegrationTestBuildContext buildContext = new IntegrationTestBuildContext()

    @Rule
    TestNameTestDirectoryProvider testProjectDir = new TestNameTestDirectoryProvider()

    @Rule
    SetSystemProperties setSystemProperties = new SetSystemProperties((NativeServices.NATIVE_DIR_OVERRIDE): buildContext.gradleUserHomeDir.file("native").absolutePath)


    TestFile getTestKitWorkspace() {
        testProjectDir.file("test-kit-workspace")
    }

    TestFile getBuildFile() {
        file('build.gradle')
    }

    TestFile file(String path) {
        testProjectDir.file(path)
    }

    String getRootProjectName() {
        testProjectDir.testDirectory.name
    }

    DefaultGradleRunner runner(List<String> arguments) {
        runner(arguments as String[])
    }

    DefaultGradleRunner runner(String... arguments) {
        runner(GradleDistribution.fromPath(buildContext.gradleHomeDir), arguments)
    }

    DefaultGradleRunner runner(GradleDistribution gradleDistribution, String... arguments) {
        GradleRunner gradleRunner = new DefaultGradleRunner(gradleDistribution)
            .withTestKitDir(testKitWorkspace)
            .withProjectDir(testProjectDir.testDirectory)
            .withArguments(arguments)

        gradleRunner.withDebug(GradleRunnerIntegTestRunner.debug)
        assert gradleRunner.debug == GradleRunnerIntegTestRunner.debug
        gradleRunner
    }

    static String helloWorldTask() {
        """
        task helloWorld {
            doLast {
                println 'Hello world!'
            }
        }
        """
    }

    DaemonsFixture daemons(File gradleUserHomeDir, String daemonDir = 'daemon') {
        DaemonLogsAnalyzer.newAnalyzer(new File(gradleUserHomeDir, daemonDir), buildContext.version.version)
    }

    DaemonsFixture daemons() {
        daemons(testKitWorkspace, ToolingApiGradleExecutor.TEST_KIT_DAEMON_DIR_NAME)
    }

    def cleanup() {
        daemons().killAll()
    }

    ExecutionResult execResult(BuildResult buildResult) {
        new OutputScrapingExecutionResult(buildResult.output, buildResult.output)
    }

    ExecutionFailure execFailure(BuildResult buildResult) {
        new OutputScrapingExecutionFailure(buildResult.output, buildResult.output)
    }
}
