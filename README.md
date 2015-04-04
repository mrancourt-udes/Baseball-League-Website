# README #

1. Source du projet
2. Configuration sur IntelliJ IDEA

#1. Source du projet

- src : Dossier source principal
    - ligueBaseball : Classes du TP3
    - ligueBaseballServlet : Les Servlets
- web : Web root
    - resources
        - css : Feuilles de styles
        - fonts : Fonts
        - js : Fichiers/Librairies javascript
    - WEB-INF : Pages jsp non public (Accessible seulement par les servlets)
        - includes : jsp qui seront inclus dans plusieurs pages

##2. Configuration d'IntelliJ IDEA

- Créer un nouveau projet
    * File->New->Project from Version Controll->Git
    * ![](http://105.imagebam.com/download/nx1x2IZ0fRMYSwD6AThXhw/40177/401765740/1.png)
- Dans le popup
    * Git repository URL : https://VOTRE_USERNAME_BITBUCKET@bitbucket.org/vonziper/ift287_tp4.git
    * Parent Directory : Laisser ça comme tel
    * Directory Name : Ce que vous voulez, ça va être le nom du dossier de votre projet
    * ![](http://105.imagebam.com/download/YfUpwSOfCFZ6SVJZRcZADQ/40177/401765742/2.png)
- Aller dans Run->Edit configurations...
    * ![](http://106.imagebam.com/download/2g_MHnPPsR5c-2aEeXVJcQ/40177/401765745/3.png)
    * Cliquer sur le plus (+), suivi de Tomcat Server->Local
    * ![](http://105.imagebam.com/download/JRA9NLH3YeP3qWo8zzeMiQ/40177/401765746/4.png)
    * Donner un beau nom à votre server
- Aller dans la tab Deployment
    * Cliquer sur le plus (+) -> Artifact...
    * ![](http://108.imagebam.com/download/tbRmCVraFAehjsQ9MtYxrQ/40177/401765749/5.png)
- Retourner dans la tab Server 
    * Metrre On 'Update action' à Update classes and resources
    * Mettre On frame deactivation à Update classes and resources
    * **Cocher la case** «Deploy applications configured in Tomcat instance»
    * ![](http://107.imagebam.com/download/wwjpnYBxoVvrphBRXRsPyg/40177/401765750/6.png)
    * Faites «Ok»
- Runner le server en cliquant sur le play en haut à droite
    * ![](http://108.imagebam.com/download/MmoZwMjcJWI5dXo9ekKp9w/40177/401767955/7.png)
### Version
0.1