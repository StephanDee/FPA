package l3_da;

import l4_dm.DmSchritt;

import javax.persistence.EntityManager;

/**
 * Created by Stephan D on 08.06.2016.
 */
public class DaSchrittImpl extends DaGenericImpl<DmSchritt> implements DaSchritt {

    public DaSchrittImpl(EntityManager manager) {
        super(manager);
    }
}
