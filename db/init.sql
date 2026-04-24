DROP TABLE IF EXISTS "CHAT_MESSAGE";
DROP TABLE IF EXISTS "ROADMAP_ITEM";
DROP TABLE IF EXISTS "GRADE";
DROP TABLE IF EXISTS "CAREER_RESULT";
DROP TABLE IF EXISTS "CHAT_SESSION";
DROP TABLE IF EXISTS "ROADMAP";
DROP TABLE IF EXISTS "USERS";

CREATE TYPE chat_role AS ENUM ('USER', 'AI');
CREATE TYPE roadmap_item_status AS ENUM (
    'TODO',
    'IN_PROGRESS',
    'DONE'
);

CREATE TABLE "USERS" (
    "user_id" bigint NOT NULL,
    "email" varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    "birthday" date NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT now(),
    "department" varchar(100) NULL,
    "career" varchar(100) NULL,
    CONSTRAINT "PK_USERS" PRIMARY KEY ("user_id"),
    CONSTRAINT "UK_USERS_EMAIL" UNIQUE ("email")
);


CREATE TABLE "GRADE" (
    "grade_id" bigint NOT NULL,
    "user_id" bigint NOT NULL,
    "score" int NOT NULL,
    "grade" varchar(10) NOT NULL,
    CONSTRAINT "PK_GRADE" PRIMARY KEY ("grade_id"),
    CONSTRAINT "FK_USERS_TO_GRADE"
        FOREIGN KEY ("user_id") REFERENCES "USERS" ("user_id")
);


CREATE TABLE "CAREER_RESULT" (
    "result_id" bigint NOT NULL,
    "user_id" bigint NOT NULL,
    "result_url" text NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT now(),
    CONSTRAINT "PK_CAREER_RESULT" PRIMARY KEY ("result_id"),
    CONSTRAINT "FK_USERS_TO_CAREER_RESULT"
        FOREIGN KEY ("user_id") REFERENCES "USERS" ("user_id")
);

CREATE TABLE "ROADMAP" (
    "roadmap_id" bigint NOT NULL,
    "user_id" bigint NOT NULL,
    "title" varchar(50) NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT now(),
    CONSTRAINT "PK_ROADMAP" PRIMARY KEY ("roadmap_id"),
    CONSTRAINT "FK_USERS_TO_ROADMAP"
        FOREIGN KEY ("user_id") REFERENCES "USERS" ("user_id")
);

CREATE TABLE "ROADMAP_ITEM" (
    "roadmap_item_id" bigint NOT NULL,
    "roadmap_id" bigint NOT NULL,
    "title" varchar(30) NOT NULL,
    "description" text NULL,
    "started_at" date NULL,
    "ended_at" date NULL,
    "status" roadmap_item_status NOT NULL,
    CONSTRAINT "PK_ROADMAP_ITEM" PRIMARY KEY ("roadmap_item_id"),
    CONSTRAINT "FK_ROADMAP_TO_ROADMAP_ITEM"
        FOREIGN KEY ("roadmap_id") REFERENCES "ROADMAP" ("roadmap_id")
);


CREATE TABLE "CHAT_SESSION" (
    "session_id" bigint NOT NULL,
    "user_id" bigint NOT NULL,
    "title" varchar(50) NULL,
    "created_at" timestamp NOT NULL DEFAULT now(),
    "last_message_at" timestamp NULL,
    CONSTRAINT "PK_CHAT_SESSION" PRIMARY KEY ("session_id"),
    CONSTRAINT "FK_USERS_TO_CHAT_SESSION"
        FOREIGN KEY ("user_id") REFERENCES "USERS" ("user_id")
);


CREATE TABLE "CHAT_MESSAGE" (
    "message_id" bigint NOT NULL,
    "session_id" bigint NOT NULL,
    "role" chat_role NOT NULL,
    "content" text NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT now(),
    CONSTRAINT "PK_CHAT_MESSAGE" PRIMARY KEY ("message_id"),
    CONSTRAINT "FK_CHAT_SESSION_TO_CHAT_MESSAGE"
        FOREIGN KEY ("session_id") REFERENCES "CHAT_SESSION" ("session_id")
);