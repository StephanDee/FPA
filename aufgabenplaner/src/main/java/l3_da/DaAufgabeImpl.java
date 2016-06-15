package l3_da;

import l4_dm.DmAufgabe;

import javax.persistence.EntityManager;

/**
 * Created by Stephan D on 14.06.2016.
 */
public class DaAufgabeImpl extends DaGenericImpl<DmAufgabe> implements DaAufgabe {

    public DaAufgabeImpl(EntityManager manager) {
        super(DmAufgabe.class, manager);
    }
}
