# Laboratorio #3 — Inciso 2: Construcción directa de AFD

Universidad del Valle de Guatemala — Teoría de la Computación (CC2019)
Docente: Tomás Gálvez P. — Semestre 2, 2026

Julio Pellecer — Carné 241071
Ronald Catún — Carné 19789

## Metodología

Para cada expresión regular se construyó el árbol sintáctico (apoyándose en el algoritmo del inciso anterior) y se aplicó el método de construcción directa de un AFD: se numeraron las hojas del árbol (posiciones), se aumentó la expresión con el marcador de fin `#`, y se calcularon anulable, firstpos y lastpos de cada nodo, así como followpos(i) para cada posición i. El estado inicial del AFD es firstpos(raíz); a partir de él se generan nuevos estados agrupando followpos de las posiciones que comparten símbolo, hasta que no aparecen conjuntos nuevos. Un estado es de aceptación si contiene la posición del marcador `#`.

**Convenciones para los operadores extendidos** (documentadas también en el código fuente):

- `r+` se expande como `r . r*`
- `r?` se expande como `(r | ε)`
- El símbolo `ε` representa la cadena vacía.

## a) Expresión: `(a*|b*)+`

### Numeración de posiciones (hojas del árbol)

| Posición | 1 | 2 | 3 | 4 | 5 |
|---|---|---|---|---|---|
| Símbolo | a | b | a | b | # (fin) |

### followpos(i)

| Posición i | followpos(i) |
|---|---|
| 1 | { 1, 3, 4, 5 } |
| 2 | { 2, 3, 4, 5 } |
| 3 | { 3, 4, 5 } |
| 4 | { 3, 4, 5 } |
| 5 | { } (vacío) |

### b.2 Listado de estados y posiciones que conforma cada uno

| Estado | Posiciones que lo conforman | Inicial | Aceptación |
|---|---|---|---|
| D0 | { 1, 2, 3, 4, 5 } | Sí | Sí |
| D1 | { 1, 3, 4, 5 } | — | Sí |
| D2 | { 2, 3, 4, 5 } | — | Sí |
| D3 | { 3, 4, 5 } | — | Sí |

### a. Tabla de transiciones del AFD

| Estado | a | b |
|---|---|---|
| D0 | D1 | D2 |
| D1 | D1 | D3 |
| D2 | D3 | D2 |
| D3 | D3 | D3 |

## b) Expresión: `((ε|a)|b*)*`

### Numeración de posiciones (hojas del árbol)

| Posición | 1 | 2 | 3 |
|---|---|---|---|
| Símbolo | a | b | # (fin) |

### followpos(i)

| Posición i | followpos(i) |
|---|---|
| 1 | { 1, 2, 3 } |
| 2 | { 1, 2, 3 } |
| 3 | { } (vacío) |

### b.2 Listado de estados y posiciones que conforma cada uno

| Estado | Posiciones que lo conforman | Inicial | Aceptación |
|---|---|---|---|
| D0 | { 1, 2, 3 } | Sí | Sí |

### a. Tabla de transiciones del AFD

| Estado | a | b |
|---|---|---|
| D0 | D0 | D0 |

## c) Expresión: `(a|b)*abb(a|b)*`

### Numeración de posiciones (hojas del árbol)

| Posición | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|---|
| Símbolo | a | b | a | b | b | a | b | # (fin) |

### followpos(i)

| Posición i | followpos(i) |
|---|---|
| 1 | { 1, 2, 3 } |
| 2 | { 1, 2, 3 } |
| 3 | { 4 } |
| 4 | { 5 } |
| 5 | { 6, 7, 8 } |
| 6 | { 6, 7, 8 } |
| 7 | { 6, 7, 8 } |
| 8 | { } (vacío) |

### b.2 Listado de estados y posiciones que conforma cada uno

| Estado | Posiciones que lo conforman | Inicial | Aceptación |
|---|---|---|---|
| D0 | { 1, 2, 3 } | Sí | — |
| D1 | { 1, 2, 3, 4 } | — | — |
| D2 | { 1, 2, 3, 5 } | — | — |
| D3 | { 1, 2, 3, 6, 7, 8 } | — | Sí |
| D4 | { 1, 2, 3, 4, 6, 7, 8 } | — | Sí |
| D5 | { 1, 2, 3, 5, 6, 7, 8 } | — | Sí |

### a. Tabla de transiciones del AFD

| Estado | a | b |
|---|---|---|
| D0 | D1 | D0 |
| D1 | D1 | D2 |
| D2 | D1 | D3 |
| D3 | D4 | D3 |
| D4 | D4 | D5 |
| D5 | D4 | D3 |

## d) Expresión: `0?(1?)?0*`

### Numeración de posiciones (hojas del árbol)

| Posición | 1 | 2 | 3 | 4 |
|---|---|---|---|---|
| Símbolo | 0 | 1 | 0 | # (fin) |

### followpos(i)

| Posición i | followpos(i) |
|---|---|
| 1 | { 2, 3, 4 } |
| 2 | { 3, 4 } |
| 3 | { 3, 4 } |
| 4 | { } (vacío) |

### b.2 Listado de estados y posiciones que conforma cada uno

| Estado | Posiciones que lo conforman | Inicial | Aceptación |
|---|---|---|---|
| D0 | { 1, 2, 3, 4 } | Sí | Sí |
| D1 | { 2, 3, 4 } | — | Sí |
| D2 | { 3, 4 } | — | Sí |

### a. Tabla de transiciones del AFD

| Estado | 0 | 1 |
|---|---|---|
| D0 | D1 | D2 |
| D1 | D2 | D2 |
| D2 | D2 | — |
