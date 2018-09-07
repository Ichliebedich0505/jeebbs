package edu.scut.jeebbs.repository;

import edu.scut.jeebbs.domain.News;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends PagingAndSortingRepository<News, Long> {
}
