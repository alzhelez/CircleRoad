package app.repository;

import app.domain.model.Road;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadRepository {
    Optional<Road> get(Long id);

    List<Road> getAll();

    void save(Road line);

    void update(Road line);

    void delete(Long id);

    void delete(Road line);

    void clear();
}
