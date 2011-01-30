#!/usr/bin/env bash

cd $1
bin/hadoop fs -rmr $2
