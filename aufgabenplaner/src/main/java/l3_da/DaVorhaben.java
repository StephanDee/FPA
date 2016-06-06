package l3_da;

import l4_dm.DmVorhaben;

public interface DaVorhaben extends DaGeneric<DmVorhaben> {

    public static void execute() throws Exception {

        {
            // Entity persistieren
            final DaFactoryForJPA transaction = new DaFactoryForJPA();
            transaction.beginTransaction();
            final DmVorhaben vorhaben = new DmVorhaben();
            vorhaben.getId();
            vorhaben.getTitel();
            vorhaben.getBeschreibung();
            vorhaben.getTeile();
            vorhaben.getIstStunden();
            vorhaben.getRestStunden();
            vorhaben.getStatus();
            vorhaben.getEndTermin();

            //Hierdurch wird schritt zu einer "managed entity":
            transaction.em.persist(vorhaben);

            //Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
            transaction.endTransaction(true);

        }
    }
}
