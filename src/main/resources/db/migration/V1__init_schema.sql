-- ============================================================
-- V1__init_schema.sql
-- Esquema inicial: users, tasks
-- Motor: PostgreSQL 13+
-- ============================================================

-- gen_random_uuid() es nativa desde PostgreSQL 13. Si el entorno
-- usa una versión anterior, esta extensión la habilita igualmente.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ------------------------------------------------------------
-- Tabla: users
-- ------------------------------------------------------------
CREATE TABLE users (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
);

COMMENT ON TABLE users IS 'Usuarios registrados en la aplicación';
COMMENT ON COLUMN users.password_hash IS 'Hash de la contraseña (bcrypt/argon2), nunca texto plano';

-- ------------------------------------------------------------
-- Tabla: tasks
-- ------------------------------------------------------------
CREATE TABLE tasks (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID NOT NULL,
    title          VARCHAR(150) NOT NULL,
    description    TEXT,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    creation_date  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date       DATE,
    CONSTRAINT fk_tasks_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT chk_tasks_status
        CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

COMMENT ON TABLE tasks IS 'Tareas asociadas a un usuario';
COMMENT ON COLUMN tasks.status IS 'Estado de la tarea: PENDING, IN_PROGRESS, COMPLETED, CANCELLED';
COMMENT ON COLUMN tasks.due_date IS 'Fecha límite opcional de la tarea (solo fecha, sin hora)';

-- ------------------------------------------------------------
-- Índices recomendados
-- ------------------------------------------------------------

-- Búsqueda de tareas por usuario (listado "mis tareas")
CREATE INDEX idx_tasks_user_id ON tasks (user_id);

-- Búsqueda/filtrado de tareas por estado
CREATE INDEX idx_tasks_status ON tasks (status);

-- Índice compuesto para el caso más frecuente en la API:
-- "tareas de un usuario filtradas por estado"
CREATE INDEX idx_tasks_user_id_status ON tasks (user_id, status);
