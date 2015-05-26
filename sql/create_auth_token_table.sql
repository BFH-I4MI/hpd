CREATE TABLE IF NOT EXISTS hpd.auth_token (
	id				INT PRIMARY KEY AUTO_INCREMENT,
	user_id			VARCHAR(32) NOT NULL,
	token_type 		VARCHAR(4) NOT NULL,
	token_element	VARCHAR(64),
	community_uid	VARCHAR(128) NOT NULL
)
ENGINE=MyISAM;