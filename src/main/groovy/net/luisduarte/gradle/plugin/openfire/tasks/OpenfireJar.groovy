package net.luisduarte.gradle.plugin.openfire.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.copy.DefaultCopySpec
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.bundling.Jar

/**
 * Created by lduarte on 18/01/16.
 */
class OpenfireJar extends Jar {

    private File pluginXml

    private FileCollection classpath
    private final DefaultCopySpec openfireSpec

    OpenfireJar() {
        openfireSpec = rootSpec.addChildBeforeSpec(mainSpec)

        openfireSpec.into('classes') {
            from {
                def classpath = getClasspath()
                classpath ? classpath.filter { File file -> file.isDirectory() } : []
            }
        }
        openfireSpec.into('lib') {
            from {
                def classpath = getClasspath()
                classpath ? classpath.filter {File file -> file.isFile()} : []
            }
        }

        openfireSpec.into('') {
            from {
                getPluginXml()
            }
            rename {
                'plugin.xml'
            }
        }
    }

    /**
     * Returns the classpath to include in the WAR archive. Any JAR or ZIP files in this classpath are included in the
     * {@code lib/} directory. Any directories in this classpath are included in the {@code classes/}
     * directory.
     *
     * @return The classpath. Returns an empty collection when there is no classpath to include in the WAR.
     */
    @InputFiles @Optional
    FileCollection getClasspath() {
        return classpath
    }

    /**
     * Sets the classpath to include in the WAR archive.
     *
     * @param classpath The classpath. Must not be null.
     */
    void setClasspath(Object classpath) {
        this.classpath = project.files(classpath)
    }

    /**
     * Adds files to the classpath to include in the JAR archive.
     *
     * @param classpath The files to add. These are evaluated as per {@link org.gradle.api.Project#files(Object [])}
     */
    void classpath(Object... classpath) {
        FileCollection oldClasspath = getClasspath()
        this.classpath = project.files(oldClasspath ?: [], classpath)
    }

    /**
     * Returns the {@code plugin.xml} file to include in the JAR archive. When {@code null}, no {@code plugin.xml} file is
     * included in the WAR.
     *
     * @return The {@code plugin.xml} file.
     */
    @InputFile
    @Optional
    public File getPluginXml() {
        return pluginXml;
    }

    /**
     * Sets the {@code plugin.xml} file to include in the JAR archive. When {@code null}, no {@code plugin.xml} file is
     * included in the WAR.
     *
     * @param webXml The {@code plugin.xml} file. Maybe null.
     */
    public void setPluginXml(File pluginXml) {
        this.pluginXml = pluginXml
    }
}
