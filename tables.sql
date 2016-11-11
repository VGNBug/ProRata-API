CREATE TABLE account (
    account_id integer NOT NULL,
    prorata_user_id integer NOT NULL,
    account_number character varying(10) NOT NULL,
    sort_code character varying(10),
    bank_id integer NOT NULL
);

CREATE SEQUENCE account_account_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE account_account_id_pk_seq OWNED BY account.account_id;

CREATE TABLE bank (
    bank_id integer NOT NULL,
    name character varying(10),
    address character varying(10),
    postcode character varying(10),
    telphone character varying(10),
    email character varying(10)
);

CREATE SEQUENCE bank_bank_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE bank_bank_id_pk_seq OWNED BY bank.bank_id;

CREATE TABLE contract (
    contract_id integer NOT NULL,
    employment_id integer
);

CREATE SEQUENCE contract_contract_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE contract_contract_id_pk_seq OWNED BY contract.contract_id;

CREATE TABLE employer (
    employer_id integer NOT NULL,
    name character varying(100) NOT NULL,
    office_address character varying(100),
    office_postcode character varying(100)
);

CREATE TABLE employer_contact (
    employer_contact_id integer NOT NULL,
    employer_id integer NOT NULL,
    contact_name character varying(10),
    contact_type character varying(10),
    contact_body character varying(10)
);

CREATE SEQUENCE employer_contact_employer_contact_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE employer_contact_employer_contact_id_pk_seq OWNED BY employer_contact.employer_contact_id;

CREATE SEQUENCE employer_employer_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE employer_employer_id_pk_seq OWNED BY employer.employer_id;

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

CREATE SEQUENCE employment_employment_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE employment_employment_id_pk_seq OWNED BY employment.employment_id;

CREATE TABLE employment_session (
    employment_session_id integer NOT NULL,
    start_time timestamp with time zone,
    end_time timestamp with time zone,
    employment_id integer NOT NULL,
    location_id integer NOT NULL
);

CREATE SEQUENCE employment_session_employment_session_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE employment_session_employment_session_id_pk_seq OWNED BY employment_session.employment_session_id;

CREATE TABLE location (
    location_id integer NOT NULL,
    prorata_user_id integer NOT NULL,
    x_coordinate numeric NOT NULL,
    y_coordinate numeric NOT NULL
);

CREATE SEQUENCE location_location_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE location_location_id_pk_seq OWNED BY location.location_id;

CREATE TABLE pay_cheque (
    pay_cheque_id integer NOT NULL,
    payment_id integer
);

CREATE SEQUENCE pay_cheque_pay_cheque_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE pay_cheque_pay_cheque_id_pk_seq OWNED BY pay_cheque.pay_cheque_id;

CREATE TABLE payment (
    payment_id integer NOT NULL,
    amount numeric(10,0) NOT NULL,
    payment_date date,
    employment_id integer NOT NULL
);

CREATE SEQUENCE payment_payment_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE payment_payment_id_pk_seq OWNED BY payment.payment_id;

CREATE TABLE prorata_user (
    prorata_user_id integer NOT NULL,
    password character varying(20) NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    email character varying(100),
    address character varying(100),
    postcode character varying(100)
);

CREATE SEQUENCE prorata_user_prorata_user_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE prorata_user_prorata_user_id_pk_seq OWNED BY prorata_user.prorata_user_id;

CREATE SEQUENCE prorata_user_user_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE subscription (
    subscription_id integer NOT NULL,
    prorata_user_id integer NOT NULL,
    subscription_type_id integer NOT NULL,
    start_date_time timestamp with time zone NOT NULL,
    end_date_time timestamp with time zone
);

CREATE SEQUENCE subscription_subscription_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE subscription_subscription_id_pk_seq OWNED BY subscription.subscription_id;

CREATE TABLE subscription_type (
    subscription_type_id integer NOT NULL,
    name character varying(30) NOT NULL,
    rate numeric NOT NULL
);

CREATE SEQUENCE subscription_type_subscription_type_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE subscription_type_subscription_type_id_pk_seq OWNED BY subscription_type.subscription_type_id;

CREATE TABLE tax_bracket (
    tax_bracket_id integer NOT NULL,
    year date,
    personal_allowance numeric(10,0),
    percent_deduction numeric(10,0),
    tax_code character varying(10)
);

CREATE SEQUENCE tax_bracket_tax_bracket_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tax_bracket_tax_bracket_id_pk_seq OWNED BY tax_bracket.tax_bracket_id;

CREATE TABLE tax_deduction (
    tax_deduction_id integer NOT NULL,
    tax_bracket_id integer NOT NULL,
    amount numeric(10,0),
    payment_id integer
);

CREATE SEQUENCE tax_deduction_tax_deduction_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tax_deduction_tax_deduction_id_pk_seq OWNED BY tax_deduction.tax_deduction_id;

