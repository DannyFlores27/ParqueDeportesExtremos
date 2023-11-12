
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
import programacion2.parquedeportes.logica.Parque;
import programacion2.parquedeportes.persistencia.exceptions.NonexistentEntityException;

public class ParqueJpaController implements Serializable {

    public ParqueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ParqueJpaController(){
        emf = Persistence.createEntityManagerFactory("parqueDeportesPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parque parque) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(parque);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parque parque) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parque = em.merge(parque);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = parque.getId();
                if (findParque(id) == null) {
                    throw new NonexistentEntityException("The parque with id " + id + " no longer exists.");
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
            Parque parque;
            try {
                parque = em.getReference(Parque.class, id);
                parque.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parque with id " + id + " no longer exists.", enfe);
            }
            em.remove(parque);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Parque> findParqueEntities() {
        return findParqueEntities(true, -1, -1);
    }

    public List<Parque> findParqueEntities(int maxResults, int firstResult) {
        return findParqueEntities(false, maxResults, firstResult);
    }

    private List<Parque> findParqueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parque.class));
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

    public Parque findParque(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parque.class, id);
        } finally {
            em.close();
        }
    }

    public int getParqueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parque> rt = cq.from(Parque.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
