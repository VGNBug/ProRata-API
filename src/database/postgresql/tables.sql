CREATE TABLE account (
    account_id integer NOT NULL,
    prorata_user_id integer NOT NULL,
    account_number character varying(10) NOT NULL,
    sort_code character varying(10),
    bank_id integer NOT NULL
);

--
-- TOC entry 201 (class 1259 OID 30080)
-- Name: account_account_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE account_account_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2249 (class 0 OID 0)
-- Dependencies: 201
-- Name: account_account_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE account_account_id_pk_seq OWNED BY account.account_id;


--
-- TOC entry 180 (class 1259 OID 29807)
-- Name: bank; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE bank (
    bank_id integer NOT NULL,
    name character varying(10),
    address character varying(10),
    postcode character varying(10),
    telphone character varying(10),
    email character varying(10)
);

--
-- TOC entry 202 (class 1259 OID 30082)
-- Name: bank_bank_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE bank_bank_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2250 (class 0 OID 0)
-- Dependencies: 202
-- Name: bank_bank_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE bank_bank_id_pk_seq OWNED BY bank.bank_id;


--
-- TOC entry 191 (class 1259 OID 29862)
-- Name: contract; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE contract (
    contract_id integer NOT NULL,
    employment_id integer
);

--
-- TOC entry 208 (class 1259 OID 30094)
-- Name: contract_contract_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE contract_contract_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2251 (class 0 OID 0)
-- Dependencies: 208
-- Name: contract_contract_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE contract_contract_id_pk_seq OWNED BY contract.contract_id;


--
-- TOC entry 181 (class 1259 OID 29812)
-- Name: employer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE employer (
    employer_id integer NOT NULL,
    name character varying(100) NOT NULL,
    office_address character varying(100),
    office_postcode character varying(100)
);

--
-- TOC entry 182 (class 1259 OID 29817)
-- Name: employer_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE employer_contact (
    employer_contact_id integer NOT NULL,
    employer_id integer NOT NULL,
    contact_name character varying(10),
    contact_type character varying(10),
    contact_body character varying(10)
);

--
-- TOC entry 200 (class 1259 OID 30078)
-- Name: employer_contact_employer_contact_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE employer_contact_employer_contact_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2252 (class 0 OID 0)
-- Dependencies: 200
-- Name: employer_contact_employer_contact_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE employer_contact_employer_contact_id_pk_seq OWNED BY employer_contact.employer_contact_id;


--
-- TOC entry 198 (class 1259 OID 30074)
-- Name: employer_employer_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE employer_employer_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2253 (class 0 OID 0)
-- Dependencies: 198
-- Name: employer_employer_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE employer_employer_id_pk_seq OWNED BY employer.employer_id;


--
-- TOC entry 187 (class 1259 OID 29842)
-- Name: employment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE employment (
    employment_id integer NOT NULL,
    employer_id integer NOT NULL,
    prorata_user_id integer NOT NULL,
    hourly_rate numeric(10,0) NOT NULL,
    start_date date NOT NULL,
    end_date date,
    hours_per_week numeric(10,0),
    name character varying(100) NOT NULL
);

--
-- TOC entry 210 (class 1259 OID 30098)
-- Name: employment_employment_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE employment_employment_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2254 (class 0 OID 0)
-- Dependencies: 210
-- Name: employment_employment_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE employment_employment_id_pk_seq OWNED BY employment.employment_id;


--
-- TOC entry 188 (class 1259 OID 29847)
-- Name: employment_session; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE employment_session (
    employment_session_id integer NOT NULL,
    start_time timestamp with time zone,
    end_time timestamp with time zone,
    employment_id integer NOT NULL,
    location_id integer NOT NULL
);

--
-- TOC entry 205 (class 1259 OID 30088)
-- Name: employment_session_employment_session_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE employment_session_employment_session_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2255 (class 0 OID 0)
-- Dependencies: 205
-- Name: employment_session_employment_session_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE employment_session_employment_session_id_pk_seq OWNED BY employment_session.employment_session_id;


