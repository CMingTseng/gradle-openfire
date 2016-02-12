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
    String webAppDirName
    String i18nDirName
    String databaseDirName
    final Project project

    def OpenfirePluginConvention(Project project) {
        this.project = project
        pluginXml = 'plugin.xml'
        webAppDirName = 'src/main/webapp'
        i18nDirName = 'src/main/i18n'
        databaseDirName = 'src/main/database'
    }

    /**
     * Returns the plugin.xml file.
     */
    File getPluginXml() {
        project.file(pluginXml)
    }

    /**
     * Returns the web application directory.
     */
    File getWebAppDir() {
        project.file(webAppDirName)
    }

    File getI18nDir() {
        project.file(i18nDirName)
    }

    File getDatabaseDir() {
        project.file(databaseDirName)
    }
}
