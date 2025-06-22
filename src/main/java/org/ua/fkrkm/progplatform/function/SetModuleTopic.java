package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SetModuleTopic implements Consumer<CourseResponse> {

    private final Function<List<Integer>, List<Topic>> function;
    private final List<ModuleStat> moduleStats;

    public SetModuleTopic(Function<List<Integer>, List<Topic>> function,
                          Supplier<List<ModuleStat>> supplier) {
        this.function = function;
        this.moduleStats = supplier.get();
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        List<ModuleView> modules = courseResponse.getModules();
        if (!modules.isEmpty()) {
            List<Integer> modulesIds = modules.stream()
                    .map(ModuleView::getId)
                    .toList();
            List<TopicView> topicViews = function.apply(modulesIds).stream()
                    .map(this::topicToTopicView)
                    .toList();
            List<ModuleView> moduleViews = modules.stream()
                    .peek((module) -> this.setTopic(module, topicViews))
                    .toList();
            courseResponse.setModules(moduleViews);
        }
    }

    /**
     * Конвертор Topic у TopicView
     *
     * @param topic тема
     * @return TopicView відображення теми
     */
    private TopicView topicToTopicView(Topic topic) {
    return TopicView.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .moduleId(topic.getModuleId())
                .done(this.checkIsTopicDone(topic.getId()))
                .created(topic.getCreated())
                .updated(topic.getUpdated())
                .build();
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

    private void setTopic(ModuleView moduleView, List<TopicView> topicViews) {
        List<TopicView> topics = topicViews.stream()
                .filter((topic) -> topic.getModuleId().equals(moduleView.getId()))
                .sorted(Comparator.comparingInt(TopicView::getId))
                .toList();
        moduleView.setTopics(topics);
    }
}
