# GMForge

Aplikacja do zarządzania sesjami i grupami w tematyce tabletopowych gier RPG, osadzona w świecie gry Dishonored.

## Technologie

- **Frontend:** Angular 21, standalone components, plain CSS
- **Backend:** Spring Boot 3, JWT, Spring Security, Spring Data JPA
- **Baza danych:** MySQL 8.0
- **Konteneryzacja:** Docker
- **Orkiestracja:** Kubernetes (Minikube)

## Struktura projektu

```
GMForge/
├── frontend/         # Aplikacja Angular
│   └── frontend/
│       ├── src/
│       ├── Dockerfile
│       └── nginx.conf
├── backend/          # Aplikacja Spring Boot
│   ├── src/
│   └── Dockerfile
└── k8s/              # Konfiguracja Kubernetes
    ├── secret.yaml
    ├── backend-configmap.yaml
    ├── mysql.yaml
    ├── backend.yaml
    ├── frontend.yaml
    └── ingress.yaml
```

## Uruchomienie lokalne (bez Kubernetes)

### Wymagania
- Java 21
- Node.js 20+
- MySQL 8.0

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend/frontend
npm install
ng serve
```

Aplikacja dostępna pod `http://localhost:4200`, backend pod `http://localhost:8080`.

---

## 🐳 Wdrożenie w klastrze Kubernetes (Minikube)

> Poniższa sekcja została zrealizowana w ramach zadania z przedmiotu
> **Programowanie full-stack w chmurze obliczeniowej**.

### Wymagania

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

### Architektura klastra

```
[ Ingress nginx ]
       |
  ┌────┴────┐
  ▼         ▼
[Frontend]  [Backend]        
(2 repliki) (2 repliki)      
                |
            [MySQL]
          (StatefulSet)
```

Ruch wchodzący przez Ingress jest kierowany:
- `/api/**` → backend (Spring Boot, port 8080)
- `/**` → frontend (nginx, port 80)

### Pliki Kubernetes *(wygenerowane w ramach zadania)*

| Plik | Kind | Opis |
|------|------|------|
| `secret.yaml` | `Secret` | Hasło do bazy danych oraz klucz JWT w formacie base64 |
| `backend-configmap.yaml` | `ConfigMap` | Konfiguracja połączenia z bazą (host, nazwa, port) |
| `mysql.yaml` | `StatefulSet` + `Service` | Baza danych MySQL z trwałym wolumenem (PersistentVolumeClaim 1Gi) oraz headless Service |
| `backend.yaml` | `Deployment` + `Service` | Backend Spring Boot — 2 repliki, liveness/readiness probe na `/actuator/health`, zmienne środowiskowe z Secret i ConfigMap |
| `frontend.yaml` | `Deployment` + `Service` | Frontend Angular serwowany przez nginx — 2 repliki |
| `ingress.yaml` | `Ingress` | Routing HTTP przez nginx ingress controller |

### Dockerfile'y *(wygenerowane w ramach zadania)*

- `backend/Dockerfile` — wieloetapowy build: Maven wrapper buduje JAR, następnie uruchamiany na `eclipse-temurin:21-jre-alpine`
- `frontend/Dockerfile` — wieloetapowy build: `node:24-alpine` buduje aplikację Angular w trybie produkcyjnym, wynik serwowany przez `nginx:alpine`
- `frontend/nginx.conf` — konfiguracja nginx z obsługą Angular routera (`try_files`) oraz proxy dla `/api/`

### Uruchomienie klastra

#### 1. Uruchom Minikube i włącz Ingress

Uruchomić Docker Desktop 

```powershell
minikube start
minikube addons enable ingress
```

#### 2. Skieruj Dockera na rejestr Minikube i zbuduj obrazy

W PowerShell (wymagane przy każdym nowym terminalu):

```powershell
$env:DOCKER_BUILDKIT=0
minikube docker-env | Invoke-Expression

docker build -t gmforge-backend:latest ./backend
docker build -t gmforge-frontend:latest ./frontend/frontend
```

#### 3. Zaaplikuj pliki Kubernetes

```powershell
cd k8s
kubectl apply -f secret.yaml
kubectl apply -f backend-configmap.yaml
kubectl apply -f mysql.yaml
kubectl apply -f backend.yaml
kubectl apply -f frontend.yaml
kubectl apply -f ingress.yaml
kubectl apply -f rbac.yaml
kubectl apply -f storageclass.yaml
kubectl apply -f namespace.yaml
```

#### 4. Sprawdź status podów

```powershell
kubectl get pods
```

Wszystkie pody powinny mieć status `Running` i `1/1 READY`. Backend może potrzebować ~60 sekund na połączenie z MySQL.

#### 5. Uruchom tunel (wymagane na Windowsie)

W **nowym terminalu uruchomionym jako administrator**:

```powershell
minikube tunnel
```

Pozostaw terminal otwarty przez cały czas korzystania z aplikacji.

#### 6. Otwórz aplikację

```
http://127.0.0.1
```

### Weryfikacja automatycznego restartu podów

Jednym z wymagań projektu było zapewnienie automatycznego restartu podów przez Kubernetes. Aby to zweryfikować:

```powershell
# Sprawdź nazwy podów
kubectl get pods

# Zabij wybrany pod
kubectl delete pod <nazwa-poda>

# Obserwuj automatyczne odtworzenie
kubectl get pods
```

Kubernetes automatycznie tworzy nowy pod w miejsce usuniętego, utrzymując zadeklarowaną liczbę replik (2 dla frontendu i backendu).

### Zmienne środowiskowe

Backend odczytuje konfigurację ze zmiennych środowiskowych wstrzykiwanych przez Kubernetes:

| Zmienna | Źródło | Opis |
|---------|--------|------|
| `DB_HOST` | ConfigMap | Host bazy danych (`mysql-service`) |
| `DB_PORT` | ConfigMap | Port bazy danych (`3306`) |
| `DB_NAME` | ConfigMap | Nazwa bazy danych (`gmforge`) |
| `DB_USER` | - | Użytkownik bazy danych |
| `DB_PASSWORD` | Secret | Hasło do bazy danych |
| `JWT_SECRET` | Secret | Klucz do podpisywania tokenów JWT |



Elementy wygenerowane przez sztuczną inteligencje:
- Dokumentacje README
- Wartswta graficzna w postaci plików css na frontendzie
- zawartość "rules" w pierwszym obiekcie w pliku "ingress.yaml"
- metoda "corsConfigurationSource" w SecurityConfig
- Część requestów/responsów na backendzie


użyte LLM:
- https://chatgpt.com/share/69fda617-c458-83eb-a26d-ea035e5eb034
- https://chatgpt.com/share/6a09f4ff-7278-83eb-9399-14ed60e9ba61
- https://claude.ai/share/3bbdb256-6896-47df-b06a-a755d87eb490
- https://claude.ai/share/133ea476-38ee-420a-b605-48352cc138a2
- https://chatgpt.com/share/6a09f5d7-27ac-83eb-9478-193ceca3ea5a
- https://chatgpt.com/share/6a09f601-6740-83eb-b842-ae6998ab5a32
