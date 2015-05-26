CREATE TABLE IF NOT EXISTS hpd.pid_tr_history (
	tr_id       INT AUTO_INCREMENT,
	tr_op_idx	VARCHAR(64) NOT NULL,
	src_desc    VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin,
	tr_date     DATETIME,
	tr_body     MEDIUMTEXT CHARACTER SET latin1 COLLATE latin1_bin,
	
	INDEX USING BTREE (src_desc),
	INDEX USING BTREE (tr_date),
	PRIMARY KEY (tr_id, tr_date)
)
ENGINE=MyISAM 
PARTITION BY HASH ( MONTH(tr_date) )
PARTITIONS 12;