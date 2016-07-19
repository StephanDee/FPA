package l3_da;

import l4_dm.DmAufgabe;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static multex.MultexUtil.create;

/**
 * Created by Stephan D on 08.06.2016.
 */
public class DaGenericImpl<E extends DmAufgabe> implements DaGeneric<E> {

    private final Class<E> managedClass;
    private final EntityManager manager;

    public DaGenericImpl(Class<E> managedClass, EntityManager manager) {
        this.managedClass = managedClass;
        this.manager = manager;
    }

    @Override
    public boolean save(E entity) {
        if (entity.getId() == null) {
            manager.persist(entity);
            return true;
        } else {
            manager.merge(entity);
            return false;
        }
    }

    @Override
    public void delete(E entity) {
        manager.remove(entity);
    }

    @Override
    public E find(Long id) throws IdNotFoundExc {
        final E result = manager.find(managedClass, id);
        if (result == null) {
            throw create(DaGeneric.IdNotFoundExc.class, managedClass.getSimpleName(), id);
        }
        return result;
    }

    @Override
    public List<E> findAll() {
        final TypedQuery<E> query = manager.createQuery("Find All", this.managedClass);
        return query.getResultList();
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
