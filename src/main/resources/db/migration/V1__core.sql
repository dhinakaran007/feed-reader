-- V1__core.sql
-- Personal News Aggregator core schema

CREATE TABLE topic (
  id          SERIAL PRIMARY KEY,
  slug        TEXT UNIQUE NOT NULL,          -- 'politics.india'
  name        TEXT NOT NULL,
  parent_id   INT REFERENCES topic(id)
);

CREATE TABLE source (
  id                   BIGSERIAL PRIMARY KEY,
  name                 TEXT NOT NULL,
  homepage_url         TEXT NOT NULL,
  feed_url             TEXT,
  adapter              TEXT NOT NULL,          -- 'RSS','ARXIV','PUBMED','COURTLISTENER','GDELT'
  tier                 TEXT NOT NULL,          -- 'PRIMARY','WIRE','PRESS','ANALYSIS','AGGREGATOR','SOCIAL'
  lean                 TEXT NOT NULL DEFAULT 'NOT_APPLICABLE', -- 'LEFT','CENTER_LEFT','CENTER','CENTER_RIGHT','RIGHT','NOT_APPLICABLE'
  country              CHAR(2),
  ownership_note       TEXT,
  reliability_note     TEXT,
  default_topic_id     INT REFERENCES topic(id),
  fetch_interval_min   INT NOT NULL DEFAULT 30,
  etag                 TEXT,
  last_modified        TEXT,
  last_success_at      TIMESTAMPTZ,
  consecutive_failures INT NOT NULL DEFAULT 0,
  next_fetch_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  health               TEXT NOT NULL DEFAULT 'OK', -- 'OK','DEGRADED','DEAD'
  enabled              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE story_cluster (
  id              BIGSERIAL PRIMARY KEY,
  headline        TEXT NOT NULL,              -- title of the earliest or highest-tier article
  first_seen_at   TIMESTAMPTZ NOT NULL,
  last_updated_at TIMESTAMPTZ NOT NULL,
  article_count   INT NOT NULL DEFAULT 1,
  lean_spread     TEXT[] NOT NULL DEFAULT '{}',
  single_lean     BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE article (
  id             BIGSERIAL PRIMARY KEY,
  source_id      BIGINT NOT NULL REFERENCES source(id),
  external_id    TEXT,                       -- feed guid / arXiv id / PMID
  url            TEXT NOT NULL,
  canonical_url  TEXT NOT NULL,
  url_hash       CHAR(64) NOT NULL UNIQUE,   -- sha256(canonical_url)
  title          TEXT NOT NULL,
  snippet        TEXT,                       -- short excerpt only
  item_type      TEXT NOT NULL DEFAULT 'NEWS', -- 'NEWS','ANALYSIS','OPINION','PRIMARY_DOC','PAPER','PODCAST_OR_VIDEO'
  language       TEXT,
  published_at   TIMESTAMPTZ NOT NULL,
  fetched_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  title_norm     TEXT NOT NULL,              -- lowercased, stripped, for similarity
  simhash        BIGINT,
  cluster_id     BIGINT REFERENCES story_cluster(id),
  status         TEXT NOT NULL DEFAULT 'NEW',-- NEW, NORMALIZED, CLASSIFIED, CLUSTERED
  tsv            TSVECTOR                    -- full-text search
);

CREATE INDEX idx_article_published_at ON article (published_at DESC);
CREATE INDEX idx_article_tsv ON article USING GIN (tsv);

CREATE TABLE article_topic (
  article_id BIGINT REFERENCES article(id) ON DELETE CASCADE,
  topic_id   INT REFERENCES topic(id),
  confidence REAL NOT NULL DEFAULT 1.0,
  method     TEXT NOT NULL,                  -- 'SOURCE','KEYWORD','LLM','MANUAL'
  PRIMARY KEY (article_id, topic_id)
);

CREATE TABLE legal_case (
  id           BIGSERIAL PRIMARY KEY,
  name         TEXT NOT NULL,                 -- 'Case name v. Party'
  court        TEXT NOT NULL,
  jurisdiction TEXT NOT NULL,
  case_number  TEXT,
  status       TEXT NOT NULL,                 -- FILED, HEARING, RESERVED, DECIDED, APPEAL
  summary      TEXT,
  keywords     TEXT[] NOT NULL DEFAULT '{}',  -- used to auto-link articles
  watched      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE case_event (
  id          BIGSERIAL PRIMARY KEY,
  case_id     BIGINT NOT NULL REFERENCES legal_case(id),
  event_date  DATE NOT NULL,
  kind        TEXT NOT NULL,                 -- FILING, HEARING, ORDER, JUDGMENT, APPEAL
  description TEXT NOT NULL,
  source_url  TEXT
);

CREATE TABLE case_article (
  case_id    BIGINT REFERENCES legal_case(id) ON DELETE CASCADE,
  article_id BIGINT REFERENCES article(id) ON DELETE CASCADE,
  PRIMARY KEY (case_id, article_id)
);

CREATE TABLE fetch_log (
  id          BIGSERIAL PRIMARY KEY,
  source_id   BIGINT REFERENCES source(id),
  started_at  TIMESTAMPTZ,
  duration_ms INT,
  http_status INT,
  items_found INT,
  items_new   INT,
  error       TEXT
);

