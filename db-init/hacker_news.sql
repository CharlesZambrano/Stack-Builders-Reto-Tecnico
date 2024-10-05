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

-- Stored Procedure para títulos con más de 5 palabras, ordenados por número de comentarios
CREATE OR REPLACE FUNCTION get_news_entries_greater_than_five_words()
RETURNS TABLE(id BIGINT, number VARCHAR, title TEXT, points INT, comments INT, "timestamp" TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT h.id, h.number, h.title, h.points, h.comments, h."timestamp"
    FROM hacker_news_entry h
    WHERE array_length(string_to_array(h.title, ' '), 1) > 5
    ORDER BY h.comments DESC;
END;
$$ LANGUAGE plpgsql;

-- Stored Procedure para títulos con 5 palabras o menos, ordenados por puntos
CREATE OR REPLACE FUNCTION get_news_entries_less_than_or_equal_five_words()
RETURNS TABLE(id BIGINT, number VARCHAR, title TEXT, points INT, comments INT, "timestamp" TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT h.id, h.number, h.title, h.points, h.comments, h."timestamp"
    FROM hacker_news_entry h
    WHERE array_length(string_to_array(h.title, ' '), 1) <= 5
    ORDER BY h.points DESC;
END;
$$ LANGUAGE plpgsql;
