# SFR3 — Payment Processing

- [Requirements](https://docs.google.com/document/d/1c9pXPJeazTAYL-Fbls8hz_ZIuJ5s_JpdwWfIviwnlFs/edit?tab=t.0#heading=h.ryhuu73w3hcc)
- [Architecture](https://docs.google.com/document/d/1F62QH--MgGb0cpbPvCJ_7AZ2Hc4MTMZESxtvXqypMHk/edit?usp=sharing)
- [MVP](https://docs.google.com/document/d/1JF4H7XtYMeJujXITNCt7GGG-lvGvxACSI-1PR6R3APs/edit?usp=sharing)

Minimal, demo-ready MVP that processes rent payments using a **dummy payment provider**.
- Java **25**, Spring Boot **3.5.6**
- Gradle Kotlin DSL
- H2 in-memory DB
- Static dark-themed UI

## Run (CLI)

```bash
./gradlew bootRun
```

Then open: **http://localhost:8080**

## Run (IntelliJ IDEA)

1. File → Open → select the project folder.
2. Let IntelliJ import Gradle settings.
3. Run `com.sfr3.mvp.PaymentProcessingApp`.

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
