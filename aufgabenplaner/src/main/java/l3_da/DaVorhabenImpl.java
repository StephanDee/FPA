package l3_da;

import l4_dm.DmVorhaben;

import javax.persistence.EntityManager;

/**
 * Created by Stephan D on 08.06.2016.
 */
public class DaVorhabenImpl extends DaGenericImpl<DmVorhaben> implements DaVorhaben {

    public DaVorhabenImpl(EntityManager manager) {
        super(manager);
    }
}
