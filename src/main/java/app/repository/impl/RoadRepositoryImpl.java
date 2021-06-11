package app.repository.impl;

import app.domain.model.Road;
import app.repository.RoadRepository;
import app.repository.RoadBlockRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoadRepositoryImpl implements RoadRepository {

    private final SessionFactory sessionFactory;
    private final RoadBlockRepository roadBlockRepository;

    @Autowired
    public RoadRepositoryImpl(SessionFactory sessionFactory, RoadBlockRepository roadBlockRepository){
        this.sessionFactory = sessionFactory;
        this.roadBlockRepository = roadBlockRepository;
    }


    @Override
    public Optional<Road> get(Long id) {
        Session session = sessionFactory.openSession();
        var result = session.get(Road.class, id);
        if (result == null){
            var c = 0;
        }

        result.getBlockList().size();

        for (int i = 0; i < result.getLineLength(); i++){
            result.getBlockList().set(i,  roadBlockRepository.get(result.getBlockList().get(i).getId()).get());
        }
        session.close();
        return Optional.of(result);
    }


    @Override
    public List<Road> getAll() {
        Session session = sessionFactory.openSession();
        var query = session.createQuery("from road", Road.class);
        var result = query.getResultList();

        result.forEach(line -> {
            line.getBlockList().size();

            for (int i = 0; i < line.getLineLength(); i++){
                line.getBlockList().set(i,  roadBlockRepository.get(line.getBlockList().get(i).getId()).get());
            }
        });

        session.close();
        return result;
    }

    @Override
    public void save(Road line) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(line);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Road line) {
        var current = get(line.getId());
        Session session = sessionFactory.openSession();
        var trans = session.beginTransaction();
        //session.evict(current);
        session.update(line);
        trans.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.openSession();
        var trans = session.beginTransaction();
        var curr = session.get(Road.class, id);
        session.delete(curr);
        trans.commit();
        session.close();
    }

    @Override
    public void delete(Road line) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        var curr = session.get(Road.class, line.getId());
        session.delete(curr);
        transaction.commit();
        session.close();
    }

    @Override
    public void clear() {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE from road").executeUpdate();
            transaction.commit();
            session.close();
        }catch (SQLGrammarException ignored){}
    }
}
