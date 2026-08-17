package org.ua.fkrkm.progplatform.function;

import org.springframework.util.CollectionUtils;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Проставляє теми
 */
public class SetTopic implements Consumer<CourseResponse> {
    // Теми
    private final List<Topic> topics;

    /**
     * Конструктор
     *
     * @param topics теми
     */
    public SetTopic(List<Topic> topics) {
        this.topics = topics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(CourseResponse courseResponse) {
        List<ModuleView> modules = courseResponse.getModules();
        if (!CollectionUtils.isEmpty(modules)) {
            List<TopicView> topicViews = topics.stream()
                    .map(this::topicToTopicView)
                    .toList();
            List<ModuleView> moduleViews = modules.stream()
                    .peek((module) -> this.setTopic(module, topicViews))
                    .peek(this::countingTopicsPage)
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
                .created(topic.getCreated())
                .updated(topic.getUpdated())
                .build();
    }

    /**
     * Проставляє теми
     *
     * @param moduleView модуль
     * @param topicViews теми
     */
    private void setTopic(ModuleView moduleView, List<TopicView> topicViews) {
        // Послідовність
        AtomicInteger sequence = new AtomicInteger(0);
        List<TopicView> topics = topicViews.stream()
                .filter((topic) -> topic.getModuleId().equals(moduleView.getId()))
                // Сортує по ID
                .sorted(Comparator.comparingInt(TopicView::getId))
                .peek((topicView) -> topicView.setPage(sequence.incrementAndGet()))
                .toList();
        moduleView.setTopics(topics);
    }

    /**
     * Рахує номери сторінок тем у модулі
     *
     * @param moduleView модуль
     */
    private void countingTopicsPage(ModuleView moduleView) {
        int topicAmount = moduleView.getTopics().size();
        List<Integer> pages = IntStream.range(1, topicAmount  + 1)
                .boxed()
                .toList();
        moduleView.setPages(pages);
    }
}
