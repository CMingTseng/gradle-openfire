package net.luisduarte.gradle.plugin.openfire
import net.luisduarte.gradle.plugin.openfire.tasks.OpenfireJar
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.internal.java.WebApplication
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet

import java.util.concurrent.Callable
/**
 * Created by lduarte on 18/01/16.
 */
class OpenfirePlugin implements Plugin<Project> {

    public static final String PROVIDED_CONFIGURATION = "providedRuntime"
    public static final String OPENFIRE_TASK_NAME = "openfire"

    @Override
    void apply(Project project) {
        project.getPluginManager().apply(JavaPlugin.class);
        final OpenfirePluginConvention pluginConvention = new OpenfirePluginConvention(project);
        project.getConvention().getPlugins().put("openfire", pluginConvention);

        project.getTasks().withType(OpenfireJar.class, new Action<OpenfireJar>() {
            public void execute(OpenfireJar task) {
                task.from(new Callable() {
                    public Object call() throws Exception {
                        return pluginConvention.getPluginXml();
                    }
                })

                task.dependsOn(new Callable() {
                    public Object call() throws Exception {
                        return project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().getByName(
                                SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
                    }
                })

                task.classpath([new Callable() {
                    @Override
                    Object call() throws Exception {
                        FileCollection runtimeClasspath = project.getConvention().getPlugin(JavaPluginConvention.class)
                                .getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
                        Configuration providedRuntime = project.getConfigurations().getByName(
                                PROVIDED_CONFIGURATION);
                        return runtimeClasspath.minus(providedRuntime);
                    }
                }])
            }
        });

        OpenfireJar jar = project.getTasks().create(OPENFIRE_TASK_NAME, OpenfireJar.class);
        jar.setDescription("Generates a jar archive with all the openfire plugin structure.");
        jar.setGroup(BasePlugin.BUILD_GROUP);
        ArchivePublishArtifact jarArtifact = new ArchivePublishArtifact(jar);
        project.getExtensions().getByType(DefaultArtifactPublicationSet.class).addCandidate(jarArtifact);
        configureConfigurations(project.getConfigurations());
        configureComponent(project, jarArtifact);

    }

    public void configureConfigurations(ConfigurationContainer configurationContainer) {
        Configuration provideConfiguration = configurationContainer.create(PROVIDED_CONFIGURATION).setVisible(false).
                setDescription("Additional compile classpath for libraries that should not be part of the JAR archive.");
        configurationContainer.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(provideConfiguration);
        configurationContainer.getByName(JavaPlugin.RUNTIME_CONFIGURATION_NAME).extendsFrom(provideConfiguration);
    }

    private void configureComponent(Project project, PublishArtifact warArtifact) {
        project.getComponents().add(new WebApplication(warArtifact));
    }

}
