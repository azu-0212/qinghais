# Qinghais

A GIS web application with a Vue frontend (`supermap_ui`) and a Spring Boot backend (`supermap_sys`). The project builds on RuoYi and SuperMap components.

## Setup

- Install frontend dependencies in `supermap_ui` with `npm install`; run `npm run dev` for development.
- Build the backend in `supermap_sys` with Maven and Java 8.
- Set `DB_URL`, `DB_USER`, `DB_PASSWORD`, and `TOKEN_SECRET` before running the backend.
- Configure `MONGODB_URI` (or `MONGODB_HOST`) for the MongoDB service.
- Set `VUE_APP_CESIUM_TOKEN` when a Cesium token is required.

The application depends on external map and data services that must be configured for each deployment. Do not commit production credentials.
