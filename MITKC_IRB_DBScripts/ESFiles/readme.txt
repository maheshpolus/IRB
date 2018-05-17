Steps for configuring Elastic
-------------------------------
1. execute "dbscript.sql" to alter "MITKC_ELASTIC_INDEX" table to accommodate IRB columns.
2. Refresh "MITKC_ELASTIC_INDEX" table with updated fields, for this execute "load_data_to_table.sql"
3. Create Elastic index "irbprotocol", commands in "create_ES_index.txt"
4. Move logstash configuration files "irbprotocol.conf"  to Logstash directory and execute the files(commands in "create_ES_index.txt").

