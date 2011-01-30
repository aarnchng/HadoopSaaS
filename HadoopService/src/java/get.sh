#!/usr/bin/env bash

cd $1
bin/hadoop fs -get $2 $3
