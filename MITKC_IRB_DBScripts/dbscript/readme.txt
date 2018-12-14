README
------
Below file does the following

1. all.sql        : This will build IRB tables , procedures and functions, also,load data from KC to new IRB tables.

2. cleanup.sql    : This script to drop all the IRB tables , procedures and functions created for Phase 1.

3. refresh_connect_from_kc.sql : This script will refresh Connect IRB tables from KC. This will wipe out all transaction data in connect and reload from KC.