--
-- TOC entry 211 (class 1259 OID 30126)
-- Name: location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE location (
    location_id integer NOT NULL,
    x_coordinate numeric NOT NULL,
    y_coordinate numeric NOT NULL
);

--
-- TOC entry 212 (class 1259 OID 30132)
-- Name: location_location_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE location_location_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2256 (class 0 OID 0)
-- Dependencies: 212
-- Name: location_location_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE location_location_id_pk_seq OWNED BY location.location_id;


--
-- TOC entry 192 (class 1259 OID 29867)
-- Name: pay_cheque; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pay_cheque (
    pay_cheque_id integer NOT NULL,
    payment_id integer
);

--
-- TOC entry 196 (class 1259 OID 30070)
-- Name: pay_cheque_pay_cheque_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE pay_cheque_pay_cheque_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2257 (class 0 OID 0)
-- Dependencies: 196
-- Name: pay_cheque_pay_cheque_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE pay_cheque_pay_cheque_id_pk_seq OWNED BY pay_cheque.pay_cheque_id;


--
-- TOC entry 189 (class 1259 OID 29852)
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE payment (
    payment_id integer NOT NULL,
    amount numeric(10,0) NOT NULL,
    payment_date date,
    employment_id integer NOT NULL
);

--
-- TOC entry 197 (class 1259 OID 30072)
-- Name: payment_payment_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE payment_payment_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2258 (class 0 OID 0)
-- Dependencies: 197
-- Name: payment_payment_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE payment_payment_id_pk_seq OWNED BY payment.payment_id;


--
-- TOC entry 183 (class 1259 OID 29822)
-- Name: prorata_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE prorata_user (
    prorata_user_id integer NOT NULL,
    password character varying(20) NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    email character varying(100),
    address character varying(100),
    postcode character varying(100)
);

--
-- TOC entry 204 (class 1259 OID 30086)
-- Name: prorata_user_prorata_user_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE prorata_user_prorata_user_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2259 (class 0 OID 0)
-- Dependencies: 204
-- Name: prorata_user_prorata_user_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE prorata_user_prorata_user_id_pk_seq OWNED BY prorata_user.prorata_user_id;


--
-- TOC entry 193 (class 1259 OID 29995)
-- Name: prorata_user_user_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE prorata_user_user_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 195 (class 1259 OID 30043)
-- Name: subscription; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE subscription (
    subscription_id integer NOT NULL,
    prorata_user_id integer NOT NULL,
    subscription_type_id integer NOT NULL,
    start_date_time timestamp with time zone NOT NULL,
    end_date_time timestamp with time zone
);

--
-- TOC entry 209 (class 1259 OID 30096)
-- Name: subscription_subscription_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE subscription_subscription_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2260 (class 0 OID 0)
-- Dependencies: 209
-- Name: subscription_subscription_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE subscription_subscription_id_pk_seq OWNED BY subscription.subscription_id;


--
-- TOC entry 194 (class 1259 OID 30020)
-- Name: subscription_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE subscription_type (
    subscription_type_id integer NOT NULL,
    name character varying(30) NOT NULL,
    rate numeric NOT NULL
);

--
-- TOC entry 206 (class 1259 OID 30090)
-- Name: subscription_type_subscription_type_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE subscription_type_subscription_type_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2261 (class 0 OID 0)
-- Dependencies: 206
-- Name: subscription_type_subscription_type_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE subscription_type_subscription_type_id_pk_seq OWNED BY subscription_type.subscription_type_id;


--
-- TOC entry 184 (class 1259 OID 29827)
-- Name: tax_bracket; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tax_bracket (
    tax_bracket_id integer NOT NULL,
    year date,
    personal_allowance numeric(10,0),
    percent_deduction numeric(10,0),
    tax_code character varying(10)
);

--
-- TOC entry 199 (class 1259 OID 30076)
-- Name: tax_bracket_tax_bracket_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tax_bracket_tax_bracket_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2262 (class 0 OID 0)
-- Dependencies: 199
-- Name: tax_bracket_tax_bracket_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tax_bracket_tax_bracket_id_pk_seq OWNED BY tax_bracket.tax_bracket_id;


