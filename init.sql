-- Use the specified database
\c turtle_chat;

-- users table
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) UNIQUE NOT NULL
);

-- chatroom table
CREATE TABLE IF NOT EXISTS chatroom (
    room_id SERIAL PRIMARY KEY,
    room_name VARCHAR(255) NOT NULL,
    host_id BIGINT NOT NULL,
    creation_timestamp BIGINT NOT NULL,
    deletion_timestamp BIGINT,
    room_status CHAR(1) DEFAULT 'A',
    participant_count BIGINT GENERATED ALWAYS AS (0) STORED,
    CONSTRAINT fk_host_user
        FOREIGN KEY(host_id)
        REFERENCES users(user_id)
);

-- invitation table
CREATE TABLE IF NOT EXISTS invitation (
    invite_id SERIAL PRIMARY KEY,
    sender_user_id BIGINT NOT NULL,
    recipient_user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    invite_status CHAR(1) DEFAULT 'P',
    invite_timestamp BIGINT NOT NULL,
    CONSTRAINT fk_invitation_sender_user
        FOREIGN KEY(sender_user_id)
        REFERENCES users(user_id),
    CONSTRAINT fk_invitation_recipient_user
        FOREIGN KEY(recipient_user_id)
        REFERENCES users(user_id),
    CONSTRAINT fk_invitation_room
        FOREIGN KEY(room_id)
        REFERENCES chatroom(room_id)
);

-- message table
CREATE TABLE IF NOT EXISTS message (
    message_id SERIAL PRIMARY KEY,
    text VARCHAR(5000) NOT NULL,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    timestamp BIGINT NOT NULL,
    CONSTRAINT fk_message_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id),
    CONSTRAINT fk_message_room
        FOREIGN KEY(room_id)
        REFERENCES chatroom(room_id)
);

-- user_chatroom table
CREATE TABLE IF NOT EXISTS user_chatroom (
    map_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    CONSTRAINT fk_user_chatroom_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id),
    CONSTRAINT fk_user_chatroom_room
        FOREIGN KEY(room_id)
        REFERENCES chatroom(room_id)
);

-- Indexes
CREATE INDEX idx_chatroom_host_id ON chatroom(host_id);
CREATE INDEX idx_invitation_sender_user_id ON invitation(sender_user_id);
CREATE INDEX idx_invitation_recipient_user_id ON invitation(recipient_user_id);
CREATE INDEX idx_message_user_id ON message(user_id);
CREATE INDEX idx_message_room_id ON message(room_id);
CREATE INDEX idx_user_chatroom_user_id ON user_chatroom(user_id);
CREATE INDEX idx_user_chatroom_room_id ON user_chatroom(room_id);
