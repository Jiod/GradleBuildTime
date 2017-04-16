package com.fishpan.buildtime;

import org.gradle.BuildResult;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yupan on 17/4/16.
 */
public class GradleBuilderTimerListener extends SimpleBuildListener implements TaskExecutionListener {
    private Project mProject;
    private Map<String, Long> mTaskBuildTime;

    private Long mTaskStartTime;
    private Long mTotalTime = 0L;
    private Long mMaxTime = 0L;

    public GradleBuilderTimerListener(Project project) {
        mProject = project;
        mTaskBuildTime = new LinkedHashMap<>();
    }

    @Override
    public void buildFinished(BuildResult buildResult) {
        super.buildFinished(buildResult);
        printTaskBuildTime();
    }

    @Override
    public void beforeExecute(Task task) {
        mTaskStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterExecute(Task task, TaskState taskState) {
        Long buildTime = System.currentTimeMillis() - mTaskStartTime;
        mTotalTime += buildTime;
        mMaxTime = buildTime > mMaxTime? buildTime : mMaxTime;
        mTaskBuildTime.put(task.getPath(), buildTime);
    }

    private void printTaskBuildTime(){
        Set<String> tasks = mTaskBuildTime.keySet();
        for (String task : tasks) {
            long time = mTaskBuildTime.get(task);
            int percent = (int) (time * 100 / mTotalTime);
            StringBuilder sb = new StringBuilder();

            int maxPercent = (int) (mMaxTime * 100 / mTotalTime);
            for(int i = 0; i < maxPercent - percent; i ++){
                sb.append(" ");
            }

            for(int i = 0; i < percent; i ++){
                sb.append("█");
            }

            sb.append("(");
            sb.append(percent > 9? "" : " ");
            sb.append(percent);
            sb.append("%)");
            sb.append(task);
            System.out.println(sb.toString());
        }
    }
}
