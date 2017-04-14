/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import entities.Kurs;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Spolka;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Kingu
 */
public class KursController implements Serializable {

    public KursController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kurs kurs) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Spolka idSpolkiFk = kurs.getIdSpolkiFk();
            if (idSpolkiFk != null) {
                idSpolkiFk = em.getReference(idSpolkiFk.getClass(), idSpolkiFk.getIdSpolka());
                kurs.setIdSpolkiFk(idSpolkiFk);
            }
            em.persist(kurs);
            if (idSpolkiFk != null) {
                idSpolkiFk.getKursList().add(kurs);
                idSpolkiFk = em.merge(idSpolkiFk);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kurs kurs) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kurs persistentKurs = em.find(Kurs.class, kurs.getIdKurs());
            Spolka idSpolkiFkOld = persistentKurs.getIdSpolkiFk();
            Spolka idSpolkiFkNew = kurs.getIdSpolkiFk();
            if (idSpolkiFkNew != null) {
                idSpolkiFkNew = em.getReference(idSpolkiFkNew.getClass(), idSpolkiFkNew.getIdSpolka());
                kurs.setIdSpolkiFk(idSpolkiFkNew);
            }
            kurs = em.merge(kurs);
            if (idSpolkiFkOld != null && !idSpolkiFkOld.equals(idSpolkiFkNew)) {
                idSpolkiFkOld.getKursList().remove(kurs);
                idSpolkiFkOld = em.merge(idSpolkiFkOld);
            }
            if (idSpolkiFkNew != null && !idSpolkiFkNew.equals(idSpolkiFkOld)) {
                idSpolkiFkNew.getKursList().add(kurs);
                idSpolkiFkNew = em.merge(idSpolkiFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = kurs.getIdKurs();
                if (findKurs(id) == null) {
                    throw new NonexistentEntityException("The kurs with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kurs kurs;
            try {
                kurs = em.getReference(Kurs.class, id);
                kurs.getIdKurs();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kurs with id " + id + " no longer exists.", enfe);
            }
            Spolka idSpolkiFk = kurs.getIdSpolkiFk();
            if (idSpolkiFk != null) {
                idSpolkiFk.getKursList().remove(kurs);
                idSpolkiFk = em.merge(idSpolkiFk);
            }
            em.remove(kurs);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kurs> findKursEntities() {
        return findKursEntities(true, -1, -1);
    }

    public List<Kurs> findKursEntities(int maxResults, int firstResult) {
        return findKursEntities(false, maxResults, firstResult);
    }

    private List<Kurs> findKursEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kurs.class));
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

    public Kurs findKurs(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kurs.class, id);
        } finally {
            em.close();
        }
    }

    public int getKursCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kurs> rt = cq.from(Kurs.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
