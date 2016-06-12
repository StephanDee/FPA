package l3_da;

import l4_dm.DmAufgabe;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Stephan D on 08.06.2016.
 */
public class DaGenericImpl<E> implements DaGeneric<E> {

    private final EntityManager manager;

    public DaGenericImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean save(E entity) {
        manager.persist(entity);
        manager.flush();
        return true;
    }

    @Override
    public void delete(E entity) {
        manager.remove(entity);
    }

    @Override
    public E find(Long id) throws IdNotFoundExc {
        return ((E) manager.find(DaGeneric.class, id));
    }

    @Override
    public List<E> findAll() {
        Query query = manager.createQuery("Select All");
        return (List<E>) query.getResultList();
    }

    // Ab hier Zusatzaufgaben.
    @Override
    public List<E> findByField(String fieldName, Object fieldValue) {
        return null;
    }

    @Override
    public List<E> findByWhere(String whereClause, Object... args) {
        return null;
    }

    @Override
    public List<E> findByExample(E example) {
        return null;
    }
}
