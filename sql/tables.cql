CREATE TABLE seed_user (
  dataset_id bigint,
  user_id bigint,
  is_active boolean,
  is_scheduled boolean,
  primary key (dataset_id, user_id))
with CLUSTERING ORDER BY (user_id ASC);

CREATE TABLE scheduled_user (
    dataset_id bigint,
    user_id bigint,
    information text,
    is_collected boolean,
    collection_date timestamp,
    primary key ((dataset_id, information), user_id))
with CLUSTERING ORDER BY (user_id ASC);

CREATE TABLE oauth_token (
    id bigint,
    api_key text,
    api_secret text,
    "token" text,
    token_secret text,
    primary key (id));

create table twitter_user(
	dataset_id bigint,
	user_id bigint,
	user_json text,
	primary key(dataset_id, user_id))
with CLUSTERING ORDER BY (user_id asc);


create table friend(
	user_id bigint,
	friend_id bigint,
	primary key(user_id, friend_id))
with CLUSTERING ORDER BY (friend_id asc);

create table like(
	user_id bigint,
	tweet_id bigint,
	tweet_json text,
	primary key(user_id, tweet_id))
with CLUSTERING ORDER BY (tweet_id asc);


create table tweet(
	user_id bigint,
	tweet_id bigint,
	tweet_json text,
	primary key(user_id, tweet_id))
with CLUSTERING ORDER BY (tweet_id asc);