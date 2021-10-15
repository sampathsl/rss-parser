DROP TABLE IF EXISTS rss_feed;

CREATE TABLE rss_feed
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    link                VARCHAR(500) NOT NULL,
    title               VARCHAR(500) NOT NULL,
    description         VARCHAR(1000) NOT NULL,
    publication_date    TIMESTAMP DEFAULT NULL,
    updated_date        TIMESTAMP DEFAULT NULL
);