import sys
import re

inf = sys.argv[1]

lines = [ l.strip() for l in open(inf,"r") ]

index = 0 
while index < len(lines):
  line = lines[index]
  if line == '--' : 
    name = lines[index-5]
    blob = lines[index-4]
    blobby = blob.split()
    sex = blobby[0]
    age = blobby[1].split("Bib")[0]
    bib = re.sub("[^0-9]", "", blobby[2])
    time = lines[index+1]
    print ",".join([bib,name,age,sex,time])
  index = index + 1 
