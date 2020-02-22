import sys

l1 = [l.strip().split(",") for l in open( sys.argv[1], "r")]

l2 = [l.strip().split(",") for l in open( sys.argv[2], "r")]

for a2 in l2:
  for a1 in l1:
    if a2[0]==a1[0]:
      a2[2] = a1[2]
      a2[3] = a1[3]
  print ",".join(a2)

    
