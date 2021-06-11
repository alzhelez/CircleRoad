package app.repository;

import app.domain.model.RoadBlock;

import java.util.List;
import java.util.Optional;

public interface RoadBlockRepository {
    Optional<RoadBlock> get(Long id);

    List<RoadBlock> getAll();

    void save(RoadBlock entity);

    void update(RoadBlock entity);

    void updateSavingNextBlocks(RoadBlock entity);

    void delete(Long id);

    void delete(RoadBlock entity);

    void clear();

}
