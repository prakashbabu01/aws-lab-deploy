#/bash/bin
cat $1 | while read line; do parameter=${parameters} ${line}; tr '\n' ' '; echo ${parameter} ; done > param.tmp
cat $2 | while read line; do parameter=${parameters} ${line}; tr '\n' ' '; echo ${parameter} ; done > param-tags.tmp