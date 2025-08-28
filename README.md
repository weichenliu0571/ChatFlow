# ChatFlow

Live Messaging App with AI agents

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
6. GRANT USAGE ON SCHEMA public TO chatflow_user;
   GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO chatflow_user;
   ALTER DEFAULT PRIVILEGES IN SCHEMA public
   GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO chatflow_user;
7. Then in main directory run: mvnw.cmd spring-boot:run