CREATE TABLE user_contact (
    user_contact_id integer NOT NULL,
    contact_name character varying(10),
    contact_type character varying(10),
    contact_body character varying(10),
    prorata_user_id integer NOT NULL
);

CREATE SEQUENCE user_contact_user_contact_id_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE user_contact_user_contact_id_pk_seq OWNED BY user_contact.user_contact_id;

ALTER TABLE ONLY location
    ADD CONSTRAINT location_pkey PRIMARY KEY (location_id);

ALTER TABLE ONLY account
    ADD CONSTRAINT pk_account PRIMARY KEY (account_id);

ALTER TABLE ONLY bank
    ADD CONSTRAINT pk_bank PRIMARY KEY (bank_id);

ALTER TABLE ONLY contract
    ADD CONSTRAINT pk_contract PRIMARY KEY (contract_id);

ALTER TABLE ONLY employer
    ADD CONSTRAINT pk_employer PRIMARY KEY (employer_id);

ALTER TABLE ONLY employer_contact
    ADD CONSTRAINT pk_employer_contact PRIMARY KEY (employer_contact_id);

ALTER TABLE ONLY employment
    ADD CONSTRAINT pk_employment PRIMARY KEY (employment_id);

ALTER TABLE ONLY employment_session
    ADD CONSTRAINT pk_employment_session PRIMARY KEY (employment_session_id);

ALTER TABLE ONLY pay_cheque
    ADD CONSTRAINT pk_pay_cheque PRIMARY KEY (pay_cheque_id);

ALTER TABLE ONLY payment
    ADD CONSTRAINT pk_payment PRIMARY KEY (payment_id);

ALTER TABLE ONLY prorata_user
    ADD CONSTRAINT pk_prorata_user PRIMARY KEY (prorata_user_id);

ALTER TABLE ONLY tax_bracket
    ADD CONSTRAINT pk_tax_bracket PRIMARY KEY (tax_bracket_id);

ALTER TABLE ONLY tax_deduction
    ADD CONSTRAINT pk_tax_deduction PRIMARY KEY (tax_deduction_id);

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT pk_user_contact PRIMARY KEY (user_contact_id);

ALTER TABLE ONLY subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (subscription_id);

ALTER TABLE ONLY subscription_type
    ADD CONSTRAINT subscription_type_pkey PRIMARY KEY (subscription_type_id);

ALTER TABLE ONLY prorata_user
    ADD CONSTRAINT unique_prorata_user_email UNIQUE (email);

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT fk_2gcgtrmnvwgv2sphapl5jmo3w FOREIGN KEY (prorata_user_id) REFERENCES user_contact(user_contact_id);

ALTER TABLE ONLY account
    ADD CONSTRAINT fk_account_0 FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY account
    ADD CONSTRAINT fk_account_1 FOREIGN KEY (bank_id) REFERENCES bank(bank_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY contract
    ADD CONSTRAINT fk_contract_0 FOREIGN KEY (employment_id) REFERENCES employment(employment_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY employer_contact
    ADD CONSTRAINT fk_employer_contact_0 FOREIGN KEY (employer_id) REFERENCES employer(employer_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY employment
    ADD CONSTRAINT fk_employment_0 FOREIGN KEY (employer_id) REFERENCES employer(employer_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY employment
    ADD CONSTRAINT fk_employment_1 FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY employment_session
    ADD CONSTRAINT fk_employment_session_0 FOREIGN KEY (employment_id) REFERENCES employment(employment_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY employment_session
    ADD CONSTRAINT fk_employment_session_location FOREIGN KEY (location_id) REFERENCES location(location_id);

ALTER TABLE ONLY location
    ADD CONSTRAINT fk_location_prorata_user FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id);

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT fk_h6syfj9fse40b9ii02tcxj3ya FOREIGN KEY (user_contact_id) REFERENCES prorata_user(prorata_user_id);

ALTER TABLE ONLY pay_cheque
    ADD CONSTRAINT fk_pay_cheque_0 FOREIGN KEY (payment_id) REFERENCES payment(payment_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY payment
    ADD CONSTRAINT fk_payment_0 FOREIGN KEY (employment_id) REFERENCES employment(employment_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY subscription
    ADD CONSTRAINT fk_subscription_prorata_user FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY subscription
    ADD CONSTRAINT fk_subscription_subscription_type FOREIGN KEY (subscription_type_id) REFERENCES subscription_type(subscription_type_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY tax_deduction
    ADD CONSTRAINT fk_tax_deduction_0 FOREIGN KEY (tax_bracket_id) REFERENCES tax_bracket(tax_bracket_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY tax_deduction
    ADD CONSTRAINT fk_tax_deduction_1 FOREIGN KEY (payment_id) REFERENCES payment(payment_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY user_contact
    ADD CONSTRAINT fk_user_contact_0 FOREIGN KEY (prorata_user_id) REFERENCES prorata_user(prorata_user_id) ON UPDATE CASCADE ON DELETE CASCADE;