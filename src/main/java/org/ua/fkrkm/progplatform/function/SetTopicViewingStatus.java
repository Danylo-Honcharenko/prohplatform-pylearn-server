package org.ua.fkrkm.progplatform.function;

import org.springframework.util.CollectionUtils;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetTopicViewingStatus implements Consumer<CourseResponse> {

    private final List<ModuleStat> moduleStats;

    public SetTopicViewingStatus(Supplier<List<ModuleStat>> supplier) {
        this.moduleStats = supplier.get();
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        List<ModuleView> modules = courseResponse.getModules();
        if (!CollectionUtils.isEmpty(modules)) {
            List<TopicView> topicViews = modules.stream()
                    .flatMap((moduleView) -> moduleView.getTopics().stream())
                    .peek((topicView) -> topicView.setDone(this.checkIsTopicDone(topicView.getId())))
                    .toList();

            List<ModuleView> moduleViews = modules.stream()
                    .peek((module) -> this.setTopic(module, topicViews))
                    .toList();

            courseResponse.setModules(moduleViews);
        }
    }

    private void setTopic(ModuleView moduleView, List<TopicView> topicViews) {
        List<TopicView> topics = topicViews.stream()
                .filter((topic) -> topic.getModuleId().equals(moduleView.getId()))
                .sorted(Comparator.comparingInt(TopicView::getId))
                .toList();
        moduleView.setTopics(topics);
    }

    /**
     * Перевіряємо статус перегляду теми true - переглянута/пройдена, false - не переглянуто
     *
     * @param topicId ID теми
     * @return Boolean true/false
     */
    private Boolean checkIsTopicDone(int topicId) {
        if (this.moduleStats.isEmpty()) return false;
        for (ModuleStat moduleStat : this.moduleStats) {
            if (moduleStat.getTopicId() == topicId) return true;
        }
        return false;
    }
}
