#!/usr/bin/env bash

cd $1
bin/hadoop fs -put $2 $3
