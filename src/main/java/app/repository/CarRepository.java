package app.repository;

import app.domain.model.Car;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository {
    Optional<Car> get(Long id);

    List<Car> getAll();

    void save(Car entity);

    void update(Car entity);

    void delete(Long id);

    void delete(Car entity);

    public void clear();
}
