#!/usr/bin/env bash

hadoopjar=`cygpath -w $1hadoop-streaming.jar`

if [ $# -eq 6 ]; then
cd $1
if [ $4 != "none" ]; then
bin/hadoop jar $hadoopjar -mapper "$2 $3" -reducer "$2 $4" -input $5 -output $6
else
bin/hadoop jar $hadoopjar -mapper "$2 $3" -input $5 -output $6 -numReduceTasks 0
fi
elif [ $# -eq 7 ]; then
cd $1
if [ $4 != "none" ]; then
bin/hadoop jar $hadoopjar -mapper "$2 $3" -reducer "$2 $4" -input $5 -output $6 $7
else
bin/hadoop jar $hadoopjar -mapper "$2 $3" -input $5 -output $6 -numReduceTasks 0 $7
fi
fi
