from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    environment: str = "development"
    host: str = "127.0.0.1"
    port: int = 8080
    service_token: str | None = None
    model_name: str = "gemini"

    model_config = SettingsConfigDict(
        env_prefix="FOCUSFORGE_",
        env_file=".env",
        extra="ignore",
    )

settings = Settings()
