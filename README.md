# SFR3 MVP — Payment Processing (Spring Boot)

Minimal, demo-ready MVP that processes rent payments using a **dummy payment provider**.
- Java **25**, Spring Boot **3.5.6**
- Gradle Kotlin DSL
- H2 in-memory DB
- Static dark-themed UI
- Package: **com.sfr3.mvp**

## Run (CLI)

```bash
./gradlew bootRun
```

Then open: **http://localhost:8080**

## Run (IntelliJ IDEA)

1. File → Open → select the project folder.
2. Let IntelliJ import Gradle settings.
3. Run `com.sfr3.mvp.MvpApplication`.

## Endpoints

- `GET /api/leases` — list all leases and statuses
- `POST /api/runScheduler` — simulate scheduled rent processing (autopay)
- `POST /api/manualPay?leaseId={id}` — trigger one-time payment

## Demo Flow

Data is **preloaded** on first run (3 sample leases):
- **Alice** (CARD) — success
- **Bob** (CARD) — fail
- **Charlie** (ACH) — pending → success (after ~5s)

In the UI:
1. Click **Run Scheduled Payments** → Alice succeeds, Bob fails, Charlie shows **Pending** then **Success**.
2. Click **Pay Now** for a failed lease to retry manually.

## Notes

- No retries, partial payments, or late fees — intentionally omitted for speed.
- No real PSP — all outcomes simulated via `DummyPaymentProvider`.
- No Docker, no Postgres, no tests — all simplified for a quick, clean demo.
