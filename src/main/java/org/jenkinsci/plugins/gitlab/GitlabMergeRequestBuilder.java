package org.jenkinsci.plugins.gitlab;

import java.util.Map;
import hudson.model.AbstractProject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitlabMergeRequestBuilder {

    private AbstractProject<?, ?> _project;
    private GitlabBuildTrigger _trigger;
    private Map<Integer, GitlabMergeRequestWrapper> _mergeRequests;
    private GitlabBuilds _builds;
    private GitlabRepository _repository;

    public static GitlabMergeRequestBuilder getBuilder() {
        return new GitlabMergeRequestBuilder();
    }

    private GitlabMergeRequestBuilder() {
    }

    public void stop() {
        _repository = null;
        _builds = null;
    }

    public void run() {
        _repository.check();
    }

    public GitlabMergeRequestBuilder setTrigger(GitlabBuildTrigger trigger) {
        _trigger = trigger;
        return this;
    }

    public GitlabBuildTrigger getTrigger() {
        return _trigger;
    }

    public GitlabMergeRequestBuilder setProject(AbstractProject<?, ?> project) {
        _project = project;

        return this;
    }

    public GitlabMergeRequestBuilder setMergeRequests(Map<Integer, GitlabMergeRequestWrapper> mergeRequests) {
        _mergeRequests = mergeRequests;
        return this;
    }

    public GitlabMergeRequestBuilder build() {
        if (_mergeRequests == null || _trigger == null || _project == null) {
            throw new IllegalStateException();
        }

        _repository = new GitlabRepository(_trigger.getProjectPath(), this, _mergeRequests);
        _repository.init();
        _builds = new GitlabBuilds(_trigger, _repository);
        return this;
    }

    public GitlabBuilds getBuilds() {
        return _builds;
    }

    public Gitlab getGitlab() {
        return GitlabBuildTrigger.DESCRIPTOR.getGitlab();
    }
    
    public boolean isEnableBuildTriggeredMessage() {
        if (_trigger != null) {
            return _trigger.getDescriptor().isEnableBuildTriggeredMessage();
        } else {
            return GitlabBuildTrigger.DESCRIPTOR.isEnableBuildTriggeredMessage();
        }
    }

    public String getRebuildTriggerRegEx() {
        if (_trigger != null) {
            return _trigger.getDescriptor().getRebuildTriggerRegEx();
        } else {
            return GitlabBuildTrigger.DESCRIPTOR.getRebuildTriggerRegEx();
        }
    }
}