--
-- TOC entry 190 (class 1259 OID 29857)
-- Name: tax_deduction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tax_deduction (
    tax_deduction_id integer NOT NULL,
    tax_bracket_id integer NOT NULL,
    amount numeric(10,0),
    payment_id integer
);

--
-- TOC entry 203 (class 1259 OID 30084)
-- Name: tax_deduction_tax_deduction_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tax_deduction_tax_deduction_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2263 (class 0 OID 0)
-- Dependencies: 203
-- Name: tax_deduction_tax_deduction_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tax_deduction_tax_deduction_id_pk_seq OWNED BY tax_deduction.tax_deduction_id;


--
-- TOC entry 185 (class 1259 OID 29832)
-- Name: user_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_contact (
    user_contact_id integer NOT NULL,
    contact_name character varying(10),
    contact_type character varying(10),
    contact_body character varying(10),
    prorata_user_id integer NOT NULL
);

--
-- TOC entry 207 (class 1259 OID 30092)
-- Name: user_contact_user_contact_id_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE user_contact_user_contact_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2264 (class 0 OID 0)
-- Dependencies: 207
-- Name: user_contact_user_contact_id_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_contact_user_contact_id_pk_seq OWNED BY user_contact.user_contact_id;


--
-- TOC entry 2109 (class 2606 OID 30135)
-- Name: location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_pkey PRIMARY KEY (location_id);


--
-- TOC entry 2091 (class 2606 OID 29841)
-- Name: pk_account; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY account
    ADD CONSTRAINT pk_account PRIMARY KEY (account_id);


--
-- TOC entry 2077 (class 2606 OID 29811)
-- Name: pk_bank; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bank
    ADD CONSTRAINT pk_bank PRIMARY KEY (bank_id);


--
-- TOC entry 2101 (class 2606 OID 29866)
-- Name: pk_contract; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contract
    ADD CONSTRAINT pk_contract PRIMARY KEY (contract_id);


--
-- TOC entry 2079 (class 2606 OID 29816)
-- Name: pk_employer; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employer
    ADD CONSTRAINT pk_employer PRIMARY KEY (employer_id);


--
-- TOC entry 2081 (class 2606 OID 29821)
-- Name: pk_employer_contact; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employer_contact
    ADD CONSTRAINT pk_employer_contact PRIMARY KEY (employer_contact_id);


--
-- TOC entry 2093 (class 2606 OID 29846)
-- Name: pk_employment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employment
    ADD CONSTRAINT pk_employment PRIMARY KEY (employment_id);


--
-- TOC entry 2095 (class 2606 OID 29851)
-- Name: pk_employment_session; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employment_session
    ADD CONSTRAINT pk_employment_session PRIMARY KEY (employment_session_id);


--
-- TOC entry 2103 (class 2606 OID 29871)
-- Name: pk_pay_cheque; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pay_cheque
    ADD CONSTRAINT pk_pay_cheque PRIMARY KEY (pay_cheque_id);


--
-- TOC entry 2097 (class 2606 OID 29856)
-- Name: pk_payment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT pk_payment PRIMARY KEY (payment_id);


--
-- TOC entry 2083 (class 2606 OID 29826)
-- Name: pk_prorata_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prorata_user
    ADD CONSTRAINT pk_prorata_user PRIMARY KEY (prorata_user_id);


--
-- TOC entry 2087 (class 2606 OID 29831)
-- Name: pk_tax_bracket; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tax_bracket
    ADD CONSTRAINT pk_tax_bracket PRIMARY KEY (tax_bracket_id);


--
-- TOC entry 2099 (class 2606 OID 29861)
-- Name: pk_tax_deduction; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tax_deduction
    ADD CONSTRAINT pk_tax_deduction PRIMARY KEY (tax_deduction_id);


--
-- TOC entry 2089 (class 2606 OID 29836)
-- Name: pk_user_contact; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT pk_user_contact PRIMARY KEY (user_contact_id);


