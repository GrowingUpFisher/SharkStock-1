/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Kurs;
import entities.Spolka;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Kingu
 */
@ManagedBean(name = "SpolkaController")
@SessionScoped
public class SpolkaController implements Serializable {

 //   private EntityManagerFactory emf = null;
        private EntityManagerFactory emf  =Persistence.createEntityManagerFactory("SharkStockPU");

    public SpolkaController() {
       // emf  = Persistence.createEntityManagerFactory("SharkStockPU");
    }

    public void test() {
        Spolka sp = new Spolka();
        sp.setNazwa("spolka testowa");
        this.create(sp);

    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Spolka spolka) {
        if (spolka.getKursList() == null) {
            spolka.setKursList(new ArrayList<Kurs>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Kurs> attachedKursList = new ArrayList<Kurs>();
            for (Kurs kursListKursToAttach : spolka.getKursList()) {
                kursListKursToAttach = em.getReference(kursListKursToAttach.getClass(), kursListKursToAttach.getIdKurs());
                attachedKursList.add(kursListKursToAttach);
            }
            spolka.setKursList(attachedKursList);
            em.persist(spolka);
            for (Kurs kursListKurs : spolka.getKursList()) {
                Spolka oldIdSpolkiFkOfKursListKurs = kursListKurs.getIdSpolkiFk();
                kursListKurs.setIdSpolkiFk(spolka);
                kursListKurs = em.merge(kursListKurs);
                if (oldIdSpolkiFkOfKursListKurs != null) {
                    oldIdSpolkiFkOfKursListKurs.getKursList().remove(kursListKurs);
                    oldIdSpolkiFkOfKursListKurs = em.merge(oldIdSpolkiFkOfKursListKurs);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Spolka spolka) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Spolka persistentSpolka = em.find(Spolka.class, spolka.getIdSpolka());
            List<Kurs> kursListOld = persistentSpolka.getKursList();
            List<Kurs> kursListNew = spolka.getKursList();
            List<String> illegalOrphanMessages = null;
            for (Kurs kursListOldKurs : kursListOld) {
                if (!kursListNew.contains(kursListOldKurs)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Kurs " + kursListOldKurs + " since its idSpolkiFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Kurs> attachedKursListNew = new ArrayList<Kurs>();
            for (Kurs kursListNewKursToAttach : kursListNew) {
                kursListNewKursToAttach = em.getReference(kursListNewKursToAttach.getClass(), kursListNewKursToAttach.getIdKurs());
                attachedKursListNew.add(kursListNewKursToAttach);
            }
            kursListNew = attachedKursListNew;
            spolka.setKursList(kursListNew);
            spolka = em.merge(spolka);
            for (Kurs kursListNewKurs : kursListNew) {
                if (!kursListOld.contains(kursListNewKurs)) {
                    Spolka oldIdSpolkiFkOfKursListNewKurs = kursListNewKurs.getIdSpolkiFk();
                    kursListNewKurs.setIdSpolkiFk(spolka);
                    kursListNewKurs = em.merge(kursListNewKurs);
                    if (oldIdSpolkiFkOfKursListNewKurs != null && !oldIdSpolkiFkOfKursListNewKurs.equals(spolka)) {
                        oldIdSpolkiFkOfKursListNewKurs.getKursList().remove(kursListNewKurs);
                        oldIdSpolkiFkOfKursListNewKurs = em.merge(oldIdSpolkiFkOfKursListNewKurs);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = spolka.getIdSpolka();
                if (findSpolka(id) == null) {
                    throw new NonexistentEntityException("The spolka with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Spolka spolka;
            try {
                spolka = em.getReference(Spolka.class, id);
                spolka.getIdSpolka();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The spolka with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Kurs> kursListOrphanCheck = spolka.getKursList();
            for (Kurs kursListOrphanCheckKurs : kursListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Spolka (" + spolka + ") cannot be destroyed since the Kurs " + kursListOrphanCheckKurs + " in its kursList field has a non-nullable idSpolkiFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(spolka);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Spolka> findSpolkaEntities() {
        return findSpolkaEntities(true, -1, -1);
    }

    public List<Spolka> findSpolkaEntities(int maxResults, int firstResult) {
        return findSpolkaEntities(false, maxResults, firstResult);
    }

    private List<Spolka> findSpolkaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Spolka.class));
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

    public Spolka findSpolka(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Spolka.class, id);
        } finally {
            em.close();
        }
    }

    public int getSpolkaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Spolka> rt = cq.from(Spolka.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @PostConstruct
    private void init() {
    //    emf = Persistence.createEntityManagerFactory("SharkStockPU");
    }
}
