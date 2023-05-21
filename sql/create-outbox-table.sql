CREATE TABLE outbox (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    topic TEXT NOT NULL,
    partition INT,
    key TEXT NOT NULL,
    message TEXT NOT NULL
);

CREATE TABLE outbox_meta (
    last_id BIGINT NOT NULL
);