--
-- TOC entry 2107 (class 2606 OID 30047)
-- Name: subscription_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (subscription_id);


--
-- TOC entry 2105 (class 2606 OID 30027)
-- Name: subscription_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription_type
    ADD CONSTRAINT subscription_type_pkey PRIMARY KEY (subscription_type_id);


--
-- TOC entry 2085 (class 2606 OID 30064)
-- Name: unique_prorata_user_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prorata_user
    ADD CONSTRAINT unique_prorata_user_email UNIQUE (email);


--
-- TOC entry 2112 (class 2606 OID 29985)
-- Name: fk_2gcgtrmnvwgv2sphapl5jmo3w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT fk_2gcgtrmnvwgv2sphapl5jmo3w FOREIGN KEY (prorata_user_id) REFERENCES user_contact(user_contact_id);


--
-- TOC entry 2114 (class 2606 OID 29882)
-- Name: fk_account_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY account
    ADD CONSTRAINT fk_account_0 FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2115 (class 2606 OID 29887)
-- Name: fk_account_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY account
    ADD CONSTRAINT fk_account_1 FOREIGN KEY (bank_id) REFERENCES bank(bank_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2123 (class 2606 OID 29922)
-- Name: fk_contract_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contract
    ADD CONSTRAINT fk_contract_0 FOREIGN KEY (employment_id) REFERENCES employment(employment_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2110 (class 2606 OID 29872)
-- Name: fk_employer_contact_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employer_contact
    ADD CONSTRAINT fk_employer_contact_0 FOREIGN KEY (employer_id) REFERENCES employer(employer_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2116 (class 2606 OID 29892)
-- Name: fk_employment_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employment
    ADD CONSTRAINT fk_employment_0 FOREIGN KEY (employer_id) REFERENCES employer(employer_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2117 (class 2606 OID 29897)
-- Name: fk_employment_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employment
    ADD CONSTRAINT fk_employment_1 FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2118 (class 2606 OID 29902)
-- Name: fk_employment_session_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employment_session
    ADD CONSTRAINT fk_employment_session_0 FOREIGN KEY (employment_id) REFERENCES employment(employment_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2119 (class 2606 OID 30141)
-- Name: fk_employment_session_location; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY employment_session
    ADD CONSTRAINT fk_employment_session_location FOREIGN KEY (location_id) REFERENCES location(location_id);


--
-- TOC entry 2113 (class 2606 OID 29990)
-- Name: fk_h6syfj9fse40b9ii02tcxj3ya; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT fk_h6syfj9fse40b9ii02tcxj3ya FOREIGN KEY (user_contact_id) REFERENCES prorata_user(prorata_user_id);


--
-- TOC entry 2124 (class 2606 OID 29927)
-- Name: fk_pay_cheque_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pay_cheque
    ADD CONSTRAINT fk_pay_cheque_0 FOREIGN KEY (payment_id) REFERENCES payment(payment_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2120 (class 2606 OID 29907)
-- Name: fk_payment_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT fk_payment_0 FOREIGN KEY (employment_id) REFERENCES employment(employment_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2125 (class 2606 OID 30048)
-- Name: fk_subscription_prorata_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription
    ADD CONSTRAINT fk_subscription_prorata_user FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2126 (class 2606 OID 30053)
-- Name: fk_subscription_subscription_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription
    ADD CONSTRAINT fk_subscription_subscription_type FOREIGN KEY (subscription_type_id) REFERENCES subscription_type(subscription_type_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2121 (class 2606 OID 29912)
-- Name: fk_tax_deduction_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tax_deduction
    ADD CONSTRAINT fk_tax_deduction_0 FOREIGN KEY (tax_bracket_id) REFERENCES tax_bracket(tax_bracket_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2122 (class 2606 OID 29917)
-- Name: fk_tax_deduction_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tax_deduction
    ADD CONSTRAINT fk_tax_deduction_1 FOREIGN KEY (payment_id) REFERENCES payment(payment_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2111 (class 2606 OID 29877)
-- Name: fk_user_contact_0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT fk_user_contact_0 FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;