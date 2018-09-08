package edu.scut.jeebbs.spider.pipeline;

import edu.scut.jeebbs.domain.News;
import edu.scut.jeebbs.repository.NewsRepository;
import edu.scut.jeebbs.utils.BeanValidators;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;

@Slf4j
public class NewsRepositoryPipeline extends RepositoryPipeline<News> {

    private NewsRepository repository;
    private Validator validator;

    public NewsRepositoryPipeline(NewsRepository repository, Validator validator) {
        super(News.class);
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    protected void save(News bean) {
        if (isValidated(bean) && !isExisted(bean)) {
            repository.save(bean);
        }
    }

    private boolean isValidated(News bean) {
        if (bean == null) {
            log.error("bean is null");
            return false;
        }
        try {
            BeanValidators.validateWithException(validator, bean);
            return true;
        } catch (ConstraintViolationException e) {
            List<String> errMsgs = BeanValidators.extractPropertyAndMessageAsList(e, ": ");
            log.error("bean is invalid: {}", errMsgs);
        }
        return false;
    }

    private boolean isExisted(News bean) {
        return repository.existsByHrefOrTitleLike(bean.getHref(), bean.getTitle());
    }
}
