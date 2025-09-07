CREATE TABLE selection (
    source VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    racing_number INT,
    PRIMARY KEY (source, external_id)
);

CREATE TABLE events (
    source VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    event_year INT NOT NULL,
    country VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    winner_external_id VARCHAR(255),
    PRIMARY KEY (source, external_id),
    CONSTRAINT fk_winner FOREIGN KEY (source, winner_external_id)
        REFERENCES selection(source, external_id)
);

CREATE TABLE event_selection_link (
    event_source VARCHAR(255) NOT NULL,
    event_external_id VARCHAR(255) NOT NULL,
    selection_source VARCHAR(255) NOT NULL,
    selection_external_id VARCHAR(255) NOT NULL,
    odds INT NOT NULL,
    PRIMARY KEY (event_source, event_external_id, selection_source, selection_external_id),
    CONSTRAINT fk_event FOREIGN KEY (event_source, event_external_id)
        REFERENCES events(source, external_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_selection FOREIGN KEY (selection_source, selection_external_id)
        REFERENCES selection(source, external_id)
        ON DELETE CASCADE
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    eur_balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE bets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_source VARCHAR(255) NOT NULL,
    event_external_id VARCHAR(255) NOT NULL,
    selection_source VARCHAR(255) NOT NULL,
    selection_external_id VARCHAR(255) NOT NULL,
    bet_amount DECIMAL(15, 2) NOT NULL,

    CONSTRAINT fk_bet_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_bet_event FOREIGN KEY (event_source, event_external_id) REFERENCES events(source, external_id) ON DELETE CASCADE,
    CONSTRAINT fk_bet_selection FOREIGN KEY (selection_source, selection_external_id) REFERENCES selection(source, external_id) ON DELETE CASCADE
);
