-- Create users table
CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN DEFAULT TRUE NOT NULL,
                       is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
                       refresh_token VARCHAR(1000),
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP,
                        created_by VARCHAR(255),
                        updated_by VARCHAR(255)
);

-- Create role table
CREATE TABLE IF NOT EXISTS role (
                      id BIGSERIAL PRIMARY KEY,
                      role_name VARCHAR(255) NOT NULL UNIQUE,
                      created_at TIMESTAMP,
                      updated_at TIMESTAMP,
                        created_by VARCHAR(255),
                        updated_by VARCHAR(255)
);

-- Create permission table
CREATE TABLE IF NOT EXISTS permission (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255)
);

-- Create user_role join table
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,
                           PRIMARY KEY (user_id, role_id),
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Create role_permission join table
CREATE TABLE role_permission (
                                 role_id BIGINT NOT NULL,
                                 permission_id BIGINT NOT NULL,
                                 PRIMARY KEY (role_id, permission_id),
                                 FOREIGN KEY (role_id) REFERENCES role(id),
                                 FOREIGN KEY (permission_id) REFERENCES permission(id)
);