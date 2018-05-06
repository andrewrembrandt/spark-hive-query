#!/bin/sh

rm -rf metastore_db

schematool -dbType derby -initSchema

sudo hadoop fs -rm -r -f /user/hive/warehouse
sudo hadoop fs -mkdir -p /user/hive/warehouse
sudo hadoop fs -chmod +rw /user/hive/warehouse
