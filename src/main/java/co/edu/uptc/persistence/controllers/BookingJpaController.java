package co.edu.uptc.persistence.controllers;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;

import java.util.Date;

import co.edu.uptc.model.Booking;
import co.edu.uptc.model.RoomType;
import co.edu.uptc.persistence.exceptions.NonexistentEntityException;

public class BookingJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public BookingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public BookingJpaController() {
        emf = Persistence.createEntityManagerFactory("co.edu.uptc_isaceboy_project_PU");
    }

    public void create(Booking booking) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Booking booking) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            booking = em.merge(booking);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = booking.getBookingId();
                if (findBooking(id) == null) {
                    throw new NonexistentEntityException("The booking with id " + id + " no longer exists.");
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
            Booking booking;
            try {
                booking = em.getReference(Booking.class, id);
                booking.getBookingId();
            } catch (EntityNotFoundException e) {
                throw new NonexistentEntityException("The booking with id " + id + " no longer exists.", e);
            }
            em.remove(booking);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Booking findBooking(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Booking.class, id);
        } finally {
            em.close();
        }
    }

    public List<Booking> findBookingEntities() {
        return findBookingEntities(true, -1, -1);
    }

    public List<Booking> findBookingEntities(int maxResults, int firstResult) {
        return findBookingEntities(false, maxResults, firstResult);
    }

    private List<Booking> findBookingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            javax.persistence.criteria.CriteriaQuery<Booking> cq = em.getCriteriaBuilder().createQuery(Booking.class);
            cq.select(cq.from(Booking.class));
            javax.persistence.Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Booking> findBookingsByRoomType(RoomType roomType) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT b FROM Booking b WHERE b.room.roomType = :roomType AND b.endDate >= :today";
            return em.createQuery(jpql, Booking.class)
                    .setParameter("roomType", roomType)
                    .setParameter("today", new Date(), TemporalType.DATE)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Booking> findActiveBookingsWithCheckOutToday() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT b FROM Booking b WHERE b.isActive = true AND b.endDate <= :today";
            return em.createQuery(jpql, Booking.class)
                    .setParameter("today", new Date(), TemporalType.DATE)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Booking> findBookingsByCustomerIdentificationLike(String query) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT b FROM Booking b WHERE b.customer.identification LIKE :query";
            return em.createQuery(jpql, Booking.class)
                    .setParameter("query", "%" + query + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean hasActiveBookingForCustomer(String customerId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(b) FROM Booking b WHERE b.customer.identification = :customerId AND b.isActive = true";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public int getBookingCount() {
        EntityManager em = getEntityManager();
        try {
            javax.persistence.criteria.CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            javax.persistence.criteria.Root<Booking> rt = cq.from(Booking.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            javax.persistence.Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int findMaxBookingId() {
        EntityManager em = getEntityManager();
        try {
            Integer maxId = em.createQuery("SELECT MAX(b.bookingId) FROM Booking b", Integer.class)
                    .getSingleResult();
            return maxId != null ? maxId : 0;
        } finally {
            em.close();
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
