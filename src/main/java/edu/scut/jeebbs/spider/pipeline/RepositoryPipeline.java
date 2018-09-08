package edu.scut.jeebbs.spider.pipeline;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.Assert;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * use spring data jpa to save data fetch from spider
 * @param <T> class of bean to store into db
 */
public abstract class RepositoryPipeline<T> implements Pipeline {
    private Class<T> clazz;

    public RepositoryPipeline(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        quartz
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        T bean = instance();
        Assert.notNull(bean, "can't create an instance of class: " + clazz.getName());
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = resultItems.getAll();
        beanMap.putAll(map);
        save(bean);
    }

    // use reflect to create an instance of class T
    private T instance() {
        return ReflectionUtils.createInstanceIfPresent(clazz.getName(), null);
    }

    protected abstract void save(T bean);
}
