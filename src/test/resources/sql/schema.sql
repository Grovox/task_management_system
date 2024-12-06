DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255)
);

CREATE TABLE refresh_tokens(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    token VARCHAR(65535) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE tasks(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    priority VARCHAR(255) NOT NULL,
    description VARCHAR(65535),
    user_creator_id UUID NOT NULL,
    user_performer_id UUID,
    FOREIGN KEY (user_creator_id) REFERENCES users(id),
    FOREIGN KEY (user_performer_id) REFERENCES users(id)
);

CREATE TABLE comments(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    description VARCHAR(255),
    task_id UUID,
    author_id UUID,
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);
