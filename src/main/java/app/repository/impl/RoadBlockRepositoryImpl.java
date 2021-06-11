package app.repository.impl;

import app.domain.model.Automobile;
import app.domain.model.RoadBlock;
import app.repository.RoadBlockRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoadBlockRepositoryImpl implements RoadBlockRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public RoadBlockRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<RoadBlock> get(Long id) {
        Session session = sessionFactory.openSession();
        var result = session.get(RoadBlock.class, id);

        var proxyLeft = (HibernateProxy) result.getLeftBlock();
        if (proxyLeft != null)
            proxyLeft.getHibernateLazyInitializer().getImplementation();

        var proxyRight = (HibernateProxy) result.getRightBlock();
        if(proxyRight != null)
            proxyRight.getHibernateLazyInitializer().getImplementation();

        var proxyCenter = (HibernateProxy) result.getCenterBlock();
        if (proxyCenter != null)
            proxyCenter.getHibernateLazyInitializer().getImplementation();

        var proxyAutomobile = (HibernateProxy) result.getAutomobile();
        if(proxyAutomobile != null)
            proxyAutomobile.getHibernateLazyInitializer().getImplementation();

        session.close();
        return Optional.of(result);
    }

    @Override
    public List<RoadBlock> getAll() {
        Session session = sessionFactory.openSession();
        var query = session.createQuery("From roadblocks ", RoadBlock.class);
        var result = query.getResultList();
        session.close();
        return result;
    }

    @Override
    public void save(RoadBlock entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(RoadBlock entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        var curr = session.get(Automobile.class, id);
        session.delete(curr);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(RoadBlock entity) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        var curr = session.get(RoadBlock.class, entity.getId());
        session.delete(curr);
        transaction.commit();
        session.close();
    }

    @Override
    public void clear() {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("delete from roadblocks").executeUpdate();
            transaction.commit();
        }catch (SQLGrammarException ignored){}
    }

    @Override
    public void updateSavingNextBlocks(RoadBlock entity){
        var curr = get(entity.getId());
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        entity.setCenterBlock(curr.get().getCenterBlock());
        entity.setRightBlock(curr.get().getRightBlock());
        entity.setLeftBlock(curr.get().getLeftBlock());

        session.update(entity);
        transaction.commit();
        session.close();

    }


//    public RoadBlockDTO getRoadBlockShiftByIndex(RoadBlockDTO roadBlockDTO, int index){
//        for (int i = 0; i < index; i++){
//            roadBlockDTO = roadBlockDTO.getAutomobileLinksList()[1];
//        }
//        return roadBlockDTO;
//        //return roadBlocks.get(roadBlocks.indexOf(roadBlock) + index);
//    }
//
//    @Override
//    public RoadBlockDTO getRoadBlockLinkByIndex(RoadBlockDTO roadBlockDTO, int index){
//        return roadBlockDTO.getAutomobileLinksList()[index];
//        //return roadBlocks.get(roadBlocks.indexOf(roadBlock)).getAutomobileLinksList()[index];
//    }
//
//    @Override
//    public void setRoadBlockLinkByIndex(RoadBlockDTO roadBlockDTOFrom, RoadBlockDTO roadBlockDTOTo, int index){
//        roadBlockDTOFrom.getAutomobileLinksList()[index] = roadBlockDTOTo;
//    }
}
