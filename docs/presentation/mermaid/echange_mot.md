sequenceDiagram
    participant S as Serveur
    participant C as Alice
    participant Cs as Clients
    C->>S: motClavier { id: "1t3bfq9", mot: "salut" }
    S->>C: motVerifie { id: "1t3bfq9", accepte: true, points: 2 }
    S->>Cs: motTrouve { nom: "Alice" }
