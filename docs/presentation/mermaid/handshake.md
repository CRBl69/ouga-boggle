sequenceDiagram
    participant S as Serveur
    participant C as Client
    C->>S: Pseudo
    C->>S: Mot de passe
    S->>C: OK ou NOPE
