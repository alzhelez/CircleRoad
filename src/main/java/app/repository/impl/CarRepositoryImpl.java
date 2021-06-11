package app.repository.impl;

import app.domain.model.Car;
import app.repository.CarRepository;
import app.repository.RoadBlockRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarRepositoryImpl implements CarRepository {

    private final SessionFactory sessionFactory;

    private final RoadBlockRepository roadBlockRepository;


    @Autowired
    public CarRepositoryImpl(SessionFactory sessionFactory, RoadBlockRepository roadBlockRepository) {
        this.sessionFactory = sessionFactory;
        this.roadBlockRepository = roadBlockRepository;
    }

    @Override
    public Optional<Car> get(Long id) {
        Session session = sessionFactory.openSession();
        var result = session.get(Car.class, id);

        if (result.getRoadBlock() instanceof HibernateProxy) {
            var proxy = (HibernateProxy) result.getRoadBlock();
            if (proxy != null) {
                proxy.getHibernateLazyInitializer().getImplementation();
            }
        }

        session.close();
        return Optional.of(result);
    }

    @Override
    public List<Car> getAll() {
        Session session = sessionFactory.openSession();
        var query = session.createQuery("from cars", Car.class);
        List<Car> result = query.getResultList();

        result.forEach(res -> {
            if (res.getRoadBlock() != null)
                res.setRoadBlock(roadBlockRepository.get(res.getRoadBlock().getId()).get());
        });

        session.close();
        return result;
    }

    @Override
    public void save(Car entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Car entity) {
        var current = get(entity.getId());
        Session session = sessionFactory.openSession();
        var trans = session.beginTransaction();
        session.update(entity);
        trans.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.openSession();
        var trans = session.beginTransaction();
        var curr = session.get(Car.class, id);

        curr.getRoadBlock().setCar(null);
        trans.commit();
        session.close();
        roadBlockRepository.update(curr.getRoadBlock());

        session = sessionFactory.openSession();
        trans = session.beginTransaction();
        //curr.setRoadBlock(null);

        session.delete(curr);
        trans.commit();
        session.close();
    }

    @Override
    public void delete(Car entity) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        var curr = session.get(Car.class, entity.getId());
        session.delete(curr);
        transaction.commit();
        session.close();
    }

    @Override
    public void clear() {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from cars ").executeUpdate();
            transaction.commit();
        } catch (Exception ignored) {
        }
    }
}
