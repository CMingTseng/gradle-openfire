package net.luisduarte.gradle.plugin.openfire

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * Created by lduarte on 18/01/16.
 */
class OpenfirePluginTest {

    @Test
    public void openfirePluginProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'net.luisduarte.openfire'

        assert project.version != null
    }
}
