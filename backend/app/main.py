from fastapi import FastAPI, Header, HTTPException
from .config import settings

app = FastAPI(
    title="FocusForge AI Gateway",
    version="0.1.0",
    docs_url="/docs" if settings.environment != "production" else None,
    redoc_url=None,
)

def authorize(token: str | None) -> None:
    if settings.environment == "development" and not settings.service_token:
        return
    if not settings.service_token or token != settings.service_token:
        raise HTTPException(status_code=401, detail="Unauthorized")

@app.get("/health")
async def health():
    return {"status": "ok", "service": "focusforge-gateway"}

@app.get("/v1/ai/health")
async def ai_health(x_focusforge_token: str | None = Header(default=None)):
    authorize(x_focusforge_token)
    return {"status": "ready", "provider": settings.model_name}
