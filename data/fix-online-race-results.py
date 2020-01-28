import sys

lines = open( sys.argv[1] )

for line in lines:
  line = line.strip().split( "," )
  try:
    n = int(line[0])
    m = ",".join(line)
    if not ("Unknown" in m):
      print m 
  except:
    pass
