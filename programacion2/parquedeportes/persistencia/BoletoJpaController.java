
package programacion2.parquedeportes.persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import programacion2.parquedeportes.logica.Boleto;
import programacion2.parquedeportes.persistencia.exceptions.NonexistentEntityException;

public class BoletoJpaController implements Serializable {

    public BoletoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public BoletoJpaController(){
        emf = Persistence.createEntityManagerFactory("parqueDeportesPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Boleto boleto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(boleto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Boleto boleto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            boleto = em.merge(boleto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = boleto.getId();
                if (findBoleto(id) == null) {
                    throw new NonexistentEntityException("The boleto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boleto boleto;
            try {
                boleto = em.getReference(Boleto.class, id);
                boleto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boleto with id " + id + " no longer exists.", enfe);
            }
            em.remove(boleto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Boleto> findBoletoEntities() {
        return findBoletoEntities(true, -1, -1);
    }

    public List<Boleto> findBoletoEntities(int maxResults, int firstResult) {
        return findBoletoEntities(false, maxResults, firstResult);
    }

    private List<Boleto> findBoletoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Boleto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Boleto findBoleto(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Boleto.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoletoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Boleto> rt = cq.from(Boleto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
