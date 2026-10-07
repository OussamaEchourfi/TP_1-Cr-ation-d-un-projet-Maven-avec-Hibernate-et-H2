TP 1 : Création d'un projet Maven avec Hibernate et H2

Réalisé par : Oussama Ech-Chourfi Module , EMSI Marrakech

1. Objectif du TP

L'objectif de ce TP est de :

créer un projet Maven ;
configurer Hibernate (implémentation de JPA) ;
utiliser la base de données H2 en mémoire ;
mapper une classe Java vers une table (entité Produit) ;
réaliser des opérations d'insertion et de lecture ;
consulter les données via la console web H2.
2. Outils et technologies
Outil	Rôle
Java	Langage de programmation
Maven	Gestion du projet et des dépendances
Hibernate / JPA	ORM (mapping objet-relationnel)
H2 Database 2.1.214	Base de données embarquée en mémoire
IntelliJ IDEA	Environnement de développement
Git / GitHub	Versionnement et partage du code
3. Structure du projet

<img width="613" height="603" alt="Screenshot 2026-10-07 203520" src="https://github.com/user-attachments/assets/81a7addf-8e22-4c15-ad1a-25faa5de255e" />


4. Étapes de réalisation
4.1 Création du projet Maven et ajout des dépendances

Le fichier pom.xml contient les dépendances nécessaires :

hibernate-core : moteur Hibernate ;
h2 : pilote et moteur de la base H2.
4.2 Configuration de la persistance (persistence.xml)

Le fichier META-INF/persistence.xml définit l'unité de persistance hibernate-demo :

Driver : org.h2.Driver
URL : jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1 (base en mémoire)
Utilisateur : sa, mot de passe vide
Dialecte : org.hibernate.dialect.H2Dialect
hbm2ddl.auto : génération automatique du schéma
show_sql / format_sql : affichage des requêtes SQL dans la console

DB_CLOSE_DELAY=-1 permet de garder la base en mémoire tant que la JVM est active, même après la fermeture de l'EntityManagerFactory.

4.3 Création de l'entité Produit

La classe Produit est annotée avec @Entity et @Table(name = "produits") :

Attribut	Type	Description
id	Long	Clé primaire, générée automatiquement (IDENTITY)
nom	String	Nom du produit
prix	BigDecimal	Prix du produit

Elle contient un constructeur sans argument (requis par JPA), un constructeur avec paramètres, les getters/setters, ainsi que equals, hashCode et toString.

4.4 Classe principale App

La classe App :

crée l'EntityManagerFactory à partir de l'unité hibernate-demo ;
insère trois produits (Ordinateur portable, Téléphone, Tablette) dans une transaction avec commit / rollback ;
affiche tous les produits via une requête JPQL (SELECT p FROM Produit p ORDER BY p.nom) ;
recherche un produit par son identifiant avec em.find ;
démarre la console web H2 sur le port 8082.
5. Résultats
5.1 Affichage dans la console IntelliJ

Hibernate génère automatiquement la table produits, puis les produits sont insérés et affichés. La recherche du produit d'identifiant 2 retourne bien le Téléphone.

<img width="476" height="142" alt="Screenshot 2026-10-07 202138" src="https://github.com/user-attachments/assets/5b9e789f-24df-4688-a885-71832102cef8" />


5.2 Vérification dans la console H2

Connexion à la console sur http://localhost:8082 avec l'URL JDBC jdbc:h2:mem:testdb, utilisateur sa et mot de passe vide. La requête suivante :

sql
SELECT * FROM PRODUITS;

retourne bien les 3 produits insérés.

<img width="851" height="496" alt="Screenshot 2026-10-07 204137" src="https://github.com/user-attachments/assets/0ad0a641-34ff-4cf5-85d8-3af4fa4c2e22" />


6. Difficultés rencontrées
La console H2 s'ouvre par défaut avec l'URL jdbc:h2:~/test, ce qui provoque l'erreur Database not found (90149). Il faut saisir l'URL définie dans le persistence.xml : jdbc:h2:mem:testdb.
Comme la base est en mémoire, l'application doit rester en cours d'exécution pour consulter les données dans la console H2.
7. Conclusion

Ce TP a permis de mettre en place un projet Maven avec Hibernate et H2, de mapper une entité Java vers une table, de réaliser des opérations d'insertion et de lecture avec JPA, et de vérifier le résultat dans la console H2. Il constitue une base pour les prochains TP sur les relations entre entités et la couche DAO.

8. Lancer le projet

bash

git clone https://github.com/OussamaEchourfi/TP_1-Cr-ation-d-un-projet-Maven-avec-Hibernate-et-H2.git

cd TP_1-Cr-ation-d-un-projet-Maven-avec-Hibernate-et-H2

mvn compile exec:java -Dexec.mainClass="com.example.App"

Puis ouvrir http://localhost:8082 et se connecter avec jdbc:h2:mem:testdb / sa.
