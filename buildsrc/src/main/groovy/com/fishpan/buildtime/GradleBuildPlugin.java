package com.fishpan.buildtime;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Created by yupan on 17/4/16.
 */
public class GradleBuildPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getGradle().addBuildListener(new GradleBuilderTimerListener(project));
    }
}
