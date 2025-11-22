# SOAPCompteApp

Application Android client pour la gestion de comptes bancaires via un Web Service SOAP.

## 📝 Description
Ce projet est une application mobile native développée en **Kotlin**. Elle agit comme un client consommant un service web SOAP pour effectuer des opérations CRUD (Create, Read, Delete) sur des comptes bancaires.

L'application suit l'architecture standard Android avec l'utilisation de **XML Views** pour l'interface utilisateur et **Coroutines** pour la gestion asynchrone des appels réseau.

## 🚀 Fonctionnalités
*   **Lister les comptes** : Récupération et affichage de la liste des comptes depuis le serveur.
*   **Ajouter un compte** : Création d'un nouveau compte (Solde et Type : COURANT/EPARGNE) via une boîte de dialogue.
*   **Supprimer un compte** : Suppression d'un compte existant par un appui sur le bouton dédié.
*   **Interface Moderne** : Utilisation de `RecyclerView` et `Material Components` (Cards, Chips, Floating Action Button).

## 🛠 Technologies utilisées
*   **Langage** : Kotlin
*   **Interface Utilisateur** : XML Layouts, RecyclerView, Material Design.
*   **Communication Réseau** : [ksoap2-android](https://github.com/simpligility/ksoap2-android) (Protocole SOAP).
*   **Concurrence** : Kotlin Coroutines (Dispatchers.IO / Dispatchers.Main).

## 📋 Prérequis
Pour que l'application fonctionne, le **serveur SOAP** (Backend) doit être démarré au préalable.

1.  **Projet Serveur** : Une application Java (non incluse dans ce dépôt) exposant un service JAX-WS.
2.  **Configuration du Service** :
    *   **URL** : `http://localhost:8082/services/ws`
    *   **Namespace** : `http://ws.soapAcount/`

> **Note :** L'application Android est configurée pour pointer vers `http://10.0.2.2:8082/services/ws`, ce qui correspond au `localhost` de la machine hôte depuis l'émulateur Android.

## 📦 Installation et Exécution
1.  Clonez ce dépôt ou ouvrez le dossier dans **Android Studio**.
2.  Laissez Gradle synchroniser les dépendances.
3.  Lancez votre serveur SOAP local (sur le port 8082).
4.  Sélectionnez un émulateur Android et cliquez sur **Run** (▶).

## 📂 Structure du projet
*   `beans/` : Classes de données (`Compte`, `TypeCompte`).
*   `ws/` : Gestion du Web Service (`Service.kt` avec ksoap2).
*   `adapter/` : Adaptateur pour le RecyclerView (`CompteAdapter`).
*   `MainActivity.kt` : Logique principale de l'application.
