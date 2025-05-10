-- Create permission table
CREATE TABLE permission
(
    id         BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    name       VARCHAR(255)
);

-- Create role table
CREATE TABLE role
(
    id         BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    role_name  VARCHAR(255) NOT NULL,
    CONSTRAINT uk_role_name UNIQUE (role_name)
);

-- Create role_permission join table
CREATE TABLE role_permission
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission (id)
);

-- Create users table
CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    created_by    VARCHAR(255),
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    updated_by    VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    username      VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    is_active     BOOLEAN DEFAULT TRUE NOT NULL,
    is_deleted    BOOLEAN DEFAULT FALSE NOT NULL,
    is_blocked    BOOLEAN DEFAULT FALSE NOT NULL,
    refresh_token VARCHAR(1000),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_refresh_token UNIQUE (refresh_token)
);

-- Create user_role join table
CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
);

-- Indexes for better performance
CREATE INDEX idx_role_permission_role ON role_permission (role_id);
CREATE INDEX idx_role_permission_permission ON role_permission (permission_id);
CREATE INDEX idx_user_role_user ON user_role (user_id);
CREATE INDEX idx_user_role_role ON user_role (role_id);