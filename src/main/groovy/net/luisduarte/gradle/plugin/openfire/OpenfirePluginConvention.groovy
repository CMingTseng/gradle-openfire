package net.luisduarte.gradle.plugin.openfire

import org.gradle.api.Project

/**
 * Created by lduarte on 18/01/16.
 */
class OpenfirePluginConvention {
    /**
     * The plugin.xml file, relative to the project directory.
     */
    String pluginXml
    final Project project

    def OpenfirePluginConvention(Project project) {
        this.project = project
        pluginXml = 'plugin.xml'
    }

    /**
     * Returns the web application directory.
     */
    File getPluginXml() {
        project.file(pluginXml)
    }
}
