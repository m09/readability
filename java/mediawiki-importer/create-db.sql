--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: rev; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rev (
    id bigint NOT NULL,
    parent_id bigint,
    comment text,
    minor boolean NOT NULL,
    "timestamp" text NOT NULL,
    text text NOT NULL,
    page_id bigint NOT NULL
);


--
-- Name: rev_current; Type: MATERIALIZED VIEW; Schema: public; Owner: -; Tablespace: 
--

CREATE MATERIALIZED VIEW rev_current AS
 SELECT q.id,
    q.parent_id,
    q.comment,
    q.minor,
    q."timestamp",
    q.text,
    q.page_id
   FROM ( SELECT rev.id,
            rev.parent_id,
            rev.comment,
            rev.minor,
            rev."timestamp",
            rev.text,
            rev.page_id,
            row_number() OVER (PARTITION BY rev.page_id ORDER BY rev."timestamp" DESC) AS rn
           FROM rev) q
  WHERE (q.rn = 1)
  WITH NO DATA;


--
-- Name: PKEY_REV; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rev
    ADD CONSTRAINT "PKEY_REV" PRIMARY KEY (id);


--
-- Name: INDEX_PAGE_ID_ASC_AND_TIMESTAMP_DESC; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX "INDEX_PAGE_ID_ASC_AND_TIMESTAMP_DESC" ON rev USING btree (page_id, "timestamp" DESC);


--
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--
