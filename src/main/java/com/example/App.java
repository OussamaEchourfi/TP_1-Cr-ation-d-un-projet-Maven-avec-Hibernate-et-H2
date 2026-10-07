package com.example;

import com.example.model.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.h2.tools.Server;

public class App {


    private static final String UNITE_PERSISTANCE = "hibernate-demo";

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNITE_PERSISTANCE);
        try {
            ajouterProduits(emf);
            afficherProduits(emf);
            afficherProduitParId(emf, 2L);
        } finally {
            emf.close();
        }

        try {
            Server.createWebServer("-web", "-webPort", "8082").start();
            System.out.println("Console H2 disponible sur : http://localhost:8082");
        } catch (Exception e) {
            System.out.println("Erreur lors du démarrage de la console H2");
            e.printStackTrace();
        }
    }

    // Exécute une action dans une transaction avec commit / rollback automatique
    private static void executerTransaction(EntityManagerFactory emf, Consumer<EntityManager> action) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            action.accept(em);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private static void ajouterProduits(EntityManagerFactory emf) {
        executerTransaction(emf, em -> {
            em.persist(new Produit("Ordinateur portable", new BigDecimal("8999.00")));
            em.persist(new Produit("Téléphone", new BigDecimal("4500.50")));
            em.persist(new Produit("Tablette", new BigDecimal("2750.00")));
        });
        System.out.println("Les produits ont été ajoutés.");
    }

    private static void afficherProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> produits = em
                    .createQuery("SELECT p FROM Produit p ORDER BY p.nom", Produit.class)
                    .getResultList();

            System.out.println("\n=== Tous les produits ===");
            produits.forEach(System.out::println);
        } finally {
            em.close();
        }
    }

    private static void afficherProduitParId(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Optional<Produit> resultat = Optional.ofNullable(em.find(Produit.class, id));

            System.out.println("\n=== Produit n°" + id + " ===");
            if (resultat.isPresent()) {
                System.out.println(resultat.get());
            } else {
                System.out.println("Aucun produit trouvé avec cet id.");
            }
        } finally {
            em.close();
        }
    }
}