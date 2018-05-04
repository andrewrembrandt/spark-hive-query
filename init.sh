#!/bin/sh

schematool -dbType derby -initSchema

sudo hadoop fs -mkdir -p /user/hive/warehouse

