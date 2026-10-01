# FocusForge AI Gateway

Single-user AI/document gateway.

The service deliberately has no human account system. If deployed with a service token, the Android installation uses that service authorization; Gemini/provider credentials remain server-side.

## Local

Install test dependencies and run:

    pip install -e '.[test]'
    uvicorn app.main:app --host 127.0.0.1 --port 8080
