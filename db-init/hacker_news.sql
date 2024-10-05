-- Tabla para las entradas de Hacker News
CREATE TABLE IF NOT EXISTS hacker_news_entry (
    id SERIAL PRIMARY KEY,
    number VARCHAR(10) NOT NULL,
    title TEXT NOT NULL,
    points INT NOT NULL,
    comments INT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla para el historial de solicitudes de filtrado
CREATE TABLE IF NOT EXISTS filter_request_history (
    id SERIAL PRIMARY KEY,
    filter_type VARCHAR(255) NOT NULL,
    request_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    entries_returned TEXT
);