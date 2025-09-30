# ChatFlow

Live Messaging App utilizimg websocket and AI Agent Chat with Grok API

## Process to Run Code
1. clone the repo
2. download postgres (with pg4admin)
3. In pg4admin, create a server called chatflow, and inside it:
4. CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled  BOOLEAN NOT NULL,
    avatar   VARCHAR(255)         
  );
5. CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    authority VARCHAR(50) NOT NULL,
    PRIMARY KEY (username, authority)
   );
6. CREATE TABLE friend_requests (
    id BIGSERIAL PRIMARY KEY,
    requester_username VARCHAR NOT NULL,
    addressee_username VARCHAR NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('PENDING','ACCEPTED','DECLINED','CANCELED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    responded_at TIMESTAMPTZ,
    CONSTRAINT uq_friend_request UNIQUE (requester_username, addressee_username),
    CONSTRAINT chk_no_self_request CHECK (requester_username <> addressee_username),
    CONSTRAINT fk_req_requester FOREIGN KEY (requester_username) REFERENCES users(username) ON DELETE CASCADE,
    CONSTRAINT fk_req_addressee FOREIGN KEY (addressee_username) REFERENCES users(username) ON DELETE CASCADE
   );
7. CREATE TABLE friendships (
    id BIGSERIAL PRIMARY KEY,
    user_username   VARCHAR NOT NULL,
    friend_username VARCHAR NOT NULL,
    since TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_friendship UNIQUE (user_username, friend_username),
    CONSTRAINT chk_no_self_friend CHECK (user_username <> friend_username),
    CONSTRAINT fk_friend_user FOREIGN KEY (user_username) REFERENCES users(username) ON DELETE CASCADE,
    CONSTRAINT fk_friend_friend FOREIGN KEY (friend_username) REFERENCES users(username) ON DELETE CASCADE
   );
8. GRANT USAGE ON SCHEMA public TO chatflow_user;
   GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO chatflow_user;
   ALTER DEFAULT PRIVILEGES IN SCHEMA public
   GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO chatflow_user;
9. Then in main directory run: mvnw.cmd spring-boot:run
