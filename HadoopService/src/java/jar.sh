#!/usr/bin/env bash

if [ $# -eq 5 ]; then
cd $1
bin/hadoop jar $2 $3 $4 $5
elif [ $# -eq 6 ]; then
cd $1
bin/hadoop jar $2 $3 $4 $5 $6
fi
