
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
import programacion2.parquedeportes.logica.Deporte;
import programacion2.parquedeportes.persistencia.exceptions.NonexistentEntityException;

public class DeporteJpaController implements Serializable {

    public DeporteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public DeporteJpaController(){
        emf = Persistence.createEntityManagerFactory("parqueDeportesPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Deporte deporte) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(deporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Deporte deporte) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            deporte = em.merge(deporte);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = deporte.getId();
                if (findDeporte(id) == null) {
                    throw new NonexistentEntityException("The deporte with id " + id + " no longer exists.");
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
            Deporte deporte;
            try {
                deporte = em.getReference(Deporte.class, id);
                deporte.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deporte with id " + id + " no longer exists.", enfe);
            }
            em.remove(deporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Deporte> findDeporteEntities() {
        return findDeporteEntities(true, -1, -1);
    }

    public List<Deporte> findDeporteEntities(int maxResults, int firstResult) {
        return findDeporteEntities(false, maxResults, firstResult);
    }

    private List<Deporte> findDeporteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Deporte.class));
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

    public Deporte findDeporte(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Deporte.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeporteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Deporte> rt = cq.from(Deporte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
