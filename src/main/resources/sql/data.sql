INSERT INTO users (email, password, role, firstname, lastname)
VALUES
    ('admin1@example.com', '$2a$10$zujHoO8RwhJl5miQsccht.sCQUCXELmtrJQn0ATL.gVpOWxYQEPde', 'ADMIN', 'Admin', 'User1'),
    ('admin2@example.com', '$2a$10$zujHoO8RwhJl5miQsccht.sCQUCXELmtrJQn0ATL.gVpOWxYQEPde', 'ADMIN', 'Admin', 'User2'),
    ('user1@example.com', '$2a$10$6Khm3diKdnhmKhIfPYXbW.wJ1yX2qPzR6ZkyRsFYaf317w/tJymtu', 'USER', 'User', 'One'),
    ('user2@example.com', '$2a$10$6Khm3diKdnhmKhIfPYXbW.wJ1yX2qPzR6ZkyRsFYaf317w/tJymtu', 'USER', 'User', 'Two'),
    ('user3@example.com', '$2a$10$6Khm3diKdnhmKhIfPYXbW.wJ1yX2qPzR6ZkyRsFYaf317w/tJymtu', 'USER', 'User', 'Three');

INSERT INTO tasks (title, status, priority, description, user_creator_id, user_performer_id)
VALUES
    ('Task 1', 'PENDING', 'HIGH', 'Description of Task 1', (SELECT id FROM users WHERE email = 'admin1@example.com'), (SELECT id FROM users WHERE email = 'user1@example.com')),
    ('Task 2', 'PROGRESS', 'MEDIUM', 'Description of Task 2', (SELECT id FROM users WHERE email = 'admin1@example.com'), (SELECT id FROM users WHERE email = 'user2@example.com')),
    ('Task 3', 'COMPLETED', 'LOW', 'Description of Task 3', (SELECT id FROM users WHERE email = 'admin2@example.com'), (SELECT id FROM users WHERE email = 'user3@example.com')),
    ('Task 4', 'PENDING', 'HIGH', 'Description of Task 4', (SELECT id FROM users WHERE email = 'admin2@example.com'), (SELECT id FROM users WHERE email = 'user2@example.com')),
    ('Task 5', 'PROGRESS', 'MEDIUM', 'Description of Task 5', (SELECT id FROM users WHERE email = 'admin2@example.com'), (SELECT id FROM users WHERE email = 'user3@example.com')),
    ('Task 6', 'PENDING', 'LOW', 'Description of Task 6', (SELECT id FROM users WHERE email = 'admin1@example.com'), (SELECT id FROM users WHERE email = 'user2@example.com')),
    ('Task 7', 'COMPLETED', 'HIGH', 'Description of Task 7', (SELECT id FROM users WHERE email = 'admin2@example.com'), (SELECT id FROM users WHERE email = 'user1@example.com'));

INSERT INTO comments (description, task_id, author_id)
VALUES
    ('Comment 1 for Task 1', (SELECT id FROM tasks WHERE title = 'Task 1'), (SELECT id FROM users WHERE email = 'user1@example.com')),
    ('Comment 2 for Task 1', (SELECT id FROM tasks WHERE title = 'Task 1'), (SELECT id FROM users WHERE email = 'user2@example.com')),
    ('Comment 1 for Task 2', (SELECT id FROM tasks WHERE title = 'Task 2'), (SELECT id FROM users WHERE email = 'user1@example.com')),
    ('Comment 2 for Task 2', (SELECT id FROM tasks WHERE title = 'Task 2'), (SELECT id FROM users WHERE email = 'user2@example.com')),
    ('Comment 1 for Task 3', (SELECT id FROM tasks WHERE title = 'Task 3'), (SELECT id FROM users WHERE email = 'user3@example.com')),
    ('Comment 2 for Task 3', (SELECT id FROM tasks WHERE title = 'Task 3'), (SELECT id FROM users WHERE email = 'admin1@example.com')),
    ('Comment 1 for Task 4', (SELECT id FROM tasks WHERE title = 'Task 4'), (SELECT id FROM users WHERE email = 'user2@example.com')),
    ('Comment 2 for Task 4', (SELECT id FROM tasks WHERE title = 'Task 4'), (SELECT id FROM users WHERE email = 'user1@example.com')),
    ('Comment 1 for Task 5', (SELECT id FROM tasks WHERE title = 'Task 5'), (SELECT id FROM users WHERE email = 'user3@example.com')),
    ('Comment 2 for Task 5', (SELECT id FROM tasks WHERE title = 'Task 5'), (SELECT id FROM users WHERE email = 'admin2@example.com')),
    ('Comment 1 for Task 6', (SELECT id FROM tasks WHERE title = 'Task 6'), (SELECT id FROM users WHERE email = 'user1@example.com')),
    ('Comment 2 for Task 6', (SELECT id FROM tasks WHERE title = 'Task 6'), (SELECT id FROM users WHERE email = 'user2@example.com'));
